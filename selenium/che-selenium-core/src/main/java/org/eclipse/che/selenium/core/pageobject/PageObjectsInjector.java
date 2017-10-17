/*
 * Copyright (c) 2012-2017 Red Hat, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 */
package org.eclipse.che.selenium.core.pageobject;

import com.google.inject.Binding;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;
import com.google.inject.spi.LinkedKeyBinding;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import org.eclipse.che.selenium.core.SeleniumWebDriver;
import org.eclipse.che.selenium.core.constant.TestBrowser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Injects fields annotated with {@link InjectPageObject}. All page objects with the same {@link
 * InjectPageObject#driverId()} must share a common {@link SeleniumWebDriver} instance.
 *
 * @author Anatolii Bazko
 */
public class PageObjectsInjector {

  private static Logger LOG = LoggerFactory.getLogger(PageObjectsInjector.class);

  @Inject
  @Named("sys.browser")
  private TestBrowser browser;

  @Inject
  @Named("sys.driver.port")
  private String webDriverPort;

  @Inject
  @Named("sys.grid.mode")
  private boolean gridMode;

  @Inject
  @Named("sys.driver.version")
  private String webDriverVersion;

  public void injectMembers(Object testInstance, Injector injector) throws Exception {
    Map<Integer, Set<Field>> toInject = collectFieldsToInject(testInstance);

    for (Integer poIndex : toInject.keySet()) {
      Map<Class<?>, Object> container = new HashMap<>();
      container.put(
          SeleniumWebDriver.class,
          new SeleniumWebDriver(browser, webDriverPort, gridMode, webDriverVersion));

      for (Field f : toInject.get(poIndex)) {
        try {
          injectField(f, testInstance, container, injector);
        } catch (Exception e) {
          throw new RuntimeException(
              String.format("Error of injection member '%s' into test '%s'.", f, testInstance), e);
        }
      }
    }
  }

  private void injectField(
      Field field, Object instance, Map<Class<?>, Object> container, Injector injector)
      throws Exception {
    Object object = instantiate(field.getType(), container, injector);
    field.setAccessible(true);
    field.set(instance, object);
  }

  private Object instantiate(Class<?> type, Map<Class<?>, Object> container, Injector injector)
      throws Exception {
    Object obj;

    Optional<Constructor<?>> constructor = findConstructor(type);
    if (!constructor.isPresent()) {
      Binding binding = injector.getBinding(type);
      if (binding instanceof LinkedKeyBinding) {
        // we can get bound type from injector
        Class boundType = ((LinkedKeyBinding) binding).getLinkedKey().getTypeLiteral().getRawType();
        obj = instantiate(boundType, container, injector);
      } else {
        // get instance from a guice container
        obj = injector.getInstance(type);
      }

    } else {
      obj = doInstantiate(container, injector, constructor);
    }

    container.put(obj.getClass(), obj);
    return obj;
  }

  private Object doInstantiate(
      Map<Class<?>, Object> container, Injector injector, Optional<Constructor<?>> constructor)
      throws Exception {
    Class<?>[] parameterTypes = constructor.get().getParameterTypes();
    Object[] params = new Object[parameterTypes.length];

    for (int i = 0; i < parameterTypes.length; i++) {
      Object pt = container.get(parameterTypes[i]);
      if (pt == null) {
        pt = instantiate(parameterTypes[i], container, injector);
      }
      params[i] = pt;
    }

    return constructor.get().newInstance(params);
  }

  private Optional<Constructor<?>> findConstructor(Class<?> type) {
    return Stream.of(type.getConstructors())
        .filter(
            c ->
                c.isAnnotationPresent(com.google.inject.Inject.class)
                    || c.isAnnotationPresent(javax.inject.Inject.class))
        .findAny();
  }

  /** Find class fields annotated with {@link InjectPageObject}. */
  private Map<Integer, Set<Field>> collectFieldsToInject(Object testInstance) {
    Map<Integer, Set<Field>> toInject = new HashMap<>();

    for (Field f : testInstance.getClass().getDeclaredFields()) {
      if (f.isAnnotationPresent(InjectPageObject.class)) {
        InjectPageObject pageObject = f.getAnnotation(InjectPageObject.class);
        int poIndex = pageObject.driverId();

        Set<Field> fields = toInject.computeIfAbsent(poIndex, k -> new HashSet<>());
        fields.add(f);
      }
    }

    return toInject;
  }
}

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
package org.eclipse.che.selenium.core;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import javax.inject.Named;
import org.eclipse.che.selenium.core.entrance.CookieEntrance;
import org.eclipse.che.selenium.core.entrance.Entrance;
import org.eclipse.che.selenium.core.entrance.LoginPageEntrance;
import org.eclipse.che.selenium.pageobject.site.CheLoginPage;
import org.eclipse.che.selenium.pageobject.site.LoginPage;

/**
 * Module which is dedicated to deal with dependencies which are injecting SeleniumWebDriver.class
 * itself.
 *
 * @author Dmytro Nochevnov
 */
public class CheSeleniumWebDriverRelatedModule extends AbstractModule {

  private static final String CHE_MULTIUSER = "che.multiuser";

  @Inject
  @Named(CHE_MULTIUSER)
  private boolean isMultiuser;

  @Override
  protected void configure() {
    bind(LoginPage.class).to(CheLoginPage.class);

    if (isMultiuser) {
      bind(Entrance.class).to(LoginPageEntrance.class);
    } else {
      bind(Entrance.class).to(CookieEntrance.class);
    }
  }
}

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
package org.eclipse.che.multiuser.keycloak.server;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import org.eclipse.che.api.core.rest.Service;

/**
 * Endpoint which provides keycloak public client authentication information (such as URL, realm,
 * cliend_id).
 *
 * @author Max Shaposhnik (mshaposh@redhat.com)
 */
@Singleton
@Path("/keycloak")
public class KeycloakConfigurationService extends Service {

  private final KeycloakSettings keycloakSettings;

  @Inject
  public KeycloakConfigurationService(KeycloakSettings keycloakSettings) {
    this.keycloakSettings = keycloakSettings;
  }

  @GET
  @Path("/settings")
  @Produces(APPLICATION_JSON)
  public Map<String, String> settings() {
    return keycloakSettings.get();
  }
}

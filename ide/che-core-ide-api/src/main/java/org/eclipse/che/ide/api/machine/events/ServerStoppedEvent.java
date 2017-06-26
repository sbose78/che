/*******************************************************************************
 * Copyright (c) 2012-2017 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package org.eclipse.che.ide.api.machine.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Fired when some server in some machine goes into a stopped state.
 *
 * @see WsAgentServerStoppedEvent
 * @see ExecAgentServerStoppedEvent
 */
public class ServerStoppedEvent extends GwtEvent<ServerStoppedEvent.Handler> {

    public static final Type<ServerStoppedEvent.Handler> TYPE = new Type<>();

    private final String serverName;
    private final String machineName;

    public ServerStoppedEvent(String serverName, String machineName) {
        this.serverName = serverName;
        this.machineName = machineName;
    }

    /** Returns the running server's name. */
    public String getServerName() {
        return serverName;
    }

    /** Returns the related machine's name. */
    public String getMachineName() {
        return machineName;
    }

    @Override
    public Type<Handler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(Handler handler) {
        handler.onServerStopped(this);
    }

    public interface Handler extends EventHandler {
        void onServerStopped(ServerStoppedEvent event);
    }
}
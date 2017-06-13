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
package org.eclipse.che.workspace.infrastructure.docker;

import com.google.inject.assistedinject.Assisted;

import org.eclipse.che.api.agent.server.AgentRegistry;
import org.eclipse.che.api.agent.server.impl.AgentSorter;
import org.eclipse.che.api.core.ValidationException;
import org.eclipse.che.api.core.model.workspace.config.Environment;
import org.eclipse.che.api.core.model.workspace.runtime.RuntimeIdentity;
import org.eclipse.che.api.core.notification.EventService;
import org.eclipse.che.api.workspace.server.URLRewriter;
import org.eclipse.che.api.workspace.server.spi.InfrastructureException;
import org.eclipse.che.api.workspace.server.spi.InternalInfrastructureException;
import org.eclipse.che.api.workspace.server.spi.InternalRuntime;
import org.eclipse.che.api.workspace.server.spi.RuntimeContext;
import org.eclipse.che.api.workspace.shared.Utils;
import org.eclipse.che.workspace.infrastructure.docker.model.DockerEnvironment;
import org.eclipse.che.workspace.infrastructure.docker.snapshot.SnapshotDao;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Alexander Garagatyi
 */
// TODO Check what if start fails and interruption called or stop called
// TODO interrupted exception, closedbyinteruptionexception

// TODO stop of starting WS - if not supported specific exception
// TODO stop add warning on errors?
// TODO stop in which cases to throw an exception?

// TODO exception on start
// TODO remove starting machine if present
// TODO Check if interruption came from stop or because of another reason
// TODO if because of another reason stop environment
public class DockerRuntimeContext extends RuntimeContext {
    private static final Logger LOG = getLogger(DockerRuntimeContext.class);

    private final NetworkLifecycle     dockerNetworkLifecycle;
    private final MachineStarter       serviceStarter;
    private final DockerEnvironment    dockerEnvironment;
    private final URLRewriter          urlRewriter;
    private final List<String>         orderedServices;
    private final StartSynchronizer    startSynchronizer;
    private final String               devMachineName;
    private final ContextsStorage      contextsStorage;
    private final SnapshotDao          snapshotDao;
    private final DockerRegistryClient dockerRegistryClient;
    private final EventService         eventService;

    @Inject
    public DockerRuntimeContext(@Assisted DockerRuntimeInfrastructure infrastructure,
                                @Assisted RuntimeIdentity identity,
                                @Assisted Environment environment,
                                @Assisted DockerEnvironment dockerEnvironment,
                                @Assisted List<String> orderedServices,
                                NetworkLifecycle dockerNetworkLifecycle,
                                MachineStarter serviceStarter,
                                URLRewriter urlRewriter,
                                AgentSorter agentSorter,
                                AgentRegistry agentRegistry,
                                ContextsStorage contextsStorage,
                                SnapshotDao snapshotDao,
                                DockerRegistryClient dockerRegistryClient,
                                EventService eventService)
            throws ValidationException, InfrastructureException {
        super(environment, identity, infrastructure, agentSorter, agentRegistry);
        this.devMachineName = Utils.getDevMachineName(environment);
        this.orderedServices = orderedServices;
        this.dockerEnvironment = dockerEnvironment;
        this.dockerNetworkLifecycle = dockerNetworkLifecycle;
        this.serviceStarter = serviceStarter;
        this.urlRewriter = urlRewriter;
        this.contextsStorage = contextsStorage;
        this.snapshotDao = snapshotDao;
        this.dockerRegistryClient = dockerRegistryClient;
        this.eventService = eventService;
        this.startSynchronizer = new StartSynchronizer();
    }

    @Override
    public URL getOutputChannel() throws InfrastructureException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public InternalRuntime getRuntime() {
        return getInternalRuntime(); //TODO: instance field?

    }

    private InternalRuntime getInternalRuntime() {
        return new DockerInternalRuntime(this,
                                         devMachineName,
                                         urlRewriter,
                                         orderedServices,
                                         contextsStorage,
                                         dockerEnvironment,
                                         dockerNetworkLifecycle,
                                         serviceStarter,
                                         snapshotDao,
                                         dockerRegistryClient,
                                         startSynchronizer,
                                         identity,
                                         eventService);
    }


    static class StartSynchronizer {
        private Thread                     startThread;
        private boolean                    stopCalled;
        private Map<String, DockerMachine> machines;

        public StartSynchronizer() {
            this.stopCalled = false;
            this.machines = new HashMap<>();
        }

        public synchronized Map<String, DockerMachine> getMachines() {
            return Collections.unmodifiableMap(machines);
        }

        public synchronized void addMachine(String name, DockerMachine machine) throws InternalInfrastructureException {
            if (machines != null) {
                machines.put(name, machine);
            } else {
                throw new InternalInfrastructureException("Machines entities are missing");
            }
        }

        public synchronized Map<String, DockerMachine> removeMachines() throws InfrastructureException {
            if (machines != null) {
                Map<String, DockerMachine> machines = this.machines;
                // unset to identify error if method called second time
                this.machines = null;
                return machines;
            }
            throw new InfrastructureException("");
        }

        public synchronized void setStartThread() throws InternalInfrastructureException {
            if (startThread != null) {
                throw new InternalInfrastructureException("Docker infrastructure context of workspace already started");
            }
            startThread = Thread.currentThread();
        }

        public synchronized void interruptStartThread() throws InfrastructureException {
            if (startThread == null) {
                throw new InternalInfrastructureException("Stop of non started context not allowed");
            }
            if (stopCalled) {
                throw new InternalInfrastructureException("Stop is called twice");
            }
            stopCalled = true;
            startThread.interrupt();
        }

        public synchronized boolean isStopCalled() {
            return stopCalled;
        }
    }
}
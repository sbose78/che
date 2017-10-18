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
package org.eclipse.che.plugin.jdb.server;

import static org.eclipse.che.plugin.jdb.server.util.JavaDebuggerUtils.ensureSuspendAtDesiredLocation;
import static org.eclipse.che.plugin.jdb.server.util.JavaDebuggerUtils.startJavaDebugger;
import static org.eclipse.che.plugin.jdb.server.util.JavaDebuggerUtils.terminateVirtualMachineQuietly;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import org.eclipse.che.api.debug.shared.model.Breakpoint;
import org.eclipse.che.api.debug.shared.model.Location;
import org.eclipse.che.api.debug.shared.model.event.BreakpointActivatedEvent;
import org.eclipse.che.api.debug.shared.model.event.DebuggerEvent;
import org.eclipse.che.api.debug.shared.model.event.DisconnectEvent;
import org.eclipse.che.api.debug.shared.model.event.SuspendEvent;
import org.eclipse.che.api.debug.shared.model.impl.BreakpointImpl;
import org.eclipse.che.api.debug.shared.model.impl.LocationImpl;
import org.eclipse.che.api.debug.shared.model.impl.action.ResumeActionImpl;
import org.eclipse.che.plugin.jdb.server.util.ProjectApiUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/** @author Anatolii Bazko */
public class BreakpointConditionTest {

  private JavaDebugger debugger;
  private BlockingQueue<DebuggerEvent> events;

  @BeforeClass
  public void setUp() throws Exception {
    ProjectApiUtils.ensure();

    Location location =
        new LocationImpl(
            "/test/src/org/eclipse/BreakpointsByConditionTest.java",
            17,
            false,
            -1,
            "/test",
            null,
            -1);

    events = new ArrayBlockingQueue<>(10);
    debugger = startJavaDebugger(new BreakpointImpl(location), events);

    ensureSuspendAtDesiredLocation(location, events);
  }

  @AfterClass
  public void tearDown() throws Exception {
    if (debugger != null) {
      terminateVirtualMachineQuietly(debugger);
    }
  }

  @Test
  public void shouldStopByCondition() throws Exception {
    Breakpoint breakpoint =
        new BreakpointImpl(
            new LocationImpl(
                "/test/src/org/eclipse/BreakpointsByConditionTest.java",
                19,
                false,
                -1,
                "/test",
                null,
                -1),
            true,
            "i==5");

    debugger.addBreakpoint(breakpoint);

    DebuggerEvent debuggerEvent = events.take();
    assertTrue(debuggerEvent instanceof BreakpointActivatedEvent);

    debugger.resume(new ResumeActionImpl());

    debuggerEvent = events.take();
    assertTrue(debuggerEvent instanceof SuspendEvent);

    assertEquals("5", debugger.evaluate("i"));
    assertEquals("4", debugger.evaluate("k"));

    debugger.resume(new ResumeActionImpl());

    debuggerEvent = events.take();
    assertTrue(debuggerEvent instanceof DisconnectEvent);
  }
}

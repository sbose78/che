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

import static org.eclipse.che.plugin.jdb.server.util.JavaDebuggerUtils.*;
import static org.testng.Assert.*;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import org.eclipse.che.api.debug.shared.model.Breakpoint;
import org.eclipse.che.api.debug.shared.model.Location;
import org.eclipse.che.api.debug.shared.model.event.BreakpointActivatedEvent;
import org.eclipse.che.api.debug.shared.model.event.DebuggerEvent;
import org.eclipse.che.api.debug.shared.model.event.SuspendEvent;
import org.eclipse.che.api.debug.shared.model.impl.BreakpointImpl;
import org.eclipse.che.api.debug.shared.model.impl.LocationImpl;
import org.eclipse.che.api.debug.shared.model.impl.action.JumpIntoActionImpl;
import org.eclipse.che.api.debugger.server.exceptions.DebuggerException;
import org.eclipse.che.plugin.jdb.server.util.ProjectApiUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/** @author Anatolii Bazko */
public class RunToLocationTest {

  private JavaDebugger debugger;
  private BlockingQueue<DebuggerEvent> events;

  @BeforeClass
  public void setUp() throws Exception {
    ProjectApiUtils.ensure();

    Location location =
        new LocationImpl(
            "/test/src/org/eclipse/BreakpointsTest.java", 19, false, -1, "/test", null, -1);

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
  public void shouldRunToLocationInsideClass() throws Exception {
    try {
      debugger.jumpTo(new JumpIntoActionImpl("/test/src/org/eclipse/BreakpointsTest.java", 20));
    } catch (DebuggerException e) {
      // class might not be loaded yet
    }

    DebuggerEvent debuggerEvent = events.take();
    assertTrue(debuggerEvent instanceof BreakpointActivatedEvent);

    Breakpoint actualBreakpoint = ((BreakpointActivatedEvent) debuggerEvent).getBreakpoint();
    Location actualLocation = actualBreakpoint.getLocation();
    assertEquals(actualLocation.getLineNumber(), 20);
    assertEquals(actualLocation.getTarget(), "/test/src/org/eclipse/BreakpointsTest.java");
    assertTrue(actualBreakpoint.getHitCount() == -1);
    assertTrue(actualBreakpoint.isEnabled());

    debuggerEvent = events.take();
    assertTrue(debuggerEvent instanceof SuspendEvent);

    Location suspendLocation = ((SuspendEvent) debuggerEvent).getLocation();
    assertEquals(suspendLocation.getLineNumber(), 20);
    assertEquals(actualLocation.getTarget(), "/test/src/org/eclipse/BreakpointsTest.java");
  }

  @Test(priority = 1)
  public void shouldRunToLocationInsideMethod() throws Exception {
    try {
      debugger.jumpTo(new JumpIntoActionImpl("/test/src/org/eclipse/BreakpointsTest.java", 36));
    } catch (DebuggerException e) {
      // class might not be loaded yet
    }

    DebuggerEvent debuggerEvent = events.take();
    assertTrue(debuggerEvent instanceof BreakpointActivatedEvent);

    Breakpoint actualBreakpoint = ((BreakpointActivatedEvent) debuggerEvent).getBreakpoint();
    Location actualLocation = actualBreakpoint.getLocation();
    assertEquals(actualLocation.getLineNumber(), 36);
    assertEquals(actualLocation.getTarget(), "/test/src/org/eclipse/BreakpointsTest.java");
    assertTrue(actualBreakpoint.isEnabled());

    debuggerEvent = events.take();
    assertTrue(debuggerEvent instanceof SuspendEvent);

    Location suspendLocation = ((SuspendEvent) debuggerEvent).getLocation();
    assertEquals(suspendLocation.getLineNumber(), 36);
    assertEquals(actualLocation.getTarget(), "/test/src/org/eclipse/BreakpointsTest.java");
  }

  @Test(priority = 2)
  public void shouldAddBreakpointByFqn() throws Exception {
    try {
      debugger.jumpTo(new JumpIntoActionImpl("org.eclipse.BreakpointsTest", 21));
    } catch (DebuggerException e) {
      // class might not be loaded yet
    }

    DebuggerEvent debuggerEvent = events.take();
    assertTrue(debuggerEvent instanceof BreakpointActivatedEvent);

    Breakpoint actualBreakpoint = ((BreakpointActivatedEvent) debuggerEvent).getBreakpoint();
    Location actualLocation = actualBreakpoint.getLocation();
    assertEquals(actualLocation.getLineNumber(), 21);
    assertEquals(actualLocation.getTarget(), "org.eclipse.BreakpointsTest");
    assertTrue(actualBreakpoint.isEnabled());
  }

  @Test(priority = 2)
  public void shouldAddBreakpointInsideInnerClass() throws Exception {
    try {
      debugger.jumpTo(new JumpIntoActionImpl("/test/src/org/eclipse/BreakpointsTest.java", 41));
    } catch (DebuggerException e) {
      // class might not be loaded yet
    }

    DebuggerEvent debuggerEvent = events.take();
    assertTrue(debuggerEvent instanceof BreakpointActivatedEvent);

    Breakpoint actualBreakpoint = ((BreakpointActivatedEvent) debuggerEvent).getBreakpoint();
    Location actualLocation = actualBreakpoint.getLocation();
    assertEquals(actualLocation.getLineNumber(), 41);
    assertEquals(actualLocation.getTarget(), "/test/src/org/eclipse/BreakpointsTest.java");
    assertTrue(actualBreakpoint.isEnabled());

    debuggerEvent = events.take();
    assertTrue(debuggerEvent instanceof SuspendEvent);

    Location suspendLocation = ((SuspendEvent) debuggerEvent).getLocation();
    assertEquals(suspendLocation.getLineNumber(), 41);
    assertEquals(actualLocation.getTarget(), "/test/src/org/eclipse/BreakpointsTest.java");
  }

  //  @Test(priority = 1)
  //  public void shouldDeleteBreakpointAfterRunToLocation() throws Exception {
  //    try {
  //      debugger.resume(new ResumeActionImpl());
  //    } catch (DebuggerException e) {
  //      // class might not be loaded yet
  //    }
  //    assertTrue(debugger.getAllBreakpoints().size() == 1);
  //    assertTrue(debugger.getAllBreakpoints().get(0).getLocation().getLineNumber() == 19);
  //  }
}

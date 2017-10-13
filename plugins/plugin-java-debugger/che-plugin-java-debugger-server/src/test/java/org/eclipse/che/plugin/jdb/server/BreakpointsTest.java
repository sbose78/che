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

import java.util.concurrent.BlockingQueue;
import org.eclipse.che.api.debug.shared.model.event.DebuggerEvent;

/** @author Anatolii Bazko */
public class BreakpointsTest {

  private JavaDebugger debugger;
  private BlockingQueue<DebuggerEvent> events;

  //  @BeforeClass
  //  public void setUp() throws Exception {
  //    ProjectApiUtils.ensure();
  //
  //    Location location =
  //        new LocationImpl(
  //            "/test/src/org/eclipse/BreakpointsTest.java", 19, false, -1, "/test", null, -1);
  //
  //    events = new ArrayBlockingQueue<>(10);
  //    debugger = startJavaDebugger(new BreakpointImpl(location), events);
  //
  //    ensureSuspendAtDesiredLocation(location, events);
  //  }
  //
  //  @AfterClass
  //  public void tearDown() throws Exception {
  //    if (debugger != null) {
  //      terminateVirtualMachineQuietly(debugger);
  //    }
  //  }
  //
  //  @Test
  //  public void shouldAddBreakpointInsideMethod() throws Exception {
  //    Location location =
  //        new LocationImpl(
  //            "/test/src/org/eclipse/BreakpointsTest.java", 35, false, -1, "/test", null, -1);
  //
  //    try {
  //      debugger.addBreakpoint(new BreakpointImpl(location, false, null));
  //    } catch (DebuggerException e) {
  //      // class might not be loaded yet
  //    }
  //
  //    debugger.resume(new ResumeActionImpl());
  //
  //    DebuggerEvent debuggerEvent = events.take();
  //    assertTrue(debuggerEvent instanceof BreakpointActivatedEvent);
  //
  //    Breakpoint actualBreakpoint = ((BreakpointActivatedEvent) debuggerEvent).getBreakpoint();
  //    Location actualLocation = actualBreakpoint.getLocation();
  //    assertEquals(actualLocation.getLineNumber(), 35);
  //    assertEquals(actualLocation.getTarget(), "/test/src/org/eclipse/BreakpointsTest.java");
  //    assertTrue(actualBreakpoint.isEnabled());
  //
  //    debuggerEvent = events.take();
  //    assertTrue(debuggerEvent instanceof SuspendEvent);
  //
  //    Location suspendLocation = ((SuspendEvent) debuggerEvent).getLocation();
  //    assertEquals(suspendLocation.getLineNumber(), 35);
  //    assertEquals(actualLocation.getTarget(), "/test/src/org/eclipse/BreakpointsTest.java");
  //  }
  //
  //  @Test(priority = 1)
  //  public void shouldAddBreakpointByFqn() throws Exception {
  //    Location location =
  //        new LocationImpl("org.eclipse.BreakpointsTest", 20, false, -1, "/test", null, -1);
  //
  //    try {
  //      debugger.addBreakpoint(new BreakpointImpl(location, false, null));
  //    } catch (DebuggerException e) {
  //      // class might not be loaded yet
  //    }
  //
  //    debugger.resume(new ResumeActionImpl());
  //
  //    DebuggerEvent debuggerEvent = events.take();
  //    assertTrue(debuggerEvent instanceof BreakpointActivatedEvent);
  //
  //    Breakpoint actualBreakpoint = ((BreakpointActivatedEvent) debuggerEvent).getBreakpoint();
  //    Location actualLocation = actualBreakpoint.getLocation();
  //    assertEquals(actualLocation.getLineNumber(), 20);
  //    assertEquals(actualLocation.getTarget(), "org.eclipse.BreakpointsTest");
  //    assertEquals(actualLocation.getResourceProjectPath(), "/test");
  //    assertTrue(actualBreakpoint.isEnabled());
  //
  //    debuggerEvent = events.take();
  //    assertTrue(debuggerEvent instanceof SuspendEvent);
  //
  //    Location suspendLocation = ((SuspendEvent) debuggerEvent).getLocation();
  //    assertEquals(suspendLocation.getLineNumber(), 20);
  //    assertEquals(actualLocation.getTarget(), "org.eclipse.BreakpointsTest");
  //    assertEquals(actualLocation.getResourceProjectPath(), "/test");
  //  }
  //
  //  @Test(priority = 2)
  //  public void shouldAddBreakpointInsideInnerClass() throws Exception {
  //    Location location =
  //        new LocationImpl(
  //            "/test/src/org/eclipse/BreakpointsTest.java", 40, false, -1, "/test", null, -1);
  //
  //    try {
  //      debugger.addBreakpoint(new BreakpointImpl(location, false, null));
  //    } catch (DebuggerException e) {
  //      // class might not be loaded yet
  //    }
  //
  //    debugger.resume(new ResumeActionImpl());
  //
  //    DebuggerEvent debuggerEvent = events.take();
  //    assertTrue(debuggerEvent instanceof BreakpointActivatedEvent);
  //
  //    Breakpoint actualBreakpoint = ((BreakpointActivatedEvent) debuggerEvent).getBreakpoint();
  //    Location actualLocation = actualBreakpoint.getLocation();
  //    assertEquals(actualLocation.getLineNumber(), 40);
  //    assertEquals(actualLocation.getTarget(), "/test/src/org/eclipse/BreakpointsTest.java");
  //    assertTrue(actualBreakpoint.isEnabled());
  //
  //    debuggerEvent = events.take();
  //    assertTrue(debuggerEvent instanceof SuspendEvent);
  //
  //    Location suspendLocation = ((SuspendEvent) debuggerEvent).getLocation();
  //    assertEquals(suspendLocation.getLineNumber(), 40);
  //    assertEquals(actualLocation.getTarget(), "/test/src/org/eclipse/BreakpointsTest.java");
  //  }
  //
  //  @Test(priority = 3)
  //  public void shouldAddBreakpointInsideAnonymousClass() throws Exception {
  //    Location location =
  //        new LocationImpl(
  //            "/test/src/org/eclipse/BreakpointsTest.java", 24, false, -1, "/test", null, -1);
  //
  //    try {
  //      debugger.addBreakpoint(new BreakpointImpl(location, false, null));
  //    } catch (DebuggerException e) {
  //      // class might not be loaded yet
  //    }
  //
  //    debugger.resume(new ResumeActionImpl());
  //
  //    DebuggerEvent debuggerEvent = events.take();
  //    assertTrue(debuggerEvent instanceof BreakpointActivatedEvent);
  //
  //    Breakpoint actualBreakpoint = ((BreakpointActivatedEvent) debuggerEvent).getBreakpoint();
  //    Location actualLocation = actualBreakpoint.getLocation();
  //    assertEquals(actualLocation.getLineNumber(), 24);
  //    assertEquals(actualLocation.getTarget(), "/test/src/org/eclipse/BreakpointsTest.java");
  //    assertTrue(actualBreakpoint.isEnabled());
  //
  //    debuggerEvent = events.take();
  //    assertTrue(debuggerEvent instanceof SuspendEvent);
  //
  //    Location suspendLocation = ((SuspendEvent) debuggerEvent).getLocation();
  //    assertEquals(suspendLocation.getLineNumber(), 24);
  //    assertEquals(actualLocation.getTarget(), "/test/src/org/eclipse/BreakpointsTest.java");
  //  }
  //
  //  @Test(priority = 4)
  //  public void shouldAddBreakpointInsideLambdaFunction() throws Exception {
  //    Location location =
  //        new LocationImpl(
  //            "/test/src/org/eclipse/BreakpointsTest.java", 30, false, -1, "/test", null, -1);
  //
  //    try {
  //      debugger.addBreakpoint(new BreakpointImpl(location, false, null));
  //    } catch (DebuggerException e) {
  //      // class might not be loaded yet
  //    }
  //
  //    debugger.resume(new ResumeActionImpl());
  //
  //    DebuggerEvent debuggerEvent = events.take();
  //    assertTrue(debuggerEvent instanceof BreakpointActivatedEvent);
  //
  //    Breakpoint actualBreakpoint = ((BreakpointActivatedEvent) debuggerEvent).getBreakpoint();
  //    Location actualLocation = actualBreakpoint.getLocation();
  //    assertEquals(actualLocation.getLineNumber(), 30);
  //    assertEquals(actualLocation.getTarget(), "/test/src/org/eclipse/BreakpointsTest.java");
  //    assertTrue(actualBreakpoint.isEnabled());
  //
  //    debuggerEvent = events.take();
  //    assertTrue(debuggerEvent instanceof SuspendEvent);
  //
  //    Location suspendLocation = ((SuspendEvent) debuggerEvent).getLocation();
  //    assertEquals(suspendLocation.getLineNumber(), 30);
  //    assertEquals(actualLocation.getTarget(), "/test/src/org/eclipse/BreakpointsTest.java");
  //  }
  //
  //  @Test(priority = 5)
  //  public void shouldRemoveAllBreakpoints() throws Exception {
  //    assertFalse(debugger.getAllBreakpoints().isEmpty());
  //
  //    debugger.deleteAllBreakpoints();
  //
  //    assertTrue(debugger.getAllBreakpoints().isEmpty());
  //  }
  //
  //  @Test(priority = 6)
  //  public void shouldRemoveBreakpoint() throws Exception {
  //    Location location =
  //        new LocationImpl(
  //            "/test/src/org/eclipse/BreakpointsTest.java", 35, false, -1, "/test", null, -1);
  //
  //    debugger.addBreakpoint(new BreakpointImpl(location, false, null));
  //    assertEquals(debugger.getAllBreakpoints().size(), 1);
  //
  //    debugger.deleteBreakpoint(location);
  //    assertTrue(debugger.getAllBreakpoints().isEmpty());
  //  }
  //
  //  @Test(priority = 7)
  //  public void shouldReturnAllBreakpoints() throws Exception {
  //    Location location =
  //        new LocationImpl(
  //            "/test/src/org/eclipse/BreakpointsTest.java", 35, false, -1, "/test", null, -1);
  //    debugger.addBreakpoint(new BreakpointImpl(location, false, null));
  //
  //    List<Breakpoint> breakpoints = debugger.getAllBreakpoints();
  //    assertEquals(breakpoints.size(), 1);
  //
  //    Breakpoint actualBreakpoint = breakpoints.get(0);
  //    Location actualLocation = actualBreakpoint.getLocation();
  //
  //    assertEquals(actualLocation.getLineNumber(), 35);
  //    assertEquals(actualLocation.getTarget(), "/test/src/org/eclipse/BreakpointsTest.java");
  //    assertEquals(actualLocation.getResourceProjectPath(), "/test");
  //    assertTrue(actualBreakpoint.isEnabled());
  //  }
}

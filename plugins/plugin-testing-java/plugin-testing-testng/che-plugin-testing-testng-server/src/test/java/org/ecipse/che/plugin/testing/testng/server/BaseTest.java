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
package org.ecipse.che.plugin.testing.testng.server;

/** @author Evgen Vidolob */
public abstract class BaseTest {
  //
  //  protected static final String wsPath = "target/workspace";
  //  protected static final String INDEX_PATH = "target/fs_index";
  //  protected static final String PROJECT_NAME = "testProject";
  //
  //  protected static Map<String, String> options = new HashMap<>();
  //  protected static EventService eventService = new EventService();
  //  protected static ResourcesPlugin plugin;
  //  protected static JavaPlugin javaPlugin = new JavaPlugin(wsPath + "/set", null, null);
  //  protected static FileBuffersPlugin fileBuffersPlugin = new FileBuffersPlugin();
  //  protected static TestWorkspaceHolder workspaceHolder;
  //
  //  protected File root;
  //  protected ProjectManager_ pm;
  //  protected LocalVirtualFileSystemProvider vfsProvider;
  //  protected ProjectRegistry projectRegistry;
  //  protected FileWatcherNotificationHandler fileWatcherNotificationHandler;
  //  protected FileTreeWatcher fileTreeWatcher;
  //  protected ProjectTypeRegistry projectTypeRegistry;
  //  protected ProjectHandlerRegistry projectHandlerRegistry;
  //  protected ProjectImporterRegistry importerRegistry;
  //
  //  public BaseTest() {
  //    options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_8);
  //    options.put(JavaCore.CORE_ENCODING, "UTF-8");
  //    options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
  //    options.put(CompilerOptions.OPTION_TargetPlatform, JavaCore.VERSION_1_8);
  //    options.put(AssistOptions.OPTION_PerformVisibilityCheck, AssistOptions.ENABLED);
  //    options.put(CompilerOptions.OPTION_ReportUnusedLocal, CompilerOptions.WARNING);
  //    options.put(CompilerOptions.OPTION_TaskTags, CompilerOptions.WARNING);
  //    options.put(CompilerOptions.OPTION_ReportUnusedPrivateMember, CompilerOptions.WARNING);
  //    options.put(CompilerOptions.OPTION_SuppressWarnings, CompilerOptions.DISABLED);
  //    options.put(JavaCore.COMPILER_TASK_TAGS, "TODO,FIXME,XXX");
  //    options.put(
  //        JavaCore.COMPILER_PB_UNUSED_PARAMETER_INCLUDE_DOC_COMMENT_REFERENCE, JavaCore.ENABLED);
  //    options.put(JavaCore.COMPILER_DOC_COMMENT_SUPPORT, JavaCore.ENABLED);
  //    options.put(CompilerOptions.OPTION_Process_Annotations, JavaCore.DISABLED);
  //  }
  //
  //  @BeforeMethod
  //  protected void initProjectApi() throws Exception {
  //    workspaceHolder = new TestWorkspaceHolder();
  //
  //    if (root == null) root = new File(wsPath);
  //
  //    if (root.exists()) {
  //      IoUtil.deleteRecursive(root);
  //    }
  //    root.mkdir();
  //
  //    File indexDir = new File(INDEX_PATH);
  //
  //    if (indexDir.exists()) {
  //      IoUtil.deleteRecursive(indexDir);
  //    }
  //    indexDir.mkdir();
  //    Set<PathMatcher> filters = new HashSet<>();
  //    filters.add(path -> true);
  //    FSLuceneSearcherProvider sProvider = new FSLuceneSearcherProvider(indexDir, filters);
  //
  //    vfsProvider = new LocalVirtualFileSystemProvider(root, sProvider);
  //
  //    projectTypeRegistry = new ProjectTypeRegistry(new HashSet<>());
  //    projectTypeRegistry.registerProjectType(new TestProjectType());
  //
  //    projectHandlerRegistry = new ProjectHandlerRegistry(new HashSet<>());
  //
  //    projectRegistry =
  //        new ProjectRegistry(
  //            workspaceHolder,
  //            vfsProvider,
  //            projectTypeRegistry,
  //            projectHandlerRegistry,
  //            eventService);
  //    projectRegistry.initProjects();
  //
  //    importerRegistry = new ProjectImporterRegistry(new HashSet<>());
  //
  //    fileWatcherNotificationHandler = new DefaultFileWatcherNotificationHandler(vfsProvider);
  //    fileTreeWatcher = new FileTreeWatcher(root, new HashSet<>(), fileWatcherNotificationHandler);
  //
  //    pm =
  //        new ProjectManager_(
  //            vfsProvider,
  //            projectTypeRegistry,
  //            projectRegistry,
  //            projectHandlerRegistry,
  //            importerRegistry,
  //            fileWatcherNotificationHandler,
  //            fileTreeWatcher,
  //            new TestWorkspaceHolder(new ArrayList<>()),
  //            Mockito.mock(FileWatcherManager.class));
  //
  //    plugin = new ResourcesPlugin("target/index", wsPath, () -> projectRegistry, () -> pm);
  //
  //    plugin.start();
  //    javaPlugin.start();
  //  }
  //
  //  @AfterMethod
  //  public void cleanJavaModel() throws Exception {
  //    JavaModelManager.getJavaModelManager().deltaState.removeExternalElementsToRefresh();
  //  }
  //
  //  protected static class TestProjectType extends ProjectTypeDef {
  //
  //    protected TestProjectType() {
  //      super("java", "java", true, true);
  //    }
  //  }
  //
  //  protected static class TestWorkspaceHolder extends WorkspaceProjectsSyncer {
  //
  //    private List<ProjectConfigDto> projects;
  //
  //    public TestWorkspaceHolder() {
  //      this.projects = new ArrayList<>();
  //    }
  //
  //    public TestWorkspaceHolder(List<ProjectConfigDto> projects) {
  //      this.projects = projects;
  //    }
  //
  //    @Override
  //    public List<? extends ProjectConfig> getProjects() {
  //      return projects;
  //    }
  //
  //    @Override
  //    public String getWorkspaceId() {
  //      return "id";
  //    }
  //
  //    @Override
  //    protected void addProject(ProjectConfig project) throws ServerException {}
  //
  //    @Override
  //    protected void updateProject(ProjectConfig project) throws ServerException {}
  //
  //    @Override
  //    protected void removeProject(ProjectConfig project) throws ServerException {}
  //  }
}

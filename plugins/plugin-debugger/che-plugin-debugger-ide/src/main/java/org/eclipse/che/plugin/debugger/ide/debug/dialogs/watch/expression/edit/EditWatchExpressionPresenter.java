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
package org.eclipse.che.plugin.debugger.ide.debug.dialogs.watch.expression.edit;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.eclipse.che.api.debug.shared.model.Expression;
import org.eclipse.che.ide.api.data.tree.Node;
import org.eclipse.che.plugin.debugger.ide.DebuggerLocalizationConstant;
import org.eclipse.che.plugin.debugger.ide.debug.DebuggerPresenter;
import org.eclipse.che.plugin.debugger.ide.debug.dialogs.DebuggerDialogFactory;
import org.eclipse.che.plugin.debugger.ide.debug.dialogs.common.TextAreaDialogView;
import org.eclipse.che.plugin.debugger.ide.debug.tree.node.WatchExpressionNode;

/**
 * Presenter to apply expression in the debugger watch list.
 *
 * @author Alexander Andrienko
 */
@Singleton
public class EditWatchExpressionPresenter implements TextAreaDialogView.ActionDelegate {

  private final TextAreaDialogView view;
  private final DebuggerPresenter debuggerPresenter;
  private final DebuggerLocalizationConstant constant;
  private WatchExpressionNode watchExpressionNode;

  @Inject
  public EditWatchExpressionPresenter(
      DebuggerDialogFactory dialogFactory,
      DebuggerLocalizationConstant constant,
      DebuggerPresenter debuggerPresenter) {
    this.view =
        dialogFactory.createTextAreaDialogView(
            constant.editExpressionTextAreaDialogView(),
            constant.editExpressionViewSaveButtonTitle(),
            constant.editExpressionViewCancelButtonTitle(),
            "debugger-edit-expression");
    this.view.setDelegate(this);
    this.debuggerPresenter = debuggerPresenter;
    this.constant = constant;
  }

  @Override
  public void showDialog() {
    Node selectedNode = debuggerPresenter.getSelectedDebugNode();
    if (selectedNode instanceof WatchExpressionNode) {
      watchExpressionNode = (WatchExpressionNode) selectedNode;

      view.setValueTitle(constant.editExpressionViewExpressionFieldTitle());
      view.setValue(watchExpressionNode.getData().getExpression());
      view.focusInValueField();
      view.selectAllText();
      view.setEnableChangeButton(false);
      view.show();
    }
  }

  @Override
  public void onCancelClicked() {
    view.close();
  }

  @Override
  public void onAgreeClicked() {
    if (watchExpressionNode != null) {
      Expression expression = watchExpressionNode.getData();
      expression.setExpression(view.getValue());
      debuggerPresenter.onEditExpressionBtnClicked(expression);
    }

    view.close();
  }

  @Override
  public void onValueChanged() {
    final String value = view.getValue();
    boolean isExpressionFieldNotEmpty = !value.trim().isEmpty();
    view.setEnableChangeButton(isExpressionFieldNotEmpty);
  }
}

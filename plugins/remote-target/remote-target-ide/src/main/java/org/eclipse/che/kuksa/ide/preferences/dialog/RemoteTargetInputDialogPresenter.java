/*
 * Copyright (c) 2018
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Pedro Cuadra - pjcuadra@gmail.com
 */
package org.eclipse.che.kuksa.ide.preferences.dialog;

import com.google.inject.Inject;
import javax.validation.constraints.NotNull;
import org.eclipse.che.ide.api.notification.NotificationManager;
import org.eclipse.che.ide.api.notification.StatusNotification;
import org.eclipse.che.ide.ui.dialogs.CancelCallback;
import org.eclipse.che.kuksa.ide.RemoteTarget;

/**
 * {@link RemoteTargetInputDialog} implementation.
 *
 * @author Mickaël Leduque
 * @author Artem Zatsarynnyi
 */
public class RemoteTargetInputDialogPresenter implements RemoteTargetInputDialogView.ActionDelegate {

  /** This component view. */
  private final RemoteTargetInputDialogView view;

  private final NotificationManager notificationManager;

  /** The callback used on OK. */
  private RemoteTargetCallback inputCallback;

  /** The callback used on cancel. */
  private CancelCallback cancelCallback;

  @Inject
  public RemoteTargetInputDialogPresenter(
      final @NotNull RemoteTargetInputDialogView view, final NotificationManager notificationManager) {
    this.view = view;
    this.inputCallback = null;
    this.view.setDelegate(this);
    this.notificationManager = notificationManager;
  }

  public void setInputCallback(RemoteTargetCallback inputCallback) {
    this.inputCallback = inputCallback;
  }

  @Override
  public void cancelled() {
    view.closeDialog();
    if (cancelCallback != null) {
      cancelCallback.cancelled();
    }
  }

  @Override
  public void accepted() {
    if (!isInputValid()) {
      notificationManager.notify(
          "Error Invalid Input",
          StatusNotification.Status.FAIL,
          StatusNotification.DisplayMode.FLOAT_MODE);
      return;
    }

    view.closeDialog();
    RemoteTarget yoctoSdk = new RemoteTarget();
    yoctoSdk.setName(view.getName());
    yoctoSdk.setVersion(view.getVersion());
    yoctoSdk.setUrl(view.getUrl());

    if (this.inputCallback != null) {
      inputCallback.accepted(yoctoSdk);
    }
  }

  @Override
  public void inputValueChanged(int sel) {
    if (isInputValid()) {
      this.view.hideErrorHint();
    }
  }

  @Override
  public void onEnterClicked() {
    if (view.isOkButtonInFocus()) {
      accepted();
      return;
    }

    if (view.isCancelButtonInFocus()) {
      cancelled();
      return;
    }

    if (isInputValid()) {
      accepted();
      return;
    }

    notificationManager.notify(
        "Error Invalid Input",
        StatusNotification.Status.FAIL,
        StatusNotification.DisplayMode.FLOAT_MODE);
  }

  public void show() {
    view.setContent("");
    view.showDialog();
  }

  public void clear() {
    view.clear();
  }

  private boolean validateSingle(String input) {
    if (input.trim().isEmpty()) {
      //      view.showErrorHint("Empty Fields");
      return false;
    }

    return true;
  }

  private boolean validateUrl(String input) {
    if (!validateSingle(input)) {
      this.view.showErrorHint("Invalid URL");
      return false;
    }

    if (input.startsWith("http://") || input.startsWith("https://")) {
      return true;
    }

    this.view.showErrorHint("Invalid URL");
    return false;
  }

  private boolean isInputValid() {
    return validateSingle(view.getName())
        && validateSingle(view.getVersion())
        && validateUrl(view.getUrl());
  }
}

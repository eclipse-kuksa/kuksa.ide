/*
 * Copyright (c) 2012-2018 Red Hat, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 */
package org.eclipse.che.kuksa.ide.preferences;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import javax.validation.constraints.NotNull;
import org.eclipse.che.ide.api.preferences.AbstractPreferencePagePresenter;
import org.eclipse.che.ide.api.preferences.PreferencesManager;
import org.eclipse.che.ide.ui.dialogs.CancelCallback;
import org.eclipse.che.ide.ui.dialogs.DialogFactory;
import org.eclipse.che.ide.ui.dialogs.confirm.ConfirmCallback;
import org.eclipse.che.kuksa.ide.RemoteTarget;
import org.eclipse.che.kuksa.ide.RemoteTargetLocalizationConstant;
import org.eclipse.che.kuksa.ide.preferences.dialog.RemoteTargetCallback;
import org.eclipse.che.kuksa.ide.preferences.dialog.RemoteTargetInputDialogPresenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Initially taken from plugin-yaml

/**
 * The presenter for managing the RemoteTargetCellTable in RemoteTargetManagerView.
 *
 * @author Joshua Pinkney
 * @author Pedro Cuadra
 */
@Singleton
public class RemoteTargetManagerPresenter extends AbstractPreferencePagePresenter
    implements RemoteTargetManagerView.ActionDelegate {

  private static final Logger LOG = LoggerFactory.getLogger(RemoteTargetManagerPresenter.class);
  private final String preferenceName = "remote.target.preferences";
  private DialogFactory dialogFactory;
  private RemoteTargetManagerView view;
  private PreferencesManager preferencesManager;
  private RemoteTargetLocalizationConstant local;
  private boolean dirty = false;
  private final RemoteTargetManager remoteTargetManager;
  private final RemoteTargetInputDialogPresenter inputPresenter;

  @Inject
  public RemoteTargetManagerPresenter(
      RemoteTargetManagerView view,
      DialogFactory dialogFactory,
      PreferencesManager preferencesManager,
      RemoteTargetLocalizationConstant local,
      RemoteTargetManager remoteTargetManager,
      RemoteTargetInputDialogPresenter inputPresenter) {
    super("Targets", "Remote Targets Settings");
    this.view = view;
    this.dialogFactory = dialogFactory;
    this.local = local;
    this.preferencesManager = preferencesManager;
    this.remoteTargetManager = remoteTargetManager;
    if (preferencesManager.getValue(preferenceName) == null
        || "".equals(preferencesManager.getValue(preferenceName))
        || "{}".equals(preferencesManager.getValue(preferenceName))) {
    } else {
      remoteTargetManager.loadJsonString(this.preferencesManager.getValue(this.preferenceName));
    }
    this.view.setDelegate(this);
    this.inputPresenter = inputPresenter;

    this.inputPresenter.setInputCallback(
        new RemoteTargetCallback() {
          @Override
          public void accepted(RemoteTarget pref) {
            remoteTargetManager.addRemoteTarget(pref);
            refreshTable();
            nowDirty();
          }
        });

    refreshTable();
  }

  /** {@inheritDoc} */
  @Override
  public void onDeleteClicked(@NotNull final RemoteTarget pairKey) {
    dialogFactory
        .createConfirmDialog(
            local.deleteRemoteTarget(),
            local.deleteRemoteTargetLabel(),
            new ConfirmCallback() {
              @Override
              public void accepted() {
                remoteTargetManager.removeSdk(pairKey);
                refreshTable();
                nowDirty();
              }
            },
            getCancelCallback())
        .show();
  }

  /** {@inheritDoc} */
  @Override
  public void onSelectClicked(@NotNull final RemoteTarget pairKey) {

    if (this.remoteTargetManager.selectRemoteTarget(pairKey)) {
      LOG.info(
          "Selected SDK: { Hostname: "
              + pairKey.getHostname()
              + ", User: "
              + pairKey.getUser()
              + "}");
      refreshTable();
      nowDirty();
    }
  }

  private CancelCallback getCancelCallback() {
    return new CancelCallback() {
      @Override
      public void cancelled() {
        // for now do nothing but it need for tests
      }
    };
  }

  /** {@inheritDoc} */
  @Override
  public void onAddRemoteTargetClicked() {
    inputPresenter.clear();
    inputPresenter.show();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isDirty() {
    return dirty;
  }

  /** {@inheritDoc} */
  @Override
  public void nowDirty() {
    dirty = true;
    delegate.onDirtyChanged();
  }

  /** {@inheritDoc} */
  @Override
  public void go(AcceptsOneWidget container) {
    container.setWidget(view);
    refreshTable();
  }

  /** Refresh RemoteTargetCellTable */
  private void refreshTable() {
    view.setPairs(this.remoteTargetManager.getAll());
  }

  @Override
  public void storeChanges() {
    this.preferencesManager.setValue(this.preferenceName, remoteTargetManager.toJsonString());
    this.preferencesManager.flushPreferences();
    dirty = false;
    delegate.onDirtyChanged();
  }

  @Override
  public void revertChanges() {}
}

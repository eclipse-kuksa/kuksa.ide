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
package org.eclipse.che.kuksa.yocto.ide.preferences;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import elemental.json.Json;
import elemental.json.JsonObject;
import java.util.List;
import javax.validation.constraints.NotNull;
import org.eclipse.che.ide.api.preferences.AbstractPreferencePagePresenter;
import org.eclipse.che.ide.api.preferences.PreferencesManager;
import org.eclipse.che.ide.ui.dialogs.CancelCallback;
import org.eclipse.che.ide.ui.dialogs.DialogFactory;
import org.eclipse.che.ide.ui.dialogs.confirm.ConfirmCallback;
import org.eclipse.che.ide.ui.dialogs.input.InputCallback;
import org.eclipse.che.kuksa.yocto.ide.YoctoLocalizationConstant;
import org.eclipse.che.kuksa.yocto.shared.YoctoSdk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Initially taken from plugin-yaml

/**
 * The presenter for managing the YoctoSdkCellTable in YoctoExtensionManagerView.
 *
 * @author Joshua Pinkney
 * @author Pedro Cuadra
 */
@Singleton
public class YoctoExtensionManagerPresenter extends AbstractPreferencePagePresenter
    implements YoctoExtensionManagerView.ActionDelegate {

  private static final Logger LOG = LoggerFactory.getLogger(YoctoExtensionManagerPresenter.class);
  private final String preferenceName = "yocto.preferences";
  private DialogFactory dialogFactory;
  private YoctoExtensionManagerView view;
  private PreferencesManager preferencesManager;
  private YoctoLocalizationConstant local;
  //  private YoctoServiceClient serviceClient;
  //  private NotificationManager notificationManager;
  private boolean dirty = false;
  private final YoctoSdkManager yoctoSdkManager;

  @Inject
  public YoctoExtensionManagerPresenter(
      YoctoExtensionManagerView view,
      DialogFactory dialogFactory,
      PreferencesManager preferencesManager,
      YoctoLocalizationConstant local,
      YoctoSdkManager yoctoSdkManager) {
    super("Yocto SDK", "Yocto Settings");
    this.view = view;
    this.dialogFactory = dialogFactory;
    this.local = local;
    this.preferencesManager = preferencesManager;
    if (preferencesManager.getValue(preferenceName) == null
        || "".equals(preferencesManager.getValue(preferenceName))
        || "{}".equals(preferencesManager.getValue(preferenceName))) {
    } else {
      //      jsonToYoctoSdk(this.preferencesManager.getValue(this.preferenceName));
    }
    this.view.setDelegate(this);
    //    this.serviceClient = serviceClient;
    //    this.notificationManager = notificationManager;
    this.yoctoSdkManager = yoctoSdkManager;

    refreshTable();
  }

  /** {@inheritDoc} */
  @Override
  public void onDeleteClicked(@NotNull final YoctoSdk pairKey) {
    dialogFactory
        .createConfirmDialog(
            local.deleteSdk(),
            local.deleteSdkLabel(),
            new ConfirmCallback() {
              @Override
              public void accepted() {
                yoctoSdkManager.removeSdk(pairKey);
                refreshTable();
                nowDirty();
              }
            },
            getCancelCallback())
        .show();
  }

  /** {@inheritDoc} */
  @Override
  public void onSelectClicked(@NotNull final YoctoSdk pairKey) {

    this.yoctoSdkManager.selectSdk(pairKey);
    LOG.info(
        "Selected SDK: { Name: "
            + pairKey.getName()
            + ", Version: "
            + pairKey.getVersion()
            + ", URL: "
            + pairKey.getUrl()
            + "}");
    refreshTable();
    nowDirty();
  }

  private CancelCallback getCancelCallback() {
    return new CancelCallback() {
      @Override
      public void cancelled() {
        // for now do nothing but it need for tests
      }
    };
  }

  /**
   * Add a SDK to your workspace
   *
   * @param sdk The SDK you would like to download and install
   */
  private void addUrlToSdkPreferences(YoctoSdk pref, String url) {
    pref.setUrl(url);
    this.yoctoSdkManager.addSdk(pref);
  }

  private void addNameToSdkPreferences(YoctoSdk pref, String name) {
    pref.setName(name);
  }

  private void addVersionToSdkPreferences(YoctoSdk pref, String version) {
    pref.setVersion(version);
  }

  /**
   * Converts json string to list of Yaml Preferences
   *
   * @param jsonStr The json string to turn into the list of Yaml Preferences
   * @return List of Yaml Preferences
   */
  private void jsonToYoctoSdk(String jsonStr) {
    JsonObject parsedJson = Json.parse(jsonStr);
    YoctoSdk fact = new YoctoSdk();

    for (String name : parsedJson.keys()) {
      JsonObject nameObj = parsedJson.getObject(name);

      for (String version : nameObj.keys()) {
        JsonObject prefObj = nameObj.getObject(version);
        this.yoctoSdkManager.addSdk(fact.with(prefObj));
      }
    }
  }

  /** {@inheritDoc} */
  @Override
  public void onAddSdkClicked() {

    YoctoSdk pref = new YoctoSdk();

    dialogFactory
        .createInputDialog(
            local.addSdk(),
            local.addSdkUrlLabel(),
            "",
            0,
            0,
            local.addSdkButtonText(),
            new InputCallback() {
              @Override
              public void accepted(String url) {
                addUrlToSdkPreferences(pref, url);
                refreshTable();
                nowDirty();
              }
            },
            getCancelCallback())
        .show();

    dialogFactory
        .createInputDialog(
            local.addSdk(),
            local.addSdkVersionLabel(),
            "",
            0,
            0,
            local.addSdkButtonText(),
            new InputCallback() {
              @Override
              public void accepted(String version) {
                addVersionToSdkPreferences(pref, version);
                nowDirty();
              }
            },
            getCancelCallback())
        .show();

    dialogFactory
        .createInputDialog(
            local.addSdk(),
            local.addSdkNameLabel(),
            "",
            0,
            0,
            local.addSdkButtonText(),
            new InputCallback() {
              @Override
              public void accepted(String name) {
                addNameToSdkPreferences(pref, name);
                nowDirty();
              }
            },
            getCancelCallback())
        .show();
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
    //    setSchemas();
  }

  /** Refresh YoctoSdkCellTable */
  private void refreshTable() {
    view.setPairs(this.yoctoSdkManager.getAll());
  }

  /**
   * Convert YoctoSdk's to JSON
   *
   * @param yoctoSdkPreferencesList
   * @return String of yoctoSdkPreferences
   */
  private String yoctoSdkPreferencesToJson(List<YoctoSdk> yoctoSdkPreferencesList) {

    JSONObject mainObj = new JSONObject();

    for (YoctoSdk pref : yoctoSdkPreferencesList) {
      JSONObject nameObj;

      // If not yet added create it
      if (!mainObj.containsKey(pref.getName())) {
        nameObj = new JSONObject();
        mainObj.put(pref.getName(), nameObj);
      }

      nameObj = mainObj.get(pref.getName()).isObject();

      if (nameObj.containsKey(pref.getVersion())) {
        continue;
      }

      nameObj.put(pref.getVersion(), pref.toJson());
    }

    return mainObj.toString();
  }

  @Override
  public void storeChanges() {
    //    setSchemas();

    this.preferencesManager.setValue(
        this.preferenceName, yoctoSdkPreferencesToJson(this.yoctoSdkManager.getAll()));
    this.preferencesManager.flushPreferences();
    dirty = false;
    delegate.onDirtyChanged();
  }

  @Override
  public void revertChanges() {}
}

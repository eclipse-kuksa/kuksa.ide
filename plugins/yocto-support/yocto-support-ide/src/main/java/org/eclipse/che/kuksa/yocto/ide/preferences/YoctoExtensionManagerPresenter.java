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

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import elemental.json.Json;
import elemental.json.JsonObject;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
import org.eclipse.che.ide.api.preferences.AbstractPreferencePagePresenter;
import org.eclipse.che.ide.api.preferences.PreferencesManager;
import org.eclipse.che.ide.ui.dialogs.CancelCallback;
import org.eclipse.che.ide.ui.dialogs.DialogFactory;
import org.eclipse.che.ide.ui.dialogs.confirm.ConfirmCallback;
import org.eclipse.che.ide.ui.dialogs.input.InputCallback;
import org.eclipse.che.kuksa.yocto.ide.YoctoLocalizationConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Initially taken from plugin-yaml

/**
 * The presenter for managing the YoctoSdkPreferencesCellTable in YoctoExtensionManagerView.
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
  private List<YoctoSdkPreferences> yoctoSdkPreferences;
  private YoctoLocalizationConstant local;
  private boolean dirty = false;

  @Inject
  public YoctoExtensionManagerPresenter(
      YoctoExtensionManagerView view,
      DialogFactory dialogFactory,
      PreferencesManager preferencesManager,
      YoctoLocalizationConstant local) {
    super("Yocto SDK", "Yocto Settings");
    this.view = view;
    this.dialogFactory = dialogFactory;
    this.local = local;
    this.preferencesManager = preferencesManager;
    if (preferencesManager.getValue(preferenceName) == null
        || "".equals(preferencesManager.getValue(preferenceName))
        || "{}".equals(preferencesManager.getValue(preferenceName))) {
      this.yoctoSdkPreferences = new ArrayList<YoctoSdkPreferences>();
    } else {
      this.yoctoSdkPreferences =
          jsonToYoctoSdkPreferences(this.preferencesManager.getValue(this.preferenceName));
    }
    this.view.setDelegate(this);
    refreshTable();
  }

  /** {@inheritDoc} */
  @Override
  public void onDeleteClicked(@NotNull final YoctoSdkPreferences pairKey) {
    dialogFactory
        .createConfirmDialog(
            local.deleteSdk(),
            local.deleteSdkLabel(),
            new ConfirmCallback() {
              @Override
              public void accepted() {
                deleteKeyFromPreferences(pairKey);
                refreshTable();
                nowDirty();
              }
            },
            getCancelCallback())
        .show();
  }

  /** {@inheritDoc} */
  @Override
  public void onSelectClicked(@NotNull final YoctoSdkPreferences pairKey) {

    for (YoctoSdkPreferences pref : this.yoctoSdkPreferences) {
      pref.setSelected(false);
    }

    pairKey.setSelected(true);
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
   * Delete a preference from Yaml Preferences
   *
   * @param pref The preference you would like to delete
   */
  private void deleteKeyFromPreferences(final YoctoSdkPreferences pref) {
    this.yoctoSdkPreferences.remove(pref);
  }

  /**
   * Add a SDK to your workspace
   *
   * @param sdk The SDK you would like to download and install
   */
  private void addUrlToSdkPreferences(YoctoSdkPreferences pref, String url) {
    pref.setUrl(url);

    for (YoctoSdkPreferences currPref : this.yoctoSdkPreferences) {
      if (pref.getName().equals(currPref.getName())
          && pref.getVersion().equals(currPref.getVersion())) {
        return;
      }
    }
    this.yoctoSdkPreferences.add(pref);
  }

  private void addNameToSdkPreferences(YoctoSdkPreferences pref, String name) {
    pref.setName(name);
  }

  private void addVersionToSdkPreferences(YoctoSdkPreferences pref, String version) {
    pref.setVersion(version);
  }

  /**
   * Converts json string to list of Yaml Preferences
   *
   * @param jsonStr The json string to turn into the list of Yaml Preferences
   * @return List of Yaml Preferences
   */
  private List<YoctoSdkPreferences> jsonToYoctoSdkPreferences(String jsonStr) {
    ArrayList<YoctoSdkPreferences> yoctoSdkPreferences = new ArrayList<YoctoSdkPreferences>();
    JsonObject parsedJson = Json.parse(jsonStr);

    for (String name : parsedJson.keys()) {
      JsonObject nameObj = parsedJson.getObject(name);

      for (String version : nameObj.keys()) {
        JsonObject prefObj = nameObj.getObject(version);
        YoctoSdkPreferences pref = new YoctoSdkPreferences();

        pref.setName(name);
        pref.setVersion(version);
        pref.setUrl(prefObj.getString("url"));
        pref.setSelected(prefObj.getBoolean("selected"));

        yoctoSdkPreferences.add(pref);
      }
    }

    return yoctoSdkPreferences;
  }

  /** {@inheritDoc} */
  @Override
  public void onAddSdkClicked() {

    YoctoSdkPreferences pref = new YoctoSdkPreferences();

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

  //  /** Send the schemas to the Language Server */
  //  private void setSchemas() {
  //    Map<String, List<String>> schemaMap =
  //        PreferenceHelper.yoctoPreferenceToMap(this.yoctoSdkPreferences);
  //
  //    Map<String, String> jsonSchemaMap = new HashMap<String, String>();
  //    for (Map.Entry<String, List<String>> entry : schemaMap.entrySet()) {
  //      jsonSchemaMap.put(entry.getKey(), prefListToJsonArray(entry.getValue()).toString());
  //    }
  //
  //    if (schemaMap != null) {
  //      //      service.putSchemas(jsonSchemaMap);
  //    }
  //  }

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

  /** Refresh YoctoSdkPreferencesCellTable */
  private void refreshTable() {
    view.setPairs(this.yoctoSdkPreferences);
  }

  /**
   * Convert a list of strings to JSON
   *
   * @param yamlStringList the List of Strings you want to convert to JSON
   * @return JSONArray of yoctoSdkPreferences
   */
  private JSONArray prefListToJsonArray(List<String> yamlStringList) {
    JSONArray yamlPreferenceJsonArr = new JSONArray();
    for (int arrNum = 0; arrNum < yamlStringList.size(); arrNum++) {
      yamlPreferenceJsonArr.set(arrNum, new JSONString(yamlStringList.get(arrNum)));
    }

    return yamlPreferenceJsonArr;
  }

  /**
   * Convert YoctoSdkPreferences's to JSON
   *
   * @param yoctoSdkPreferencesList
   * @return String of yoctoSdkPreferences
   */
  private String yoctoSdkPreferencesToJson(List<YoctoSdkPreferences> yoctoSdkPreferencesList) {

    JSONObject mainObj = new JSONObject();

    for (YoctoSdkPreferences pref : yoctoSdkPreferencesList) {
      JSONObject nameObj;

      // If not yet added create it
      if (!mainObj.containsKey(pref.getName())) {
        nameObj = new JSONObject();
        mainObj.put(pref.getName(), nameObj);
      }

      nameObj = mainObj.get(pref.getName()).isObject();

      JSONObject versionObj = new JSONObject();

      if (nameObj.containsKey(pref.getVersion())) {
        continue;
      }

      versionObj.put("name", new JSONString(pref.getName()));
      versionObj.put("version", new JSONString(pref.getVersion()));
      versionObj.put("url", new JSONString(pref.getUrl()));
      versionObj.put("selected", JSONBoolean.getInstance(pref.isSelected()));

      nameObj.put(pref.getVersion(), versionObj);
    }

    return mainObj.toString();
  }

  @Override
  public void storeChanges() {
    //    setSchemas();

    this.preferencesManager.setValue(
        this.preferenceName, yoctoSdkPreferencesToJson(this.yoctoSdkPreferences));
    this.preferencesManager.flushPreferences();
    dirty = false;
    delegate.onDirtyChanged();
  }

  @Override
  public void revertChanges() {}
}

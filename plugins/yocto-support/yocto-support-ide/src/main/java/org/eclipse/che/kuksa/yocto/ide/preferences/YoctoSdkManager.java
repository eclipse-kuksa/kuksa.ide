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
import com.google.inject.Inject;
import com.google.inject.Singleton;
import elemental.json.Json;
import elemental.json.JsonObject;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.che.ide.api.command.CommandImpl;
import org.eclipse.che.ide.api.notification.NotificationManager;
import org.eclipse.che.ide.api.notification.StatusNotification;
import org.eclipse.che.kuksa.yocto.ide.YoctoConstants;
import org.eclipse.che.kuksa.yocto.ide.YoctoLocalizationConstant;
import org.eclipse.che.kuksa.yocto.ide.YoctoSdk;
import org.eclipse.che.kuksa.yocto.ide.command.CustomSilentCommandExecutor;
import org.eclipse.che.kuksa.yocto.ide.macro.YoctoSdkEnvPathMacro;
import org.eclipse.che.kuksa.yocto.ide.macro.YoctoSdkPathMacro;

// Initially taken from plugin-yaml

/**
 * The presenter for managing the YoctoSdkCellTable in YoctoExtensionManagerView.
 *
 * @author Joshua Pinkney
 * @author Pedro Cuadra
 */
@Singleton
public class YoctoSdkManager {

  //  private static final Logger LOG = LoggerFactory.getLogger(YoctoSdkManager.class);
  private List<YoctoSdk> yoctoSdkList;
  private YoctoLocalizationConstant local;
  private final NotificationManager notificationManager;
  private final YoctoSdkEnvPathMacro yoctoSdkEnvPathMacro;
  private final YoctoSdkPathMacro yoctoSdkPathMacro;
  private CustomSilentCommandExecutor commandExecutor;
  private static final String SDK_TMP_PATH = "/projects/.sdk/tmp";

  @Inject
  public YoctoSdkManager(
      YoctoLocalizationConstant local,
      NotificationManager notificationManager,
      YoctoSdkEnvPathMacro yoctoSdkEnvPathMacro,
      YoctoSdkPathMacro yoctoSdkPathMacro,
      CustomSilentCommandExecutor commandExecutor) {
    this.local = local;
    this.notificationManager = notificationManager;
    this.yoctoSdkList = new ArrayList<YoctoSdk>();
    this.yoctoSdkEnvPathMacro = yoctoSdkEnvPathMacro;
    this.yoctoSdkPathMacro = yoctoSdkPathMacro;
    this.commandExecutor = commandExecutor;
  }

  private String getInstallDirectory(final YoctoSdk pref) {
    return YoctoConstants.SDK_ROOT_PATH + "/" + pref.getName() + "/" + pref.getVersion();
  }

  private String getDownloadPath(final YoctoSdk pref) {
    return YoctoSdkManager.SDK_TMP_PATH + "/" + pref.getName() + "_" + pref.getVersion() + ".sh";
  }

  private void installSdk(final YoctoSdk pref) {
    String cmdLine = "";

    cmdLine = "mkdir -p " + YoctoSdkManager.SDK_TMP_PATH + " && ";
    cmdLine += "cd " + YoctoSdkManager.SDK_TMP_PATH + " && ";
    cmdLine +=
        "wget --quiet -O " + getDownloadPath(pref) + " " + pref.getUrl() + " -o /dev/null && ";
    // Check if was downloaded successfully
    cmdLine += "`[ -s " + getDownloadPath(pref) + " ] || exit 1` && ";
    cmdLine += "chmod +x " + getDownloadPath(pref) + " && ";
    cmdLine += getDownloadPath(pref) + " -y -d " + getInstallDirectory(pref);

    CommandImpl installCmd = new CommandImpl("Install SDK", cmdLine, "yocto");

    this.commandExecutor.executeCommand(
        installCmd,
        new StatusNotification(
            "Installing SDK " + pref.getName() + " (" + pref.getVersion() + ")",
            StatusNotification.Status.PROGRESS,
            StatusNotification.DisplayMode.FLOAT_MODE));
  }

  private boolean compareYoctoSdk(YoctoSdk pref_1, YoctoSdk pref_2) {
    return pref_1.getName().equals(pref_2.getName())
        && pref_1.getVersion().equals(pref_2.getVersion());
  }

  public boolean addSdk(final YoctoSdk pref) {

    for (YoctoSdk curr_pref : this.yoctoSdkList) {
      if (this.compareYoctoSdk(curr_pref, pref)) {
        notificationManager.notify(
            "Error " + pref.getName() + " " + pref.getVersion() + " already added",
            StatusNotification.Status.FAIL,
            StatusNotification.DisplayMode.FLOAT_MODE);
        return false;
      }
    }

    //    downloadSdk(pref);
    installSdk(pref);
    this.yoctoSdkList.add(pref);

    // To force notification throw
    if (this.yoctoSdkList.size() == 1) {
      this.yoctoSdkEnvPathMacro.deselect();
      this.yoctoSdkPathMacro.deselect();
    }

    return true;
  }

  private void uninstallSdk(final YoctoSdk pref) {
    String cmdLine = "rm -rf " + getInstallDirectory(pref) + " && ";

    cmdLine += "rm -rf " + getDownloadPath(pref);

    CommandImpl uninstallCmd = new CommandImpl("Uninstall SDK", cmdLine, "yocto");

    this.commandExecutor.executeCommand(
        uninstallCmd,
        new StatusNotification(
            "Uninstalling SDK " + pref.getName() + " (" + pref.getVersion() + ")",
            StatusNotification.Status.PROGRESS,
            StatusNotification.DisplayMode.FLOAT_MODE));
  }

  /**
   * Delete a preference from Yaml Preferences
   *
   * @param pref The preference you would like to delete
   */
  public void removeSdk(final YoctoSdk pref) {
    if (pref.isSelected()) {
      this.yoctoSdkEnvPathMacro.deselect();
      this.yoctoSdkPathMacro.deselect();
    }
    this.uninstallSdk(pref);
    this.yoctoSdkList.remove(pref);
  }

  public boolean selectSdk(final YoctoSdk pref) {

    if (pref.isSelected()) {
      return false;
    }

    for (YoctoSdk curr_pref : this.yoctoSdkList) {
      curr_pref.setSelected(false);
    }

    notificationManager.notify(
        "Yocto SDK " + pref.getName() + " " + pref.getVersion() + " selected",
        StatusNotification.Status.SUCCESS,
        StatusNotification.DisplayMode.FLOAT_MODE);

    pref.setSelected(true);

    // Update the macros for expansion
    this.yoctoSdkEnvPathMacro.setSelected(pref);
    this.yoctoSdkPathMacro.setSelected(pref);

    return true;
  }

  public List<YoctoSdk> getAll() {
    return this.yoctoSdkList;
  }

  /**
   * Converts json string to list of Yaml Preferences
   *
   * @param jsonStr The json string to turn into the list of Yaml Preferences
   * @return List of Yaml Preferences
   */
  public void loadJsonString(String jsonStr) {
    JsonObject parsedJson = Json.parse(jsonStr);

    for (String name : parsedJson.keys()) {
      JsonObject nameObj = parsedJson.getObject(name);

      for (String version : nameObj.keys()) {
        JsonObject prefObj = nameObj.getObject(version);
        YoctoSdk pref = YoctoSdk.with(prefObj);
        this.yoctoSdkList.add(pref);

        if (pref.isSelected()) {
          this.yoctoSdkEnvPathMacro.setSelected(pref);
          this.yoctoSdkPathMacro.setSelected(pref);
        }
      }
    }
  }

  /**
   * Convert YoctoSdk's to JSON
   *
   * @param yoctoSdkPreferencesList
   * @return String of yoctoSdkPreferences
   */
  public String toJsonString() {

    JSONObject mainObj = new JSONObject();

    for (YoctoSdk pref : yoctoSdkList) {
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
}

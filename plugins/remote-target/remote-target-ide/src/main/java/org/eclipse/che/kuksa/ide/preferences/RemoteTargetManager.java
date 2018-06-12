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

// Initially taken from plugin-yaml

/**
 * The presenter for managing the RemoteTargetCellTable in YoctoExtensionManagerView.
 * @author Pedro Cuadra
 */
@Singleton
public class RemoteTargetManager {

  //  private static final Logger LOG = LoggerFactory.getLogger(RemoteTargetManager.class);
  private List<RemoteTarget> remoteTargetList;
  private YoctoLocalizationConstant local;
  private NotificationManager notificationManager;
  private final RemoteTargetHostnameMacro remoteTargetHostnameMacro;

  @Inject
  public RemoteTargetManager(
      RemoteTargetLocalizationConstant local,
      NotificationManager notificationManager,
      RemoteTargetHostnameMacro remoteTargetHostnameMacro) {
    this.local = local;
    this.notificationManager = notificationManager;
    this.remoteTargetList = new ArrayList<RemoteTarget>();
    this.remoteTargetHostnameMacro = remoteTargetHostnameMacro;
  }

  private boolean compareRemoteTarget(RemoteTarget pref_1, RemoteTarget pref_2) {
    return pref_1.getHostname().equals(pref_2.getHostname())
        && pref_1.getUser().equals(pref_2.getUser());
  }

  public boolean addRemoteTarget(final RemoteTarget pref) {

    for (RemoteTarget curr_pref : this.remoteTargetList) {
      if (this.compareRemoteTarget(curr_pref, pref)) {
        notificationManager.notify(
            "Error " + pref.getUser() + "@" + pref.getHostname() + " already added",
            StatusNotification.Status.FAIL,
            StatusNotification.DisplayMode.FLOAT_MODE);
        return false;
      }
    }

    this.remoteTargetList.add(pref);

    return true;
  }

  /**
   * Delete a preference from Yaml Preferences
   *
   * @param pref The preference you would like to delete
   */
  public void removeSdk(final RemoteTarget pref) {
    this.remoteTargetList.remove(pref);
  }

  public boolean selectRemoteTarget(final RemoteTarget pref) {

    if (pref.isSelected()) {
      return false;
    }

    for (RemoteTarget curr_pref : this.remoteTargetList) {
      curr_pref.setSelected(false);
    }

    notificationManager.notify(
        "Yocto SDK " + pref.getHostname() + " " + pref.getUser() + " selected",
        StatusNotification.Status.SUCCESS,
        StatusNotification.DisplayMode.FLOAT_MODE);

    pref.setSelected(true);

    // Update the macros for expansion

    return true;
  }

  public List<RemoteTarget> getAll() {
    return this.remoteTargetList;
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

      for (String user : nameObj.keys()) {
        JsonObject prefObj = nameObj.getObject(user);
        RemoteTarget pref = RemoteTarget.with(prefObj);
        this.remoteTargetList.add(pref);

        if (pref.isSelected()) {
          this.yoctoSdkEnvPathMacro.setSelectedSdk(pref);
          this.yoctoSdkPathMacro.setSelectedSdk(pref);
        }
      }
    }
  }

  /**
   * Convert RemoteTarget's to JSON
   *
   * @param yoctoSdkPreferencesList
   * @return String of yoctoSdkPreferences
   */
  public String toJsonString() {

    JSONObject mainObj = new JSONObject();

    for (RemoteTarget pref : remoteTargetList) {
      JSONObject nameObj;

      // If not yet added create it
      if (!mainObj.containsKey(pref.getHostname())) {
        nameObj = new JSONObject();
        mainObj.put(pref.getHostname(), nameObj);
      }

      nameObj = mainObj.get(pref.getUser()).isObject();

      if (nameObj.containsKey(pref.getUser())) {
        continue;
      }

      nameObj.put(pref.getUser(), pref.toJson());
    }

    return mainObj.toString();
  }
}

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
package org.eclipse.che.kuksa.ide;

import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import elemental.json.JsonObject;

/**
 * Remote Target object
 *
 * @author Pedro Cuadra
 */
public class RemoteTarget {

  private String hostname;
  private String user;
  private boolean selected;

  public RemoteTarget(String hostname, String user) {
    this.hostname = hostname;
    this.user = user;
    selected = false;
  }

  public static RemoteTarget with(JsonObject prefObj) {
    RemoteTarget newObj = new RemoteTarget();
    newObj.setHostname(prefObj.getString("hostname"));
    newObj.setUser(prefObj.getString("user"));
    newObj.setSelected(prefObj.getBoolean("selected"));
    return newObj;
  }

  public JSONObject toJson() {
    JSONObject versionObj = new JSONObject();

    versionObj.put("hostname", new JSONString(this.getHostname()));
    versionObj.put("user", new JSONString(this.getUser()));
    versionObj.put("selected", JSONBoolean.getInstance(this.isSelected()));

    return versionObj;
  }

  public RemoteTarget() {
    this.hostname = "127.0.0.1";
    this.user = "user";
  }

  public String getHostname() {
    return this.hostname;
  }

  public String getUser() {
    return this.user;
  }

  public void setHostname(String hostname) {
    this.hostname = hostname;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public boolean isSelected() {
    return this.selected;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
  }
}

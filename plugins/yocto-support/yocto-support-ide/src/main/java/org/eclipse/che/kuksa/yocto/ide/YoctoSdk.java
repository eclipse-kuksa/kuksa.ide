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
package org.eclipse.che.kuksa.yocto.ide;

import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import elemental.json.JsonObject;

/**
 * Yocto SDK object
 *
 * @author Pedro Cuadra
 */
public class YoctoSdk {

  private String version;
  private String name;
  private String url;
  private String glob;
  private boolean selected;

  public YoctoSdk(String name, String version, String url, String glob) {
    this.url = url;
    this.glob = glob;
    this.version = version;
    this.name = name;
    selected = false;
  }

  public static YoctoSdk with(JsonObject prefObj) {
    YoctoSdk newObj = new YoctoSdk();
    newObj.setName(prefObj.getString("name"));
    newObj.setVersion(prefObj.getString("version"));
    newObj.setUrl(prefObj.getString("url"));
    newObj.setSelected(prefObj.getBoolean("selected"));
    return newObj;
  }

  public JSONObject toJson() {
    JSONObject versionObj = new JSONObject();

    versionObj.put("name", new JSONString(this.getName()));
    versionObj.put("version", new JSONString(this.getVersion()));
    versionObj.put("url", new JSONString(this.getUrl()));
    versionObj.put("selected", JSONBoolean.getInstance(this.isSelected()));

    return versionObj;
  }

  public YoctoSdk() {
    this.url = "http://unknown.sh";
    this.glob = "/*";
    this.version = "0.0.0";
    this.name = "unknown";
  }

  public String getName() {
    return this.name;
  }

  public String getVersion() {
    return this.version;
  }

  public String getUrl() {
    return this.url;
  }

  public String getGlob() {
    return this.glob;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public void setGlob(String glob) {
    this.glob = glob;
  }

  public boolean isSelected() {
    return this.selected;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
  }
}

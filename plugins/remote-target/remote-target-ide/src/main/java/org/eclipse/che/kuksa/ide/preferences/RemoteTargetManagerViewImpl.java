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

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.*;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.List;
import javax.validation.constraints.NotNull;
import org.eclipse.che.ide.ui.cellview.CellTableResources;
import org.eclipse.che.kuksa.ide.RemoteTarget;
import org.eclipse.che.kuksa.ide.RemoteTargetLocalizationConstant;

/**
 * The implementation of {@link RemoteTargetManagerView}.
 *
 * @author Joshua Pinkney
 */
@Singleton
public class RemoteTargetManagerViewImpl extends Composite implements RemoteTargetManagerView {
  interface RemoteTargetManagerViewImplUiBinder
      extends UiBinder<Widget, RemoteTargetManagerViewImpl> {}

  private static RemoteTargetManagerViewImplUiBinder uiBinder =
      GWT.create(RemoteTargetManagerViewImplUiBinder.class);

  @UiField Button addRemoteTarget;

  @UiField(provided = true)
  CellTable<RemoteTarget> remoteTargetPreferenceCellTable;

  @UiField Label headerUiMsg;

  private ActionDelegate delegate;

  private RemoteTargetLocalizationConstant local;

  @Inject
  protected RemoteTargetManagerViewImpl(
      CellTableResources res, RemoteTargetLocalizationConstant local) {
    this.local = local;
    initRemoteTargetTable(res);
    initWidget(uiBinder.createAndBindUi(this));
  }

  /**
   * Creates table which contains list of available preferences
   *
   * @param res Celltable resources
   */
  private void initRemoteTargetTable(final CellTable.Resources res) {

    remoteTargetPreferenceCellTable = new CellTable<RemoteTarget>(20, res);
    Column<RemoteTarget, String> nameColumn =
        new Column<RemoteTarget, String>(new TextCell()) {
          @Override
          public String getValue(RemoteTarget object) {
            return object.getHostname();
          }

          @Override
          public void render(Context context, RemoteTarget object, SafeHtmlBuilder sb) {
            sb.appendHtmlConstant(
                "<div id=\""
                    + UIObject.DEBUG_ID_PREFIX
                    + "-preferences-cellTable-url-"
                    + context.getIndex()
                    + "\">");
            super.render(context, object, sb);
          }
        };

    Column<RemoteTarget, String> userColumn =
        new Column<RemoteTarget, String>(new TextCell()) {
          @Override
          public String getValue(RemoteTarget object) {
            return object.getUser();
          }

          @Override
          public void render(Context context, RemoteTarget object, SafeHtmlBuilder sb) {
            sb.appendHtmlConstant(
                "<div id=\""
                    + UIObject.DEBUG_ID_PREFIX
                    + "-preferences-cellTable-glob-"
                    + context.getIndex()
                    + "\">");
            if (object != null) {
              super.render(context, object, sb);
            }
          }
        };

    Column<RemoteTarget, String> selectPreferenceColumn =
        new Column<RemoteTarget, String>(new ButtonCell()) {
          @Override
          public String getValue(RemoteTarget object) {
            if (object.isSelected()) {
              return "";
            }
            return "Select";
          }

          @Override
          public void render(Context context, RemoteTarget object, SafeHtmlBuilder sb) {
            String visibility = object.isSelected() ? " style=\"visibility: hidden\" " : "";
            sb.appendHtmlConstant(
                "<div id=\""
                    + UIObject.DEBUG_ID_PREFIX
                    + "-preferences-cellTable-select-"
                    + context.getIndex()
                    + "\" "
                    + visibility
                    + ">");
            super.render(context, object, sb);
          }
        };

    // Creates handler on button clicked
    selectPreferenceColumn.setFieldUpdater(
        new FieldUpdater<RemoteTarget, String>() {
          @Override
          public void update(int index, RemoteTarget object, String value) {
            delegate.onSelectClicked(object);
          }
        });

    Column<RemoteTarget, String> deletePreferenceColumn =
        new Column<RemoteTarget, String>(new ButtonCell()) {
          @Override
          public String getValue(RemoteTarget object) {
            return "Delete";
          }

          @Override
          public void render(Context context, RemoteTarget object, SafeHtmlBuilder sb) {

            sb.appendHtmlConstant(
                "<div id=\""
                    + UIObject.DEBUG_ID_PREFIX
                    + "-preferences-cellTable-delete-"
                    + context.getIndex()
                    + "\" >");
            super.render(context, object, sb);
          }
        };

    // Creates handler on button clicked
    deletePreferenceColumn.setFieldUpdater(
        new FieldUpdater<RemoteTarget, String>() {
          @Override
          public void update(int index, RemoteTarget object, String value) {
            delegate.onDeleteClicked(object);
          }
        });

    remoteTargetPreferenceCellTable.addColumn(nameColumn, local.hostnameColumnHeader());
    remoteTargetPreferenceCellTable.addColumn(userColumn, local.userColumnHeader());
    //    remoteTargetPreferenceCellTable.addColumn(urlColumn, local.urlColumnHeader());
    remoteTargetPreferenceCellTable.addColumn(selectPreferenceColumn, local.selectColumnHeader());
    remoteTargetPreferenceCellTable.addColumn(deletePreferenceColumn, local.deleteColumnHeader());
    remoteTargetPreferenceCellTable.setWidth("100%", true);
    remoteTargetPreferenceCellTable.setColumnWidth(nameColumn, 45, Style.Unit.PCT);
    remoteTargetPreferenceCellTable.setColumnWidth(userColumn, 30, Style.Unit.PCT);
    //    remoteTargetPreferenceCellTable.setColumnWidth(urlColumn, 30, Style.Unit.PCT);
    remoteTargetPreferenceCellTable.setColumnWidth(selectPreferenceColumn, 40, Style.Unit.PCT);
    remoteTargetPreferenceCellTable.setColumnWidth(deletePreferenceColumn, 40, Style.Unit.PCT);

    // don't show loading indicator
    remoteTargetPreferenceCellTable.setLoadingIndicator(null);
  }

  /** {@inheritDoc} */
  @Override
  public void setPairs(@NotNull List<RemoteTarget> pairs) {
    this.remoteTargetPreferenceCellTable.setRowData(pairs);
  }

  /** {@inheritDoc} */
  @Override
  public void setDelegate(ActionDelegate delegate) {
    this.delegate = delegate;
  }

  @UiHandler("addRemoteTarget")
  public void onAddRemoteTargetClicked(ClickEvent event) {
    delegate.onAddRemoteTargetClicked();
  }
}

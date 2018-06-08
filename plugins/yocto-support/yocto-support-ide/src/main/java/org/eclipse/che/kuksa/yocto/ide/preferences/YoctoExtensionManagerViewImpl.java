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

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.EditTextCell;
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
import org.eclipse.che.kuksa.yocto.ide.YoctoLocalizationConstant;
import org.eclipse.che.kuksa.yocto.shared.YoctoSdk;

/**
 * The implementation of {@link YoctoExtensionManagerView}.
 *
 * @author Joshua Pinkney
 */
@Singleton
public class YoctoExtensionManagerViewImpl extends Composite implements YoctoExtensionManagerView {
  interface YoctoExtensionManagerViewImplUiBinder
      extends UiBinder<Widget, YoctoExtensionManagerViewImpl> {}

  private static YoctoExtensionManagerViewImplUiBinder uiBinder =
      GWT.create(YoctoExtensionManagerViewImplUiBinder.class);

  @UiField Button addSdk;

  @UiField(provided = true)
  CellTable<YoctoSdk> yoctoSdkPreferenceCellTable;

  @UiField Label headerUiMsg;

  private ActionDelegate delegate;

  private YoctoLocalizationConstant local;

  @Inject
  protected YoctoExtensionManagerViewImpl(CellTableResources res, YoctoLocalizationConstant local) {
    this.local = local;
    initYoctoExtensionTable(res);
    initWidget(uiBinder.createAndBindUi(this));
  }

  /**
   * Creates table which contains list of available preferences
   *
   * @param res Celltable resources
   */
  private void initYoctoExtensionTable(final CellTable.Resources res) {

    yoctoSdkPreferenceCellTable = new CellTable<YoctoSdk>(20, res);
    Column<YoctoSdk, String> nameColumn =
        new Column<YoctoSdk, String>(new EditTextCell()) {
          @Override
          public String getValue(YoctoSdk object) {
            return object.getName();
          }

          @Override
          public void render(Context context, YoctoSdk object, SafeHtmlBuilder sb) {
            sb.appendHtmlConstant(
                "<div id=\""
                    + UIObject.DEBUG_ID_PREFIX
                    + "-preferences-cellTable-url-"
                    + context.getIndex()
                    + "\">");
            super.render(context, object, sb);
          }
        };

    nameColumn.setFieldUpdater(
        new FieldUpdater<YoctoSdk, String>() {
          @Override
          public void update(int index, YoctoSdk object, String value) {
            object.setName(value);
            delegate.nowDirty();
          }
        });

    Column<YoctoSdk, String> versionColumn =
        new Column<YoctoSdk, String>(new EditTextCell()) {
          @Override
          public String getValue(YoctoSdk object) {
            return object.getVersion();
          }

          @Override
          public void render(Context context, YoctoSdk object, SafeHtmlBuilder sb) {
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

    versionColumn.setFieldUpdater(
        new FieldUpdater<YoctoSdk, String>() {
          @Override
          public void update(int index, YoctoSdk object, String value) {
            object.setVersion(value);
            delegate.nowDirty();
          }
        });

    Column<YoctoSdk, String> urlColumn =
        new Column<YoctoSdk, String>(new TextCell()) {
          @Override
          public String getValue(YoctoSdk object) {
            return object.getUrl();
          }

          @Override
          public void render(Context context, YoctoSdk object, SafeHtmlBuilder sb) {
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

    urlColumn.setFieldUpdater(
        new FieldUpdater<YoctoSdk, String>() {
          @Override
          public void update(int index, YoctoSdk object, String value) {
            //            object.setUrl(value);
            //            delegate.refreshTable();
          }
        });

    Column<YoctoSdk, String> selectPreferenceColumn =
        new Column<YoctoSdk, String>(new ButtonCell()) {
          @Override
          public String getValue(YoctoSdk object) {
            if (object.isSelected()) {
              return "";
            }
            return "Select";
          }

          @Override
          public void render(Context context, YoctoSdk object, SafeHtmlBuilder sb) {
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
        new FieldUpdater<YoctoSdk, String>() {
          @Override
          public void update(int index, YoctoSdk object, String value) {
            delegate.onSelectClicked(object);
          }
        });

    Column<YoctoSdk, String> deletePreferenceColumn =
        new Column<YoctoSdk, String>(new ButtonCell()) {
          @Override
          public String getValue(YoctoSdk object) {
            return "Delete";
          }

          @Override
          public void render(Context context, YoctoSdk object, SafeHtmlBuilder sb) {
              
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
        new FieldUpdater<YoctoSdk, String>() {
          @Override
          public void update(int index, YoctoSdk object, String value) {
            delegate.onDeleteClicked(object);
          }
        });

    yoctoSdkPreferenceCellTable.addColumn(nameColumn, local.sdkColumnHeader());
    yoctoSdkPreferenceCellTable.addColumn(versionColumn, local.versionColumnHeader());
    yoctoSdkPreferenceCellTable.addColumn(urlColumn, local.urlColumnHeader());
    yoctoSdkPreferenceCellTable.addColumn(selectPreferenceColumn, local.selectColumnHeader());
    yoctoSdkPreferenceCellTable.addColumn(deletePreferenceColumn, local.deleteColumnHeader());
    yoctoSdkPreferenceCellTable.setWidth("100%", true);
    yoctoSdkPreferenceCellTable.setColumnWidth(nameColumn, 45, Style.Unit.PCT);
    yoctoSdkPreferenceCellTable.setColumnWidth(versionColumn, 30, Style.Unit.PCT);
    yoctoSdkPreferenceCellTable.setColumnWidth(urlColumn, 30, Style.Unit.PCT);
    yoctoSdkPreferenceCellTable.setColumnWidth(selectPreferenceColumn, 25, Style.Unit.PCT);
    yoctoSdkPreferenceCellTable.setColumnWidth(deletePreferenceColumn, 25, Style.Unit.PCT);

    // don't show loading indicator
    yoctoSdkPreferenceCellTable.setLoadingIndicator(null);
  }

  /** {@inheritDoc} */
  @Override
  public void setPairs(@NotNull List<YoctoSdk> pairs) {
    this.yoctoSdkPreferenceCellTable.setRowData(pairs);
  }

  /** {@inheritDoc} */
  @Override
  public void setDelegate(ActionDelegate delegate) {
    this.delegate = delegate;
  }

  @UiHandler("addSdk")
  public void onAddSdkClicked(ClickEvent event) {
    delegate.onAddSdkClicked();
  }
}

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

import static org.eclipse.che.ide.util.dom.DomUtils.isWidgetOrChildFocused;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import javax.validation.constraints.NotNull;
import org.eclipse.che.ide.ui.window.Window;

/**
 * Implementation of the Yocto SDK input dialog view.
 *
 * @author Pedro Cuadra
 */
public class RemoteTargetInputDialogViewImpl extends Window implements RemoteTargetInputDialogView {

  /** The UI binder instance. */
  private static ConfirmWindowUiBinder uiBinder = GWT.create(ConfirmWindowUiBinder.class);
  /** The window footer. */
  private final RemoteTargetInputDialogFooter footer;

  @UiField Label nameLabel;
  @UiField TextBox nameValue;
  @UiField Label versionLabel;
  @UiField TextBox versionValue;
  @UiField Label urlLabel;
  @UiField TextBox urlValue;
  @UiField Label errorHint;

  private ActionDelegate delegate;
  private int selectionStartIndex;
  private int selectionLength;

  @Inject
  public RemoteTargetInputDialogViewImpl(final @NotNull RemoteTargetInputDialogFooter footer) {
    Widget widget = uiBinder.createAndBindUi(this);
    setWidget(widget);

    this.footer = footer;
    addFooterWidget(footer);

    ensureDebugId("askValueDialog-window");
    nameValue.ensureDebugId("askNameValueDialog-textBox");
    versionValue.ensureDebugId("askVersionValueDialog-textBox");
    urlValue.ensureDebugId("askUrlValueDialog-textBox");

    setTitleCaption("Add new SDK");
  }

  @Override
  public void setDelegate(final ActionDelegate delegate) {
    this.delegate = delegate;
    this.footer.setDelegate(this.delegate);
  }

  @Override
  public void onEnterPress(NativeEvent evt) {
    delegate.onEnterClicked();
  }

  @Override
  public void showDialog() {
    show();
  }

  @Override
  public void setTitleCaption(String title) {
    setTitle(title);
  }

  @Override
  protected void onShow() {
    nameValue.setSelectionRange(selectionStartIndex, selectionLength);
    versionValue.setSelectionRange(selectionStartIndex, selectionLength);
    urlValue.setSelectionRange(selectionStartIndex, selectionLength);
    new Timer() {
      @Override
      public void run() {
        nameValue.setFocus(true);
      }
    }.schedule(300);
  }

  @Override
  public void closeDialog() {
    this.hide();
  }

  @Override
  public void setContent(final String label) {
    this.nameLabel.setText("Name:");
    this.versionLabel.setText("Version:");
    this.urlLabel.setText("Download Link:");
  }

  @Override
  public void setValue(String value) {
    //    this.value.setText(value);
  }

  @Override
  public void clear() {
    nameValue.setValue("sdk-name");
    versionValue.setValue("x.x.x");
    urlValue.setValue("https://sdk_install_script.sh");
  }

  @Override
  public void setOkButtonLabel(String label) {
    footer.getOkButton().setText(label);
  }

  @Override
  public String getName() {
    return nameValue.getValue();
  }

  @Override
  public String getVersion() {
    return versionValue.getValue();
  }

  @Override
  public String getUrl() {
    return urlValue.getValue();
  }

  @Override
  public void setSelectionStartIndex(int selectionStartIndex) {
    this.selectionStartIndex = selectionStartIndex;
  }

  @Override
  public void setSelectionLength(int selectionLength) {
    this.selectionLength = selectionLength;
  }

  @Override
  public void showErrorHint(String text) {
    errorHint.setText(text);
    footer.getOkButton().setEnabled(false);
  }

  @Override
  public void hideErrorHint() {
    errorHint.setText("");
    footer.getOkButton().setEnabled(true);
  }

  @Override
  public boolean isOkButtonInFocus() {
    return isWidgetOrChildFocused(footer.okButton);
  }

  @Override
  public boolean isCancelButtonInFocus() {
    return isWidgetOrChildFocused(footer.cancelButton);
  }

  @UiHandler("nameValue")
  void onNameKeyUp(KeyUpEvent event) {
    delegate.inputValueChanged(RemoteTargetInputDialogView.NAME_FIELD);
  }

  @UiHandler("versionValue")
  void onVersionKeyUp(KeyUpEvent event) {
    delegate.inputValueChanged(RemoteTargetInputDialogView.VERSION_FIELD);
  }

  @UiHandler("urlValue")
  void onUrlKeyUp(KeyUpEvent event) {
    delegate.inputValueChanged(RemoteTargetInputDialogView.URL_FIELD);
  }

  /** The UI binder interface for this components. */
  interface ConfirmWindowUiBinder extends UiBinder<Widget, RemoteTargetInputDialogViewImpl> {}
}

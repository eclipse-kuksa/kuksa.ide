package org.eclipse.che.kuksa.yocto.ide.preferences.dialog;

import org.eclipse.che.kuksa.yocto.shared.YoctoSdk;

public interface YoctoSdkCallback {
  /**
   * Action called when the user clicks on OK.
   *
   * @param value the string typed into input dialog
   */
  void accepted(YoctoSdk value);
}

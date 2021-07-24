package com.pubg.utils;

import android.app.AlertDialog;


/**
 * This is a interface that is used to show {@link AlertDialog}.
 */
public interface OneTimeDialogInterface {
    /**
     * This shows a alert dialog
     * @return <code>true</code> if the dialog has to be shown once more, <code>false</code> otherwise
     * @param builder
     */
    boolean OneTimeDialog(AlertDialog.Builder builder);
}

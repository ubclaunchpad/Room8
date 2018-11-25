package com.ubclaunchpad.room8;

import android.widget.TextView;

class PasswordsNotMatch extends Throwable {
    public PasswordsNotMatch(String errorMessage, TextView textView) {
        textView.setError(errorMessage);
        textView.requestFocus();
    }
}

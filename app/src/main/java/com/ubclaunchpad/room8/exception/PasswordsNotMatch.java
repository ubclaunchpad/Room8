package com.ubclaunchpad.room8.exception;

import android.widget.TextView;

public class PasswordsNotMatch extends Throwable {

    public PasswordsNotMatch(String errorMessage, TextView textView) {
        textView.setError(errorMessage);
        textView.requestFocus();
    }
}
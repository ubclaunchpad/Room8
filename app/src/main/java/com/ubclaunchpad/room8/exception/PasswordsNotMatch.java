package com.ubclaunchpad.room8.exception;

import android.widget.EditText;

public class PasswordsNotMatch extends Throwable {

    public PasswordsNotMatch(String errorMessage, EditText editText) {
        editText.setError(errorMessage);
        editText.requestFocus();
    }
}
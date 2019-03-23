package com.ubclaunchpad.room8.exception;

import android.widget.EditText;

public class EmptyValueException extends Exception {

    public EmptyValueException(String errorMessage, EditText editText) {
        editText.setError(errorMessage);
        editText.requestFocus();
    }
}

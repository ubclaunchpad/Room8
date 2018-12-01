package com.ubclaunchpad.room8.exception;

import android.widget.TextView;

public class EmptyValueException extends Exception {

    public EmptyValueException(String errorMessage, TextView textView) {
        textView.setError(errorMessage);
        textView.requestFocus();
    }
}

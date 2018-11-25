package com.ubclaunchpad.room8;

import android.widget.TextView;

class EmptyValueException extends Exception {

    public EmptyValueException(String errorMessage, TextView textView) {
        textView.setError(errorMessage);
        textView.requestFocus();
    }
}

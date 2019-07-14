package com.fafu.app.dfb.view;

import android.app.Dialog;
import android.content.Context;
import android.view.KeyEvent;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.fafu.app.dfb.R;

public class ProgressDialog extends Dialog {

    public ProgressDialog(@NonNull Context context, String text) {
        super(context, R.style.ProgressDialog);
        setContentView(R.layout.dialog_progress);
        setCanceledOnTouchOutside(false);
        setOnKeyListener((dialog, keyCode, event) -> keyCode == KeyEvent.KEYCODE_BACK);
        TextView textView = findViewById(R.id.dialogProgressText);
        textView.setText(text);
    }

    public void show() {
        super.show();
    }
}

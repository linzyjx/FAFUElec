package com.fafu.app.dfb.view;

import android.app.Dialog;
import android.content.Context;
import android.view.KeyEvent;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.fafu.app.dfb.R;

public class ProgressDialog extends Dialog {

    private TextView textView;

    public ProgressDialog(@NonNull Context context) {
        super(context, R.style.styleProgressDialog);
        setContentView(R.layout.dialog_progress);
        setCanceledOnTouchOutside(false);
        setOnKeyListener((dialog, keyCode, event) -> keyCode == KeyEvent.KEYCODE_BACK);
        textView = findViewById(R.id.dialogProgressText);
    }

    public void show(String text) {
        textView.setText(text);
        super.show();
    }
}

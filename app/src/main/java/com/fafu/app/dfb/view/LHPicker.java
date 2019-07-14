package com.fafu.app.dfb.view;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.contrarywind.adapter.WheelAdapter;
import com.contrarywind.listener.OnItemSelectedListener;
import com.contrarywind.view.WheelView;
import com.fafu.app.dfb.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LHPicker extends AlertDialog {

    private WheelView wheelView;

    private InWheelAdapter wheelAdapter;

    public LHPicker(Context context) {
        this(context, new ArrayList<>());
    }

    public LHPicker(Context context, List<String> data) {
        super(context, R.style.styleProgressDialog);
        setContentView(R.layout.dialog_picker);
        wheelAdapter = new InWheelAdapter(data);
        wheelView = findViewById(R.id.wheelView);
        Objects.requireNonNull(wheelView).setAdapter(wheelAdapter);
        setCancelable(false);
        Objects.requireNonNull((TextView) findViewById(R.id.tv_finish))
                .setOnClickListener(l -> cancel());
    }

    public void setWheelData(List<String> data) {
        wheelAdapter.updateData(data);
        Objects.requireNonNull(wheelView).setAdapter(wheelAdapter);
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        wheelView.setOnItemSelectedListener(listener);
    }

    private class InWheelAdapter implements WheelAdapter<String> {
        private List<String> data;

        InWheelAdapter(List<String> data) {
            this.data = data;
        }

        void updateData(List<String> data) {
            this.data = data;
        }

        @Override
        public int getItemsCount() {
            return data.size();
        }

        @Override
        public String getItem(int i) {
            return data.get(i);
        }

        @Override
        public int indexOf(String s) {
            return data.indexOf(s);
        }
    }
}

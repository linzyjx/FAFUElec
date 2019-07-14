package com.fafu.app.dfb.mvp.main;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.fafu.app.dfb.R;
import com.fafu.app.dfb.mvp.base.BaseActivity;
import com.fafu.app.dfb.view.ProgressDialog;

import java.util.List;

public class MainActivity extends BaseActivity<MainContract.Presenter>
        implements MainContract.View, View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private ProgressDialog progress;

    private TextView xqTv;
    private TextView ldTv;
    private TextView lcTv;

    private EditText roomEt;
    private TextView elecTv;

    private OptionsPickerView<String> xqOpv;
    private OptionsPickerView<String> ldOpv;
    private OptionsPickerView<String> lcOpv;

    private List<String> xqData;
    private List<String> ldData;
    private List<String> lcData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progress = new ProgressDialog(this);

        xqTv = findViewById(R.id.xqTV);
        xqTv.setOnClickListener(this);
        ldTv = findViewById(R.id.ldTV);
        ldTv.setOnClickListener(this);
        lcTv = findViewById(R.id.lcTV);
        lcTv.setOnClickListener(this);

        roomEt = findViewById(R.id.roomET);
        elecTv = findViewById(R.id.elecTV);
        elecTv.setOnClickListener(this);

        xqOpv =
                new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {
                    mPresenter.onXQSelect(xqData.get(options1));
                    xqTv.setText(xqData.get(options1));
                })
                .setSubmitText("确认")
                .setSubmitColor(Color.BLACK)
                .setTitleText("选择校区")
                .isDialog(true)
                .setSelectOptions(0)
                .setOutSideCancelable(false)
                .build();
        ldOpv =
                new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {
                    mPresenter.onLDSelect(ldData.get(options1));
                    ldTv.setText(ldData.get(options1));
                })
                .setSubmitText("确认")
                .setSubmitColor(Color.BLACK)
                .setTitleText("选择楼栋")
                .isDialog(true)
                .setSelectOptions(0)
                .setOutSideCancelable(false)
                .build();
        lcOpv =
                new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {
                    mPresenter.onLCSelect(lcData.get(options1));
                    lcTv.setText(lcData.get(options1));
                })
                .setSubmitText("确认")
                .setSubmitColor(Color.BLACK)
                .setTitleText("选择楼层")
                .isDialog(true)
                .setSelectOptions(0)
                .setOutSideCancelable(false)
                .build();

        RadioGroup rg = findViewById(R.id.radioGroup);
        rg.setOnCheckedChangeListener(this);
        mPresenter = new MainPresenter(this);
    }

    public void setSelectorView(int i, List<String> list) {
        if (i == 1) {
            xqTv.setVisibility(View.VISIBLE);
            xqData = list;
            xqOpv.setPicker(list);
        } else if (i == 2) {
            ldTv.setVisibility(View.VISIBLE);
            ldData = list;
            ldOpv.setPicker(list);
        } else if (i == 3) {
            lcTv.setVisibility(View.VISIBLE);
            lcData = list;
            lcOpv.setPicker(list);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb1:
                mPresenter.onDKSelect(1);
                break;
            case R.id.rb2:
                mPresenter.onDKSelect(2);
                break;
            case R.id.rb3:
                mPresenter.onDKSelect(3);
                break;
            case R.id.rb4:
                mPresenter.onDKSelect(4);
                break;
        }
    }

    @Override
    public void clearViewData() {
        xqTv.setVisibility(View.GONE);
        xqTv.setText("");
        ldTv.setVisibility(View.GONE);
        ldTv.setText("");
        lcTv.setVisibility(View.GONE);
        lcTv.setText("");
        roomEt.setVisibility(View.GONE);
        roomEt.setText("");
        elecTv.setVisibility(View.GONE);
        elecTv.setText("");
    }

    @Override
    public void setElecText(String text) {
        elecTv.setText(text);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.xqTV:
                xqOpv.show();
                break;
            case R.id.ldTV:
                ldOpv.show();
                break;
            case R.id.lcTV:
                lcOpv.show();
                break;
            case R.id.elecTV:
                mPresenter.checkElec(roomEt.getText().toString());
                break;
        }
    }

    @Override
    public void showLoading() {
        progress.show("加载中");
    }

    @Override
    public void showElecCheckView() {
        roomEt.setVisibility(View.VISIBLE);
        elecTv.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progress.cancel();
    }
}

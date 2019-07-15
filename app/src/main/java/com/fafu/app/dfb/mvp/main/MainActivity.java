package com.fafu.app.dfb.mvp.main;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.fafu.app.dfb.R;
import com.fafu.app.dfb.mvp.base.BaseActivity;
import com.fafu.app.dfb.view.ProgressDialog;

import java.util.List;

public class MainActivity extends BaseActivity<MainContract.Presenter>
        implements MainContract.View, View.OnClickListener, RadioGroup.OnCheckedChangeListener,
        DialogInterface.OnClickListener {

    private ProgressDialog progress;

    private TextView xqTv;
    private TextView ldTv;
    private TextView lcTv;

    private TextView elecTv;
    private TextView snoTv;

    private TextView balanceTv;
    private EditText priceEt;
    private EditText roomEt;
    private Button payBtn;

    private OptionsPickerView<String> xqOpv;
    private OptionsPickerView<String> ldOpv;
    private OptionsPickerView<String> lcOpv;

    private List<String> xqData;
    private List<String> ldData;
    private List<String> lcData;

    private AlertDialog confirmDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progress = new ProgressDialog(this, "加载中");

        xqTv = findViewById(R.id.xqTV);
        xqTv.setOnClickListener(this);
        ldTv = findViewById(R.id.ldTV);
        ldTv.setOnClickListener(this);
        lcTv = findViewById(R.id.lcTV);
        lcTv.setOnClickListener(this);

        snoTv = findViewById(R.id.accountTV);
        TextView quitOutTv = findViewById(R.id.quitTV);
        quitOutTv.setOnClickListener(this);

        balanceTv = findViewById(R.id.balanceTV);
        ImageView balanceBtn = findViewById(R.id.balanceBtn);
        balanceBtn.setOnClickListener(this);

        roomEt = findViewById(R.id.roomET);
        priceEt = findViewById(R.id.priceET);
        elecTv = findViewById(R.id.elecTV);
        payBtn = findViewById(R.id.payBtn);
        elecTv.setOnClickListener(this);
        payBtn.setOnClickListener(this);
        RadioGroup rg = findViewById(R.id.radioGroup);
        rg.setOnCheckedChangeListener(this);

        xqOpv = new OptionsPickerBuilder(this,
                (options1, options2, options3, v) -> {
                    mPresenter.onAreaSelect(xqData.get(options1));
                    xqTv.setText(xqData.get(options1));
                })
                .setSubmitText("确认")
                .setSubmitColor(Color.BLACK)
                .setTitleText("选择校区")
                .isDialog(true)
                .setSelectOptions(0)
                .setOutSideCancelable(false)
                .build();

        ldOpv = new OptionsPickerBuilder(this,
                (options1, options2, options3, v) -> {
                    mPresenter.onBuildingSelect(ldData.get(options1));
                    ldTv.setText(ldData.get(options1));
                })
                .setSubmitText("确认")
                .setSubmitColor(Color.BLACK)
                .setTitleText("选择楼栋")
                .isDialog(true)
                .setSelectOptions(0)
                .setOutSideCancelable(false)
                .build();

        lcOpv = new OptionsPickerBuilder(this,
                (options1, options2, options3, v) -> {
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

        confirmDialog = new AlertDialog.Builder(this)
                .setMessage(R.string.confirm_pay)
                .setPositiveButton("确认", this)
                .setNegativeButton("取消", this)
                .create();

        initViewVisibility();
        mPresenter = new MainPresenter(this);
    }

    @Override
    public void setBalanceText(String text) {
        balanceTv.setText(getString(R.string.balance, text));
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
    public void onClick(DialogInterface dialog, int which) {
        if (which == AlertDialog.BUTTON_POSITIVE) {
            mPresenter.pay();
        }
        dialog.cancel();
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
    public void initViewVisibility() {
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
        priceEt.setVisibility(View.GONE);
        priceEt.setText("");
        payBtn.setVisibility(View.GONE);
    }

    @Override
    public void setElecText(String text) {
        elecTv.setText(text);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.quitTV:
                mPresenter.quit();
                break;
            case R.id.balanceBtn:
                mPresenter.queryBalance();
                break;
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
                mPresenter.queryElecFees();
                break;
            case R.id.payBtn:
                mPresenter.whetherPay();
                break;
        }
    }

    @Override
    public void setSnoText(String text) {
        snoTv.setText(text);
    }

    @Override
    public void showLoading() {
        progress.show();
    }

    @Override
    public void showPayView() {
        priceEt.setVisibility(View.VISIBLE);
        payBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void showConfirmDialog() {
        confirmDialog.show();
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

    @Override
    public EditText getPriceET() {
        return priceEt;
    }

    @Override
    public EditText getRoomET() {
        return roomEt;
    }
}

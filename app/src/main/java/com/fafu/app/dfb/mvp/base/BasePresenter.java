package com.fafu.app.dfb.mvp.base;

import com.fafu.app.dfb.mvp.base.i.IModel;
import com.fafu.app.dfb.mvp.base.i.IPresenter;
import com.fafu.app.dfb.mvp.base.i.IView;

import io.reactivex.disposables.CompositeDisposable;

public abstract class BasePresenter<V extends IView, M extends IModel> implements IPresenter {

    protected V mView;
    protected M mModel;
    protected CompositeDisposable mCompDisposable = new CompositeDisposable();

    protected final String TAG  = this.getClass().getSimpleName();

    @Override
    public void onStart() {

    }

    @Override
    public void onDestroy() {
        if (mModel != null) {
            mModel.onDestroy();
            mModel = null;
        }
        mCompDisposable.dispose();
        mCompDisposable = null;
        mView = null;
    }


    protected void onError(Throwable throwable) {
        mView.showMessage(throwable.getMessage());
        throwable.printStackTrace();
        mView.hideLoading();
    }
}

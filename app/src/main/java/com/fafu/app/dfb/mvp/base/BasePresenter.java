package com.fafu.app.dfb.mvp.base;

import com.fafu.app.dfb.mvp.base.i.IModel;
import com.fafu.app.dfb.mvp.base.i.IPresenter;
import com.fafu.app.dfb.mvp.base.i.IView;

import io.reactivex.disposables.CompositeDisposable;

public abstract class BasePresenter<V extends IView, M extends IModel> implements IPresenter {

    protected V mView;
    protected M mModel;
    protected CompositeDisposable mCDisposable = new CompositeDisposable();

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
        mCDisposable.dispose();
        mCDisposable = null;
        mView = null;
    }
}

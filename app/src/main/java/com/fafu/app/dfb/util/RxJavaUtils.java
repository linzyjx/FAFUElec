package com.fafu.app.dfb.util;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RxJavaUtils {

    public static <T> Observable<T> create(ObservableOnSubscribe<T> source) {
        return Observable.<T>create(source)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}

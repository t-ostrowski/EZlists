package com.ostro.ezlists.rx;

import android.database.sqlite.SQLiteException;

import rx.Observer;

import static com.ostro.ezlists.util.LogUtils.LOGE;
import static com.ostro.ezlists.util.LogUtils.LOGI;

/**
 * Created by Thomas Ostrowski
 * ostrowski.thomas@gmail.com
 * on 19/08/2016.
 */

public abstract class EndlessObserver<T> implements Observer<T> {

    @Override
    public void onCompleted() {
        LOGI("EndlessObserver", "onComplete");
    }

    @Override
    public void onError(Throwable e) {
//        if (e instanceof RetrofitError) {
//            if (NetworkUtils.get().isNetworkError(e) || ((RetrofitError) e).getResponse() != null && ((RetrofitError) e).getResponse().getStatus() == 400) {
//                LOGW("EndlessObserver", "RetrofitError " + ((RetrofitError) e).getUrl(), e);
//            } else {
//                LOGE("EndlessObserver", "RetrofitError " + ((RetrofitError) e).getUrl(), e);
//            }
//        } else
        if (e instanceof SQLiteException) {
            LOGE("EndlessObserver", "SQLiteException", e);
        } else {
            LOGE("EndlessObserver", "EndlessObserver Error", e);
        }
    }

    @Override
    public void onNext(T t) {

    }
}


package com.ostro.myshoppinglist.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle.FragmentEvent;
import com.trello.rxlifecycle.components.support.RxFragment;

import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Thomas Ostrowski
 * ostrowski.thomas@gmail.com
 * on 29/07/2016.
 */

public abstract class BaseFragment extends RxFragment {

    public abstract int getLayoutResources();

    private Bundle mLastSavedInstanceState = null;
    private boolean hasBeenStopped = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLastSavedInstanceState = savedInstanceState;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int layoutResource = getLayoutResources();
        if (layoutResource != 0) {
            return inflater.inflate(layoutResource, container, false);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onStop() {
        super.onStop();
        hasBeenStopped = true;
    }

    public boolean hasSavedInstanceState() {
        return mLastSavedInstanceState != null;
    }

    public Bundle getLastSavedInstanceState() {
        return mLastSavedInstanceState;
    }

    public void resetLastSavedInstanceState() {
        mLastSavedInstanceState = null;
    }

    public boolean hasBeenStopped() {
        return hasBeenStopped;
    }

    public void setTitle(String title) {
        BaseActivity activity = getBaseActivity();
        if(activity != null) {
            activity.setTitle(title);
        }
    }

    public void hideSoftKeyboard() {
        BaseActivity activity = getBaseActivity();
        if(activity != null) {
            activity.hideSoftKeyboard();
        }
    }

    public BaseActivity getBaseActivity() {
        Activity activity = getActivity();
        if (activity instanceof BaseActivity) {
            return (BaseActivity) activity;
        } else {
            return null;
        }
    }

    public void errorSnackBar(View view, String message) {
        BaseActivity activity = getBaseActivity();
        if(activity != null) {
            activity.errorSnackBar(view, message);
        }
    }

    public void errorSnackBar(View view, String message, String actionMsg, View.OnClickListener onClickListener) {
        BaseActivity activity = getBaseActivity();
        if(activity != null) {
            activity.errorSnackBar(view, message, actionMsg, onClickListener);
        }
    }

    public void errorSnackBar(View view, String message, String actionMsg, View.OnClickListener onClickListener, int time) {
        BaseActivity activity = getBaseActivity();
        if(activity != null) {
            activity.errorSnackBar(view, message, actionMsg, onClickListener, time);
        }
    }

    public <O> Subscription subscribe(final Observable<O> source, final Observer<O> observer, FragmentEvent fragmentEvent) {
        return source.observeOn(AndroidSchedulers.mainThread()).compose(this.<O>bindUntilEvent(fragmentEvent)).subscribe(observer);
    }

    public <O> Subscription subscribeAsync(final Observable<O> source, final Observer<O> observer, FragmentEvent fragmentEvent) {
        return subscribe(source.subscribeOn(Schedulers.io()), observer, fragmentEvent);
    }
}

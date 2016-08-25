package com.ostro.ezlists.base;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

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

public abstract class BaseActivity extends RxAppCompatActivity {

    public abstract int getLayoutResources();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int layoutRes = getLayoutResources();
        if (layoutRes != 0) {
            setContentView(layoutRes);
        }
        ButterKnife.bind(this);
    }

    public void setTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    public void hideSoftKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    protected void replaceFragment(int container, String name, Fragment fragment, boolean backStack,
                                   int animEnter, int animExit, int animPopEnter, int animPopExit) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(animEnter, animExit, animPopEnter, animPopExit);
        if (backStack) {
            fragmentTransaction.addToBackStack(name);
        }
        fragmentTransaction.replace(container, fragment).commit();
    }

    protected int tryPopStack() {
        int backStackCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackCount > 0) {
            getSupportFragmentManager().popBackStack();
            return backStackCount;
        }
        return 0;
    }

    public void errorSnackBar(View view, String message) {
        if (view == null) {
            return;
        }
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }

    public void errorSnackBar(View view, String message, String actionMsg, View.OnClickListener onClickListener) {
        errorSnackBar(view, message, actionMsg, onClickListener, Snackbar.LENGTH_LONG);
    }

    public void errorSnackBar(View view, String message, String actionMsg, View.OnClickListener onClickListener, int duration) {
        if (view == null) {
            return;
        }
        Snackbar.make(view, message, duration).setAction(actionMsg, onClickListener).show();
    }


    public <O> Subscription subscribe(final Observable<O> source, final Observer<O> observer, ActivityEvent activityEvent) {
        return source.observeOn(AndroidSchedulers.mainThread()).compose(this.<O>bindUntilEvent(activityEvent)).subscribe(observer);
    }

    public <O> Subscription subscribeAsync(final Observable<O> source, final Observer<O> observer, ActivityEvent activityEvent) {
        return subscribe(source.subscribeOn(Schedulers.io()), observer, activityEvent);
    }
}

package com.ostro.ezlists.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.ostro.ezlists.base.BaseActivity;
import com.ostro.ezlists.ui.details.DetailsListActivity;
import com.ostro.ezlists.ui.list.ListActivity;

/**
 * Created by Thomas Ostrowski
 * ostrowski.thomas@gmail.com
 * on 25/08/2016.
 */

public class SplashActivity extends BaseActivity {

    private Handler mHandler;

    private boolean splashScreenIsFinished = false;
    private boolean isLookingForUser = false;
    private boolean activityStopped = false;
    private boolean enableSplash = true;

    @Override
    public int getLayoutResources() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler(Looper.getMainLooper());
        if (enableSplash) {
            mHandler.postDelayed(startActivity, 700);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        activityStopped = false;
        // find existing account
        chooseActivity();
    }

    @Override
    protected void onStop() {
        super.onStop();
        splashScreenIsFinished = true;
        activityStopped = true;
    }

    private void startListActivity() {
        isLookingForUser = false;
        launchActivity(new Intent(this, ListActivity.class));
    }

    private Runnable startActivity = new Runnable() {
        @Override
        public void run() {
            splashScreenIsFinished = true;
            if (isLookingForUser) {
                return;
            }
            chooseActivity();
        }
    };

    private void launchActivity(Intent intent) {
        if (enableSplash) {
            mHandler.removeCallbacks(startActivity);
        }
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private boolean chooseActivity() {
        if (enableSplash && (!splashScreenIsFinished || activityStopped)) {
            return false;
        }
        startListActivity();
        return true;
    }
}

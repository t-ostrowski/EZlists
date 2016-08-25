package com.ostro.ezlists.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ostro.ezlists.R;

import butterknife.BindView;

public abstract class ToolbarActivity extends BaseActivity {

    @NonNull
    @BindView(R.id.main_toolbar)
    protected Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolbar();
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    @SuppressWarnings("all")
    public void initToolbar() {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
//            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return true;
    }
}

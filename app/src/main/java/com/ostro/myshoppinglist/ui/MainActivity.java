package com.ostro.myshoppinglist.ui;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import com.ostro.myshoppinglist.R;
import com.ostro.myshoppinglist.auth.SignInActivity;
import com.ostro.myshoppinglist.ui.drawer.NavigationDrawerActivity;
import com.ostro.myshoppinglist.ui.list.ListFragment;
import com.ostro.myshoppinglist.ui.widget.CustomAlertDialog;

import butterknife.BindString;

public class MainActivity extends NavigationDrawerActivity {

    @BindString(R.string.logout_title)
    String strTitle;
    @BindString(R.string.logout_message)
    String strMessage;
    @BindString(R.string.yes)
    String strYes;

    public static final int FRAGMENT_LIST = 0;
    public static final int FRAGMENT_ACCOUNT = 1;
    public static final int DIALOG_LOGOUT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment getFragment(int position) {
        switch (position) {
            case FRAGMENT_LIST:
                return new ListFragment();
            case FRAGMENT_ACCOUNT:
                return new Fragment();
            case DIALOG_LOGOUT:
                return new Fragment();
            default:
                return new Fragment();
        }
    }

    @Override
    protected String getFragmentTag(int position) {
        switch (position) {
            case FRAGMENT_LIST:
                return "fragment_list";
            case FRAGMENT_ACCOUNT:
                return "fragment_account";
            case DIALOG_LOGOUT:
                return null;
            default:
                return "fragment_list";
        }
    }

    @Override
    protected int getFragmentBasePosition() {
        return 0;
    }

    private void getLogoutDialog() {
        new CustomAlertDialog()
                .setTitle(strTitle)
                .setMessage(strMessage)
                .setPositiveListener(strYes, mDialogLogoutListener)
                .show(this.getSupportFragmentManager(), "dialog_logout");
    }

    private CustomAlertDialog.PositiveListener mDialogLogoutListener = new CustomAlertDialog.PositiveListener() {
        @Override
        public void onNegativeClicked(CustomAlertDialog dialog) {
            Intent intent = new Intent(getApplication(), SignInActivity.class);
            startActivity(intent);
            finish();
        }
    };
}

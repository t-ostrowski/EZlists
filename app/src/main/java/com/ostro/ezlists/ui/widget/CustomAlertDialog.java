package com.ostro.ezlists.ui.widget;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.ArrayList;

/**
 * Created by Thomas Ostrowski
 * ostrowski.thomas@gmail.com
 * on 29/07/2016.
 */

public class CustomAlertDialog extends DialogFragment implements DialogInterface.OnClickListener {

    private static final String TITLE = "TITLE";
    private static final String MSG = "MSG";
    private static final String POSITIVE_MSG = "POSITIVE_MSG";
    private static final String NEGATIVE_MSG = "NEGATIVE_MSG";
    private static final String POSITIVE_DISABLED = "POSITIVE_DISABLED";
    private static final String ITEMS = "ITEMS";
    private static final String WIDTH = "WIDTH";
    private static final String HEIGHT = "HEIGHT";

    private Resources mResources;

    private NegativeListener mNegativeListener;
    private PositiveListener mPositiveListener;
    private DialogInterface.OnClickListener mAdapterClickListener;
    private DialogInterface.OnDismissListener onDismissListener;

    private String mTitle;
    private String mMessage;
    private String mPositiveMessage;
    private String mNegativeMessage;
    private boolean isPostiveBtnDisabled = false;
    private ArrayList<String> mItems;

    private int mCustomWidth = 0;
    private int mCustomHeight = 0;

    public static CustomAlertDialog newInstance() {
        return new CustomAlertDialog();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            initFromOldState(savedInstanceState);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        mResources = getResources();
        builder.setTitle(mTitle);
        builder = initContent(builder);
        AlertDialog alertDialog = initButtons(builder);
        if (mCustomHeight != 0) {
            alertDialog.getWindow().setLayout(mCustomWidth, mCustomHeight);
        } else {
            alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        return alertDialog;
    }

    private void initFromOldState(@NonNull Bundle savedInstanceState) {
        mTitle = savedInstanceState.getString(TITLE);
        mMessage = savedInstanceState.getString(MSG);
        mPositiveMessage = savedInstanceState.getString(POSITIVE_MSG);
        mNegativeMessage = savedInstanceState.getString(NEGATIVE_MSG);
        isPostiveBtnDisabled = savedInstanceState.getBoolean(POSITIVE_DISABLED);
        mItems = savedInstanceState.getStringArrayList(ITEMS);
        mCustomWidth = savedInstanceState.getInt(WIDTH);
        mCustomHeight = savedInstanceState.getInt(HEIGHT);
    }

    private AlertDialog.Builder initContent(AlertDialog.Builder builder) {
        if (!TextUtils.isEmpty(mMessage)) {
            builder.setMessage(mMessage);
        } else if (mItems != null) {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                    getActivity(),
                    android.R.layout.select_dialog_item);
            arrayAdapter.addAll(mItems);
            builder.setAdapter(arrayAdapter, this);
        }
        return builder;
    }

    private AlertDialog initButtons(AlertDialog.Builder builder) {
        if (!isPostiveBtnDisabled && !TextUtils.isEmpty(mPositiveMessage)) {
            builder.setPositiveButton(mPositiveMessage, this);
        }
        if (TextUtils.isEmpty(mNegativeMessage)) {
            mNegativeMessage = mResources.getString(android.R.string.cancel);
        }
        builder.setNegativeButton(mNegativeMessage, this);
        AlertDialog alertDialog = builder.create();
        if (isPostiveBtnDisabled) {
            Button button = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            if (button != null) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
            }
        }
        return alertDialog;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case AlertDialog.BUTTON_POSITIVE:
                if (mPositiveListener != null) {
                    mPositiveListener.onNegativeClicked(CustomAlertDialog.this);
                    dismiss();
                }
                break;
            case AlertDialog.BUTTON_NEGATIVE:
                if (mNegativeListener != null) {
                    mNegativeListener.onNegativeClicked(CustomAlertDialog.this);
                    dismiss();
                }
                break;
            case AlertDialog.BUTTON_NEUTRAL:
                break;
            default:
                if (mAdapterClickListener != null) {
                    mAdapterClickListener.onClick(dialog, which);
                }
        }
    }

    @Override
    public void onDestroyView() {
        if (getDialog() != null) {
            getDialog().dismiss();
        }
        if (onDismissListener != null) {
            onDismissListener.onDismiss(null);
        }
        super.onDestroyView();
    }

    public CustomAlertDialog setTitle(String resourceTitle) {
        mTitle = resourceTitle;
        return this;
    }

    public CustomAlertDialog setMessage(String message) {
        mMessage = message;
        return this;
    }

    public CustomAlertDialog setPositiveListener(String positiveMessage, PositiveListener positiveListener) {
        mPositiveListener = positiveListener;
        mPositiveMessage = positiveMessage;
        return this;
    }

    public CustomAlertDialog setNegativeListener(String negativeMessage, NegativeListener negativeListener) {
        mNegativeListener = negativeListener;
        mNegativeMessage = negativeMessage;
        return this;
    }

    public CustomAlertDialog setStringAdapter(ArrayList<String> list, DialogInterface.OnClickListener clickListener) {
        mItems = list;
        mAdapterClickListener = clickListener;
        return this;
    }

    public CustomAlertDialog setOnAdapterClickListener(DialogInterface.OnClickListener clickListener) {
        this.mAdapterClickListener = clickListener;
        return this;
    }

    public CustomAlertDialog setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
        return this;
    }

    public CustomAlertDialog disablePositiveButton() {
        isPostiveBtnDisabled = true;
        return this;
    }

    public CustomAlertDialog setCustomLayoutSize(int width, int height) {
        mCustomWidth = width;
        mCustomHeight = height;
        return this;
    }

    public interface NegativeListener {
        void onNegativeClicked(CustomAlertDialog dialog);
    }

    public interface PositiveListener {
        void onNegativeClicked(CustomAlertDialog dialog);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TITLE, mTitle);
        outState.putString(MSG, mMessage);
        outState.putString(POSITIVE_MSG, mPositiveMessage);
        outState.putString(NEGATIVE_MSG, mNegativeMessage);
        outState.putBoolean(POSITIVE_DISABLED, isPostiveBtnDisabled);
        outState.putInt(WIDTH, mCustomWidth);
        outState.putInt(HEIGHT, mCustomHeight);
        if (mItems != null) {
            outState.putStringArrayList(ITEMS, mItems);
        }
    }
}

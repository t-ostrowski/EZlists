package com.ostro.ezlists.ui.list.dialog;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.ostro.ezlists.R;
import com.ostro.ezlists.model.Item;
import com.ostro.ezlists.model.List;
import com.ostro.ezlists.ui.widget.CustomAlertDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

/**
 * Created by Thomas Ostrowski
 * ostrowski.thomas@gmail.com
 * on 25/08/2016.
 */

public class EditListDialog extends DialogFragment {

    @BindView(R.id.et_name_list)
    EditText etNameList;
    @BindView(R.id.btn_validate)
    Button btnValidate;
    @BindView(R.id.btn_delete_list)
    Button btnDeleteList;

    private long mListId;
    private List mList;
    private Realm realm;

    public static EditListDialog newInstance(long listId) {
        EditListDialog editListDialog = new EditListDialog();
        Bundle args = new Bundle();
        args.putLong("listId", listId);
        editListDialog.setArguments(args);
        return editListDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        realm = Realm.getDefaultInstance();
        mListId = getArguments().getLong("listId");
        mList = getList(mListId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dialog_edit_list, container, false);
        ButterKnife.bind(this, v);
        setFields();
        return v;
    }

    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance()) {
            getDialog().setDismissMessage(null);
        }
        realm.close();
        super.onDestroyView();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        Activity activity = getActivity();
        if (activity instanceof DialogCloseListener) {
            ((DialogCloseListener) activity).handleDialogClose(dialog);
        }
    }

    @OnClick(R.id.btn_delete_list)
    public void onDeleteList() {
        CustomAlertDialog.newInstance()
                .setTitle(getString(R.string.dialog_deletion_title))
                .setMessage(getString(R.string.dialog_deletion_message))
                .setPositiveListener(getString(R.string.yes), new CustomAlertDialog.PositiveListener() {
                    @Override
                    public void onNegativeClicked(CustomAlertDialog dialog) {
                        List list = realm.where(List.class)
                                .equalTo("id", mListId)
                                .findFirst();
                        java.util.List<Item> items = realm.where(Item.class)
                                .equalTo("listId", mListId)
                                .findAll();
                        realm.beginTransaction();
                        for (Item item : items) {
                            item.deleteFromRealm();
                        }
                        list.deleteFromRealm();
                        realm.commitTransaction();
                        dialog.dismiss();
                        dismiss();
                    }
                })
                .setNegativeListener(getString(R.string.cancel), new CustomAlertDialog.NegativeListener() {
                    @Override
                    public void onNegativeClicked(CustomAlertDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .show(((FragmentActivity) getActivity()).getSupportFragmentManager(), "deletion_message");
    }

    @OnClick(R.id.btn_validate)
    public void onValidate() {
        if (!TextUtils.isEmpty(etNameList.getText().toString())) {
            List list = realm.where(List.class)
                    .equalTo("id", mListId)
                    .findFirst();
            realm.beginTransaction();
            list.setName(etNameList.getText().toString().trim());
            realm.commitTransaction();
            dismiss();
        }
    }

    private List getList(long listId) {
        if (listId == 0L) {
            return null;
        }
        Realm realm = Realm.getDefaultInstance();
        return realm.where(List.class)
                .equalTo("id", listId)
                .findFirst();
    }

    private void setFields() {
        if (mList != null) {
            if (!TextUtils.isEmpty(mList.getName())) {
                etNameList.setText(mList.getName());
            }
        }
    }
}

package com.ostro.ezlists.model;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Thomas Ostrowski
 * ostrowski.thomas@gmail.com
 * on 19/08/2016.
 */

public class Item extends RealmObject {

    @PrimaryKey
    private long id;
    private long listId;
    private String wording;
    private boolean checked;

    public Item() {
        this.wording = "";
        this.checked = false;
    }

    public Item(String wording, long listId) {
        this.wording = wording;
        this.listId = listId;
        this.checked = false;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getWording() {
        return wording;
    }

    public void setWording(String wording) {
        this.wording = wording;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public long getListId() {
        return listId;
    }

    public void setListId(long listId) {
        this.listId = listId;
    }

    public static int getNextKey() {
        Realm realm = Realm.getDefaultInstance();
        if (realm.where(Item.class).max("id") == null) {
            return 1;
        }
        return realm.where(Item.class).max("id").intValue() + 1;
    }
}

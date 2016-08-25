package com.ostro.ezlists.model;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Thomas Ostrowski
 * ostrowski.thomas@gmail.com
 * on 25/08/2016.
 */

public class List extends RealmObject {

    @PrimaryKey
    private long id;
    private String name;

    public List() {

    }

    public List(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static int getNextKey() {
        Realm realm = Realm.getDefaultInstance();
        if (realm.where(List.class).max("id") == null) {
            return 1;
        }
        return realm.where(List.class).max("id").intValue() + 1;
    }
}

package com.ostro.myshoppinglist.model;

/**
 * Created by Thomas Ostrowski
 * ostrowski.thomas@gmail.com
 * on 19/08/2016.
 */

public class Item {

    public String wording;
    public boolean checked;

    public Item() {
        this.wording = "";
        this.checked = false;
    }

    public Item(String wording) {
        this.wording = wording;
        this.checked = false;
    }
}

package com.ostro.myshoppinglist.ui.drawer.drawer;

/**
 * Created by Thomas Ostrowski
 * thomas.o@tymate.com
 * on 12/10/15.
 */
public class NavigationDrawerItem {

    private String title;
    private int icon;
    private Type type = Type.PRIMARY;

    public enum Type {
        PRIMARY, SECONDARY
    }

    public NavigationDrawerItem() {
    }

    public NavigationDrawerItem(String title, int icon, Type type) {
        this.title = title;
        this.icon = icon;
        this.type = type;
    }

    public String getTitle() {
        return this.title;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return this.icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}

package com.weex.wdialog_lib;

import android.os.Parcel;

import java.io.Serializable;

/**
 * Created by xuwx on 2018/10/15.
 */

public class Item implements Serializable {

    private String itemName;
    private int itemResource;

    public Item(String itemName, int itemResource) {
        this.itemName = itemName;
        this.itemResource = itemResource;
    }

    protected Item(Parcel in) {
        itemName = in.readString();
        itemResource = in.readInt();
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemResource() {
        return itemResource;
    }

    public void setItemResource(int itemResource) {
        this.itemResource = itemResource;
    }
}

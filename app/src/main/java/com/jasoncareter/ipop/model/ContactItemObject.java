package com.jasoncareter.ipop.model;

import android.graphics.drawable.Drawable;

public class ContactItemObject {

    private String name ;
    private Drawable drawable ;

    public ContactItemObject(String name , Drawable drawable){
        this.name = name ;
        this.drawable = drawable;
    }

    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public Drawable getDrawable() {
        return drawable;
    }
}

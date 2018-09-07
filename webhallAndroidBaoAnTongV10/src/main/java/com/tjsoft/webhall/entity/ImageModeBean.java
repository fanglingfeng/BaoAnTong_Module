package com.tjsoft.webhall.entity;

/**
 * Created by Dino on 2018/1/11.
 */

public class ImageModeBean {
    private String name;
    private boolean selected;

    public ImageModeBean(String name,boolean selected){
        this.name = name;
        this.selected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}

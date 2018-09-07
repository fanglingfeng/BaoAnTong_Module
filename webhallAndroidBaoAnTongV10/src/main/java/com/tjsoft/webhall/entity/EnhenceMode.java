package com.tjsoft.webhall.entity;

/**
 * Created by lenovo on 2018/1/11.
 */

public class EnhenceMode {
    private String modeName;

    public String getModeName() {
        return modeName;
    }

    public EnhenceMode(String modeName, int position) {
        this.modeName = modeName;
        this.position = position;
    }

    public void setModeName(String modeName) {
        this.modeName = modeName;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    private int position;
    private boolean isSelected;

}

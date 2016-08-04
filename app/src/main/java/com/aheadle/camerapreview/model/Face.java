package com.aheadle.camerapreview.model;

import android.graphics.Rect;

/**
 * Created by Jing on 16/8/5.
 */
public class Face {
    public Rect rect;
    public int id;
    public double score;
    public boolean alive = false;
    public String living;

    public Face() {
    }
}

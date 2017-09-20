package com.kong.prerect.model;

import android.graphics.RectF;

/**
 * Created by Jing on 17/4/6.
 */

public class FaceInfo {
    public RectF rect;
    public int id;
    public int living;
    public int score;

    public RectF getRect() {
        return rect;
    }

    public void setRect(RectF rect) {
        this.rect = rect;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLiving() {
        return living;
    }

    public void setLiving(int living) {
        this.living = living;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}

package com.jing.prerect.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.jing.prerect.model.FaceInfo;


/**
 * Created by Jing on 16/8/5.
 */
public class FaceRectView extends View {

    Context mContext;
    private Paint mLinePaint;
    Paint textPaint;
    private FaceInfo[] mFaces;

    public FaceRectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.initPaint();
    }

    public void setFace(FaceInfo[] faces) {
        this.mFaces = faces;
        this.invalidate();
    }

    public void clearFace() {
        this.mFaces = null;
        this.invalidate();
    }

    private void initPaint() {
        float scale = this.getResources().getDisplayMetrics().density;
        this.mLinePaint = new Paint(1);
        int color = Color.rgb(98, 212, 68);
        this.mLinePaint.setColor(color);
        this.mLinePaint.setStyle(Paint.Style.STROKE);
        this.mLinePaint.setStrokeWidth(3.0F);
        this.mLinePaint.setAlpha(180);
        this.textPaint = new Paint(1);
        this.textPaint.setTextSize(40.0F);
        this.textPaint.setColor(Color.BLUE);
    }

    protected void onDraw(Canvas canvas) {
        if(this.mFaces != null) {
            int len = this.mFaces.length;

            for(int i = 0; i < len; ++i) {
                this.textPaint.setColor(Color.BLUE);
                canvas.drawRect(this.mFaces[i].rect, this.mLinePaint);
                canvas.drawText(this.mFaces[i].id + "", (float)this.mFaces[i].rect.left, (float)this.mFaces[i].rect.top, this.textPaint);
                String result = String.format("%.2f", new Object[]{Double.valueOf(this.mFaces[i].score)});
                canvas.drawText(result, (float)this.mFaces[i].rect.right, (float)this.mFaces[i].rect.top, this.textPaint);
                canvas.drawText(this.mFaces[i].living + "", (float)this.mFaces[i].rect.right, (float)this.mFaces[i].rect.bottom, this.textPaint);
            }
        }

    }
}

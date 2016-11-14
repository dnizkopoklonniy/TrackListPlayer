package com.niz.d.playerplaylist;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

public class BaselineTextView extends TextView {

    public BaselineTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int yOffset = getHeight() - getBaseline();
        ///canvas.translate(0, yOffset);
        setHeight(getBaseline());
        super.onDraw(canvas);
    }

}
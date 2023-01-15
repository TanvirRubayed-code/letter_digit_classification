package com.example.letterdigitrecognition.java;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class DrawView extends View {

    private Paint paint = new Paint();
    private Path path = new Path();
    Canvas canvas;


    public DrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(5f);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(path,paint);

    }




    float downx = 0; float downy = 0; float upx = 0; float upy = 0;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);


//        int action = event.getAction();
//
//        switch (action) {
//
//            case MotionEvent.ACTION_DOWN: downx = event.getX(); downy = event.getY(); break;
//
//            case MotionEvent.ACTION_MOVE: break;
//
//            case MotionEvent.ACTION_UP: upx = event.getX(); upy = event.getY();
//
//
//                canvas.drawLine(downx, downy, upx, upy, paint);
//
//                imageView.invalidate();
//
//                break;
//
//            case MotionEvent.ACTION_CANCEL: break;
//
//            default: break;
//        // Schedules a repaint.
//        invalidate();
//        return true;


    }

}

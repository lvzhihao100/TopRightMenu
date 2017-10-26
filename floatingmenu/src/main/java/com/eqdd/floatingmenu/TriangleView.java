package com.eqdd.floatingmenu;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author吕志豪 .
 * @date 17-10-26  上午10:02.
 * Github :https://github.com/lvzhihao100
 * E-Mail：1030753080@qq.com
 * 简书 :http://www.jianshu.com/u/6e525b929aac
 */

public class TriangleView extends View {
    private int width = 200;
    private int height = 200;
    private Paint paint;
    private Path path;
    private int type = 1;

    public TriangleView(Context context) {
        super(context);
        init();
    }


    public TriangleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TriangleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = makeMeasure(widthMeasureSpec);
        height = makeMeasure(heightMeasureSpec);
        System.out.println(width + "   " + height + "    ");
        setMeasuredDimension(width, height);
        switch (type) {
            case 0:
                setLeftPath();
                break;
            case 1:
                setTopPath();
                break;
            case 2:
                setRightPath();
                break;
            case 3:
                setBottomPath();
                break;

        }
    }

    private int makeMeasure(int measureSpec) {
        int measureMode = MeasureSpec.getMode(measureSpec);
        int measureSize = MeasureSpec.getSize(measureSpec);
        if (measureMode == MeasureSpec.AT_MOST || measureMode == MeasureSpec.EXACTLY) {//精确值,或match_parent
            return measureSize;
        } else {
            return 200;
        }
    }

    private void init() {

        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);//抗锯齿
        paint.setStrokeCap(Paint.Cap.ROUND);//圆形线冒
        paint.setStrokeJoin(Paint.Join.ROUND);//圆形结合处
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public void setBottomPath() {
        type = 3;
        path = new Path();
        path.moveTo(0, 0);
        path.lineTo(width / 2, height);
        path.lineTo(width, 0);
        path.lineTo(0, 0);
        invalidate();
    }

    public void setRightPath() {
        type = 2;
        path = new Path();
        path.moveTo(0, 0);
        path.lineTo(width, height / 2);
        path.lineTo(0, height);
        path.lineTo(0, 0);
        invalidate();

    }

    public void setLeftPath() {
        type = 0;
        path = new Path();
        path.moveTo(width, 0);
        path.lineTo(0, height / 2);
        path.lineTo(width, height);
        path.lineTo(width, 0);
        invalidate();

    }

    public void setTopPath() {
        type = 1;
        path = new Path();
        path.moveTo(0, height);
        path.lineTo(width / 2, 0);
        path.lineTo(width, height);
        path.lineTo(0, height);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawPath(path, paint);
    }

}

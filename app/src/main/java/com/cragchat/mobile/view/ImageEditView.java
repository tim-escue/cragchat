package com.cragchat.mobile.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;

import java.util.ArrayList;
import java.util.List;

public class ImageEditView extends AppCompatImageView {

    private Bitmap mBitmap;
    private Canvas mCanvas;
    private List<Path> mPaths;
    private Paint mBitmapPaint;
    private Paint mPaint;
    private int bitmapEndX;
    private int bitmapEndY;
    private int bitmapStartX;
    private int bitmapStartY;
    private float[] matrixValues;


    public ImageEditView(Context context) {
        this(context, null);
    }

    public ImageEditView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageEditView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaths = new ArrayList<>();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(0xFFFF0000);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(20);
        matrixValues = new float[9];
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getImageMatrix().getValues(matrixValues);

                final float scaleX = matrixValues[Matrix.MSCALE_X];
                final float scaleY = matrixValues[Matrix.MSCALE_Y];

                final Drawable d = getDrawable();
                if (d != null) {
                    final int origW = d.getIntrinsicWidth();
                    final int origH = d.getIntrinsicHeight();

                    int bitmapWidth = Math.round(origW * scaleX);
                    int bitmapHeight = Math.round(origH * scaleY);

                    int imgViewW = getWidth();
                    int imgViewH = getHeight();

                    bitmapStartY = (imgViewH - bitmapHeight) / 2;
                    bitmapStartX = (imgViewW - bitmapWidth) / 2;
                    Log.d("metris", "imgViewW" + imgViewW + " imageViewH" + imgViewH + " bitmapWidth" + bitmapWidth + " bitmapHeiht" + bitmapHeight + " bitmapStartX" + bitmapStartX);

                    bitmapEndX = bitmapStartX + bitmapWidth;
                    bitmapEndY = bitmapStartY + bitmapHeight;
                    mBitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
                    mCanvas = new Canvas(mBitmap);
                }
            }
        });
    }

    @Override
    public void setImageBitmap(Bitmap bitmap) {
        super.setImageBitmap(bitmap);
        mPaths.clear();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d("ON DRAW", "DR");
        super.onDraw(canvas);
        if (mBitmap != null) {
            canvas.drawBitmap(mBitmap, bitmapStartX, bitmapStartY, mBitmapPaint);
        }
    }

    int[] points = new int[4];
    int lastX;
    int lastY;
    boolean stopped = true;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        if (x >= bitmapStartX && x <= bitmapEndX && y >= bitmapStartY && y <= bitmapEndY) {

            x = x - bitmapStartX;
            y = y - bitmapStartY;
            if (stopped) {
                lastX = x;
                lastY = y;
                points = new int[]{lastX, lastY, x, y};
                stopped = false;
            } else {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        stopped = true;
                    case MotionEvent.ACTION_DOWN:
                        int xDelta = lastX - x;
                        int yDelta = lastY - y;
                        if (xDelta < -10 || xDelta > 10 || yDelta < -10 || yDelta > 10) {
                            points = new int[]{lastX, lastY, x, y};
                            lastX = x;
                            lastY = y;
                            mCanvas.drawLine(points[0], points[1], points[2], points[3], mPaint);
                            invalidate();
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        points = new int[]{lastX, lastY, x, y};
                        mCanvas.drawLine(points[0], points[1], points[2], points[3], mPaint);
                        invalidate();
                        lastX = x;
                        lastY = y;
                        break;
                }
            }
        } else {
            stopped = true;
        }
        return true;
    }
}


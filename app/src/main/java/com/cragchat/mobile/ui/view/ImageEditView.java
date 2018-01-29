package com.cragchat.mobile.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ImageEditView extends AppCompatImageView {

    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mBitmapPaint;
    private Paint mPaint;
    private int bitmapEndX;
    private int bitmapEndY;
    private int bitmapStartX;
    private int bitmapStartY;
    private int lastX;
    private int lastY;
    private boolean stopped;
    private boolean drawing;
    private int bitmapWidth;
    private int bitmapHeight;
    private Stack<Path> pointMap;
    private List<Point> currentPoitns;

    public ImageEditView(Context context) {
        this(context, null);
    }

    public ImageEditView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageEditView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(0xFFFF0000);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(20);
        stopped = true;
        drawing = false;
        final float[] matrixValues = new float[9];
        currentPoitns = new ArrayList<>();
        pointMap = new Stack<>();
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (mBitmap == null) {
                    getImageMatrix().getValues(matrixValues);

                    final float scaleX = matrixValues[Matrix.MSCALE_X];
                    final float scaleY = matrixValues[Matrix.MSCALE_Y];

                    final Drawable d = getDrawable();
                    if (d != null) {
                        final int origW = d.getIntrinsicWidth();
                        final int origH = d.getIntrinsicHeight();

                        bitmapWidth = Math.round(origW * scaleX);
                        bitmapHeight = Math.round(origH * scaleY);

                        int imgViewW = getWidth();
                        int imgViewH = getHeight();

                        bitmapStartY = (imgViewH - bitmapHeight) / 2;
                        bitmapStartX = (imgViewW - bitmapWidth) / 2;

                        bitmapEndX = bitmapStartX + bitmapWidth;
                        bitmapEndY = bitmapStartY + bitmapHeight;
                        mBitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
                        mCanvas = new Canvas(mBitmap);
                    }
                }
            }
        });
    }

    public static Bitmap convertToMutable(Bitmap imgIn) {
        try {
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "temp.tmp");

            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");

            int width = imgIn.getWidth();
            int height = imgIn.getHeight();
            Bitmap.Config type = imgIn.getConfig();

            FileChannel channel = randomAccessFile.getChannel();
            MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, imgIn.getRowBytes() * height);
            imgIn.copyPixelsToBuffer(map);
            imgIn.recycle();
            System.gc();

            imgIn = Bitmap.createBitmap(width, height, type);
            map.position(0);
            imgIn.copyPixelsFromBuffer(map);
            channel.close();
            randomAccessFile.close();

            file.delete();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imgIn;
    }

    public void setDrawColor(int color) {
        mPaint.setColor(color);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBitmap != null) {
            canvas.drawBitmap(mBitmap, bitmapStartX, bitmapStartY, mBitmapPaint);
        }
    }

    public Bitmap scaleDown() {

        Bitmap bitmap = convertToMutable(((BitmapDrawable) getDrawable()).getBitmap());
        Bitmap newBitmap = Bitmap.createScaledBitmap(mBitmap, bitmap.getWidth(),
                bitmap.getHeight(), true);
        mBitmap.recycle();
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(newBitmap, 0, 0, mBitmapPaint);
        return bitmap;
    }

    public void setDrawing(boolean drawing) {
        this.drawing = drawing;
    }

    public void undoLast() {
        if (pointMap.size() > 0) {
            pointMap.pop();
            mBitmap.recycle();
            mBitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
            int lastColor = mPaint.getColor();
            int lastStroke = (int) mPaint.getStrokeWidth();
            for (Path path : pointMap) {
                List<Point> list = path.points;
                mPaint.setColor(path.color);
                mPaint.setStrokeWidth(path.stroke);
                for (int i = 0; i < list.size() - 1; i++) {
                    Point start = list.get(i);
                    Point next = list.get(i + 1);
                    mCanvas.drawLine(start.x, start.y, next.x, next.y, mPaint);
                }
            }
            mPaint.setColor(lastColor);
            mPaint.setStrokeWidth(lastStroke);
            invalidate();
        }
    }

    public void setStroke(int stroke) {
        mPaint.setStrokeWidth(stroke);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (drawing) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            if (x >= bitmapStartX && x <= bitmapEndX && y >= bitmapStartY && y <= bitmapEndY) {
                int curX = x - bitmapStartX;
                int curY = y - bitmapStartY;
                if (stopped) {
                    lastX = curX;
                    lastY = curY;
                    stopped = false;
                    currentPoitns = new ArrayList<>();
                    currentPoitns.add(new Point(lastX, lastY));
                } else {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_UP:
                            stopped = true;
                            pointMap.push(new Path(currentPoitns, mPaint.getColor(), (int) mPaint.getStrokeWidth()));
                            break;
                        case MotionEvent.ACTION_DOWN:
                            int xDelta = lastX - curX;
                            int yDelta = lastY - curY;
                            if (xDelta < -10 || xDelta > 10 || yDelta < -10 || yDelta > 10) {
                                addPoint(curX, curY);
                            }
                            break;
                        case MotionEvent.ACTION_MOVE:
                            addPoint(curX, curY);
                            break;
                    }
                }
            } else {
                stopped = true;
                pointMap.push(new Path(currentPoitns, mPaint.getColor(), (int) mPaint.getStrokeWidth()));
            }
        }
        return true;
    }

    private void addPoint(int curX, int curY) {
        mCanvas.drawLine(lastX, lastY, curX, curY, mPaint);
        invalidate();
        lastX = curX;
        lastY = curY;
        currentPoitns.add(new Point(curX, curY));
    }

    class Path {

        private List<Point> points;
        private int color;
        private int stroke;

        public Path(List<Point> points, int color, int stroke) {
            this.points = points;
            this.color = color;
            this.stroke = stroke;
        }
    }
}


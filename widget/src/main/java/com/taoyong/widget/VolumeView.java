package com.taoyong.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Creation Time :  2017/9/18.<br/>
 * Creator : tao yong.<br/>
 * Module description : <br/>
 */
public class VolumeView extends View {
    private static final String TAG = "VolumeView";
    private Paint paint;
    private float borderWidth = 50f;//默认宽度
    private int chooseColor = Color.RED;//默认选中颜色
    private int unChooseColor = Color.BLACK;//默认未选中颜色
    private int sumVolume = 12;//默认总音量数
    private int currentVolume = 6;//默认选中音量数
    private float intervalAngle = 0f;//默认间隔角度
    private boolean incomplete = false;//默认 不 残缺
    private int centerImage = R.drawable.love;//默认中心图片资源

    public VolumeView(Context context) {
        super(context);
        init(context, null);
    }

    public VolumeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public VolumeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public VolumeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (null != attrs) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.VolumeView);
            /****************************************************/
            borderWidth = a.getDimension(R.styleable.VolumeView_borderWidth, 50f);
            /****************************************************/
            chooseColor = a.getColor(R.styleable.VolumeView_chooseColor, Color.RED);
            /****************************************************/
            unChooseColor = a.getColor(R.styleable.VolumeView_unChooseColor, Color.BLACK);
            /****************************************************/
            sumVolume = a.getInteger(R.styleable.VolumeView_sumVolume, 12);
            /****************************************************/
            currentVolume = a.getColor(R.styleable.VolumeView_currentVolume, 6);
            /****************************************************/
            intervalAngle = a.getFloat(R.styleable.VolumeView_intervalAngle, 0f);
            /****************************************************/
            incomplete = a.getBoolean(R.styleable.VolumeView_incomplete, false);
            /****************************************************/
            centerImage = a.getResourceId(R.styleable.VolumeView_centerImage, R.drawable.love);
            a.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (null == paint) {
            paint = new Paint();
        }
        int width = getWidth();
        int height = getHeight();
        paint.setAntiAlias(true); // 消除锯齿
        paint.setStrokeWidth(borderWidth); // 设置圆环的宽度
        paint.setStrokeCap(Paint.Cap.ROUND); // 定义线段断电形状为圆头
        paint.setStyle(Paint.Style.STROKE); // 设置空心
        float radius = (width < height ? width : height) / 2f - borderWidth / 2f;// 半径
        RectF oval = new RectF(width / 2f - radius, height / 2f - radius, width / 2f + radius, height / 2f + radius); //画圆弧的范围
        double v = (180 * borderWidth) / (Math.PI * radius);//计算 线段断电形状为圆头 部分占用的角度
        float average, startPos;
        if (incomplete) {
            average = 270f / sumVolume;
            startPos = (float) (135 + v / 2f);
        } else {
            average = 360f / sumVolume;
            startPos = (float) (270 + v / 2f);
        }
        if (intervalAngle > average - v) {
            intervalAngle = (float) (average - v);
        }
        startPos += intervalAngle / 2f;
        float ft = (float) (average - v - intervalAngle / 2f);
        /*画弧度*/
        paint.setColor(chooseColor);
        for (int i = 0; i < currentVolume; i++) {
            canvas.drawArc(oval, startPos + average * i, ft, false, paint);
        }
        paint.setColor(unChooseColor);
        for (int i = currentVolume; i < sumVolume; i++) {
            canvas.drawArc(oval, startPos + average * i, ft, false, paint);
        }
        paint.reset();
        /*画中心图片*/
        paint.setAntiAlias(true);
        radius = (width < height ? width : height) / 2f - borderWidth;
        radius = (float) (StrictMath.pow(StrictMath.pow(radius, 2) * 2, 0.5) / 2f);
        oval = new RectF(width / 2f - radius, height / 2f - radius, width / 2f + radius, height / 2f + radius);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), centerImage);
        int bitmapWidth = bitmap.getWidth(),
                bitmapHeight = bitmap.getHeight();
        int min = bitmapWidth < bitmapHeight ? bitmapWidth : bitmapHeight;
        bitmap = Bitmap.createBitmap(bitmap, (bitmapWidth - min) / 2, (bitmapHeight - min) / 2, min, min);
        int layer = canvas.saveLayer(0, 0, canvas.getWidth(), canvas.getHeight(), null, Canvas.ALL_SAVE_FLAG);
        /*如果不需要，显示为圆形可以去掉*/
        canvas.drawArc(oval, 0, 360, true, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        /********************************/
        canvas.drawBitmap(bitmap, null, oval, paint);
        paint.setXfermode(null);
        canvas.restoreToCount(layer);
        paint.reset();
        if (null != volumeChangeListener) {
            volumeChangeListener.VolumeChange(currentVolume);
        }
    }

    private float downY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        float y = e.getY();
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = y;
                break;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                if (downY < y) {
                    int num = (int) ((y - downY) / (getHeight() / sumVolume));
                    if (num > 0) {
                        for (int i = 0; i < num; i++) {
                            downVolume();
                        }
                        downY = y;
                    }
                } else {
                    int num = (int) ((downY - y) / (getHeight() / sumVolume));
                    if (num > 0) {
                        for (int i = 0; i < num; i++) {
                            upVolume();
                        }
                        downY = y;
                    }
                }
                break;
        }
        super.dispatchTouchEvent(e);
        return true;
    }

    public void upVolume() {
        if (currentVolume >= sumVolume) {
            return;
        }
        currentVolume++;
        invalidate();
    }

    public void downVolume() {
        if (currentVolume <= 0) {
            return;
        }
        currentVolume--;
        invalidate();
    }

    private VolumeChangeListener volumeChangeListener;

    public void addVolumeChangeListener(VolumeChangeListener volumeChangeListener) {
        this.volumeChangeListener = volumeChangeListener;
    }

    public interface VolumeChangeListener {
        void VolumeChange(int currentVolume);
    }
}

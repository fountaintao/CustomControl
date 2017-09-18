package com.taoyong.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * Creation Time :  2017/9/18.<br/>
 * Creator : tao yong.<br/>
 * Module description : <br/>
 */
public class MultiLineTextView extends View {
    private DisplayMetrics displayMetrics;
    private Paint paint;
    private Rect bounds;
    private String text;

    public MultiLineTextView(Context context) {
        super(context);
        init(context, null);
    }

    public MultiLineTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MultiLineTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public MultiLineTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (null == displayMetrics) {
            displayMetrics = context.getResources().getDisplayMetrics();
        }
        if (null != attrs) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MultiLineTextView);

            a.recycle();
        }
        if (null == paint) {
            paint = new Paint();
        }
        paint.setColor(Color.BLACK);
        paint.setTextSize(100f);
        bounds = new Rect();
        text = "有一个美丽的小女孩，她的名字叫做小薇.";
        paint.getTextBounds(text, 0, text.length(), bounds);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec),
                widthMode = MeasureSpec.getMode(widthMeasureSpec),
                heightSize = MeasureSpec.getSize(heightMeasureSpec),
                heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int lines = 0;
        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
            default:
                float textWidth = bounds.width();
                if (textWidth > displayMetrics.widthPixels) {
                    lines = (int) (textWidth / displayMetrics.widthPixels);
                }
                widthSize = (int) (getPaddingLeft() + textWidth + getPaddingRight());
                break;
        }
        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
            default:
                textHeight = bounds.bottom + bounds.height();
                heightSize = (int) (getPaddingTop() + textHeight * (lines + 1) + getPaddingBottom());
                break;
        }
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, widthMode);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, heightMode);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private int textHeight;

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        int index = 0, line = 1;
        int length = TextUtils.isEmpty(text) ? 0 : text.length();
        for (int i = 0; i < length; i++) {
            String s = text.substring(index, i);
            paint.getTextBounds(s, 0, s.length(), bounds);
            if (bounds.width() > displayMetrics.widthPixels) {
                s = text.substring(index, i - 1);
                canvas.drawText(s, getPaddingLeft(), textHeight * line, paint);
                index = i - 1;
                line += 1;
            }
            if (i + 1 == length) {
                s = text.substring(index, length);
                canvas.drawText(s, getPaddingLeft(), textHeight * line, paint);
            }
        }
    }
}

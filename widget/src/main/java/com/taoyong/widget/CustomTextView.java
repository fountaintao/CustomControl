package com.taoyong.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

/**
 * Creation Time :  2017/9/18.<br/>
 * Creator : tao yong.<br/>
 * Module description : <br/>
 */
public class CustomTextView extends View {
    private static final String TAG = "CustomTextView";
    private String text = "";
    private int textColor = Color.BLACK;
    private float textSize = 0f;
    private Paint paint;
    private Rect bounds;
    private DisplayMetrics displayMetrics;

    public CustomTextView(Context context) {
        super(context);
        init(context, null);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (null == displayMetrics) {
            displayMetrics = context.getResources().getDisplayMetrics();
        }
        if (null != attrs) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView);
            /***************************************************/
            text = a.getString(R.styleable.CustomTextView_text);
            /***************************************************/
            textColor = a.getColor(R.styleable.CustomTextView_textColor, Color.BLACK);
            /***************************************************/
            textSize = a.getDimension(R.styleable.CustomTextView_textSize,
                    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16f, displayMetrics));
            a.recycle();
        } else {
            textSize = sp2px(16f);
        }
        if (null == paint) {
            paint = new Paint();
        }
        if (null == bounds) {
            bounds = new Rect();
        }
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.getTextBounds(text, 0, text.length(), bounds);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec),
                widthMode = MeasureSpec.getMode(widthMeasureSpec),
                heightSize = MeasureSpec.getSize(heightMeasureSpec),
                heightMode = MeasureSpec.getMode(heightMeasureSpec);
        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
            default:
                float textWidth = bounds.width();
                widthSize = (int) (getPaddingLeft() + textWidth + getPaddingRight());
                break;
        }
        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
            default:
                float textHeight = bounds.height();
                heightSize = (int) (getPaddingTop() + textHeight + getPaddingBottom());
                break;
        }
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, widthMode);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, heightMode);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        canvas.drawText(text, getWidth() / 2 - bounds.width() / 2, getHeight() / 2 + bounds.height() / 2, paint);
    }

    public void setText(Object text) {
        this.text = "" + text;
        paint.getTextBounds(this.text, 0, this.text.length(), bounds);
        requestLayout();
    }

    public void setTextColor(int textColor) {
        paint.setColor(textColor);
        invalidate();
    }

    public void setTextSize(float textSize) {
        paint.setTextSize(textSize);
        paint.getTextBounds(text, 0, text.length(), bounds);
        requestLayout();
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * @return pxValue
     */
    public int sp2px(float spValue) {
        float fontScale = displayMetrics.scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}

package com.taoyong.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.taoyong.widget.abnormal.ColorExceptions;

import java.util.Arrays;

/**
 * Creation Time :  2017/9/9.<br/>
 * Creator : tao yong.<br/>
 * Module description : <br/>
 */
public class SlideBar extends View {
    private static final String TAG = "SlideBar";

    public SlideBar(Context context) {
        super(context);
        init(context, null);
    }

    public SlideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SlideBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SlideBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    //DataSource
    private String[] data;
    //standard source
    private String standard;
    //Calculate the height of a single character based on the number of data sources
    private int singleHeight;
    //brush
    private Paint paint;
    //Current selected location
    private int choose;
    //Open an additional selected condition
    private boolean additionalConditions;
    //Select the effect
    private SpecialEffect specialEffect;
    //The background color of the selected time
    private int chooseColor;
    //Unselected background color
    private int unChooseColor;
    //The font color of the selected time
    private int chooseTextColor;
    //The color of the unselected font
    private int unChooseTextColor;

    /**
     * initialize
     */
    private void init(Context context, AttributeSet attrs) {
        paint = new Paint();
        choose = -1;
        additionalConditions = true;
        if (null != attrs) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SlideBar);
            /********************************************************************/
            String slideBarData = a.getString(R.styleable.SlideBar_data);
            if (TextUtils.isEmpty(slideBarData)) {
                data = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};
            } else {
                data = slideBarData.split(",");
            }
            /********************************************************************/
            standard = a.getString(R.styleable.SlideBar_standard);
            if (TextUtils.isEmpty(standard)) {
                getStandard();
            } else {
                standard = standard.substring(0, 1);
            }
            /********************************************************************/
            int slideBarChooseColor = a.getColor(R.styleable.SlideBar_chooseColor, -1);
            if (slideBarChooseColor == -1) {
                chooseColor = getResources().getColor(R.color.color_dc);
            } else {
                chooseColor = slideBarChooseColor;
            }
            /********************************************************************/
            int slideBarUnChooseColor = a.getColor(R.styleable.SlideBar_unChooseColor, -1);
            if (slideBarUnChooseColor == -1) {
                unChooseColor = getResources().getColor(android.R.color.transparent);
            } else {
                unChooseColor = slideBarUnChooseColor;
            }
            /********************************************************************/
            int slideBarChooseTextColor = a.getColor(R.styleable.SlideBar_chooseTextColor, -1);
            if (slideBarChooseTextColor == -1) {
                chooseTextColor = getResources().getColor(android.R.color.holo_red_dark);
            } else {
                chooseTextColor = slideBarChooseTextColor;
            }
            /********************************************************************/
            int slideBarUnChooseTextColor = a.getColor(R.styleable.SlideBar_unChooseTextColor, -1);
            if (slideBarUnChooseTextColor == -1) {
                unChooseTextColor = getResources().getColor(android.R.color.black);
            } else {
                unChooseTextColor = slideBarUnChooseTextColor;
            }
            /********************************************************************/
            switch (a.getInteger(R.styleable.SlideBar_specialEffect, 0)) {
                case 0:
                    specialEffect = SpecialEffect.NONE;
                    break;
                case 1:
                    specialEffect = SpecialEffect.RECTABGLE;
                    break;
            }
            a.recycle();
        } else {
            data = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};
            getStandard();
            chooseColor = getResources().getColor(R.color.color_dc);
            unChooseColor = getResources().getColor(android.R.color.transparent);
            chooseTextColor = getResources().getColor(android.R.color.holo_red_dark);
            unChooseTextColor = getResources().getColor(android.R.color.black);
            specialEffect = SpecialEffect.NONE;
        }
        if (specialEffect == SpecialEffect.NONE) {
            setBackgroundColor(unChooseColor);
        }
    }

    /**
     * When the standard source is not set, the method of manually obtaining the standard source is obtained
     */
    private void getStandard() {
        paint.setTextSize(20);
        int count = data == null ? 0 : data.length;
        float[] dataWidth = new float[count];
        String[] newData = new String[count];
        for (int i = 0; i < count; i++) {
            newData[i] = data[i];
            dataWidth[i] = paint.measureText(data[i]);
        }
        paint.reset();
        for (int i = 0; i < count; i++) {
            for (int j = i + 1; j < count; j++) {
                if (dataWidth[i] < dataWidth[j]) {
                    //Sort the width of the string
                    float stamp = dataWidth[i];
                    dataWidth[i] = dataWidth[j];
                    dataWidth[j] = stamp;
                    //Sort string
                    String variable = newData[i];
                    newData[i] = newData[j];
                    newData[j] = variable;
                }
            }
        }
        if (count % 2 == 0) {
            standard = newData[count / 2];
        } else {
            standard = newData[(count + 1) / 2];
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //Calculate the available width
        int usableWidth = getWidth() - getPaddingLeft() - getPaddingRight(),
                usableHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        //Get the number of data sources
        int count = data == null ? 0 : data.length;
        //Calculate the height of a single character based on the number of data sources
        singleHeight = usableHeight / count;
        //Draw the character of the loop data source
        for (int i = 0; i < count; i++) {
            if (i == choose && additionalConditions) {
                if (specialEffect == SpecialEffect.RECTABGLE) {
                    paint.setColor(chooseColor);
                    paint.setAntiAlias(true);
                    if (usableWidth > singleHeight) {
                        int median = usableWidth - singleHeight;
                        canvas.drawRect(new RectF(getPaddingLeft() + median / 2f, getPaddingTop() + singleHeight * i,
                                getPaddingLeft() + usableWidth - median / 2f, getPaddingTop() + singleHeight * (i + 1)), paint);
                    } else {
                        int median = singleHeight - usableWidth;
                        canvas.drawRect(new RectF(getPaddingLeft(), getPaddingTop() + singleHeight * i + median / 2f,
                                getPaddingLeft() + usableWidth, getPaddingTop() + singleHeight * (i + 1) - median / 2f), paint);
                    }
                    paint.reset();
                }
                paint.setColor(chooseTextColor);
            } else {
                paint.setColor(unChooseTextColor);
            }
            paint.setFakeBoldText(false);
            paint.setAntiAlias(true);//anti-aliasing
            float textSize = usableWidth / 2f >= singleHeight ? singleHeight : usableWidth / 2f;
            paint.setTextSize(textSize);
            /***********************************************************/
            float offx = usableWidth / 2 - paint.measureText(data[i]) / 2 + getPaddingLeft(),
                    offy = singleHeight * (i + 0.5f) + getPaddingTop() + paint.measureText(standard) / 2;
            canvas.drawText(data[i], offx, offy, paint);
            paint.reset();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        float y = e.getY();
        int currentChoose = (int) ((y - getPaddingTop()) / singleHeight);
        int count = data == null ? 0 : data.length;
        switch (e.getAction()) {
            case MotionEvent.ACTION_UP:
                if (specialEffect == SpecialEffect.NONE) {
                    setBackgroundColor(unChooseColor);
                }
                if (null != mTextDialog) {
                    if (mTextDialog.isShown()) {
                        mTextDialog.setVisibility(GONE);
                    }
                }
                choose = -1;
                invalidate();
                break;
            default:
                if (specialEffect == SpecialEffect.NONE) {
                    setBackgroundColor(chooseColor);
                }
                if (choose != currentChoose) {
                    if (currentChoose >= 0 && currentChoose < count) {
                        if (null != slideListening) {
                            slideListening.slideChange(data[currentChoose]);
                        }
                        if (null != mTextDialog) {
                            mTextDialog.setText(data[currentChoose]);
                            if (!mTextDialog.isShown()) {
                                mTextDialog.setVisibility(VISIBLE);
                            }
                        }
                        choose = currentChoose;
                        invalidate();
                    }
                }
                break;
        }
        super.dispatchTouchEvent(e);
        return true;
    }

    /**
     * Set the selected position
     *
     * @param choose
     */
    public void setChoose(int choose) {
        this.choose = choose;
        invalidate();
    }

    /**
     * Set additional selection criteria
     *
     * @param additionalConditions
     */
    public void setAdditionalConditions(boolean additionalConditions) {
        this.additionalConditions = additionalConditions;
        invalidate();
    }

    /**
     * Set the sliding effect
     *
     * @param specialEffect
     */
    public void setSpecialEffect(SpecialEffect specialEffect) {
        this.specialEffect = specialEffect;
        if (specialEffect == SpecialEffect.NONE) {
            setBackgroundColor(unChooseColor);
        } else {
            setBackgroundColor(getResources().getColor(android.R.color.transparent));
        }
        invalidate();
    }

    /**
     * Set up the data source
     *
     * @param data
     */
    public void setData(String[] data) {
        this.data = data;
        getStandard();
        invalidate();
    }

    /**
     * Set up the data standard source
     *
     * @param standard
     */
    public void setStandard(String standard) {
        this.standard = standard;
        invalidate();
    }

    /**
     * Set the background color
     *
     * @param chooseColor Color type
     */
    public void setChooseColor(int chooseColor) throws ColorExceptions {
        if (chooseColor == -1) {
            throw new ColorExceptions();
        }
        this.chooseColor = chooseColor;
        invalidate();
    }

    /**
     * Sets the unselected background color
     *
     * @param unChooseColor Color type
     */
    public void setUnChooseColor(int unChooseColor) throws ColorExceptions {
        if (unChooseColor == -1) {
            throw new ColorExceptions();
        }
        this.unChooseColor = unChooseColor;
        if (specialEffect == SpecialEffect.NONE) {
            setBackgroundColor(unChooseColor);
        } else {
            setBackgroundColor(getResources().getColor(android.R.color.transparent));
        }
        invalidate();
    }

    /**
     * @param chooseColor   Check the background color, Color type
     * @param unChooseColor Unselected background color, Color type
     */
    public void setBackgroundColor(int chooseColor, int unChooseColor) throws ColorExceptions {
        if (chooseColor == -1 || unChooseColor == -1) {
            throw new ColorExceptions();
        }
        this.chooseColor = chooseColor;
        this.unChooseColor = unChooseColor;
        invalidate();
    }

    /**
     * Sets the color of the selected font
     *
     * @param chooseTextColor Color type
     */

    public void setChooseTextColor(int chooseTextColor) throws ColorExceptions {
        if (chooseTextColor == -1) {
            throw new ColorExceptions();
        }
        this.chooseTextColor = chooseTextColor;
        invalidate();
    }

    /**
     * Sets the color of the unselected font
     *
     * @param unChooseTextColor Color type
     */
    public void setUnChooseTextColor(int unChooseTextColor) throws ColorExceptions {
        if (unChooseTextColor == -1) {
            throw new ColorExceptions();
        }
        this.unChooseTextColor = unChooseTextColor;
        invalidate();
    }

    /**
     * @param chooseTextColor   Check font color, Color type
     * @param unChooseTextColor Unselected font color, Color type
     */
    public void setTextColor(int chooseTextColor, int unChooseTextColor) throws ColorExceptions {
        if (chooseTextColor == -1 || unChooseTextColor == -1) {
            throw new ColorExceptions();
        }
        this.chooseTextColor = chooseTextColor;
        this.unChooseTextColor = unChooseTextColor;
        invalidate();
    }

    private TextView mTextDialog;

    /**
     * This is a simple open text view that displays the letters
     *
     * @param mTextDialog
     */
    public void setTextDialog(TextView mTextDialog) {
        this.mTextDialog = mTextDialog;
        if (mTextDialog.isShown()) {
            mTextDialog.setVisibility(GONE);
        }
    }

    private SlideListening slideListening;

    /**
     * Add slide listening
     *
     * @param slideListening
     */
    public void addSlideListening(SlideListening slideListening) {
        this.slideListening = slideListening;
    }

    /**
     * Swipes the inner class
     */
    public interface SlideListening {
        void slideChange(String c);
    }

    /**
     * Enumerations that are displayed when selected
     */
    public enum SpecialEffect {
        NONE,
        RECTABGLE
    }

    @Override
    public String toString() {
        return "SlideBar{" +
                "data=" + Arrays.toString(data) +
                ", standard='" + standard + '\'' +
                ", specialEffect=" + specialEffect +
                ", chooseColor=" + chooseColor +
                ", unChooseColor=" + unChooseColor +
                ", chooseTextColor=" + chooseTextColor +
                ", unChooseTextColor=" + unChooseTextColor +
                '}';
    }
}
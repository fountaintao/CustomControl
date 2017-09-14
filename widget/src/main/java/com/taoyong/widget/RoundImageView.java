package com.taoyong.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

/**
 * Creation Time :  2017/9/12.<br/>
 * Creator : tao yong.<br/>
 * Module description : <br/>
 * {@linkplain RoundImageView.imageMode} --- The way the picture is displayed<br/>
 * {@linkplain RoundImageView.borderMode} --- Injection mode of boundary layer<br/>
 * {@linkplain RoundImageView.borderWidth} --- Width of boundary<br/>
 * {@linkplain RoundImageView.borderColor} --- Color of boundary<br/>
 * {@linkplain RoundImageView.upperLeftRadius} --- The round Angle of the rounded rectangle , The upper left corner<br/>
 * {@linkplain RoundImageView.upperRightRadius} --- The round Angle of the rounded rectangle , The upper right corner<br/>
 * {@linkplain RoundImageView.lowerRightRadius} --- The round Angle of the rounded rectangle , The lower right corner<br/>
 * {@linkplain RoundImageView.lowerLeftRadius} --- The round Angle of the rounded rectangle , The lower left corner<br/>
 */
public class RoundImageView extends ImageView {
    private static final String TAG = "RoundImageView";
    private boolean DEBUG = false;//Whether to open the "DeBug" mode
    private static final float OFFSET = 10f;//Define an offset to make it clear that you can't erase an image
    private static final int COLOR_BITMAP_SIZE = 2;//The size of the color bitmap
    private ImageMode imageMode = ImageMode.NONE;//The way the picture is displayed
    private BorderMode borderMode = BorderMode.COVER;//Injection mode of boundary layer
    private int borderWidth = -1;//Width of boundary
    private int borderColor = Color.BLACK;//Color of boundary
    private float upperLeftRadius = -1f;//The round Angle of the rounded rectangle , The upper left corner
    private float upperRightRadius = -1f;//The round Angle of the rounded rectangle , The upper right corner
    private float lowerRightRadius = -1f;//The round Angle of the rounded rectangle , The lower right corner
    private float lowerLeftRadius = -1f;//The round Angle of the rounded rectangle , The lower left corner
    private Paint paint;//Create a brush
    private Path path;//Create a drawing path
    private Path borderPath;//Create a drawing border path
    private float mScale;//The proportion calculated according to the demand
    private float width,//Width of view
            height,//Height of view
            bitmapWidth,//Bitmap width
            bitmapHeight;//Bitmap height
    private BitmapShader bitmapShader;//The bitmap rendering
    private Matrix matrix;//Rendered matrix
    private float startX;//The initial X coordinate of the drawing
    private float startY;//The initial Y coordinates of the drawing
    private RectF frameRectF;//Frame matrix

    public RoundImageView(Context context) {
        super(context);
        init(context, null);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (null != attrs) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView);
            /*********************************************************************/
            imageMode = ImageMode.getImageMode(a.getInteger(R.styleable.RoundImageView_imageMode, 0));
            /*********************************************************************/
            borderMode = BorderMode.getBorderMode(a.getInteger(R.styleable.RoundImageView_borderMode, 0));
            /*********************************************************************/
            borderWidth = a.getDimensionPixelOffset(R.styleable.RoundImageView_borderWidth, -1);
            /*********************************************************************/
            borderColor = a.getColor(R.styleable.RoundImageView_borderColor, Color.BLACK);
            /*********************************************************************/
            float radius = a.getDimension(R.styleable.RoundImageView_radius, -1f);
            /*********************************************************************/
            upperLeftRadius = a.getDimension(R.styleable.RoundImageView_upperLeftRadius, -1f);
            if (upperLeftRadius == -1f) {
                upperLeftRadius = radius;
            }
            /*********************************************************************/
            upperRightRadius = a.getDimension(R.styleable.RoundImageView_upperRightRadius, -1f);
            if (upperRightRadius == -1f) {
                upperRightRadius = radius;
            }
            /*********************************************************************/
            lowerRightRadius = a.getDimension(R.styleable.RoundImageView_lowerRightRadius, -1f);
            if (lowerRightRadius == -1f) {
                lowerRightRadius = radius;
            }
            /*********************************************************************/
            lowerLeftRadius = a.getDimension(R.styleable.RoundImageView_lowerLeftRadius, -1f);
            if (lowerLeftRadius == -1f) {
                lowerLeftRadius = radius;
            }
        }
        Log.d(TAG, "init: " + this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec),
                widthSize = MeasureSpec.getSize(widthMeasureSpec),
                heightMode = MeasureSpec.getMode(heightMeasureSpec),
                heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (borderWidth > 0) {
            if (borderMode.equals(BorderMode.COVER)) {
            } else if (borderMode.equals(BorderMode.APPEND)) {
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize + borderWidth * 2, widthMode);
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize + borderWidth * 2, heightMode);
            } else if (borderMode.equals(BorderMode.SUBTRACT)) {
            } else {
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!setImageContent(canvas)) {
            super.onDraw(canvas);
        }
    }

    private boolean setImageContent(Canvas canvas) {
        Bitmap bitmap = getBitmapFromDrawable(getDrawable());
        if (null == bitmap) {
            return false;
        }
        if (null == paint) {
            paint = new Paint();
        }
        /*************************************/
        width = getWidth();
        height = getHeight();
        bitmapWidth = bitmap.getWidth();
        bitmapHeight = bitmap.getHeight();
        if (borderWidth > 0) {
            if (borderMode.equals(BorderMode.APPEND) ||
                    borderMode.equals(BorderMode.SUBTRACT)) {
                width = width - borderWidth * 2;
                height = height - borderWidth * 2;
            }
        }
        /*************************************/
        boolean flag = false;
        if (imageMode.equals(ImageMode.NONE)) {
            flag = matchNoneContent(canvas, bitmap);
        } else if (imageMode.equals(ImageMode.ROUNDRECT)) {
            flag = matchRoundrectContent(canvas, bitmap);
        } else if (imageMode.equals(ImageMode.CIRCLE)) {
            flag = matchCircleContent(canvas, bitmap);
        } else if (imageMode.equals(ImageMode.OVAL)) {
            flag = matchOvalContent(canvas, bitmap);
        }
        paint.reset();
        return flag;
    }

    private boolean matchNoneContent(Canvas canvas, Bitmap bitmap) {
        if (borderWidth <= 0) {
            return false;
        }
        //Here, for the sake of the picture, we use the smallest value shown in this picture to divide by the maximum value of the image.
        mScale = (width < height ? width : height) /
                (bitmapWidth > bitmapHeight ? bitmapWidth : bitmapHeight);
        setPaintBitmapShader(bitmap);
        canvas.drawRect(new RectF(startX, startY, bitmapWidth + startX, bitmapHeight + startY), paint);
        //canvas.drawBitmap(bitmap, startX, startY, paint);
        setPaintBorderColor();
        if (borderMode.equals(BorderMode.COVER)) {
            frameRectF = new RectF(startX + borderWidth / 2f, startY + borderWidth / 2f,
                    bitmapWidth + startX - borderWidth / 2f, bitmapHeight + startY - borderWidth / 2f);
        } else if (borderMode.equals(BorderMode.APPEND) ||
                borderMode.equals(BorderMode.SUBTRACT)) {
            frameRectF = new RectF(startX - borderWidth / 2f, startY - borderWidth / 2f,
                    bitmapWidth + startX + borderWidth / 2f, bitmapHeight + startY + borderWidth / 2f);
        }
        canvas.drawRect(frameRectF, paint);
        return true;
    }

    private boolean matchRoundrectContent(Canvas canvas, Bitmap bitmap) {
        //Here, for the sake of the picture, we use the smallest value shown in this picture to divide by the maximum value of the image.
        mScale = (width < height ? width : height) /
                (bitmapWidth > bitmapHeight ? bitmapWidth : bitmapHeight);
        //Helper version of saveLayer() that takes 4 values rather than a RectF.
        //执行了canvas.saveLayer()之后，我们所有的绘制操作都绘制到了我们新建的layer上，而不是canvas默认的layer。
        int layer = canvas.saveLayer(0, 0, canvas.getWidth(), canvas.getHeight(), null, Canvas.ALL_SAVE_FLAG);
        setPaintBitmapShader(bitmap);
        canvas.drawRect(new RectF(startX, startY, bitmapWidth + startX, bitmapHeight + startY), paint);
        //Set the brush to the eraser mode
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        if (isSettingRoundrect()) {
            float radius = width / 16f;
            drawLiftUp(canvas, radius);
            drawLiftDown(canvas, radius);
            drawRightDown(canvas, radius);
            drawRightUp(canvas, radius);
        } else {
            if (upperLeftRadius > 0f) {
                drawLiftUp(canvas, upperLeftRadius);
            }
            if (upperRightRadius > 0f) {
                drawRightUp(canvas, upperRightRadius);
            }
            if (lowerRightRadius > 0f) {
                drawRightDown(canvas, lowerRightRadius);
            }
            if (lowerLeftRadius > 0f) {
                drawLiftDown(canvas, lowerLeftRadius);
            }
        }
        //restore
        paint.setXfermode(null);
        canvas.restoreToCount(layer);
        //No border to return
        if (borderWidth <= 0) {
            return true;
        }
        //Calculate the border position to start the stroke
        setPaintBorderColor();
        drawRoundrectBorder(canvas);
        return true;
    }

    private boolean matchCircleContent(Canvas canvas, Bitmap bitmap) {
        float min = width < height ? width : height;
        mScale = min / (bitmapWidth < bitmapHeight ? bitmapWidth : bitmapHeight);
        setPaintBitmapShader(bitmap);
        //When drawing circles, you need to render the brush again
        matrix.set(null);
        matrix.setScale(mScale, mScale);
        matrix.postTranslate((getWidth() - bitmapWidth) / 2f, (getHeight() - bitmapHeight) / 2f);
        bitmapShader.setLocalMatrix(matrix);
        paint.setShader(bitmapShader);
        /************************************************************/
        canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, min / 2f, paint);
        //No border to return
        if (borderWidth <= 0) {
            return true;
        }
        setPaintBorderColor();
        if (borderMode.equals(BorderMode.COVER)) {
            canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, min / 2f - borderWidth / 2f, paint);
        } else if (borderMode.equals(BorderMode.APPEND) ||
                borderMode.equals(BorderMode.SUBTRACT)) {
            canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, min / 2f + borderWidth / 2f, paint);
        }
        return true;
    }

    private boolean matchOvalContent(Canvas canvas, Bitmap bitmap) {
        mScale = (width > height ? width : height) /
                (bitmapWidth < bitmapHeight ? bitmapWidth : bitmapHeight);
        setPaintBitmapShader(bitmap);
        if (borderMode.equals(BorderMode.COVER)) {
            canvas.drawOval(new RectF(0, 0, width, height), paint);
        } else if (borderMode.equals(BorderMode.APPEND) ||
                borderMode.equals(BorderMode.SUBTRACT)) {
            canvas.drawOval(new RectF(borderWidth, borderWidth, width, height), paint);
        }
        if (borderWidth > 0) {
            setPaintBorderColor();
            if (borderMode.equals(BorderMode.COVER)) {
                canvas.drawOval(new RectF(borderWidth / 2f, borderWidth / 2f,
                        width - borderWidth / 2f, height - borderWidth / 2f), paint);
            } else if (borderMode.equals(BorderMode.APPEND) ||
                    borderMode.equals(BorderMode.SUBTRACT)) {
                canvas.drawOval(new RectF(borderWidth / 2f, borderWidth / 2f,
                        width + borderWidth / 2f, height + borderWidth / 2f), paint);
            }
        }
        return true;
    }

    /**
     * Draw the border of the rounded rectangle
     *
     * @param canvas canvas
     */
    private void drawRoundrectBorder(Canvas canvas) {
        if (null == borderPath) {
            borderPath = new Path();
        }
        float radius = width / 16f;
        radius = anewCalculateRadius(radius);
        if (isSettingRoundrect()) {
            if (borderMode.equals(BorderMode.COVER)) {
                borderPath.moveTo(startX + borderWidth / 2f, startY + radius);
                borderPath.arcTo(new RectF(startX + borderWidth / 2f,
                        startY + borderWidth / 2f, startX + radius * 2 - borderWidth / 2f,
                        startY + radius * 2 - borderWidth / 2f), 180, 90);
                borderPath.arcTo(new RectF(startX + bitmapWidth - 2 * radius + borderWidth / 2f,
                        startY + borderWidth / 2f, startX + bitmapWidth - borderWidth / 2f,
                        startY + 2 * radius - borderWidth / 2f), -90, 90);
                borderPath.arcTo(new RectF(startX + bitmapWidth - 2 * radius + borderWidth / 2f,
                        startY + bitmapHeight - 2 * radius + borderWidth / 2f, startX + bitmapWidth - borderWidth / 2f,
                        startY + bitmapHeight - borderWidth / 2f), 0, 90);
                borderPath.arcTo(new RectF(startX + borderWidth / 2f,
                        startY + bitmapHeight - 2 * radius + borderWidth / 2f, startX + 2 * radius - borderWidth / 2f,
                        startY + bitmapHeight - borderWidth / 2f), 90, 90);
            } else if (borderMode.equals(BorderMode.APPEND) ||
                    borderMode.equals(BorderMode.SUBTRACT)) {
                borderPath.moveTo(startX - borderWidth / 2f, startY + radius);
                borderPath.arcTo(new RectF(startX - borderWidth / 2f,
                        startY - borderWidth / 2f, startX + radius * 2 + borderWidth / 2f,
                        startY + radius * 2 + borderWidth / 2f), 180, 90);
                borderPath.arcTo(new RectF(startX + bitmapWidth - 2 * radius - borderWidth / 2f,
                        startY - borderWidth / 2f, startX + bitmapWidth + borderWidth / 2f,
                        startY + 2 * radius + borderWidth / 2f), -90, 90);
                borderPath.arcTo(new RectF(startX + bitmapWidth - 2 * radius - borderWidth / 2f,
                        startY + bitmapHeight - 2 * radius - borderWidth / 2f, startX + bitmapWidth + borderWidth / 2f,
                        startY + bitmapHeight + borderWidth / 2f), 0, 90);
                borderPath.arcTo(new RectF(startX - borderWidth / 2f,
                        startY + bitmapHeight - 2 * radius - borderWidth / 2f, startX + 2 * radius + borderWidth / 2f,
                        startY + bitmapHeight + borderWidth / 2f), 90, 90);
            }
        } else {
            if (borderMode.equals(BorderMode.COVER)) {
                if (upperLeftRadius > 0f) {
                    radius = anewCalculateRadius(upperLeftRadius);
                    borderPath.moveTo(startX + borderWidth / 2f, startY + radius);
                    borderPath.arcTo(new RectF(startX + borderWidth / 2f,
                            startY + borderWidth / 2f, startX + radius * 2 - borderWidth / 2f,
                            startY + radius * 2 - borderWidth / 2f), 180, 90);
                } else {
                    borderPath.moveTo(startX + borderWidth / 2f, startY + borderWidth / 2f);
                }
                if (upperRightRadius > 0f) {
                    radius = anewCalculateRadius(upperRightRadius);
                    borderPath.arcTo(new RectF(startX + bitmapWidth - 2 * radius + borderWidth / 2f,
                            startY + borderWidth / 2f, startX + bitmapWidth - borderWidth / 2f,
                            startY + 2 * radius - borderWidth / 2f), -90, 90);
                } else {
                    borderPath.lineTo(startX + bitmapWidth - borderWidth / 2f, startY + borderWidth / 2f);
                }
                if (lowerRightRadius > 0f) {
                    radius = anewCalculateRadius(lowerRightRadius);
                    borderPath.arcTo(new RectF(startX + bitmapWidth - 2 * radius + borderWidth / 2f,
                            startY + bitmapHeight - 2 * radius + borderWidth / 2f, startX + bitmapWidth - borderWidth / 2f,
                            startY + bitmapHeight - borderWidth / 2f), 0, 90);
                } else {
                    borderPath.lineTo(startX + bitmapWidth - borderWidth / 2f, startY + bitmapHeight - borderWidth / 2f);
                }
                if (lowerLeftRadius > 0f) {
                    radius = anewCalculateRadius(lowerLeftRadius);
                    borderPath.arcTo(new RectF(startX + borderWidth / 2f,
                            startY + bitmapHeight - 2 * radius + borderWidth / 2f, startX + 2 * radius - borderWidth / 2f,
                            startY + bitmapHeight - borderWidth / 2f), 90, 90);
                } else {
                    borderPath.lineTo(startX + borderWidth / 2f, startY + bitmapHeight - borderWidth / 2f);
                }
            } else if (borderMode.equals(BorderMode.APPEND) ||
                    borderMode.equals(BorderMode.SUBTRACT)) {
                if (upperLeftRadius > 0f) {
                    radius = anewCalculateRadius(upperLeftRadius);
                    borderPath.moveTo(startX - borderWidth / 2f, startY + radius);
                    borderPath.arcTo(new RectF(startX - borderWidth / 2f,
                            startY - borderWidth / 2f, startX + radius * 2 + borderWidth / 2f,
                            startY + radius * 2 + borderWidth / 2f), 180, 90);
                } else {
                    borderPath.moveTo(startX - borderWidth / 2f, startY - borderWidth / 2f);
                }
                if (upperRightRadius > 0f) {
                    radius = anewCalculateRadius(upperRightRadius);
                    borderPath.arcTo(new RectF(startX + bitmapWidth - 2 * radius - borderWidth / 2f,
                            startY - borderWidth / 2f, startX + bitmapWidth + borderWidth / 2f,
                            startY + 2 * radius + borderWidth / 2f), -90, 90);
                } else {
                    borderPath.lineTo(startX + bitmapWidth + borderWidth / 2f, startY - borderWidth / 2f);
                }
                if (lowerRightRadius > 0f) {
                    radius = anewCalculateRadius(lowerRightRadius);
                    borderPath.arcTo(new RectF(startX + bitmapWidth - 2 * radius - borderWidth / 2f,
                            startY + bitmapHeight - 2 * radius - borderWidth / 2f, startX + bitmapWidth + borderWidth / 2f,
                            startY + bitmapHeight + borderWidth / 2f), 0, 90);
                } else {
                    borderPath.lineTo(startX + bitmapWidth + borderWidth / 2f, startY + bitmapHeight + borderWidth / 2f);
                }
                if (lowerLeftRadius > 0f) {
                    radius = anewCalculateRadius(lowerLeftRadius);
                    borderPath.arcTo(new RectF(startX - borderWidth / 2f,
                            startY + bitmapHeight - 2 * radius - borderWidth / 2f, startX + 2 * radius + borderWidth / 2f,
                            startY + bitmapHeight + borderWidth / 2f), 90, 90);
                } else {
                    borderPath.lineTo(startX - borderWidth / 2f, startY + bitmapHeight + borderWidth / 2f);
                }
            }
        }
        borderPath.close();
        canvas.drawPath(borderPath, paint);
    }

    /**
     * To revalue rounded corners
     *
     * @param radius radius
     * @return radius
     */
    private float anewCalculateRadius(float radius) {
        float maxRadius = bitmapWidth < bitmapHeight ? bitmapWidth / 2f : bitmapHeight / 2f;
        if (radius > maxRadius) {
            radius = maxRadius;
        }
        return radius;
    }

    /**
     * Cut the top left corner
     *
     * @param canvas canvas
     * @param radius radius
     */
    private void drawLiftUp(Canvas canvas, float radius) {
        if (null == path) {
            path = new Path();
        }
        radius = anewCalculateRadius(radius);
        path.moveTo(startX - OFFSET, startY + radius);
        path.lineTo(startX, startY);
        path.lineTo(startX + radius, startY - OFFSET);
        path.arcTo(new RectF(startX, startY, startX + radius * 2, startY + radius * 2), -90, -90);
        path.close();
        canvas.drawPath(path, paint);
    }

    /**
     * Cut the bottom left corner
     *
     * @param canvas canvas
     * @param radius radius
     */
    private void drawLiftDown(Canvas canvas, float radius) {
        if (null == path) {
            path = new Path();
        }
        radius = anewCalculateRadius(radius);
        path.moveTo(startX - OFFSET, startY + bitmapHeight - radius);
        path.lineTo(startX, startY + bitmapHeight);
        path.lineTo(startX + radius, startY + bitmapHeight + OFFSET);
        path.arcTo(new RectF(startX, startY + bitmapHeight - radius * 2, startX + radius * 2, startY + bitmapHeight), 90, 90);
        path.close();
        canvas.drawPath(path, paint);
    }

    /**
     * Cut the bottom right hand corner
     *
     * @param canvas canvas
     * @param radius radius
     */
    private void drawRightDown(Canvas canvas, float radius) {
        if (null == path) {
            path = new Path();
        }
        radius = anewCalculateRadius(radius);
        path.moveTo(startX + bitmapWidth + OFFSET, startY + bitmapHeight - radius);
        path.lineTo(startX + bitmapWidth, startY + bitmapHeight);
        path.lineTo(startX + bitmapWidth - radius, startY + bitmapHeight + OFFSET);
        path.arcTo(new RectF(startX + bitmapWidth - radius * 2,
                startY + bitmapHeight - radius * 2,
                startX + bitmapWidth, startY + bitmapHeight), 90, -90);
        path.close();
        canvas.drawPath(path, paint);
    }

    /**
     * Cut the top right corner
     *
     * @param canvas canvas
     * @param radius radius
     */
    private void drawRightUp(Canvas canvas, float radius) {
        if (null == path) {
            path = new Path();
        }
        radius = anewCalculateRadius(radius);
        path.moveTo(startX + bitmapWidth + OFFSET, startY + radius);
        path.lineTo(startX + bitmapWidth, startY);
        path.lineTo(startX + bitmapWidth - radius, startY - OFFSET);
        path.arcTo(new RectF(startX + bitmapWidth - radius * 2, startY, startX + bitmapWidth, startY + radius * 2), -90, 90);
        path.close();
        canvas.drawPath(path, paint);
    }

    /**
     * Set the border color for the brush
     */
    private void setPaintBorderColor() {
        paint.reset();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(borderWidth);
        paint.setColor(borderColor);
        paint.setAntiAlias(true);
    }

    /**
     * Set a bitmap for the brush
     *
     * @param bitmap bitmap
     */
    private Bitmap setPaintBitmapShader(Bitmap bitmap) {
        bitmapWidth *= mScale;
        bitmapHeight *= mScale;
        startX = (width - bitmapWidth) / 2f;
        startY = (height - bitmapHeight) / 2f;
        if (borderWidth > 0) {
            if (borderMode.equals(BorderMode.APPEND) ||
                    borderMode.equals(BorderMode.SUBTRACT)) {
                startX += borderWidth;
                startY += borderWidth;
            }
        }
        bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        if (null == matrix) {
            matrix = new Matrix();
        }
        matrix.set(null);
        matrix.setScale(mScale, mScale);
        matrix.postTranslate(startX, startY);
        bitmapShader.setLocalMatrix(matrix);
        paint.setShader(bitmapShader);
        paint.setAntiAlias(true);
        return Bitmap.createScaledBitmap(bitmap, (int) bitmapWidth, (int) bitmapHeight, true);
    }

    /**
     * Whether or not the rectangle corners have been set
     *
     * @return
     */
    private boolean isSettingRoundrect() {
        return upperLeftRadius == -1f &&
                upperRightRadius == -1f &&
                lowerRightRadius == -1f &&
                lowerLeftRadius == -1f;
    }

    /**
     * Get "Bitmap" from "Drawable"
     *
     * @param drawable drawable
     * @return bitmap
     */
    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (null == drawable) {
            return null;
        }
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        try {
            Bitmap bitmap;
            if (drawable instanceof ColorDrawable) {
                //Returns a mutable bitmap with the specified width and height.
                bitmap = Bitmap.createBitmap(COLOR_BITMAP_SIZE, COLOR_BITMAP_SIZE, Bitmap.Config.ARGB_8888);
            } else {
                //Returns a mutable bitmap with the specified width and height.
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            }
            //Construct a canvas with the specified bitmap to draw into.
            Canvas canvas = new Canvas(bitmap);
            //Draw in its bounds (set via setBounds) respecting optional effects such as alpha (set via setAlpha) and color filter (set via setColorFilter).
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            //Draw in its bounds (set via setBounds) respecting optional effects such as alpha (set via setAlpha) and color filter (set via setColorFilter).
            drawable.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            if (DEBUG) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public String toString() {
        return "RoundImageView{" +
                "imageMode=" + imageMode +
                ", borderMode=" + borderMode +
                ", borderWidth=" + borderWidth +
                ", borderColor=" + borderColor +
                ", upperLeftRadius=" + upperLeftRadius +
                ", upperRightRadius=" + upperRightRadius +
                ", lowerRightRadius=" + lowerRightRadius +
                ", lowerLeftRadius=" + lowerLeftRadius +
                '}';
    }

    /**
     * The way the picture is displayed<br/>
     * {@link ImageMode.NONE} - 0 : Don't make any changes<br/>
     * {@link ImageMode.ROUNDRECT} - 1 : Shows a rounded rectangle<br/>
     * {@link ImageMode.CIRCLE} - 2 : It's going to be a circle<br/>
     * {@link ImageMode.OVAL} - 3 : Display as an ellipse
     */
    public enum ImageMode {
        NONE("none", 0),
        ROUNDRECT("roundrect", 1),
        CIRCLE("circle", 2),
        OVAL("oval", 3);

        private String name;
        private int value;

        ImageMode(String name, int value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public int getValue() {
            return value;
        }

        public static ImageMode getImageMode(int value) {
            ImageMode[] imageModes = values();
            for (ImageMode imageMode : imageModes) {
                if (imageMode.getValue() == value) {
                    return imageMode;
                }
            }
            return NONE;
        }

        public static ImageMode getImageMode(String name) {
            ImageMode[] imageModes = values();
            for (ImageMode imageMode : imageModes) {
                if (imageMode.getName().equals(name)) {
                    return imageMode;
                }
            }
            return NONE;
        }
    }

    /**
     * Injection mode of boundary layer<br/>
     * {@link BorderMode.COVER} - 0 : Cover the boundary of the image<br/>
     * {@link BorderMode.APPEND} - 1 : Add width to the image<br/>
     * {@link BorderMode.SUBTRACT} - 2 : Subtract the width from the image
     */
    public enum BorderMode {
        COVER("cover", 0),
        APPEND("append", 1),
        SUBTRACT("subtract", 2);

        private String name;
        private int value;

        BorderMode(String name, int value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public int getValue() {
            return value;
        }

        public static BorderMode getBorderMode(int value) {
            BorderMode[] borderModes = values();
            for (BorderMode borderMode : borderModes) {
                if (borderMode.getValue() == value) {
                    return borderMode;
                }
            }
            return COVER;
        }

        public static BorderMode getBorderMode(String name) {
            BorderMode[] borderModes = values();
            for (BorderMode borderMode : borderModes) {
                if (borderMode.getName().equals(name)) {
                    return borderMode;
                }
            }
            return COVER;
        }
    }

    public void setImageMode(ImageMode imageMode) {
        this.imageMode = imageMode;
        invalidate();
    }

    public void setBorderMode(BorderMode borderMode) {
        this.borderMode = borderMode;
        invalidate();
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
        invalidate();
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
        invalidate();
    }

    public void setUpperLeftRadius(float upperLeftRadius) {
        this.upperLeftRadius = upperLeftRadius;
        invalidate();
    }

    public void setUpperRightRadius(float upperRightRadius) {
        this.upperRightRadius = upperRightRadius;
        invalidate();
    }

    public void setLowerRightRadius(float lowerRightRadius) {
        this.lowerRightRadius = lowerRightRadius;
        invalidate();
    }

    public void setLowerLeftRadius(float lowerLeftRadius) {
        this.lowerLeftRadius = lowerLeftRadius;
        invalidate();
    }

    public void setRadius(float upperLeftRadius, float upperRightRadius, float lowerRightRadius, float lowerLeftRadius) {
        this.upperLeftRadius = upperLeftRadius;
        this.upperRightRadius = upperRightRadius;
        this.lowerRightRadius = lowerRightRadius;
        this.lowerLeftRadius = lowerLeftRadius;
        invalidate();
    }
}
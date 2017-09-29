package com.taoyong.widget.group;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.taoyong.widget.R;

/**
 * Creation Time :  2017/9/19.<br/>
 * Creator : tao yong.<br/>
 * Module description : <br/>
 */
public class CustomLayout extends ViewGroup {

    /**
     * 左沟里儿童使用的空间。
     */
    private int mLeftWidth;
    /**
     * 孩子们在右边的排水沟里所使用的空间。
     */
    private int mRightWidth;
    /**
     * 这些都是基于它们的重力来计算儿童帧的。
     */
    private final Rect mTmpContainerRect = new Rect();
    private final Rect mTmpChildRect = new Rect();

    public CustomLayout(Context context) {
        super(context);
    }

    public CustomLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 任何不滚动的布局管理器都需要这个。
     */
    @Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    /**
     * 要求所有的孩子测量自己，并根据孩子的情况来计算这个布局的尺寸。
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        // 这些都记录了我们在左边和右边所使用的空间的位置;我们需要成员变量，以便以后可以使用这些变量。
        mLeftWidth = 0;
        mRightWidth = 0;
        // 度量最终将计算这些值。
        int maxHeight = 0;
        int maxWidth = 0;
        int childState = 0;
        // 遍历所有的孩子，测量他们，并根据他们的尺寸计算我们的尺寸。
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                // 测量的孩子。
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
                // 根据布局参数更新我们的大小信息。那些被要求放在左边或右边的孩子会被放在那些排水沟里。
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if (lp.position == LayoutParams.POSITION_LEFT) {
                    mLeftWidth += Math.max(maxWidth,
                            child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
                } else if (lp.position == LayoutParams.POSITION_RIGHT) {
                    mRightWidth += Math.max(maxWidth,
                            child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
                } else {
                    maxWidth = Math.max(maxWidth,
                            child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
                }
                maxHeight = Math.max(maxHeight,
                        child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
                childState = combineMeasuredStates(childState, child.getMeasuredState());
            }
        }
        // 总宽度是所有内子的最大宽度加上排水沟的宽度。
        maxWidth += mLeftWidth + mRightWidth;
        // 检查我们的最小高度和宽度
        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());
        // 我们最终的尺寸报告。
        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
                resolveSizeAndState(maxHeight, heightMeasureSpec,
                        childState << MEASURED_HEIGHT_STATE_SHIFT));
    }

    /**
     * 在这个布局中放置所有的孩子。
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int count = getChildCount();
        // 这些是我们正在进行布局的左和右边缘。
        int leftPos = getPaddingLeft();
        int rightPos = right - left - getPaddingRight();
        // 这是阴沟里的中间区域。
        final int middleLeft = leftPos + mLeftWidth;
        final int middleRight = rightPos - mRightWidth;
        // 这些是我们执行布局的顶部和底部边缘。
        final int parentTop = getPaddingTop();
        final int parentBottom = bottom - top - getPaddingBottom();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                final int width = child.getMeasuredWidth();
                final int height = child.getMeasuredHeight();
                // 计算我们放置这个子的坐标系。
                if (lp.position == LayoutParams.POSITION_LEFT) {
                    mTmpContainerRect.left = leftPos + lp.leftMargin;
                    mTmpContainerRect.right = leftPos + width + lp.rightMargin;
                    leftPos = mTmpContainerRect.right;
                } else if (lp.position == LayoutParams.POSITION_RIGHT) {
                    mTmpContainerRect.right = rightPos - lp.rightMargin;
                    mTmpContainerRect.left = rightPos - width - lp.leftMargin;
                    rightPos = mTmpContainerRect.left;
                } else {
                    mTmpContainerRect.left = middleLeft + lp.leftMargin;
                    mTmpContainerRect.right = middleRight - lp.rightMargin;
                }
                mTmpContainerRect.top = parentTop + lp.topMargin;
                mTmpContainerRect.bottom = parentBottom - lp.bottomMargin;
                // 使用孩子的重力和大小来决定其容器内的最终框架。
                Gravity.apply(lp.gravity, width, height, mTmpContainerRect, mTmpChildRect);
                // 孩子的地方。
                child.layout(mTmpChildRect.left, mTmpChildRect.top,
                        mTmpChildRect.right, mTmpChildRect.bottom);
            }
        }
    }

    //实现的其余部分用于自定义的每个子布局参数。
    //如果您不需要这些(例如，您正在编写一个布局管理器
    //那对孩子的定位是固定的)，你可以把这一切都放下来。
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new CustomLayout.LayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    /**
     * 自定义每个孩子布局信息。
     */
    public static class LayoutParams extends MarginLayoutParams {
        /**
         * 应用于这些布局参数的视图的重力。
         */
        public int gravity = Gravity.TOP | Gravity.START;
        public static final int POSITION_MIDDLE = 0;
        public static final int POSITION_LEFT = 1;
        public static final int POSITION_RIGHT = 2;
        public int position = POSITION_MIDDLE;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            //在通货膨胀期间，从布局XML中拉出布局的param值。如果您不关心改变XML中的布局行为，那么这是不需要的。
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.CustomLayout);
            gravity = a.getInt(R.styleable.CustomLayout_android_layout_gravity, gravity);
            position = a.getInt(R.styleable.CustomLayout_layout_position, position);
            a.recycle();
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }
}
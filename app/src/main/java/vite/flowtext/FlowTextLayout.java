package vite.flowtext;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by trs on 17-8-10.
 */

public class FlowTextLayout extends ViewGroup {

    public FlowTextLayout(@NonNull Context context) {
        this(context, null);
    }

    public FlowTextLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowTextLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int childCount = getChildCount();
        int height = 0;//计算当前的高度
        int lineWidth = 0;//单行宽度
        int lineHeight = 0;//单行高度

        if (heightMode == MeasureSpec.AT_MOST) {//wrap_content
            //按加入的child分配高度
            View childView = null;
            for (int i = 0; i < childCount; i++) {
                childView = getChildAt(i);
                measureChild(childView, widthMeasureSpec, heightMeasureSpec);
                MarginLayoutParams params = (MarginLayoutParams) childView.getLayoutParams();
                int childWidth = childView.getMeasuredWidth() + params.leftMargin + params.rightMargin;
                int childHeight = childView.getMeasuredHeight() + params.topMargin + params.bottomMargin;

                if (lineWidth + childWidth > widthSize - getPaddingLeft() - getPaddingRight()) {
                    //如果当前child的宽度加上目前单行的宽度大于当前根布局的宽度
                    lineWidth = childWidth;//重置
                    height += lineHeight;//递增
                    lineHeight = childHeight;//重置高度
                } else {
                    //加上child也没满一行
                    lineWidth += childWidth;
                    lineHeight = Math.max(lineHeight, childHeight);
                }
                if (i == childCount - 1) {
                    //如果是当前最后一个
                    height += lineHeight;
                }
            }
            setMeasuredDimension(widthSize, height);
        } else {
            //按限制的高度分配child
//            mLineCount = (heightSize - getPaddingTop() - getPaddingBottom()) / (mItemHeight + mItemMarginTop + mItemMarginBottom);
            View childView = null;
            for (int i = 0; i < childCount; i++) {
                childView = getChildAt(i);
                measureChild(childView, widthMeasureSpec, heightMeasureSpec);
                MarginLayoutParams params = (MarginLayoutParams) childView.getLayoutParams();
                int childWidth = childView.getMeasuredWidth() + params.leftMargin + params.rightMargin;
                int childHeight = childView.getMeasuredHeight() + params.topMargin + params.bottomMargin;

                if (lineWidth + childWidth > widthSize - getPaddingLeft() - getPaddingRight()) {
                    //如果当前child的宽度加上目前单行的宽度大于当前根布局的宽度
                    lineWidth = childWidth;//重置
                    height += lineHeight;//递增
                    lineHeight = childHeight;//重置高度
                } else {
                    //加上child也没满一行
                    lineWidth += childWidth;
                    lineHeight = Math.max(lineHeight, childHeight);
                }
                if (i == childCount - 1) {
                    //如果是当前最后一个
                    height += lineHeight;
                }

                if (height >= heightSize /* || currentRowCount > mLineCount*/) {
                    //抛弃view
                    for (int j = i; j < childCount; j++)
                        removeViewAt(i);
                    break;
                }
            }
            setMeasuredDimension(widthSize, heightSize);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = getWidth();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();

        int lineWidth = 0;
        int lineHeight = 0;

        int childLeft = 0;
        int childTop = paddingTop;
        int childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            MarginLayoutParams params = (MarginLayoutParams) childView.getLayoutParams();
//            int childWidth = childView.getMeasuredWidth() + params.leftMargin + params.rightMargin;
//            int childHeight = childView.getMeasuredHeight() + params.topMargin + params.bottomMargin;

            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();

            if (childWidth + params.leftMargin + params.rightMargin + lineWidth >
                    width - paddingLeft - paddingRight) {
                //大于当前行，则另起一行
                childLeft = paddingLeft;
                childTop += childHeight + (params.topMargin + params.bottomMargin);
                lineWidth = paddingLeft + childWidth + params.leftMargin + params.rightMargin;
            } else {
                //未满一行
                childLeft = lineWidth;
                lineWidth = childLeft + childWidth + params.leftMargin + params.rightMargin;
            }

            childView.layout(childLeft + params.leftMargin,
                    childTop + params.topMargin,
                    childLeft + childWidth + params.rightMargin,
                    childTop + childHeight + params.bottomMargin);
            Log.i("FlowTextLayout", "prams[" + i + "]:" + childView.getMeasuredWidth() + "," + childView.getMeasuredHeight());
            Log.i("FlowTextLayout", "layout[" + i + "]:" + (childLeft + params.leftMargin)
                    + "," + (childTop + params.topMargin) +
                    "," + (childLeft + childWidth + params.rightMargin) +
                    "," + (childTop + childHeight + params.bottomMargin));
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }
}

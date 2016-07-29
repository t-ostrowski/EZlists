package com.ostro.myshoppinglist.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.ostro.myshoppinglist.R;

/**
 * Created by Thomas Ostrowski
 * ostrowski.thomas@gmail.com
 * on 29/07/2016.
 */

public class DynamicHeightImageView extends ImageView {

    private float mHeightRatio = 0.0f;
    private int mHeight = 0;

    public DynamicHeightImageView(Context context) {
        super(context);
    }

    public DynamicHeightImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        heightRatioFromAttributes(context, attrs);
    }

    public DynamicHeightImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        heightRatioFromAttributes(context, attrs);
    }

    private void heightRatioFromAttributes(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DynamicHeightImageView);
        mHeightRatio = a.getFloat(R.styleable.DynamicHeightImageView_ratio, 0);
    }

    public void setHeightRatio(float ratio) {
        if (ratio != mHeightRatio) {
            mHeightRatio = ratio;
            requestLayout();
        }
    }

    public void setHeight(int height) {
        if (height != mHeight) {
            mHeight = height;
            requestLayout();
        }
    }

    public double getHeightRatio() {
        return mHeightRatio;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mHeightRatio > 0.0) {
            // set the image views size
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = (int) (width * mHeightRatio);
            setMeasuredDimension(width, height);
        } else if (mHeight != 0) {
            int height = mHeight;
            int width = MeasureSpec.getSize(widthMeasureSpec);
            setMeasuredDimension(width, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}


package com.vce.baselib.custom.shapeview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatCheckBox;

import com.vce.baselib.R;
import com.vce.baselib.custom.shapeview.builder.ButtonDrawableBuilder;
import com.vce.baselib.custom.shapeview.builder.ShapeDrawableBuilder;
import com.vce.baselib.custom.shapeview.builder.TextColorBuilder;
import com.vce.baselib.custom.shapeview.styleable.ShapeCheckBoxStyleable;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/ShapeView
 *    time   : 2021/07/17
 *    desc   : 支持直接定义 Shape 背景的 CheckBox
 */
public class ShapeCheckBox extends AppCompatCheckBox {

    private static final ShapeCheckBoxStyleable STYLEABLE = new ShapeCheckBoxStyleable();

    private final ShapeDrawableBuilder mShapeDrawableBuilder;
    private final TextColorBuilder mTextColorBuilder;
    private final ButtonDrawableBuilder mButtonDrawableBuilder;

    public ShapeCheckBox(Context context) {
        this(context, null);
    }

    public ShapeCheckBox(Context context, AttributeSet attrs) {
        this(context, attrs, androidx.appcompat.R.attr.checkboxStyle);
    }

    public ShapeCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShapeCheckBox);
        mShapeDrawableBuilder = new ShapeDrawableBuilder(this, typedArray, STYLEABLE);
        mTextColorBuilder = new TextColorBuilder(this, typedArray, STYLEABLE);
        mButtonDrawableBuilder = new ButtonDrawableBuilder(this, typedArray, STYLEABLE);
        typedArray.recycle();

        mShapeDrawableBuilder.intoBackground();

        if (mTextColorBuilder.isTextGradientColors() || mTextColorBuilder.isTextStrokeColor()) {
            setText(getText());
        } else {
            mTextColorBuilder.intoTextColor();
        }

        mButtonDrawableBuilder.intoButtonDrawable();
    }

    @Override
    public void setTextColor(int color) {
        super.setTextColor(color);
        if (mTextColorBuilder == null) {
            return;
        }
        mTextColorBuilder.setTextColor(color);
        mTextColorBuilder.clearTextGradientColors();
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (mTextColorBuilder != null &&
                (mTextColorBuilder.isTextGradientColors() || mTextColorBuilder.isTextStrokeColor())) {
            super.setText(mTextColorBuilder.buildTextSpannable(text), type);
        } else {
            super.setText(text, type);
        }
    }

    @Override
    public void setButtonDrawable(Drawable drawable) {
        super.setButtonDrawable(drawable);
        if (mButtonDrawableBuilder == null) {
            return;
        }
        mButtonDrawableBuilder.setButtonDrawable(drawable);
    }

    public ShapeDrawableBuilder getShapeDrawableBuilder() {
        return mShapeDrawableBuilder;
    }

    public TextColorBuilder getTextColorBuilder() {
        return mTextColorBuilder;
    }

    public ButtonDrawableBuilder getButtonDrawableBuilder() {
        return mButtonDrawableBuilder;
    }
}
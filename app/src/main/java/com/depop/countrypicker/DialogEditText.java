package com.depop.countrypicker;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;

/**
 * Subclass of edit text that can be used to suppress input when used with a dialog
 * {@link #setWithDialog()}
 */
public class DialogEditText extends TextInputEditText {

    protected View.OnFocusChangeListener mOnFocusChangeListener;

    public DialogEditText(final Context context) {
        super(context);
    }

    public DialogEditText(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public DialogEditText(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setOnFocusChangeListener(final View.OnFocusChangeListener onFocusChangeListener) {
        mOnFocusChangeListener = onFocusChangeListener;
    }

    public void setWithDialog(final boolean noKeyboard) {
        setInputType(noKeyboard ? InputType.TYPE_NULL : InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        setLongClickable(false);
        setTextIsSelectable(false);
        setCursorVisible(false);
        super.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View view, final boolean hasFocus) {
                if (mOnFocusChangeListener != null) {
                    mOnFocusChangeListener.onFocusChange(view, hasFocus);
                }
            }
        });
        // This stops the keyboard being shown and allows the fragment or activity to show a dialog
        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (mOnFocusChangeListener != null) {
                    mOnFocusChangeListener.onFocusChange(view, true);
                }
            }
        });
    }

    public void setWithDialog() {
        setWithDialog(false);
    }
}

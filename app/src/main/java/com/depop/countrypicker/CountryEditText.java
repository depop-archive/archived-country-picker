package com.depop.countrypicker;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.Locale;

public class CountryEditText extends DialogEditText {

    private CountryPickerDialog mCountryPickerDialog;
    // This needs to override the listener on the dialog edit text
    private View.OnFocusChangeListener mOnFocusChangeListener;
    private Locale mSelectedLocale;
    @DrawableRes
    private int mCountryFlag;


    public CountryEditText(final Context context) {
        this(context, null);
    }

    public CountryEditText(final Context context, final AttributeSet attrs) {
        this(context, attrs, R.attr.editTextStyle);
    }

    public CountryEditText(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setCountryText(new Locale(Locale.getDefault().getLanguage(), getResources().getConfiguration().locale.getCountry()));
        setWithDialog();
        super.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View view, final boolean hasFocus) {
                if (mOnFocusChangeListener != null) {
                    mOnFocusChangeListener.onFocusChange(view, hasFocus);
                }
                if (hasFocus) {
                    showCountryDialog();
                }
            }
        });
    }

    @Override
    public Parcelable onSaveInstanceState() {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(Args.SUPER_STATE, super.onSaveInstanceState());
        bundle.putString(Args.COUNTRY, mSelectedLocale.getCountry());
        bundle.putInt(Args.FLAG, mCountryFlag);
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(final Parcelable state) {
        if (state instanceof Bundle) { //Implicit null check
            final Bundle bundle = (Bundle) state;
            setCountry(bundle.getString(Args.COUNTRY));
            mCountryFlag = bundle.getInt(Args.FLAG);
            super.onRestoreInstanceState(bundle.getParcelable(Args.SUPER_STATE));
        } else {
            super.onRestoreInstanceState(state);
        }
    }

    public void setCountry(final String countryCode) {
        setCountryText(new Locale(Locale.getDefault().getLanguage(),
                TextUtils.isEmpty(countryCode) ? getResources().getConfiguration().locale.getCountry() : countryCode));
    }

    private void showCountryDialog() {
        if (mCountryPickerDialog == null || !mCountryPickerDialog.isShowing()) {
            mCountryPickerDialog = new CountryPickerDialog(getContext(), Country.valueOf(mSelectedLocale.getCountry()), new CountryPickerDialog.OnCountrySelectedListener() {
                @Override
                public void onCountrySelected(final Country country) {
                    setCountry(country.name());
                }
            });
            mCountryPickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(final DialogInterface dialog) {
                    clearFocus();
                }
            });
            mCountryPickerDialog.setTitle(R.string.dialog_title_country);
            mCountryPickerDialog.show();
        }
    }

    private void setCountryText(final Locale locale) {
        mSelectedLocale = locale;
        mCountryFlag = Country.valueOf(locale.getCountry()).getCountryFlag();
        setText(mSelectedLocale.getDisplayCountry());
        setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, mCountryFlag, 0);
    }

    public Locale getSelectedLocale() {
        return mSelectedLocale;
    }

    @Override
    public void setOnFocusChangeListener(final View.OnFocusChangeListener onFocusChangeListener) {
        mOnFocusChangeListener = onFocusChangeListener;
    }

    private class Args {
        private static final String COUNTRY = "country";
        private static final String SUPER_STATE = "superState";
        private static final String FLAG = "flag";
    }
}

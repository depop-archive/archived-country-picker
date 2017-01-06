package com.depop.countrypicker;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;

class CountryPickerDialog extends AppCompatDialog implements ClickViewHolder.OnItemClickListener {

    private Country mSelectedCountry;
    private CountryAdapter mCountryAdapter;
    private OnCountrySelectedListener mListener;

    CountryPickerDialog(final Context context, final Country selectedCountry, final OnCountrySelectedListener listener) {
        super(context);
        mSelectedCountry = selectedCountry;
        mListener = listener;
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_country_picker);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mCountryAdapter = new CountryAdapter(getContext(), this, mSelectedCountry);
        recyclerView.setAdapter(mCountryAdapter);
        recyclerView.scrollToPosition(mCountryAdapter.getSelectedCountryPosition());
    }

    @Override
    public void onDetachedFromWindow() {
        mCountryAdapter.stopLoadDrawables();
        super.onDetachedFromWindow();
    }

    @Override
    public void onItemClick(final View view, final int position) {
        final Country country = mCountryAdapter.getCountryAtPosition(position);
        if (mSelectedCountry != country) {
            mListener.onCountrySelected(country);
        }
        dismiss();
    }

    interface OnCountrySelectedListener {
        void onCountrySelected(final Country country);

    }

}

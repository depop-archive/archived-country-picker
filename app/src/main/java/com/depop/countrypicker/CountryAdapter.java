package com.depop.countrypicker;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.Collator;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.ViewHolder> {
    private final ClickViewHolder.OnItemClickListener mClickListener;
    private final Country mSelectedCountry;
    private List<Country> mCountries = Arrays.asList(Country.values());
    private Map<Country, Pair<String, Drawable>> mCountryStringHashMap = new HashMap<>(mCountries.size());

    public CountryAdapter(final Context context, final ClickViewHolder.OnItemClickListener clickListener, final Country selectedCountry) {
        final Locale locale = context.getResources().getConfiguration().locale;
        final Collator collator = Collator.getInstance(locale);
        final String language = locale.getLanguage();
        collator.setStrength(Collator.PRIMARY);
        Collections.sort(mCountries, new Comparator<Country>() {
            @Override
            public int compare(final Country country1, final Country country2) {
                return collator.compare(
                        country1.getCountryName(language), country2.getCountryName(language));
            }
        });
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                for (final Country country : mCountries) {
                    mCountryStringHashMap.put(country, new Pair<>(country.getCountryName(language), country.getFlagDrawable(context)));
                }
            }
        });
        mClickListener = clickListener;
        mSelectedCountry = selectedCountry;
    }

    @Override
    public int getItemCount() {
        return mCountries.size();
    }

    @Override
    public CountryAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        return new CountryAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_country, parent, false));
    }

    public Country getCountryAtPosition(final int position) {
        return mCountries.get(position);
    }


    @Override
    public void onBindViewHolder(final CountryAdapter.ViewHolder holder, final int position) {
        final Country country = mCountries.get(position);
        final Pair<String, Drawable> countryPair = getCountryPair(country, holder.itemView.getContext());
        holder.countryNameTextView.setText(countryPair.first);
        holder.countryNameTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(countryPair.second, null, null, null);
        holder.countryNameTextView.setActivated(country == mSelectedCountry);
    }

    private Pair<String, Drawable> getCountryPair(final Country country, final Context context) {
        if (!mCountryStringHashMap.containsKey(country)) {
            mCountryStringHashMap.put(country, new Pair<>(country.getCountryName(context.getResources().getConfiguration().locale.getLanguage()), country.getFlagDrawable(context)));
        }
        return mCountryStringHashMap.get(country);
    }

    public int getSelectedCountryPosition() {
        return mCountries.indexOf(mSelectedCountry);
    }

    public class ViewHolder extends ClickViewHolder {
        private TextView countryNameTextView;

        public ViewHolder(final View itemView) {
            super(itemView, mClickListener);
            countryNameTextView = (TextView) itemView;
        }
    }

}

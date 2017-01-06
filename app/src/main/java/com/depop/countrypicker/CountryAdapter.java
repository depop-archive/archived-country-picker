package com.depop.countrypicker;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
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

class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.ViewHolder> {
    private static final String DRAWABLE_PAYLOAD = "DRAWABLE_PAYLOAD";
    private final ClickViewHolder.OnItemClickListener mClickListener;
    private final Country mSelectedCountry;
    private List<Country> mCountries = Arrays.asList(Country.values());
    private Map<Country, Drawable> mCountryDrawableHashMap = new HashMap<>(mCountries.size());
    private LoadDrawables mLoadDrawables;
    private Context mContext;
    private boolean mTaskRunning;


    CountryAdapter(final Context context, final ClickViewHolder.OnItemClickListener clickListener, final Country selectedCountry) {
        mContext = context;
        final Collator collator = Collator.getInstance(Locale.getDefault());
        collator.setStrength(Collator.PRIMARY);
        Collections.sort(mCountries, new Comparator<Country>() {
            @Override
            public int compare(final Country country1, final Country country2) {
                return collator.compare(
                        country1.getCountryName(), country2.getCountryName());
            }
        });
        mLoadDrawables = new LoadDrawables();
        mLoadDrawables.execute((Void) null);
        mClickListener = clickListener;
        mSelectedCountry = selectedCountry;
    }

    void stopLoadDrawables() {
        if (mTaskRunning) {
            mLoadDrawables.cancel(true);
        }
    }

    @Override
    public int getItemCount() {
        return mCountries.size();
    }

    @Override
    public CountryAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        return new CountryAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_country, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        onBindViewHolder(holder, position, Collections.emptyList());
    }

    Country getCountryAtPosition(final int position) {
        return mCountries.get(position);
    }


    @Override
    public void onBindViewHolder(@NonNull final CountryAdapter.ViewHolder holder, final int position, @NonNull final List<Object> payloads) {
        final Country country = mCountries.get(position);
        if (payloads.isEmpty()) {
            holder.countryNameTextView.setText(country.getCountryName());
            holder.countryNameTextView.setActivated(country == mSelectedCountry);
        }
        holder.countryNameTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(mCountryDrawableHashMap.get(country) != null ? mCountryDrawableHashMap.get(country) : ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.empty_flag), null, null, null);
    }

    int getSelectedCountryPosition() {
        return mCountries.indexOf(mSelectedCountry);
    }

    class ViewHolder extends ClickViewHolder {
        private TextView countryNameTextView;

        ViewHolder(final View itemView) {
            super(itemView, mClickListener);
            countryNameTextView = (TextView) itemView;
        }
    }

    private class LoadDrawables extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mTaskRunning = true;
        }

        @Override
        protected Void doInBackground(final Void... notUsed) {
            final int size = mCountries.size();
            // Loop circularly through entire list from selected country onwards
            for (int i = 0, j = mCountries.indexOf(mSelectedCountry); i < size; i++, j = (j + 1) % size) {
                if (isCancelled()) {
                    break;
                }
                mCountryDrawableHashMap.put(mCountries.get(j), mCountries.get(j).getFlagDrawable(mContext));
                publishProgress(j);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(final Integer... values) {
            super.onProgressUpdate(values);
            CountryAdapter.this.notifyItemChanged(values[0], DRAWABLE_PAYLOAD);
        }

        @Override
        protected void onPostExecute(final Void aVoid) {
            super.onPostExecute(aVoid);
            mTaskRunning = false;
        }
    }

}

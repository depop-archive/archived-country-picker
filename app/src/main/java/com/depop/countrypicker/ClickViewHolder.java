package com.depop.countrypicker;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ClickViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    private final OnItemClickListener onItemClickListener;
    private final OnItemLongClickListener onItemLongClickListener;

    public ClickViewHolder(final View itemView, final OnItemClickListener onItemClickListener) {
        this(itemView, onItemClickListener, null);
    }

    public ClickViewHolder(final View itemView, final OnItemClickListener onItemClickListener, final OnItemLongClickListener onItemLongClickListener) {
        super(itemView);
        this.onItemClickListener = onItemClickListener;
        this.onItemLongClickListener = onItemLongClickListener;
        itemView.setOnClickListener(this);
        if (onItemLongClickListener != null) {
            itemView.setOnLongClickListener(this);
        }
    }

    @Override
    public void onClick(final View view) {
        if (onItemClickListener != null) {
            final int itemPosition = getItemPosition();
            if (itemPosition != RecyclerView.NO_POSITION) {
                onItemClickListener.onItemClick(view, itemPosition);
            }
        }
    }

    @Override
    public boolean onLongClick(final View view) {
        return onItemLongClickListener.onItemLongClick(view, getAdapterPosition());
    }

    protected int getItemPosition() {
        return getAdapterPosition();
    }

    public interface OnItemClickListener {
        void onItemClick(final View view, final int position);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(final View view, final int position);
    }
}

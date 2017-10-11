package com.example.debarshibanerjee.projectfiredemon.helpers;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by debarshibanerjee on 11/10/17.
 */

public class BetterLinearLayoutManager extends LinearLayoutManager {
    public BetterLinearLayoutManager(Context context) {
        super(context);
    }

    public BetterLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public BetterLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            Log.e("LinearLayoutManager", "LinearLayoutManager Crashed Fix when google adds support https://issuetracker.google.com/issues/37007605");
        }
    }
}

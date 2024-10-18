package com.zybooks.weighttracker.DailyWeights;

import android.view.View;

import com.zybooks.weighttracker.data.model.Weights;

/**
 * Class container used to set the onClick action for the delete button on the weight list.
 * The weight list is a recycler so there are n # of buttons, the onClick action passes the current
 * record count argument to get the correct row in the database.
 * Last updated 10/16/2024, by Laura Brooks
 * NOTE: this is reusable for any additional onClicks needed within a View Adapter.
 */

public interface OnItemClickListener {
    void onItemClick(View view, int position, Weights objRow);
    void onLongItemClick(View view, int position, Weights objRow);
}

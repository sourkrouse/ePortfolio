package com.zybooks.weighttracker.DailyWeights;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.zybooks.weighttracker.R;
import com.zybooks.weighttracker.data.model.Weights;

import java.text.SimpleDateFormat;
import java.util.List;

/*
Last Updated 10/16/2024, Laura Brooks
This screen is a view adapter which runs in the background of the main activity.
All data found will be placed in XML file titled - fragment_weight_list
Called from main activity in a Recycler View using Weights object which is pulled from DB in main view
OnClick events are called from main activity on delete buttons, uses a position to find the row to delete
Layout Manager from the main activity runs the number of times matching the size of the dataset.

 */
public class WeightsViewAdapter extends RecyclerView.Adapter<WeightsViewAdapter.ViewHolder> {

    private List<Weights> localDataSet;
    private OnItemClickListener mListener;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView amtTextView;
        private final TextView dateTextView;
        public ImageButton deleteButton;


        // find the data fields from recycler view on main activity
        public ViewHolder(View view) {
            super(view);

            amtTextView = (TextView) view.findViewById(R.id.weight_amt);
            dateTextView = (TextView) view.findViewById(R.id.weight_date);
            deleteButton = (ImageButton) itemView.findViewById(R.id.delete_button);

        }

        // binds the onClick event to the delete button
        // onClick events are called from main activity
        public void bind(final OnItemClickListener listener, final int position, Weights objRow) {
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("ON CLICK",Integer.toString(position));
                    listener.onItemClick(v.findViewById(R.id.delete_button), position, objRow);
                }
            });

            deleteButton.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onLongItemClick(v, position, objRow);
                    return true;
                }
            });
        }

        public TextView getAmtTextView() {
            return amtTextView;
        }
        public TextView getDateTextView() {
            return dateTextView;
        }

    }

    /**
     * Initialize the dataset of the Adapter
     *
     * @param dataSet Weight Object containing the data to populate views to be used
     * by RecyclerView
     */
    public WeightsViewAdapter(@Nullable List<Weights> dataSet, OnItemClickListener listener) {
        localDataSet = dataSet;
        this.mListener = listener;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fragment_weight_list, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        if(localDataSet != null){
            Weights objRow = localDataSet.get(position);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy");
            String recordDate = simpleDateFormat.format(objRow.getRecordDate());
            viewHolder.getAmtTextView().setText(Float.toString(objRow.getWeight()));
            viewHolder.getDateTextView().setText(recordDate);
            viewHolder.bind(mListener, position, objRow);
        }


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if(localDataSet == null){
            return 0;
        } else {
            return localDataSet.size();
        }

    }
}
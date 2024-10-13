package com.zybooks.weighttracker.DailyWeights;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.zybooks.weighttracker.AddDailyWeight;
import com.zybooks.weighttracker.R;
import com.zybooks.weighttracker.WeightsActivity;
import com.zybooks.weighttracker.data.model.Weights;

import java.text.SimpleDateFormat;
import java.util.List;

/*
Last Updated 10/6/2024, Laura Brooks
This screen is a view adapter which runs in the background of the main activity.
PLACEHOLDER - not being used yet until some other issues are resolved. This page will serve the
results of the database query in a Recycler View.




 */
public class WeightsViewAdapter extends RecyclerView.Adapter<WeightsViewAdapter.ViewHolder> {

    private List<Weights> localDataSet;
    private OnClickListener onClickListener;
    private OnItemClickListener mListener;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView amtTextView;
        private final TextView dateTextView;
        public ImageButton deleteButton;



        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View


            amtTextView = (TextView) view.findViewById(R.id.weight_amt);
            dateTextView = (TextView) view.findViewById(R.id.weight_date);
            deleteButton = (ImageButton) itemView.findViewById(R.id.delete_button);

            // Set click listener on the ViewHolder's item view

        }

        public void bind(final OnItemClickListener listener, final int position) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v, position);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onLongItemClick(v, position);
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
        public ImageButton getDeleteButton() {
            return deleteButton;
        }
    }

    /**
     * Initialize the dataset of the Adapter
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView
     */
    public WeightsViewAdapter(List<Weights> dataSet, OnItemClickListener listener) {
        localDataSet = dataSet;
        this.mListener = listener;

    }

    // Setter for the click listener
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    // Interface for the click listener
    public interface OnClickListener {
        void onClick(int position, Weights model);
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
        Weights objRow = localDataSet.get(position);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy");
        String recordDate = simpleDateFormat.format(objRow.getRecordDate());
        viewHolder.getAmtTextView().setText(Float.toString(objRow.getWeight()));
        viewHolder.getDateTextView().setText(recordDate);
        viewHolder.bind(mListener, position);
        //button.setText("Delete?");
        //button.setId(objRow.getWId());
        // Set click listener on the item view
        /*
        button.setOnClickListener(view -> {
            if (onClickListener != null) {
                onClickListener.onClick(position, objRow);
            }
        });


        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeightsViewAdapter.this, DeleteWeightActivity.class);
                intent.putExtra(DeleteWeightActivity.SINGLE_WEIGHT_ID, objRow.getWId());
                startActivity(intent);
                setResult(RESULT_OK, intent);
            }
        });
        */
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
package com.zybooks.weighttracker;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddDailyWeight extends AppCompatActivity {

    public static final String EXTRA_WEIGHT_ID = "com.zybooks.studyhelper.weights_Wid";
    public static final String EXTRA_PROFILE_ID = "com.zybooks.studyhelper.register_id";


    private EditText mWeightAmt;
    private EditText mWeightDate;


    private WeightAndSeeDatabase mWeightsDb;

    private long mRId; //create variable to store user profile ID
    private long mWId;
    private Weights mWeights;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_daily_weight);

        mWeightAmt = findViewById(R.id.editWeight);
        mWeightDate = findViewById(R.id.editDate);
    // Input type for weight should be a decimal
        mWeightAmt.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);


        mWeightsDb = WeightAndSeeDatabase.getInstance(getApplicationContext());

        // Get weight ID from Weights
        Intent intent = getIntent();
        mWId = intent.getLongExtra(EXTRA_WEIGHT_ID, -1);

        mRId = intent.getLongExtra(EXTRA_PROFILE_ID, -1);


        if (mWId == -1) {
            // Add new weights
            mWeights = new Weights();
            //TODO - set title in action bar
            //setTitle(R.string.add_question);
        }
        else {
            // Update existing question
            mWeights = mWeightsDb.weightDao().getWeight(mWId);
            mWeights.setRId(mRId);
            mWeightAmt.setText(mWeights.getWeight().toString());
            mWeightDate.setText(String.valueOf(mWeights.getRecordDate()));

            //TODO - for action bar
            //setTitle(R.string.update_question);
        }


    }
    @SuppressLint("SimpleDateFormat")
    public static Date formatDate(String inputDate) {
        Date formattedDate;
        try {

            DateFormat targetFormat = new SimpleDateFormat("MM/dd/yyyy"); //Output date format
            Date date = targetFormat.parse(inputDate);
            formattedDate = date;
            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void addWeightButtonClick(View view){

        //Intent intent = new Intent(this, RegisterActivity.class);
        //startActivity(new Intent(AddDailyWeight.this, WeightsActivity.class));
        //finish();
        //SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

        // TODO This section needs the register ID of the person viewing the weights.
        //mWeights.setRId(mRId);
        mWeights.setRId(2);
        mWeights.setWeight(Float.valueOf(mWeightAmt.getText().toString()));
        mWeights.setRecordDate(formatDate(mWeightDate.getText().toString()));

        if (mWId == -1) {
            // New weight
            mWeightsDb.weightDao().insertWeight(mWeights);
        } else {
            // Existing weight
            mWeightsDb.weightDao().updateWeight(mWeights);
        }

        // Send back register ID and new weight ID
        Intent intent = new Intent(this, WeightsActivity.class);
        intent.putExtra(EXTRA_WEIGHT_ID, mWeights.getWId());
        intent.putExtra(EXTRA_PROFILE_ID, mWeights.getRId());
        setResult(RESULT_OK, intent);
        finish();
    }
}
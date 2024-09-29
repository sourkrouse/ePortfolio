package com.zybooks.weighttracker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zybooks.weighttracker.data.DAO.RegisterDao;
import com.zybooks.weighttracker.data.DAO.WeightsDao;
import com.zybooks.weighttracker.data.InitDb;
import com.zybooks.weighttracker.data.model.Register;
import com.zybooks.weighttracker.data.model.Weights;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
Last Updated 9/29/2024, Laura Brooks
PAGE DISPLAYS - weight and date text fields and button to add

UPDATES INCLUDE:
1) Changed colors to a softer green
2) Added the special back button to go back to the home page
3) Updated the header so it only displays the title and settings button
4) Adjusted manifest file to ensure all pages have an intended direction
TODO Items line 192,205
 */
public class AddDailyWeight extends AppCompatActivity {

    public static final String EXTRA_WEIGHT_ID = "com.zybooks.studyhelper.weights_Wid";
    public static final String EXTRA_PROFILE_ID = "com.zybooks.studyhelper.register_id";
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private EditText mWeightAmt;
    private EditText mWeightDate;
    private Button addDailyWeight;

    private int mRId; //create variable to store user profile ID
    private int mWId;

    private RegisterDao registerDao;
    private WeightsDao weightsDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_daily_weight);

        mWeightAmt = findViewById(R.id.editWeight);
        mWeightDate = findViewById(R.id.editDate);
    // Input type for weight should be a decimal
        mWeightAmt.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        addDailyWeight = findViewById(R.id.addDailyWeight);

        // custom back arrow in the action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.back_arrow);
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Get profile ID from the passed Bundle
        savedInstanceState = getIntent().getExtras();
        mRId = savedInstanceState.getInt(EXTRA_PROFILE_ID, -1);

        // Initialize the database
        weightsDao = InitDb.appDatabase.weightsDao();


        // Set onClickListener for the login button
        addDailyWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertWeight();
                goToDailyWeights(mRId);
            }
        });



    }

    // format function for the inputted date to ensure correct format
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

    public void addWeightButtonClick(){
        insertWeight();
    }

    private void insertWeight() {

        // TODO: check for existing profile before inserting new record
        executorService.execute(new Runnable() {
            @Override
            public void run() {

                Float weightAmt = Float.valueOf(mWeightAmt.getText().toString());
                Date dateEntered = formatDate((mWeightDate.getText().toString()));


                // Insert new weight
                if (weightAmt != null) {
                    Weights addWeight = new Weights();
                    addWeight.setWeight(weightAmt);
                    addWeight.setRecordDate(dateEntered);
                    addWeight.setRId(mRId);

                    weightsDao.insertWeight(addWeight);
                } else {

                    //TODO: not working when trying to pull on same activity
                    //error();
                }


            }
        });
    }

    private void goToDailyWeights(int newID){


        //Log.d("IDTAG",String.valueOf(mProfile.getId()));
        //Log.d("NAMETAG",String.valueOf(mProfile.getLast()));
        Intent intent = new Intent(AddDailyWeight.this, WeightsActivity.class);
        intent.putExtra(WeightsActivity.EXTRA_PROFILE_ID, newID);
        startActivity(intent);
        setResult(RESULT_OK, intent);
    }
}
package com.zybooks.weighttracker.DailyWeights;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.zybooks.weighttracker.R;
import com.zybooks.weighttracker.SettingsFragment;
import com.zybooks.weighttracker.data.DAO.WeightsDao;
import com.zybooks.weighttracker.data.InitDb;
import com.zybooks.weighttracker.data.model.Weights;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
Last Updated - 10/16/2024, by Laura Brooks
Delete Weight - runs a delete query in the database to delete the weight by entry ID.

This activity class runs a delete query against the weights database using the weight entry ID
The delete function returns a bool, if true the user returns to the weight list
if false, the user sees a delete error.

 */
public class DeleteWeightActivity extends AppCompatActivity {
    public static final String SINGLE_WEIGHT_ID = "com.zybooks.weighttracker.weights_id";
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private WeightsDao weightsDao;
    private boolean mDarkTheme;
    private SharedPreferences mSharedPrefs;
    private boolean deleteStatus;
    public int weightId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mDarkTheme = mSharedPrefs.getBoolean(SettingsFragment.PREFERENCE_THEME, false);
        if (mDarkTheme) {
            setTheme(R.style.DarkTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deleteweight);

        // custom back arrow in the action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.back_arrow);
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Get weight ID from the passed Bundle, -1 indicates not found
        savedInstanceState = getIntent().getExtras();
        weightId = savedInstanceState.getInt(SINGLE_WEIGHT_ID, -1);


        // Initialize the RegisterDao
        weightsDao = InitDb.appDatabase.weightsDao();
        // call main method to start activity
        runDelete(weightId);

    }

    // main method to run the delete query and return a status from the object class.
    final private void runDelete(int weightId) {
        deleteWeight(weightId);
        TextView textView = (TextView)findViewById(R.id.delete_message);
        if(deleteStatus == true){

            textView.setText("DELETE SUCCESSFUL!"); //message will not be seen if working correctly
            finish();
            //go back
        } else {
            textView.setText("DELETE ERROR!"); //message for user, delete error only
        }
    }

    // delete method runs on executor service thread to run delete query, sets bool for delete status
    private void deleteWeight(int weightID){
        // Execute the database query on a background thread
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                weightsDao.deleteByWeightId(weightID);
                final Weights weight = weightsDao.getWeight(weightID);

                // Handle the result on the main thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weight != null) {
                            // delete was NOT successful
                            deleteStatus = getDeleteStatus(false);

                        } else {
                            // delete success
                            Log.d("DELETE","DELETE SUCCESS");
                            deleteStatus = getDeleteStatus(true);

                        }
                    }
                });
            }
        });
    }

    // object getter method to check if delete was successful from main activity
    private boolean getDeleteStatus(boolean status){
        return status;
    }

}

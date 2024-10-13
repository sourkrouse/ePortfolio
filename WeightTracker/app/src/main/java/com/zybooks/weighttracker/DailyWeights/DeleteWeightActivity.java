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
import com.zybooks.weighttracker.data.model.Register;
import com.zybooks.weighttracker.data.model.Weights;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DeleteWeightActivity extends AppCompatActivity {
    public static final String SINGLE_WEIGHT_ID = "com.zybooks.weighttracker.weights_id";
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private WeightsDao weightsDao;
    private boolean mDarkTheme;
    private SharedPreferences mSharedPrefs;
    private boolean deleteStatus;

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

        // Get weight ID from the passed Bundle
        savedInstanceState = getIntent().getExtras();
        int weightId = savedInstanceState.getInt(SINGLE_WEIGHT_ID, -1);


        // Initialize the RegisterDao
        weightsDao = InitDb.appDatabase.weightsDao();

        deleteWeight(weightId);
        TextView textView = (TextView)findViewById(R.id.delete_message);
        if(deleteStatus == true){

            textView.setText("DELETE SUCCESSFUL!"); //set text for text view
            //go back
        } else {
            textView.setText("DELETE ERROR!"); //set text for text view
        }
    }



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
                            // Invalid login credentials
                            Log.d("DELETE","DELETE SUCCESS");
                            deleteStatus = getDeleteStatus(true);
                            //Toast.makeText(LoginActivity.this, "Invalid login credentials", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private boolean getDeleteStatus(boolean status){
        return status;
    }

}

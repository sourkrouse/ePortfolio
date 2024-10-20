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

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
Last Updated - 10/16/2024, by Laura Brooks
Delete Weight - runs a delete query in the database to delete the weight by entry ID.

This activity class runs a delete query against the weights database using the weight entry ID
An object is created in the WeightsActivity that sets the weight ID and runs the database code.
 A true/false is returned back to the main activity where the decision statement will display a message.

 */
public class DeleteWeightActivity extends AppCompatActivity {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private WeightsDao weightsDao = InitDb.appDatabase.weightsDao();;
    private boolean deleteStatus;
    public int weightId;

    // constructor sets the weight ID variables
    public DeleteWeightActivity(int weightID){
        this.weightId = weightID;

    }

    // delete method runs on executor service thread to run delete query, sets bool for delete status
    private void deleteWeight(int weightID){
        // using a Future object to call the DB Query in executor
        Future<Boolean> result = executorService.submit(new Callable<Boolean>() {
            // Callable requires the call() function
            public Boolean call() throws Exception {
                // the other thread - running DB Query, check that weight object exists
                weightsDao.deleteByWeightId(weightID);
                final Weights weight = weightsDao.getWeight(weightID);

                if (weight != null) {
                    // delete was NOT successful
                    return false;

                } else {
                    // delete success
                    Log.d("DELETE","DELETE SUCCESS");
                    return true;

                }

            }
        });
        // try/catch to get the result from the Future object or the exception
        try {
            deleteStatus = result.get();
        } catch (Exception e) {
            // failed
            deleteStatus = false;
        }
        executorService.shutdown(); // shutdown executor once complete



    }


    // object getter method to check if delete was successful from main activity
    public boolean getDeleteStatus(){
        deleteWeight(weightId);
        return deleteStatus;
    }

}

package com.zybooks.weighttracker.DailyWeights;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;


import com.zybooks.weighttracker.data.DAO.WeightsDao;
import com.zybooks.weighttracker.data.InitDb;
import com.zybooks.weighttracker.data.model.Weights;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/*
Last Updated 10/6/2024, Laura Brooks
This screen is a view model which runs in the background of the main activity.

FUNCTIONS INCLUDE - taking the user ID and running a query in the database to pull a list
of weights based on the user ID. Returns a list object back to the main thread..


 */
public class WeightsViewModel extends ViewModel {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private WeightsDao weightsDao;
    public List<Weights> returnedList = new ArrayList<>();
    //private WeightsViewModel wvm;

    // set and initialize the database connection to the weights table
    WeightsViewModel(WeightsRepository weightRepository) {
        // Initialize the Weights Database
        weightsDao = InitDb.appDatabase.weightsDao();

    }

    // function runs from the main thread and returns the list of weights
    public List<Weights> pullList(@Nullable int userID) {
        // can be launched in a separate asynchronous job
        if (userID == -1){
            returnedList = null;
        } else {
            this.getDBWeightList(userID);
        }



        if (returnedList != null){
            //loginResult.setValue(new LoginResult(R.string.login_success));
            return returnedList;

        } else {
            //loginResult.setValue(new LoginResult(R.string.login_failed));
            return null;
        }

    }

    // get database query result in a list object format
    private void getDBWeightList(int userID) {


        executorService.execute(new Runnable() {
            @Override
            public void run() {
                List<Weights> getList = weightsDao.getWeightsNewerFirst(userID);

                Log.d("WEIGHTLIST","weightsfound");
                if (getList != null) {
                    // Successful login, navigate to the main activity
                    setWeightArr(getList);
                } else {
                    // Invalid login credentials
                    //Log.d("LOGINERROR","Invalid Login");
                    //Toast.makeText(LoginActivity.this, "Invalid login credentials", Toast.LENGTH_SHORT).show();
                }
                // Handle the result on the main thread
/*
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });

 */

            }
        });


    }

    private void setWeightArr(List<Weights> listVar){
        returnedList = listVar;

    }
}

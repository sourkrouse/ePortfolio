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
Last Updated 10/13/2024, Laura Brooks
This screen is a view model which runs in the background of the main activity.

FUNCTIONS INCLUDE - taking the user ID and running a query in the database to pull a list
of weights based on the user ID. Returns a list object back to the main thread..


 */
public class WeightsViewModel extends ViewModel {


    private WeightsRepository weightRepository;
    private WeightsDao weightsDao;
    //private WeightsViewModel wvm;

    WeightsViewModel(){
        weightsDao = InitDb.appDatabase.weightsDao();
    }
    // set and initialize the database connection to the weights table

    WeightsViewModel(WeightsRepository weightRepository) {
        // Initialize the Weights Database
        weightsDao = InitDb.appDatabase.weightsDao();
        this.weightRepository = weightRepository;

    }

    // function runs from the main thread and returns the list of weights

    public List<Weights> pullList(@Nullable int userID) {
        // can be launched in a separate asynchronous job
        if (userID == -1){
            return null;
        } else {
            return weightRepository.seeList(userID);
        }



    }






}

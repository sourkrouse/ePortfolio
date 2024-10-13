package com.zybooks.weighttracker.DailyWeights;


import android.util.Log;

import androidx.annotation.Nullable;

import com.zybooks.weighttracker.data.DAO.WeightsDao;
import com.zybooks.weighttracker.data.InitDb;
import com.zybooks.weighttracker.data.LoginRepository;
import com.zybooks.weighttracker.data.model.Weights;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
Last Updated 10/6/2024, Laura Brooks
PLACEHOLDER - may be used to set the database connection


 */


public class WeightsDataSource {

    //private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private WeightsRepository weightRepository;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public List<Weights> returnedList = new ArrayList<>();


    public List<Weights> getWeights(@Nullable int userID) {

        try {
            // TODO: handle loggedInUser authentication
            // Execute the database query on a background thread
            WeightsDao weightsDao = InitDb.appDatabase.weightsDao();

            executorService.execute(new Runnable() {
                @Override
                public void run() {


                    Log.d("WEIGHTLIST","weightsfound");
                    if (Integer.toString(userID) != null) {
                        // userID is not null
                        List<Weights> getList = weightsDao.getWeightsNewerFirst(userID, 5);
                        setWeightArr(getList);
                    } else {
                        // Invalid login credentials
                        returnedList = null;
                        Log.d("WEIGHTERROR","NULL USER");
                        //Toast.makeText(LoginActivity.this, "Invalid login credentials", Toast.LENGTH_SHORT).show();
                    }



                }
            });
            return returnedList;
            //return new ResultList.Success<>(weightRepository.seeList());
        } catch (Exception e) {
            return null;
            //return new ResultList.Error(new IOException("Error logging in", e));
        }
    }

    private void setWeightArr(List<Weights> listVar){
        returnedList = listVar;

    }

    public void logout() {
        // TODO: revoke authentication
    }
}

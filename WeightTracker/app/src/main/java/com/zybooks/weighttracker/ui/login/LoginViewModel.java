package com.zybooks.weighttracker.ui.login;


import androidx.lifecycle.ViewModel;
import android.view.View;
import android.view.ViewGroup;

import com.zybooks.weighttracker.data.DAO.RegisterDao;
import com.zybooks.weighttracker.data.InitDb;
import com.zybooks.weighttracker.data.LoginRepository;

/**
Last updated 10/19/2024 by Laura Brooks
THIS PAGE IS A VIEW MODEL - it works behind the scenes

Completed items:
1) Added DB code to get the userID from the SQL query when passing the uname/pwd
2) Reformatted some methods to remove previous process of getting data from local cache
3) Commented out some other code meant to set the login status (not using this functionality)

 */
public class LoginViewModel extends ViewModel {


    private RegisterDao registerDao;
    private LoginRepository loginRepository;



    // constructor sets the database repository
    LoginViewModel(LoginRepository loginRepository) {
        // Initialize the RegisterDao
        registerDao = InitDb.appDatabase.registerDao();

        //OR use local cache
        this.loginRepository = loginRepository;


    }


    // arguments are uname/pwd passed from front end, returns user ID to front end
    public int login(String username, String password) {


        if (username == null || password == null){
            return -1;

        } else {
            return loginRepository.login(username, password);
        }


    }


}
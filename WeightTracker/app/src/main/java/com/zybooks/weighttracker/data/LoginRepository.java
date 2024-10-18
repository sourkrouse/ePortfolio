package com.zybooks.weighttracker.data;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import com.zybooks.weighttracker.MainActivity;
import com.zybooks.weighttracker.data.model.LoggedInUser;

/**
 * Last Updated 10/16/2024, by Laura Brooks
 * Class that requests authentication and user information from the room database and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {

    private static volatile LoginRepository instance;


    private LoginDataSource dataSource;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private LoggedInUser user = null;




    // private constructor : singleton access
    private LoginRepository(LoginDataSource dataSource) {


        this.dataSource = dataSource;
    }

    public static LoginRepository getInstance(LoginDataSource dataSource) {
        if (instance == null) {
            instance = new LoginRepository(dataSource);
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public void logout() {
        user = null;
        dataSource = null;

    }

    private void setLoggedInUser(LoggedInUser user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public int login(String username, String password) {
        // handle login


        int result = dataSource.run(username, password);
        if(Integer.toString(result) != null){
            return result;
        }

        return -1;
        /*
        if (result instanceof Result.Success) {
            setLoggedInUser(((Result.Success<LoggedInUser>) result).getData());
        }

         */
        //return result;
    }

}
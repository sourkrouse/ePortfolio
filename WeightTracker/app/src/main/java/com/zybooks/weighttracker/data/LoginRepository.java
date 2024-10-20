package com.zybooks.weighttracker.data;


import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.zybooks.weighttracker.data.model.LoggedInUser;

/**
 * Last Updated 10/16/2024, by Laura Brooks
 * Class that requests authentication and user information from the room database and
 * maintains an in-memory cache of login status and user credentials information.
 * This is an interim class that can store login data as an object but that functionality is
 * not being used for this phase.
 */
public class LoginRepository {

    private static volatile LoginRepository instance;


    private LoginDataSource dataSource;
    private LoggedInUser user = null;

    // private constructor : singleton access
    private LoginRepository(LoginDataSource dataSource) {


        this.dataSource = dataSource;
    }

    // set instance of login repository
    public static LoginRepository getInstance(LoginDataSource dataSource) {
        if (instance == null) {
            instance = new LoginRepository(dataSource);
        }
        return instance;
    }
    // handle login through datasource object
    public int login(String username, String password) {


        int result = dataSource.run(username, password);
        if(Integer.toString(result) != null){
            return result;
        }

        return -1;

    }

    /*
    // functions saved as placeholders in case logged in user should be stored in cache
    public boolean isLoggedIn() {
            return user != null;
    }

    private void setLoggedInUser(LoggedInUser user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
    */


}
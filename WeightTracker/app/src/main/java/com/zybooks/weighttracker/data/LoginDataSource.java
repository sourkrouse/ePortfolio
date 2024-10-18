package com.zybooks.weighttracker.data;

import com.zybooks.weighttracker.ui.login.RunOnThread;

/**
 * Last updated 10/17/2024, by Laura Brooks
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {


    public int run(String username, String password) {

        RunOnThread future =  new RunOnThread(username, password) ;
        int userID = future.getUser();
        return userID;



    }





    public void logout() {
        // TODO: revoke authentication


    }


}
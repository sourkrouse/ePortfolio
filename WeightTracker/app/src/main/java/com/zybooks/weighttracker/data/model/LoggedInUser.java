package com.zybooks.weighttracker.data.model;

import com.zybooks.weighttracker.AuthenticateUser;
import com.zybooks.weighttracker.data.DAO.RegisterDao;
import com.zybooks.weighttracker.data.InitDb;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * * PLACEHOLDER
 * Data class that captures user information for logged in users retrieved from LoginRepository
 * This class would be used if the application plans to store the logged in user info in cache
 */
public class LoggedInUser {

    private String userId;
    private String displayName;


    public LoggedInUser(String userId, String displayName) {
        this.userId = userId;
        this.displayName = displayName;


    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }
}
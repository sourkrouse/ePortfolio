package com.zybooks.weighttracker.data;

import android.app.Application;

/**
Last Updated 10/6/2024, Laura Brooks
This file runs the database called by the user.


 */

public class InitDb extends Application {
    public static AppDatabase appDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        appDatabase = AppDatabase.getDatabase(this);
    }

}
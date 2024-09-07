package com.zybooks.weighttracker;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

// TODO add Weights.class between brackets
@Database(entities = {Register.class, Weights.class}, version = 1)
public abstract class WeightAndSeeDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "register.db";

    private static WeightAndSeeDatabase mWASDatabase;

    // Singleton
    public static WeightAndSeeDatabase getInstance(Context context) {
        if (mWASDatabase == null) {
            mWASDatabase = Room.databaseBuilder(context, WeightAndSeeDatabase.class,
                    DATABASE_NAME).allowMainThreadQueries().build();
        }
        return mWASDatabase;
    }

    public abstract RegisterDao registerDao();
    public abstract WeightsDao weightDao();
}


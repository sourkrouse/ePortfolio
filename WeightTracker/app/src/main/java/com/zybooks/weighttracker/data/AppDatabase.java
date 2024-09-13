package com.zybooks.weighttracker.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.zybooks.weighttracker.data.DAO.UserDao;
import com.zybooks.weighttracker.data.DAO.RegisterDao;
import com.zybooks.weighttracker.data.model.Register;

@Database(entities = {Register.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract RegisterDao registerDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, DbConfig.ROOM_DB_NAME)
                            //.addMigrations(AppDatabase.MIGRATION_1_2)  // Add your migration(s)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /*
    // For database migration
    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Define SQL statements for the migration
            // For example: database.execSQL("ALTER TABLE your_table ADD COLUMN new_column_name TEXT");
        }
    };
     */

}

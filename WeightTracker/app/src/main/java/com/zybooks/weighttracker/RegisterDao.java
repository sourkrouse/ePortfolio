package com.zybooks.weighttracker;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;


@Dao
public interface RegisterDao {

    @Query("SELECT * FROM Registration WHERE id = :id")
    public Register getProfile(long id);

    @Query("SELECT * FROM Registration WHERE username = :uname")
    public List<Register> getProfiles(String uname);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long insertProfile(Register register);

    @Update
    public void updateProfile(Register register);

    @Delete
    public void deleteProfile(Register register);
}

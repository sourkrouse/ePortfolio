package com.zybooks.weighttracker.data.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.zybooks.weighttracker.data.DbConfig;
import com.zybooks.weighttracker.data.model.Register;

import java.util.List;


@Dao
public interface RegisterDao {

    @Query("SELECT * FROM "+ DbConfig.REGISTER_TABLE+ " WHERE id = :id")
    public Register getProfile(int id);

    @Query("SELECT * FROM "+ DbConfig.REGISTER_TABLE+ " WHERE username = :uname")
    public Register getProfileByUsername(String uname);

    @Query("SELECT * FROM "+ DbConfig.REGISTER_TABLE+ " WHERE username = :uname")
    public List<Register> getProfiles(String uname);

    @Query("SELECT * FROM "+ DbConfig.REGISTER_TABLE+ " WHERE username = :uname AND password = :pwd")
    public Register getUser(String uname, String pwd);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long insertProfile(Register register);

    @Update
    public void updateProfile(Register register);

    @Delete
    public void deleteProfile(Register register);
}

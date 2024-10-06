package com.zybooks.weighttracker.data.DAO;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.zybooks.weighttracker.data.DbConfig;
import com.zybooks.weighttracker.data.model.Register;

import java.util.List;

/*
PLACEHOLDER - this file will be deleted


 */
@Dao
public interface UserDao {

    @Insert
    void insert(Register user);

    @Update
    void update(Register user);

    @Delete
    void delete(Register user);

    @Query("SELECT * FROM "+ DbConfig.REGISTER_TABLE+ " WHERE id = :userId")
    Register getUserByLoginId(String userId);

    @Query("SELECT * FROM " + DbConfig.REGISTER_TABLE)
    List<Register> getAllUsers();
}






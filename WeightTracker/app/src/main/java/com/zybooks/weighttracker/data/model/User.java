package com.zybooks.weighttracker.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.zybooks.weighttracker.data.DbConfig;
/*
PLACEHOLDER - will be deleted later
 */
@Entity(tableName = DbConfig.USER_TABLE)
public class User {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int mId;

    // UPDATE TIME - AUTO CAPTURED
    @ColumnInfo(name = "updated")
    private long mUpdateTime;

    @ColumnInfo(name = "login_id")
    public String loginId;

    @ColumnInfo(name = "password")
    public String password;

    @ColumnInfo(name = "full_name")
    public String fullName;

    @ColumnInfo(name = "contact")
    public String contact;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}

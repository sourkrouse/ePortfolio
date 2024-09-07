package com.zybooks.weighttracker;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// TODO - may need to add a phone number column later
@Entity(tableName = "Registration")
public class Register {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long mId;

    public void setId(long id) {
        mId = id;
    }

    public long getId() {
        return mId;
    }

    // UPDATE TIME - AUTO CAPTURED
    @ColumnInfo(name = "updated")
    private long mUpdateTime;

    public Register() {
        mUpdateTime = System.currentTimeMillis();
    }

    public long getUpdateTime() {
        return mUpdateTime;
    }

    public void setUpdateTime(long updateTime) {
        mUpdateTime = updateTime;
    }

    // FIRST NAME FIELD
    @ColumnInfo(name = "firstname")
    public String mFirst;

    public void setFirst(String firstname) {
        mFirst = firstname;
    }

    public String getFirst() {
        return mFirst;
    }

    // LAST NAME FIELD
    @ColumnInfo(name = "lastname")
    public String mLast;

    public void setLast(String lastname) {
        mLast = lastname;
    }

    public String getLast() {
        return mLast;
    }

    // EMAIL FIELD
    @ColumnInfo(name = "email")
    public String mEmail;

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getEmail() {
        return mEmail;
    }

    // USERNAME FIELD
    @ColumnInfo(name = "username")
    public String mUsername;

    public void setUser(String username) {
        mUsername = username;
    }

    public String getUser() {
        return mUsername;
    }

    // PASSWORD FIELD
    @ColumnInfo(name = "password")
    private String mPassword;

    public void setPassword(String password) {
        mPassword = password;
    }

    public String getPassword() {
        return mPassword;
    }





}

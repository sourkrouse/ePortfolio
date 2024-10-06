package com.zybooks.weighttracker.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.zybooks.weighttracker.data.DbConfig;
/*
Last Updated 10/6/2024, Laura Brooks
This file is a data model for the registration_table.
Data Fields include:
id = primary and auto-generated,unique id representing the user ID
updated = date auto generated as last updated date
firstname = string, accepts entry from the user
lastname = string, accepts entry from the user
email = string, accepts entry from the user
username = string, accepts entry from the user
password = string, accepts entry from the user (adding hashing)


 */
// TODO - may need to add a phone number column later
@Entity(tableName = DbConfig.REGISTER_TABLE)
public class Register {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int mId;

    public void setId(int id) {
        this.mId = id;
    }

    public int getId() {
        return mId;
    }

    // UPDATE TIME - AUTO CAPTURED
    @ColumnInfo(name = "updated")
    private long mUpdateTime;

    public Register() {
        this.mUpdateTime = System.currentTimeMillis();
    }

    public long getUpdateTime() {
        return mUpdateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.mUpdateTime = updateTime;
    }

    // FIRST NAME FIELD
    @ColumnInfo(name = "firstname")
    public String mFirst;

    public void setFirst(String firstname) {
        this.mFirst = firstname;
    }

    public String getFirst() {
        return mFirst;
    }

    // LAST NAME FIELD
    @ColumnInfo(name = "lastname")
    public String mLast;

    public void setLast(String lastname) {
        this.mLast = lastname;
    }

    public String getLast() {
        return mLast;
    }

    // EMAIL FIELD
    @ColumnInfo(name = "email")
    public String mEmail;

    public void setEmail(String email) {
        this.mEmail = email;
    }

    public String getEmail() {
        return mEmail;
    }

    // USERNAME FIELD
    @ColumnInfo(name = "username")
    public String mUsername;

    public void setUser(String username) {
        this.mUsername = username;
    }

    public String getUser() {
        return mUsername;
    }

    // PASSWORD FIELD
    @ColumnInfo(name = "password")
    private String mPassword;

    public void setPassword(String password) {
        this.mPassword = password;
    }

    public String getPassword() {
        return mPassword;
    }





}

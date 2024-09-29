package com.zybooks.weighttracker.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.zybooks.weighttracker.data.DbConfig;

import java.util.Date;

@Entity(tableName = DbConfig.WEIGHTS_TABLE,
        foreignKeys = @ForeignKey(entity = Register.class,
                parentColumns = "id",
                childColumns = "Rid"))
@TypeConverters(Weights.DateConverter.class)
public class Weights {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Wid")
    private int mWId;

    public void setWId(int Wid) {
        mWId = Wid;
    }

    public int getWId() {
        return mWId;
    }

    // Foreign key connected to Register ID of the user
    @ColumnInfo(name = "Rid")
    private int mRId;

    public void setRId(int Rid) {
        mRId = Rid;
    }

    public int getRId() {
        return mRId;
    }

    // UPDATE TIME - AUTO CAPTURED
    @ColumnInfo(name = "updated")
    private long mUpdateTime;

    public Weights() {
        mUpdateTime = System.currentTimeMillis();
    }

    public long getUpdateTime() {
        return mUpdateTime;
    }

    public void setUpdateTime(long updateTime) {
        mUpdateTime = updateTime;
    }

    // DATE FIELD
    @ColumnInfo(name = "record_date")
    public Date mRecordDate;

    public void setRecordDate(Date record_date) {
        mRecordDate = record_date;
    }

    public Date getRecordDate() {
        return mRecordDate;
    }

    // WEIGHT FIELD
    @ColumnInfo(name = "weight")
    public Float mWeight;

    public void setWeight(Float weight) {
        mWeight = weight;
    }

    public Float getWeight() {
        return mWeight;
    }

    public static class DateConverter {

        @TypeConverter
        public static Date toDate(Long dateLong){
            return dateLong == null ? null: new Date(dateLong);
        }

        @TypeConverter
        public static Long fromDate(Date date){
            return date == null ? null : date.getTime();
        }
    }

}

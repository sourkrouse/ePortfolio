package com.zybooks.weighttracker;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity(tableName = "DailyWeights",
        foreignKeys = @ForeignKey(entity = Register.class,
                parentColumns = "id",
                childColumns = "Rid"))
@TypeConverters(Weights.DateConverter.class)
public class Weights {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Wid")
    private long mWId;

    public void setWId(long Wid) {
        mWId = Wid;
    }

    public long getWId() {
        return mWId;
    }
    @ColumnInfo(name = "Rid")
    private long mRId;

    public void setRId(long Rid) {
        mRId = Rid;
    }

    public long getRId() {
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

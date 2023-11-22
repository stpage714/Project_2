package com.example.project_2;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.project_2.DB.AppDataBase;

import java.util.Date;


@Entity(tableName = AppDataBase.GEMLOG_TABLE)
public class GemLog {

    @PrimaryKey(autoGenerate = true)
    //declare primary key for table
    private int mLogId;//mLogId is primary key

    private int mReps;
    private String mExercise;
    private Double mWeight;
    private Date mDate;
    public GemLog(String exercise, Double weight, int reps) {
        mExercise = exercise;
        mWeight = weight;
        mReps = reps;
        mDate = new Date();
    }

    public int getLogId() {
        return mLogId;
    }

    public void setLogId(int logId) {
        mLogId = logId;
    }

    public String getExercise() {
        return mExercise;
    }

    public void setExercise(String exercise) {
        mExercise = exercise;
    }

    public Double getWeight() {
        return mWeight;
    }

    public void setWeight(Double weight) {
        mWeight = weight;
    }

    public int getReps() {
        return mReps;
    }

    public void setReps(int reps) {
        mReps = reps;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    @Override
    public String toString() {
        return "Log # " + mLogId + "\n" +
                "Exercise: " + mExercise + "\n" +
                "Weight: " + mWeight + "\n" +
                "Reps: " + mReps + "\n" +
                "Date: " + mDate + "\n" +
                "=-=-=-=-=-=-=-=-=-=-\n";
    }
}

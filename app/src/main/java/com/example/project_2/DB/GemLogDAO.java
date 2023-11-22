package com.example.project_2.DB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.project_2.GemLog;

import java.util.List;

@Dao //must annotate with Dao here
public interface GemLogDAO {
    //these are were queries are made; entry point for database
    //created whenever database objects are manipulated
    @Insert //built in crud query
    void insert(GemLog... GemLogs);

    @Update //built in crud query
    void update(GemLog... GemLogs);

    @Delete // built in crud query
    void delete(GemLog GemLogs);
    //only delete single log

    //custom queries below you have to write
    @Query("SELECT * FROM " + AppDataBase.GEMLOG_TABLE + " ORDER BY mDate desc" )
        //custom = @Query
    List<GemLog> getGemLogs();// query returns this

    @Query("SELECT * FROM " + AppDataBase.GEMLOG_TABLE + " WHERE mLogId = :logId")
    List<GemLog> getGemLogsById(int logId);//only return records where id matches logId


}

package com.example.project_2.DB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.project_2.GemLog;

@Database(entities = {GemLog.class}, version = 1)
@TypeConverters({DateTypeConverter.class})
public abstract class AppDataBase extends RoomDatabase {
    //database access point through abstraction layer
    public static final String DATABASE_NAME = "GEMLOG_DATABASE";
    public static final String GEMLOG_TABLE = "GEMLOG_TABLE";
    public static final String USER_TABLE = "USER_TABLE";

    private static volatile AppDataBase instance;
    //volatile means you can only access from main memory; keeps memory intact
    //no race conditions and persistent
    private static final Object LOCK = new Object();
    //make sure nothing else can access database at same time

    public abstract GemLogDAO GemLogDAO();

    public static AppDataBase getInstance(Context context){
        //singleton
        if(instance == null){
            synchronized (LOCK){
                if(instance == null){
                    //lock and create
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                                AppDataBase.class,DATABASE_NAME).build();
                }
            }
        }
        return instance;
    }
}

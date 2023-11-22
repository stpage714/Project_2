package com.example.project_2.DB;


import androidx.room.TypeConverter;

import java.util.Date;

public class DateTypeConverter {
    @TypeConverter
    public long convertDateToLong(Date date){
        return date.getTime();//epoch time
    }
    @TypeConverter
    public Date convertLongToDate(long epoch){
        return new Date(epoch);
    }

}

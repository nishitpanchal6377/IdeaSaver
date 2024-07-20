package com.example.ideasaver;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;


@Database(entities = IdeaDetails.class,exportSchema = false,version = 7)

@TypeConverters({Converters.class})
public abstract class DatabaseHelper extends RoomDatabase {

    private static final String DB_NAME ="ideaDetailsdb";
    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getDB(Context context){

        if(instance==null){
            instance= Room.databaseBuilder(context,DatabaseHelper.class,DB_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract IdeaDetailsDAO ideaDetailsDAO();



}

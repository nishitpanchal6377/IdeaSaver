package com.example.ideasaver;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface IdeaDetailsDAO {


    @Query("select * from ideadetails")
    List<IdeaDetails> getAllIdeas();

    @Insert
    void addIdea(IdeaDetails ideaDetails);

    @Delete
    void deleteIdea(IdeaDetails ideaDetails);


    @Update
    void updateIdea(IdeaDetails ideaDetails);

}

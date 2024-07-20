package com.example.ideasaver;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "ideadetails")
    public class IdeaDetails {

        @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "Ideaname")
    private String Ideaname;
    @ColumnInfo(name = "Ideadescription")
    private String Ideadescription;

    @ColumnInfo(name = "SavedDate")
    private String SavedDate;

    @ColumnInfo(name = "IsCompleted")
    private int IsCompleted;


    @ColumnInfo(name = "AlarmId")
    private int AlarmId;

    @ColumnInfo(name = "AlarmIsEnabled")
    private int AlarmIsEnabled;

    @ColumnInfo(name = "Substeps")
    private ArrayList<String> Substeps;



    public ArrayList<String> getSubsteps() {
        return Substeps;
    }

    public void setSubsteps(ArrayList<String> substeps) {
        Substeps = substeps;
    }

    public IdeaDetails() {
        }

        public IdeaDetails(int Id,String ideaname, String ideadescription,String savedDate,int Iscompleted,int alarmId,int alarmIsEnabled,ArrayList<String> Substps) {
            id=Id;
            Ideaname = ideaname;
            Ideadescription = ideadescription;
            Substeps=Substps;
            SavedDate=savedDate;
            IsCompleted=Iscompleted;
            AlarmId=alarmId;
            AlarmIsEnabled=alarmIsEnabled;
        }

        @Ignore
        public IdeaDetails(String Ideanm,String Ideadesc,String savedDate,int Iscompleted,int alarmId,int alarmIsEnabled,ArrayList<String> Substps) {
            Ideaname=Ideanm;
            Ideadescription=Ideadesc;
            Substeps=Substps;
            SavedDate=savedDate;
            IsCompleted=Iscompleted;
            AlarmId=alarmId;
            AlarmIsEnabled=alarmIsEnabled;
        }

    public String getSavedDate() {
        return SavedDate;
    }

    public void setSavedDate(String savedDate) {
        SavedDate = savedDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdeaname() {
        return Ideaname;
    }

    public void setIdeaname(String ideaname) {
        Ideaname = ideaname;
    }

    public String getIdeadescription() {
        return Ideadescription;
    }

    public void setIdeadescription(String ideadescription) {

        Ideadescription = ideadescription;
    }

    public int getIsCompleted() {
        return IsCompleted;
    }

    public void setIsCompleted(int isCompleted) {
        IsCompleted = isCompleted;
    }

    public int getAlarmId() {
        return AlarmId;
    }

    public void setAlarmId(int alarmId) {
        AlarmId = alarmId;
    }

    public int getAlarmIsEnabled() {
        return AlarmIsEnabled;
    }

    public void setAlarmIsEnabled(int alarmIsEnabled) {
        AlarmIsEnabled = alarmIsEnabled;
    }

    //    IdeaDetails(int ID,String Ideanm,String Ideadesc){
//        id=ID;
//        Ideaname=Ideanm;
//        Ideadescription=Ideadesc;
//    }

//    IdeaDetails(String Ideanm,String Ideadesc){
//        Ideaname=Ideanm;
//        Ideadescription=Ideadesc;
//    }
}

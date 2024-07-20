package com.example.ideasaver;

import java.util.ArrayList;

public class Customdata {
    String Ideaname;
    String Ideadescription;
    ArrayList<String> Ideasubsteps =new ArrayList<>();


    public Customdata(String IdeaName,String IdeaDescription, ArrayList<String> IdeaSubsteps) {
        Ideaname =IdeaName ;
        Ideadescription=IdeaDescription;
        Ideasubsteps=IdeaSubsteps;
    }

    public String getIdeaname() {
        return Ideaname;
    }

    public String getIdeadescription() {
        return Ideadescription;
    }

    public ArrayList<String> getIdeasubsteps() {
        return Ideasubsteps;
    }

    public void setIdeaname(String ideaname) {
        Ideaname = ideaname;
    }

    public void setIdeadescription(String ideadescription) {
        Ideadescription = ideadescription;
    }

    public void setIdeasubsteps(ArrayList<String> ideasubsteps) {
        Ideasubsteps = ideasubsteps;
    }
}

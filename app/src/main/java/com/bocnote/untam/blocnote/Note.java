package com.bocnote.untam.blocnote;

/**
 * Created by untam on 22/03/2018.
 */

public class Note {
    private int id;
    private String titre;
    private String note;

    public Note (){}
    public Note (int id, String titre, String note ){
        this.id=id;
        this.titre=titre;
        this.note=note;
    }

    public void setId(int id){
        this.id=id;
    }
    public int getId(){
        return this.id;
    }
    public void setTitre(String titre) {
        this.titre = titre;
    }
    public String getTitre() {
        return titre;
    }
    public void setNote(String note) {
        this.note = note;
    }
    public String getNote() {
        return note;
    }

    @Override
    public String toString() {
        return titre;
    }
}

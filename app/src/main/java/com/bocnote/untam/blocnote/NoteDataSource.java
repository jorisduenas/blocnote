package com.bocnote.untam.blocnote;

/**
 * Created by untam on 22/03/2018.
 */

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class NoteDataSource {
    private SQLiteDatabase database;
    private NoteMySQLiteHelper dbHelper;
    private String[] allColumns = {
            NoteMySQLiteHelper.COLUMN_ID,
            NoteMySQLiteHelper.COLUMN_TITRE,
            NoteMySQLiteHelper.COLUMN_NOTE
    };


    public NoteDataSource(Context context) {
        dbHelper = new NoteMySQLiteHelper(context);
    }
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }
    public void close() {
        dbHelper.close();
    }

    public Note createNote(String titre, String note) throws NoteExisteException {
        Note newNote = new Note();
        if(noteExist(titre)){
            throw new NoteExisteException("Le titre de votre note est déjà utilisé ! neheh");
        }
        else{
            ContentValues values = new ContentValues();
            values.put(NoteMySQLiteHelper.COLUMN_TITRE, titre);
            values.put(NoteMySQLiteHelper.COLUMN_NOTE, note);
            long insertId = database.insert(NoteMySQLiteHelper.TABLE_NOTE, null,
                    values);
            Cursor cursor = database.query(NoteMySQLiteHelper.TABLE_NOTE,
                    allColumns, NoteMySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                    null, null, null);
            cursor.moveToFirst();
            newNote = cursorToNote(cursor);
            cursor.close();
        }

        return newNote;
    }
    private boolean noteExist(String titre){
        boolean res;
        Cursor cursor=database.rawQuery("SELECT * FROM notes WHERE titre = '"+titre+"'", null);
        if (cursor.getCount()>0){
            res = true;
        }
        else{
            res = false;
        }
        cursor.close();
        return res;
    }

    public Note selectNote(String titre){
        Note note;
        Cursor cursor=database.rawQuery("SELECT * FROM notes WHERE titre = '"+titre+"'", null);
        cursor.moveToFirst();
        note = cursorToNote(cursor);
        cursor.close();
        return note;
    }

    public void deleteNote(Note note) {
        long id = note.getId();
        System.out.println("Note deleted with id: " + id);
        database.delete(NoteMySQLiteHelper.TABLE_NOTE, NoteMySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public void updateNote(int id, String titre, String note) throws  NoteExisteException{
        ContentValues contentValues = new ContentValues();
        contentValues.put(NoteMySQLiteHelper.COLUMN_TITRE, titre);
        contentValues.put(NoteMySQLiteHelper.COLUMN_NOTE, note);
        database.update(NoteMySQLiteHelper.TABLE_NOTE, contentValues, NoteMySQLiteHelper.COLUMN_ID+"="+id, null);
    }
    public List<Note> getAllNotes (){
        List<Note> notes = new ArrayList<Note>();

        Cursor cursor = database.query(NoteMySQLiteHelper.TABLE_NOTE,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Note note = cursorToNote(cursor);
            notes.add(note);
            cursor.moveToNext();
        }
        // assurez-vous de la fermeture du curseur
        cursor.close();

        return notes;
    }

    private Note cursorToNote(Cursor cursor) {
        Note note = new Note();
        note.setId(cursor.getInt(0));
        note.setTitre(cursor.getString(1));
        note.setNote(cursor.getString(2));
        return note;
    }

}

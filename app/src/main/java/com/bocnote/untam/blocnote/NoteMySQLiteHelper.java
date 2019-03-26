package com.bocnote.untam.blocnote;

/**
 * Created by untam on 22/03/2018.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class NoteMySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_NOTE = "notes";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITRE = "titre";
    public static final String COLUMN_NOTE = "note";

    private static final String DATABASE_NAME = "note.db";
    private static final int DATABASE_VERSION = 1;

    // Commande sql pour la création de la base de données
    private static final String DATABASE_CREATE = "create table "
            + TABLE_NOTE + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_TITRE
            + " text not null, " + COLUMN_NOTE
            + " text not null);";

    public NoteMySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(NoteMySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTE);
        onCreate(db);
    }
}
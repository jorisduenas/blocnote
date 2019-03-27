package com.bocnote.untam.blocnote;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.widget.EditText;
import android.view.Menu;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private FloatingActionButton btn_nouveau;

    private EditText sai_titre;
    private EditText sai_note;

    private Note selected_note=null;

    private NoteDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_nouveau = (FloatingActionButton) findViewById(R.id.btnNouveau);

        sai_note = (EditText) findViewById(R.id.saiNote);
        sai_titre = (EditText) findViewById(R.id.saiTitre);

        btn_nouveau.setOnClickListener(this);

        dataSource = new NoteDataSource(this);
        dataSource.open();

        Intent intent = getIntent();

        if (intent != null) {
            if (intent.getStringExtra("titre")!=null){
                selected_note=new Note(intent.getIntExtra("id",1),intent.getStringExtra("titre"),intent.getStringExtra("note") );
                sai_titre.setText(selected_note.getTitre());
                sai_note.setText(selected_note.getNote());
            }
        }
        else{
            selected_note=null;
        }
    }
    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog=null;
        switch (v.getId()){
            case R.id.btnNouveau:
                builder.setMessage("Les modifications seront perdues !")
                        .setTitle("Attention");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                dialog = builder.create();
                dialog.show();
                break;
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog=null;

        switch (item.getItemId()) {
            case R.id.action_open:
                Intent intent = new Intent(MainActivity.this, ListeNotesActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_save:
                String message;
                String current_titre= sai_titre.getText().toString();
                String current_note= sai_note.getText().toString();
                if(current_titre.length()>0 || current_note.length()>0){
                    try{
                        if(selected_note!=null){
                            dataSource.updateNote(selected_note.getId(),current_titre,current_note);
                            message="Note Modifiée";
                        }
                        else{
                            dataSource.createNote(current_titre,current_note);
                            message="Note enregistrée";

                        }
                        Intent i = new Intent(MainActivity.this, ListeNotesActivity.class);
                        i.putExtra("message", message);
                        startActivity(i);
                    }
                    catch (NoteExisteException e){
                        builder.setMessage(e.getMessage())
                                .setTitle("Attention");
                        builder.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                        dialog = builder.create();
                        dialog.show();
                    }
                }
                return true;

            case R.id.action_suppr:
                builder.setMessage("Voulez-vous vraiment supprimer la note '"+selected_note.getTitre()+"' ?")
                        .setTitle("Attention");
                builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dataSource.deleteNote(selected_note);
                        Intent i = new Intent(MainActivity.this, ListeNotesActivity.class);
                        i.putExtra("message", "Note Supprimée");
                        startActivity(i);
                    }
                });
                builder.setNegativeButton("non", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                dialog = builder.create();
                dialog.show();
                return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onPause() {
        dataSource.close();
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        if(selected_note==null) {
            menu.removeItem(R.id.action_suppr);
        }
        return true;
    }
}


class NoteExisteException extends Exception{

    public NoteExisteException(String message){
        super(message);
    }

}

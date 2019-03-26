package com.bocnote.untam.blocnote;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import android.app.ListActivity;

import org.w3c.dom.Text;

public class ListeNotesActivity extends ListActivity implements OnClickListener {

    private Button btn_nouveau;
    private Button btn_ouvrir;
    private Button btn_enregistrer;
    private Button btn_supprimer;
    private Button btn_reset;

    private LinearLayout div_select_note;
    private LinearLayout div_saisie_note;

    private ListView listView;

    private EditText sai_titre;
    private EditText sai_note;
    private TextView txt_message;

    private Note selected_note=null;
    private List<Note> les_notes;

    private NoteDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_nouveau = (Button) findViewById(R.id.btnNouveau);
        listView = (ListView) findViewById(android.R.id.list);
        btn_nouveau.setOnClickListener(this);


        dataSource = new NoteDataSource(this);
        dataSource.open();

        les_notes= dataSource.getAllNotes();

        ArrayAdapter<Note> adapter = new ArrayAdapter<Note>(ListeNotesActivity.this,
                android.R.layout.simple_list_item_1, les_notes);

        setListAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selected_note=dataSource.selectNote(listView.getItemAtPosition(i).toString());
                changeView("saisie");
                sai_titre.setText(selected_note.getTitre());
                sai_note.setText(selected_note.getNote());
            }
        });
    }
    @Override
    public void onClick(View v) {
        ArrayAdapter<Note> adapter = (ArrayAdapter<Note>) getListAdapter();
        switch (v.getId()){
            case R.id.btnNouveau:
                changeView("saisie");
                break;
        }
    }

    public void reloadList(){
        listView.removeAllViews();
    }
    @Override
    protected void onResume() {
        dataSource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        dataSource.close();
        super.onPause();
    }

    public void changeView(String new_view){
        txt_message.setText("");
        switch (new_view){
            case "ouvrir":
                div_saisie_note.setVisibility(View.GONE);
                btn_ouvrir.setVisibility(View.GONE);
                btn_nouveau.setVisibility(View.VISIBLE);
                div_select_note.setVisibility(View.VISIBLE);
                selected_note=null;
                break;
            case "saisie":
                btn_nouveau.setVisibility(View.GONE);
                div_select_note.setVisibility(View.GONE);
                div_saisie_note.setVisibility(View.VISIBLE);
                btn_ouvrir.setVisibility(View.VISIBLE);
                sai_titre.setText("");
                sai_note.setText("");
                if(selected_note==null){
                    btn_supprimer.setVisibility(View.GONE);
                }
                else{
                    btn_supprimer.setVisibility(View.VISIBLE);
                }
                break;
        }
    }
}

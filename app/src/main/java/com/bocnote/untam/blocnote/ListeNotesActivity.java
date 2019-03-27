package com.bocnote.untam.blocnote;

import android.os.Bundle;
import android.content.Intent;

import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.support.v7.app.AppCompatActivity;
import android.widget.AdapterView;
import android.support.design.widget.FloatingActionButton;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import java.util.List;
import android.support.design.widget.Snackbar;

public class ListeNotesActivity extends AppCompatActivity implements OnClickListener {


    private FloatingActionButton btn_nouveau;
    private ListView listView;
    private Note selected_note=null;
    private List<Note> les_notes;
    private NoteDataSource dataSource;
    private ArrayAdapter<Note> adapter =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listes_notes_activity);

        btn_nouveau = (FloatingActionButton) findViewById(R.id.btnNouveau);
        listView = (ListView) findViewById(android.R.id.list);
        btn_nouveau.setOnClickListener(this);


        dataSource = new NoteDataSource(this);
        dataSource.open();

        loadList();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selected_note=dataSource.selectNote(listView.getItemAtPosition(i).toString());
                Intent intent = new Intent(ListeNotesActivity.this, MainActivity.class);
                intent.putExtra("id",selected_note.getId());
                intent.putExtra("titre",selected_note.getTitre());
                intent.putExtra("note",selected_note.getNote());
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                selected_note=dataSource.selectNote(listView.getItemAtPosition(i).toString());
                return false;
            }
        });
        Intent intent = getIntent();
        if (intent.getStringExtra("message")!=null){
            Snackbar.make(findViewById(R.id.body), intent.getStringExtra("message"), Snackbar.LENGTH_LONG).show();
        }
        registerForContextMenu(listView);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnNouveau:
                Intent intent = new Intent(ListeNotesActivity.this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void loadList(){
        les_notes=dataSource.getAllNotes();
        adapter = new ArrayAdapter<Note>(this,
                android.R.layout.simple_list_item_1, les_notes);

        listView.setAdapter(adapter);
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        menu.removeItem(R.id.action_open);
        menu.removeItem(R.id.action_save);
        menu.removeItem(R.id.action_suppr);
        return true;
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_item_list, menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_suppr:
                dataSource.deleteNote(selected_note);
                Snackbar.make(findViewById(R.id.body), "Note supprimée !", Snackbar.LENGTH_LONG).show();
                loadList();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}

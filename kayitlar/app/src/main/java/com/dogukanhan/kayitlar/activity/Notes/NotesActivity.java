package com.dogukanhan.kayitlar.activity.Notes;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.dogukanhan.kayitlar.R;

import java.util.ArrayList;
import java.util.List;

import com.dogukanhan.kayitlar.activity.Product.ProductActivity;
import com.dogukanhan.kayitlar.activity.Product.ProductEditActivity;
import com.dogukanhan.kayitlar.config.DatabaseHelper;
import com.dogukanhan.kayitlar.model.Note;
import com.dogukanhan.kayitlar.repository.NoteRepository;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import java.sql.SQLException;


public class NotesActivity extends OrmLiteBaseActivity<DatabaseHelper> {

    private List<Note> notes = new ArrayList<>();
    private List<Object>notesTitle=new ArrayList<>();
    private ArrayAdapter<Object> adapter;

    private ListView listView;
    private NoteRepository repository;
    private ImageButton imagebuttonAdd;
    private ImageButton imagebuttonEdit;
    private ImageButton imagebuttonDelete;
    private ImageButton imagebuttonBack;

    private Note selected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        imagebuttonAdd=findViewById(R.id.imageButtonAdd);
        imagebuttonEdit=findViewById(R.id.imageButtonEdit);
        imagebuttonDelete=findViewById(R.id.imageButtonDelete);
        imagebuttonBack=findViewById(R.id.imageButtonBack);
        listView=findViewById(R.id.noteView);

        imagebuttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotesActivity.this,
                        NotesAddEditActivity.class);

                NotesActivity.this.startActivity(intent);

            }
        });

        imagebuttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(NotesActivity.this,
                        NotesAddEditActivity.class);
                if (selected != null) {
                    intent.putExtra("notesId", selected.getId());
                    NotesActivity.this.startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Lütfen Not Seçiniz", Toast.LENGTH_LONG).show();
                }


            }
        });

        imagebuttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (selected != null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(NotesActivity.this);
                    builder.setTitle("notu Sil");
                    builder.setMessage(selected.getTitle() + " Başlıklı notu Silmek İstediğinize Emin Misiniz?");
                    builder.setNegativeButton("Hayır", null);
                    builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                                try {
                                    repository.remove(selected);

                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                adapter.remove(selected.getTitle());
                                onResume();

                                Toast.makeText(getApplicationContext(), selected.getTitle() + " silindi.", Toast.LENGTH_LONG).show();

                        }
                    });
                    builder.show();
                } else {
                    Toast.makeText(getApplicationContext(), "Lütfen silmek istediğiniz notu seçiniz", Toast.LENGTH_LONG).show();
                }



            }
        });

        imagebuttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotesActivity.this.finish();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (notes != null) {
                    selected = notes.get(i);
                }

            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();

        notesTitle.clear();

        try {
            repository = getHelper().getNoteRepository();
            notes = repository.findAll();
            for (Note note :notes){
                notesTitle.add(note.getTitle());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notesTitle);
        listView.setAdapter(adapter);

    }


    }

package com.dogukanhan.kayitlar.activity.Notes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.dogukanhan.kayitlar.R;
import com.dogukanhan.kayitlar.activity.Product.ProductEditActivity;
import com.dogukanhan.kayitlar.config.DatabaseHelper;
import com.dogukanhan.kayitlar.model.Note;
import com.dogukanhan.kayitlar.model.Product;
import com.dogukanhan.kayitlar.repository.NoteRepository;
import com.dogukanhan.kayitlar.repository.ProductRepository;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

import java.sql.SQLException;

public class NotesAddEditActivity extends OrmLiteBaseActivity<DatabaseHelper> {

    EditText noteTitle;
    EditText noteContent;
    ImageButton ımageButtonBack;
    ImageButton ımageButtonApply;

    private Long notesId;
    private NoteRepository repository;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_add_edit);

        noteTitle=findViewById(R.id.editText4);
        noteContent=findViewById(R.id.editText5);
        ımageButtonBack=findViewById(R.id.imageButton);
        ımageButtonApply=findViewById(R.id.imageButton3);

        try {
            repository = getHelper().getNoteRepository();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        if(getIntent().getExtras() != null && getIntent().getExtras().containsKey("notesId")){
            notesId =  getIntent().getExtras().getLong("notesId");
            try {

                Note note = repository.findById(notesId);
                noteTitle.setText(note.getTitle());
                noteContent.setText(note.getContent());

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        ımageButtonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    if(notesId == null){
                        Note note = new Note();
                        note.setTitle(noteTitle.getText().toString());
                        note.setContent(noteContent.getText().toString());


                        if(!note.getTitle().isEmpty()){
                            repository.create(note);

                        }else{
                            Toast.makeText(getApplicationContext(),"Başlık kısmı boş kalamaz..",Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Note note = repository.findById(notesId);
                        note.setTitle(noteTitle.getText().toString());
                        note.setContent(noteContent.getText().toString());


                        if(!note.getTitle().isEmpty()){
                            repository.update(note);
                        }else{
                            Toast.makeText(getApplicationContext(),"Başlık kısmı boş kalamaz..",Toast.LENGTH_LONG).show();
                        }

                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                NotesAddEditActivity.this.finish();
                //Kategoriyi db ye  ekle...
                //eklenen kategori db de varsa toast mesajı yolla  bırdaha ekleme yanlışlıkla 2  kez eklemesin...

            }
        });

        ımageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotesAddEditActivity.this.finish();
            }
        });



    }
}

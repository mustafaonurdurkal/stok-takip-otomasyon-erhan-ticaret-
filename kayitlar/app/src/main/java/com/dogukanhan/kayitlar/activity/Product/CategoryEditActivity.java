package com.dogukanhan.kayitlar.activity.Product;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.dogukanhan.kayitlar.R;
import com.dogukanhan.kayitlar.config.DatabaseHelper;
import com.dogukanhan.kayitlar.model.Category;
import com.dogukanhan.kayitlar.repository.CategoryRepository;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

import java.sql.SQLException;
import java.util.Calendar;

public class CategoryEditActivity extends OrmLiteBaseActivity<DatabaseHelper> {

    private ImageButton imageButtonApply;
    private ImageButton imageButtonBack;
    private EditText editText;
    private Long catId;
    private CategoryRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_edit);

        editText = findViewById(R.id.editTextProductBarcod);

        try {
            repository = getHelper().getCategoryRepository();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        if(getIntent().getExtras() != null && getIntent().getExtras().containsKey("catId")){
            catId =  getIntent().getExtras().getLong("catId");
            try {
                Category category = repository.findById(catId);
                editText.setText(category.getName());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        imageButtonApply = findViewById(R.id.imageButtonApply);
        imageButtonBack = findViewById(R.id.imageButtonBack);

        imageButtonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean flag= false;
                try {
                    if(!editText.getText().toString().equals("")){
                        if(catId == null){
                            for (Category category : repository.findAll()){
                                if(editText.getText().toString().equalsIgnoreCase(category.getName())){
                                    flag=true;
                                    break;
                                }
                            }
                            if (!flag){
                                Category category = new Category();
                                category.setName(editText.getText().toString());
                                repository.create(category);
                                Toast.makeText(getApplicationContext(),"Kategori Eklendi...",Toast.LENGTH_LONG).show();

                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Ayn?? isimde kategori olu??turulamaz...",Toast.LENGTH_LONG).show();
                            }


                        }else{
                            Category old = repository.findById(catId);
                            old.setName(editText.getText().toString());
                            repository.update(old);
                        }
                        CategoryEditActivity.this.finish();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"L??tfen Kategori ??smi Yaz??n??z...",Toast.LENGTH_LONG).show();

                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }


                //Kategoriyi db ye  ekle...
                //eklenen kategori db de varsa toast mesaj?? yolla  b??rdaha ekleme yanl????l??kla 2  kez eklemesin...

            }
        });

        imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CategoryEditActivity.this.finish();

            }
        });

    }

}

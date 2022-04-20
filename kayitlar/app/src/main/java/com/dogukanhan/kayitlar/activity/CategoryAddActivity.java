package com.dogukanhan.kayitlar.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.dogukanhan.kayitlar.R;
import com.dogukanhan.kayitlar.config.DatabaseHelper;
import com.dogukanhan.kayitlar.model.Category;
import com.dogukanhan.kayitlar.repository.CategoryRepository;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

import java.sql.SQLException;
import java.util.List;

public class CategoryAddActivity extends OrmLiteBaseActivity<DatabaseHelper> {

    private CategoryRepository categoryRepository;

    private EditText editText;

    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_add);

        try {
            categoryRepository = getHelper().getCategoryRepository();
        } catch (SQLException e) {
            Log.e("test", "test", e);
        }

        editText = findViewById(R.id.editText);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new CategoryAddButtonClick());

        listView = findViewById(R.id.listView);

    }

    @Override
    public void onStart() {
        super.onStart();

        listCategories();
    }

    private void listCategories() {
        try {

            List<Category> categories = categoryRepository.findAll();
            Log.i("cateogories", categories.toString());

            ArrayAdapter<Category> arrayAdapter = new ArrayAdapter<>
                    (this, android.R.layout.simple_list_item_1, android.R.id.text1, categories);

            listView.setAdapter(arrayAdapter);

        } catch (SQLException e) {
            Log.e("test", "test", e);
        }
    }

    private class CategoryAddButtonClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            Category category = new Category();
            category.setName(editText.getText().toString());

            try {
                categoryRepository.create(category);
            } catch (SQLException e) {
                Log.e("test", "test", e);
            }

            listCategories();
        }

    }

}

package com.dogukanhan.kayitlar.activity.Product;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.dogukanhan.kayitlar.R;
import com.dogukanhan.kayitlar.activity.MainScreen;
import com.dogukanhan.kayitlar.activity.customer.CustomerShowActivity;
import com.dogukanhan.kayitlar.config.DatabaseHelper;
import com.dogukanhan.kayitlar.model.Category;
import com.dogukanhan.kayitlar.repository.CategoryRepository;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends OrmLiteBaseActivity<DatabaseHelper> {

    private ListView categoryLv;
    private List<String> categoryName = new ArrayList<>();
    private List<Category> categories = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private ImageButton imageButtonAdd;
    private ImageButton imageButtonEdit;
    private ImageButton imageButtonDelete;
    private ImageButton imageButtonBack;
    private ImageButton imageButtonApply;
    private Category selected;
    private CategoryRepository repository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_category);
        categoryLv = findViewById(R.id.categoriList);



        try {
            repository = getHelper().getCategoryRepository();

            boolean flag=false;
            for (Category category : repository.findAll()){
                if (category.getName().equalsIgnoreCase("Eski Ürünler")){
                    flag=true;
                    break;
                }
            }
            if (!flag){
                Category category=new Category();
                category.setName("Eski Ürünler");
                repository.create(category);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        addListenerOnButton();



        categoryLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                selected = categories.get(i);
                Toast.makeText(getApplicationContext(), categories.get(i).getName(), Toast.LENGTH_LONG).show();

            }
        });

    }


    private void filterCategories(String query) {

        List<String> filteredList = new ArrayList<>();

        for (String category : categoryName) {
            if (category != null && category.toLowerCase().startsWith(query.toLowerCase())) {
                filteredList.add(category);
            }
        }

        if (filteredList.size() == 0) {
            filteredList.add("Sonuç Bulunamadı");
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, filteredList);
        categoryLv.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_customer, menu);
        MenuItem menuItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint(getString(R.string.search));
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                filterCategories(s.trim().toLowerCase());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                if (categories.size() < 1000) {
                    if (s.isEmpty()) {
                        adapter = new ArrayAdapter<>(CategoryActivity.this, android.R.layout.simple_list_item_1, categoryName);
                        categoryLv.setAdapter(adapter);
                    } else {
                        filterCategories(s.trim().toLowerCase());

                    }
                } else if (s.isEmpty()) {
                    adapter = new ArrayAdapter<>(CategoryActivity.this, android.R.layout.simple_list_item_1, categoryName);
                    categoryLv.setAdapter(adapter);
                }

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    public void addListenerOnButton() {

        imageButtonAdd = findViewById(R.id.imageButtonAdd);
        imageButtonDelete = findViewById(R.id.imageButtonDelete);
        imageButtonBack = findViewById(R.id.imageButtonBack);
        imageButtonEdit = findViewById(R.id.imageButtonEdit);
        imageButtonApply = findViewById(R.id.imageButtonAccept);

        imageButtonAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {


                    Intent intent = new Intent(CategoryActivity.this,
                            CategoryEditActivity.class);
                    CategoryActivity.this.startActivity(intent);


            }

        });
        imageButtonDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (selected != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CategoryActivity.this);
                    builder.setTitle("Kategori Sil");
                    builder.setMessage(selected.getName() + " İsimli Kategoriyi Silmek İstediğinize Emin Misiniz?");
                    builder.setNegativeButton("Hayır", null);
                    builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (selected.getProducts().isEmpty()) {
                                //CategoryActivity i tekrar başlat(güncel hali ile)...
                                try {
                                    repository.remove(selected);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                adapter.remove(selected.getName());
                                Toast.makeText(getApplicationContext(), selected.getName() + " silindi.", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), selected.getName() + " ürünlerinin önce silinmesi gerekli.", Toast.LENGTH_LONG).show();

                            }
                            onResume();

                        }
                    });
                    builder.show();
                } else {
                    Toast.makeText(getApplicationContext(), "Lütfen Kategori Seçiniz", Toast.LENGTH_LONG).show();
                }


            }


        });
        imageButtonBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(CategoryActivity.this,
                        MainScreen.class);
                CategoryActivity.this.startActivity(intent);
            }

        });

        imageButtonEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(CategoryActivity.this,
                        CategoryEditActivity.class);
                if (selected != null) {
                    intent.putExtra("catId", selected.getId());
                    CategoryActivity.this.startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Lütfen Kategori Seçiniz", Toast.LENGTH_LONG).show();
                }

            }

        });
        imageButtonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               if (selected!=null){
                   Intent intent = new Intent(CategoryActivity.this,
                           ProductActivity.class);
                   intent.putExtra("catId", selected.getId());
                   CategoryActivity.this.startActivity(intent);
               }
               else{
                   Toast.makeText(getApplicationContext(),"Lütfen Kategori Seçiniz...",Toast.LENGTH_SHORT).show();
               }

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        selected=null;

        try {
            categories = repository.findAll();
            categoryName.clear();
            for (Category categories : categories) {
                if(!(categories.getName().equals("Eski Ürünler"))){
                }
                categoryName.add(categories.getName());
            }
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, categoryName);
            categoryLv.setAdapter(adapter);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}

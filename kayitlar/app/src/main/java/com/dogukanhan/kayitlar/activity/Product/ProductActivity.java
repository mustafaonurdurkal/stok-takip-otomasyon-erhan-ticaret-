package com.dogukanhan.kayitlar.activity.Product;

import androidx.appcompat.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dogukanhan.kayitlar.R;
import com.dogukanhan.kayitlar.config.DatabaseHelper;
import com.dogukanhan.kayitlar.model.Category;
import com.dogukanhan.kayitlar.model.Product;
import com.dogukanhan.kayitlar.repository.CategoryRepository;
import com.dogukanhan.kayitlar.repository.ProductRepository;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends OrmLiteBaseActivity<DatabaseHelper> {

    private ProductRepository productRepository;

    private CategoryRepository categoryRepository;

    private ListView listView;

    private TextView textView;

    private ImageButton imageButtonAdd;

    private ImageButton imageButtonEdit;

    private ImageButton imageButtonDelete;

    private ImageButton imageButtonBack;

    private Category category;

    private  Category deletedCategory;

    private Product selected;

    private  Long catId;

    private ArrayAdapter<Object> adapter;

    private List<Product> products = new ArrayList<>();


    private List<Object> productName = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        listView = findViewById(R.id.categoriList);
        textView = findViewById(R.id.categorynameId);

        addListenerOnButton();

    }

    public void addListenerOnButton() {

        imageButtonAdd = findViewById(R.id.imageButtonAdd);
        imageButtonDelete = findViewById(R.id.imageButtonDelete);
        imageButtonBack = findViewById(R.id.imageButtonBack);
        imageButtonEdit = findViewById(R.id.imageButtonEdit);

        imageButtonAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(ProductActivity.this,
                        ProductEditActivity.class);
                intent.putExtra("catId", category.getId());

                ProductActivity.this.startActivity(intent);
            }

        });

        imageButtonDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (selected != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProductActivity.this);
                    builder.setTitle("ürün Sil");
                    builder.setMessage(selected.getName() + " İsimli Ürünü Silmek İstediğinize Emin Misiniz?");
                    builder.setNegativeButton("Hayır", null);
                    builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                                try {
                                    for (Category category : categoryRepository.findAll()){
                                        if(category.getName().equals("Eski Ürünler")){
                                            deletedCategory=category;
                                            break;
                                        }

                                    }

                                    for(Product product : productRepository.findAll()){
                                        if (selected.getName().equalsIgnoreCase(product.getName())){

                                                product.setCategory(deletedCategory);
                                                productRepository.update(product);

                                            break;
                                        }
                                    }
                                } catch (SQLException e) {
                                    e.printStackTrace();


                                }
                            Toast.makeText(getApplicationContext(), selected.getName() + " silindi.", Toast.LENGTH_LONG).show();
                            adapter.remove(selected.getName());
                                onResume();
                        }
                    });
                    builder.show();
                } else {

                        Toast.makeText(getApplicationContext(), "Lütfen Ürün Seçiniz", Toast.LENGTH_LONG).show();


                }


            }


        });

        imageButtonBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {


                ProductActivity.this.finish();
            }

        });

        imageButtonEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(ProductActivity.this,
                        ProductEditActivity.class);
                if (selected != null) {
                    intent.putExtra("productId", selected.getId());
                    intent.putExtra("catId", category.getId());

                    ProductActivity.this.startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Lütfen Urun Seçiniz", Toast.LENGTH_LONG).show();
                }

            }

        });

    }


        @Override
        protected void onResume() {
            super.onResume();

            productName.clear();
            products.clear();
            selected=null;

            try {
                productRepository = getHelper().getProductRepository();
                categoryRepository = getHelper().getCategoryRepository();
                catId = getIntent().getExtras().getLong("catId");
                category = categoryRepository.findById(catId);

            } catch (SQLException e) {
                e.printStackTrace();
            }

            for(Product product : category.getProducts()){

                products.add(product);
                productName.add(product.getName());
            }

           adapter = new ArrayAdapter<Object>(this, android.R.layout.simple_list_item_1, productName);
           listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    if (products != null) {
                        selected = products.get(i);
                    }
                }
            });
            textView.setText(category.getName());

    }
}
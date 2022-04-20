package com.dogukanhan.kayitlar.activity.Product;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
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

public class ProductEditActivity extends OrmLiteBaseActivity<DatabaseHelper> {

    private ImageButton imageButtonApply;
    private ImageButton imageButtonBack;
    private EditText editTextProductName,editTextProductBrand,editTextProductBarcod;

    private Long productId;
    private Long categoryId;


    private ProductRepository repository;
    private CategoryRepository categoryRepository;
    private Category category;
    private Spinner categorySpin;

    private List<String> categoryName = new ArrayList<>();
    private List<Category> categories = new ArrayList<>();
    private List<Product> oldProduct = new ArrayList<>();


    private ArrayAdapter categoryAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_edit);

        editTextProductBarcod = findViewById(R.id.editTextProductBarcod);
        editTextProductBrand = findViewById(R.id.editTextProductBrand);
        editTextProductName = findViewById(R.id.editTextProductName);
        categorySpin = findViewById(R.id.catSpinner);


        Long catId = getIntent().getExtras().getLong("catId");

        try {
            repository = getHelper().getProductRepository();
            categoryRepository = getHelper().getCategoryRepository();
            category = categoryRepository.findById(catId);
            categories=categoryRepository.findAll();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (Category category : categories) {
            if(!(category.getName().equals("Eski Ürünler"))){
                categoryName.add(category.getName());

            }
        }

        categoryAdapter = new ArrayAdapter<String>(this,  R.layout.spinner_item, categoryName);
        categorySpin.setAdapter(categoryAdapter);

        try{
            categoryId =  getIntent().getExtras().getLong("catId");
            Category takencategory = categoryRepository.findById(catId);

            int i=0;
            for (String categoryName: categoryName){  //defaultta ürünün kategorisi spinnerda seçili
                if (takencategory.getName().equalsIgnoreCase(categoryName)){
                    Toast.makeText(getApplicationContext(),categoryName,Toast.LENGTH_LONG).show();
                    categorySpin.setSelection(i);
                }
                i++;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        if(getIntent().getExtras() != null && getIntent().getExtras().containsKey("productId")){  // edit butonu ile gelindiğinde..
            productId =  getIntent().getExtras().getLong("productId");
            try {

                Product product = repository.findById(productId);
                editTextProductName.setText(product.getName());
                editTextProductBrand.setText(product.getBrand());
                editTextProductBarcod.setText(product.getBarcode());

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        imageButtonApply = findViewById(R.id.imageButtonApply);
        imageButtonBack = findViewById(R.id.imageButtonBack);

        imageButtonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                boolean flag=false;

                try {
                    for (Product product1 :repository.findAll()){
                        if ((product1.getName().equalsIgnoreCase(editTextProductName.getText().toString()))
                                ){

                            flag=true;
                            break;
                        }
                    }
                    for (Category category1 : categoryRepository.findAll()){// tıklanıldıgında son secili kategoriyi bulan fonksiyon
                        if (category1.getName().equalsIgnoreCase( categorySpin.getSelectedItem().toString())){
                            category=category1;
                        }
                    }
                    if(productId == null){


                        if (!flag){  // Aynı isimde ürün oluşturmama kontrolu..

                            for (Product product :repository.findAll()){
                                if (product.getCategory().getName().equalsIgnoreCase("eski ürünler")){
                                    oldProduct.add(product);
                                }
                            }
                            Product product = new Product();
                            product.setName(editTextProductName.getText().toString());
                            product.setBrand(editTextProductBrand.getText().toString());
                            product.setBarcode(editTextProductBarcod.getText().toString());
                            product.setCategory(category);



                            if(!product.getName().isEmpty()){
                                repository.create(product);
                                Toast.makeText(getApplicationContext(),product.getName()+" isimli ürün oluşturuldu...",Toast.LENGTH_LONG).show();

                                ProductEditActivity.this.finish();

                            }else{
                                Toast.makeText(getApplicationContext(),"Lütfen ürün adı giriniz...",Toast.LENGTH_LONG).show();
                            }
                        }
                        else{

                            Toast.makeText(getApplicationContext(),"Bu isimde bir ürün zaten var." +
                                    "\n Bu Ürün Daha Önce Silinen Bir Ürünse Eski Ürünler Kategorisinden Ulaşınız ",Toast.LENGTH_LONG).show();
                        }

                    }else{
                        Product product = repository.findById(productId);
                        product.setName(editTextProductName.getText().toString());
                        product.setBrand(editTextProductBrand.getText().toString());
                        product.setBarcode(editTextProductBarcod.getText().toString());
                        product.setCategory(category);

                        Toast.makeText(getApplicationContext(),product.getCategory().getName(),Toast.LENGTH_LONG).show();

                        if(!product.getName().isEmpty()){
                            repository.update(product);
                            ProductEditActivity.this.finish();
                            Toast.makeText(getApplicationContext(),"Ürün güncellendi...",Toast.LENGTH_LONG).show();

                        }else{
                            Toast.makeText(getApplicationContext(),"Ürün ismi giriniz...",Toast.LENGTH_LONG).show();
                            ProductEditActivity.this.finish();

                        }

                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                //Kategoriyi db ye  ekle...
                //eklenen kategori db de varsa toast mesajı yolla  bırdaha ekleme yanlışlıkla 2  kez eklemesin...

            }
        });

        imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ProductEditActivity.this.finish();

            }
        });

    }

    protected void onResume() {
        super.onResume();


    }


}

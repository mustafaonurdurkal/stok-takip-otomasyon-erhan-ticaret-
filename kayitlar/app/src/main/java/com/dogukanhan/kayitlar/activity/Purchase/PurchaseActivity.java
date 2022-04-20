package com.dogukanhan.kayitlar.activity.Purchase;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dogukanhan.kayitlar.R;
import com.dogukanhan.kayitlar.activity.Sales.SalesActivity;
import com.dogukanhan.kayitlar.config.DatabaseHelper;
import com.dogukanhan.kayitlar.model.Category;
import com.dogukanhan.kayitlar.model.Customer;
import com.dogukanhan.kayitlar.model.Product;
import com.dogukanhan.kayitlar.model.Purchase;
import com.dogukanhan.kayitlar.model.Wholesaler;
import com.dogukanhan.kayitlar.repository.CategoryRepository;
import com.dogukanhan.kayitlar.repository.ProductRepository;
import com.dogukanhan.kayitlar.repository.PurchaseRepository;
import com.dogukanhan.kayitlar.repository.WholesalerRepository;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PurchaseActivity extends OrmLiteBaseActivity<DatabaseHelper> {


    private ImageButton buttonPurchaseSave;
    private ImageButton buttonPurchaseBack;


    private TextView textViewTotalCost;

    private EditText editTextAmount, editTextCost;

    private Spinner productSpin;
    private Spinner wholesalerSpin;
    private Spinner categorySpin;


    private ArrayAdapter productAdapter;
    private ArrayAdapter wholesalerAdapter;
    private ArrayAdapter categoryAdapter;


    private List<String> productName = new ArrayList<>();
    private List<String> wholesalerName = new ArrayList<>();
    private List<String> categoryName = new ArrayList<>();


    private List<Product> products = new ArrayList<>();
    private List<Product> catProducts = new ArrayList<>();

    private List<Wholesaler> wholesalers = new ArrayList<>();
    private List<Category> categories = new ArrayList<>();


    private ProductRepository productRepository;
    private WholesalerRepository wholesalerRepository;
    private PurchaseRepository purchaseRepository;
    private CategoryRepository categoryRepository;

    private Category selectedCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        productSpin = findViewById(R.id.spinner);
        wholesalerSpin = findViewById(R.id.spinner2);
        categorySpin = findViewById(R.id.spinner3);


        textViewTotalCost = findViewById(R.id.textView18);

        editTextAmount = findViewById(R.id.editText2);
        editTextCost = findViewById(R.id.editText3);

        buttonPurchaseSave = findViewById(R.id.imageButtonSaleSave);
        buttonPurchaseBack = findViewById(R.id.imageButton2);

        try {
            productRepository = getHelper().getProductRepository();
            wholesalerRepository = getHelper().getWholesalerRepository();
            purchaseRepository = getHelper().getPurchaseRepository();
            categoryRepository = getHelper().getCategoryRepository();

            products = productRepository.findAll();
            wholesalers = wholesalerRepository.findAll();
            categories = categoryRepository.findAll();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (Category category : categories) {
            if (!(category.getName().equalsIgnoreCase("eski ürünler"))) {
                categoryName.add(category.getName());
            }
        }
        categoryAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, categoryName);
        categorySpin.setAdapter(categoryAdapter);

        for (Wholesaler wholesaler : wholesalers) {
            wholesalerName.add(wholesaler.getName()+" "+wholesaler.getSurname());
        }
        wholesalerAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, wholesalerName);
        wholesalerSpin.setAdapter(wholesalerAdapter);

        buttonPurchaseBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PurchaseActivity.this.finish();

            }
        });

        buttonPurchaseSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Wholesaler wholesaler = null;
                Product product = null;

                if (productSpin.getSelectedItem() != null)
                    for (Product product1 : products) {
                        if (productSpin.getSelectedItem().toString().equals(product1.getName())) {
                            product = product1;
                        }
                    }

                if (wholesalerSpin.getSelectedItem() != null)
                    for (Wholesaler wholesaler1 : wholesalers) {
                        if ((wholesaler1.getName()+" "+wholesaler1.getSurname()).equals(wholesalerSpin.getSelectedItem().toString())) {
                            wholesaler = wholesaler1;
                        }
                    }

                if (product == null) {

                    Toast.makeText(PurchaseActivity.this, "Lutfen urun seciniz", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (wholesaler == null) {

                    Toast.makeText(PurchaseActivity.this, "Lutfen toptanci seciniz", Toast.LENGTH_SHORT).show();
                    return;
                }

                Purchase purchase = new Purchase();

                purchase.setWholesaler(wholesaler);
                purchase.setProduct(product);

                purchase.setAmount(new BigDecimal(editTextAmount.getText().toString()));
                purchase.setPerCost(new BigDecimal(editTextCost.getText().toString()));
                purchase.setCost(purchase.getPerCost().multiply(purchase.getAmount()));
                java.util.Date now = new java.util.Date();
                java.sql.Date sqlDate = new java.sql.Date(now.getTime());
                purchase.setDate(sqlDate);
                wholesaler.setCurrentPayout(wholesaler.getCurrentPayout().add(purchase.getCost()));


                try {
                    purchaseRepository.create(purchase);
                    wholesalerRepository.update(wholesaler);
                    Toast.makeText(getApplicationContext(), "Satın alım Oluşturuldu.", Toast.LENGTH_SHORT).show();
                    PurchaseActivity.this.finish();

                } catch (SQLException e) {
                    Toast.makeText(PurchaseActivity.this, "Hata olustu", Toast.LENGTH_LONG).show();
                    Log.d("Purchase save", "PurchaseRepository create method error", e);
                }
            }
        });

        editTextAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!editTextAmount.getText().toString().isEmpty() && !editTextCost.getText().toString().isEmpty())
                    try {
                        textViewTotalCost.setText(
                                new BigDecimal(editTextAmount.getText().toString())
                                        .multiply(new BigDecimal(editTextCost.getText().toString())).toString() + " TL"
                        );
                    }catch (NumberFormatException e)
                    {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),"Hatalı sayı girişi yaptınız...",Toast.LENGTH_SHORT).show();
                    }

            }
        });

        editTextCost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!editTextAmount.getText().toString().isEmpty() && !editTextCost.getText().toString().isEmpty())

                    try{
                        textViewTotalCost.setText(
                                new BigDecimal(editTextAmount.getText().toString())
                                        .multiply(new BigDecimal(editTextCost.getText().toString())).toString() + " TL"
                        );
                    }catch (NumberFormatException e)
                    {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),"Hatalı sayı girişi yaptınız...",Toast.LENGTH_SHORT).show();
                    }

            }
        });
        categorySpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                for (Category category : categories) {
                    if (category.getName().equals(categorySpin.getItemAtPosition(position).toString())) {
                        selectedCategory = category;
                        break;
                    }
                }
                catProducts.clear();
                for (Product product : products) {

                    if (product.getCategory().getId() == selectedCategory.getId()) {
                        catProducts.add(product);
                    }
                }
                productName.clear();
                for (Product product : catProducts) {
                    productName.add(product.getName());
                }

                productAdapter = new ArrayAdapter<String>(PurchaseActivity.this, R.layout.spinner_item, productName);
                productSpin.setAdapter(productAdapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getApplicationContext(), "catgegori seciniz", Toast.LENGTH_LONG).show();
            }
        });


    }
}

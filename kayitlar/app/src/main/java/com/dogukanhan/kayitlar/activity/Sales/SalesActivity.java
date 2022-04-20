package com.dogukanhan.kayitlar.activity.Sales;

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

import com.dogukanhan.kayitlar.MainActivity;
import com.dogukanhan.kayitlar.R;
import com.dogukanhan.kayitlar.config.DatabaseHelper;
import com.dogukanhan.kayitlar.model.Category;
import com.dogukanhan.kayitlar.model.Customer;
import com.dogukanhan.kayitlar.model.Product;
import com.dogukanhan.kayitlar.model.Sale;
import com.dogukanhan.kayitlar.repository.CategoryRepository;
import com.dogukanhan.kayitlar.repository.CustomerRepository;
import com.dogukanhan.kayitlar.repository.ProductRepository;
import com.dogukanhan.kayitlar.repository.SaleRepository;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SalesActivity extends OrmLiteBaseActivity<DatabaseHelper> {

    private ImageButton buttonSaleSave;
    private ImageButton buttonSaleBack;


    private TextView textViewTotalCost;

    private EditText editTextAmount, editTextCost;

    private Spinner productSpin;
    private Spinner customerSpin;
    private Spinner categorySpin;


    private ArrayAdapter productAdapter;
    private ArrayAdapter customerAdapter;
    private ArrayAdapter categoryAdapter;


    private List<String> productName = new ArrayList<>();
    private List<String> customerName = new ArrayList<>();
    private List<String> categoryName = new ArrayList<>();


    private List<Product> products = new ArrayList<>();
    private List<Product> catProducts = new ArrayList<>();

    private List<Customer> customers = new ArrayList<>();
    private List<Category> categories = new ArrayList<>();


    private ProductRepository productRepository;
    private CustomerRepository customerRepository;
    private SaleRepository saleRepository;
    private CategoryRepository categoryRepository;

    private Category selectedCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);

        productSpin = findViewById(R.id.spinner);
        customerSpin = findViewById(R.id.spinner2);
        categorySpin = findViewById(R.id.spinner3);


        textViewTotalCost = findViewById(R.id.textView18);

        editTextAmount = findViewById(R.id.editText2);
        editTextCost = findViewById(R.id.editText3);

        buttonSaleSave = findViewById(R.id.imageButtonSaleSave);
        buttonSaleBack = findViewById(R.id.imageButton2);


        try {
            productRepository = getHelper().getProductRepository();
            customerRepository = getHelper().getCustomerRepository();
            saleRepository = getHelper().getSaleRepository();
            categoryRepository = getHelper().getCategoryRepository();

            products = productRepository.findAll();
            customers = customerRepository.findAll();
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


        for (Customer customer : customers) {
            customerName.add(customer.getName()+" "+customer.getSurname());
        }
        customerAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, customerName);
        customerSpin.setAdapter(customerAdapter);

        buttonSaleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SalesActivity.this.finish();

            }
        });


        buttonSaleSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Sale sale = new Sale();

                Customer customer = null;
                Product product = null;

                if (productSpin.getSelectedItem() != null)
                    for (Product product1 : products) {

                        if (product1.getName().equals(productSpin.getSelectedItem().toString())) {
                            product = product1;
                        }
                    }
                if (customerSpin.getSelectedItem() != null)
                    for (Customer customer1 : customers) {
                        if ((customer1.getName()+" "+customer1.getSurname()).equals(customerSpin.getSelectedItem().toString())) {
                            customer = customer1;
                        }
                    }

                if (product == null) {

                    Toast.makeText(SalesActivity.this, "Lutfen urun seciniz", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (customer == null) {

                    Toast.makeText(SalesActivity.this, "Lutfen musteri seciniz", Toast.LENGTH_SHORT).show();
                    return;
                }


                sale.setCustomer(customer);
                sale.setProduct(product);


                sale.setAmount(new BigDecimal(editTextAmount.getText().toString()));
                sale.setPerCost(new BigDecimal(editTextCost.getText().toString()));
                sale.setCost(sale.getPerCost().multiply(sale.getAmount()));

                java.util.Date now = new java.util.Date();
                java.sql.Date sqlDate = new java.sql.Date(now.getTime());
                sale.setDate(sqlDate);

                customer.setCurrentIncome(customer.getCurrentIncome().add(sale.getCost()));


                try {
                    saleRepository.create(sale);
                    customerRepository.update(customer);
                    Toast.makeText(getApplicationContext(), "Satış Oluşturuldu.", Toast.LENGTH_SHORT).show();
                    SalesActivity.this.finish();

                } catch (SQLException e) {
                    Toast.makeText(SalesActivity.this, "Hata olustu", Toast.LENGTH_LONG).show();
                    Log.d("Sale save", "SaleRepository create method error", e);
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

                productAdapter = new ArrayAdapter<String>(SalesActivity.this, R.layout.spinner_item, productName);
                productSpin.setAdapter(productAdapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getApplicationContext(), "catgegori seciniz", Toast.LENGTH_LONG).show();
            }
        });


    }
}

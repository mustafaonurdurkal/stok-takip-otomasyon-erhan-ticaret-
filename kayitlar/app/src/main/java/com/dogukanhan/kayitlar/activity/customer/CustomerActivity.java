package com.dogukanhan.kayitlar.activity.customer;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toolbar;

import androidx.appcompat.widget.SearchView;

import com.dogukanhan.kayitlar.OrmLiteAppCompatActivity;
import com.dogukanhan.kayitlar.R;
import com.dogukanhan.kayitlar.activity.customer.ui.main.CustomerAdapter;
import com.dogukanhan.kayitlar.config.DatabaseHelper;
import com.dogukanhan.kayitlar.model.Customer;
import com.dogukanhan.kayitlar.repository.CustomerRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class CustomerActivity extends OrmLiteAppCompatActivity {

    private ListView listView;
    private CustomerRepository customerRepository;
    private CustomerAdapter customerAdapter;
    private List<Customer> customers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        listView = findViewById(R.id.customer_list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {     //Clicked ListView item...
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Customer clicked = customerAdapter.getItem(i);

                Intent intent = new Intent(CustomerActivity.this,
                        CustomerShowActivity.class);
                intent.putExtra("customer", clicked);
                CustomerActivity.this.startActivity(intent);

            }
        });

        FloatingActionButton fab = findViewById(R.id.fab2);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomerActivity.this.finish();
            }
        });

        FloatingActionButton floationButtonAdd = findViewById(R.id.fab4);
        floationButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerActivity.this,
                        CustomerNewActivity.class);
                CustomerActivity.this.startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            customerRepository = getHelper().getCustomerRepository();
            customers = customerRepository.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        customerAdapter = new CustomerAdapter(this, customers);
        listView.setAdapter(customerAdapter);

    }

    private void filterCustomers(String query) {

        List<Customer> filteredList = new ArrayList<>();

        for (Customer customer : customers) {
            if (customer.getName() != null && customer.getName().toLowerCase().startsWith(query.toLowerCase())) {
                filteredList.add(customer);
            } else if (customer.getSurname() != null && customer.getSurname().toLowerCase().startsWith(query.toLowerCase())) {
                filteredList.add(customer);
            } else if (customer.getPhone() != null && customer.getPhone().startsWith(query)) {
                filteredList.add(customer);
            }
        }

        if (filteredList.size() == 0) {
            filteredList.add(new Customer("Sonuc bulamadi", "", new BigDecimal(0)));
        }

        customerAdapter = new CustomerAdapter(this, filteredList);
        listView.setAdapter(customerAdapter);
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
                filterCustomers(s.trim().toLowerCase());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                if (customers.size() < 1000) {
                    if (s.isEmpty()) {
                        customerAdapter = new CustomerAdapter(CustomerActivity.this, customers);
                        listView.setAdapter(customerAdapter);
                    } else {
                        filterCustomers(s.trim().toLowerCase());

                    }
                } else if (s.isEmpty()) {
                    customerAdapter = new CustomerAdapter(CustomerActivity.this, customers);
                    listView.setAdapter(customerAdapter);
                }

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}
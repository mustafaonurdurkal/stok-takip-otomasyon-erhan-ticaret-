package com.dogukanhan.kayitlar.activity.wholesaler;

import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dogukanhan.kayitlar.R;
import com.dogukanhan.kayitlar.activity.wholesaler.main.WholeSalerNewActivity;
import com.dogukanhan.kayitlar.activity.wholesaler.main.WholesalerAdapter;
import com.dogukanhan.kayitlar.config.DatabaseHelper;
import com.dogukanhan.kayitlar.model.Wholesaler;
import com.dogukanhan.kayitlar.repository.WholesalerRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.dogukanhan.kayitlar.OrmLiteAppCompatActivity;


import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WholesalerActivity extends OrmLiteAppCompatActivity {

    private ListView listView;
    private WholesalerAdapter wholesalerAdapter;
    private WholesalerRepository wholesalerRepository;
    private List<Wholesaler> wholesalers ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wholesaler);

        listView = findViewById(R.id.wholesaler_list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {     //Clicked ListView item...
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                 Wholesaler clicked = wholesalerAdapter.getItem(i);

                Intent intent = new Intent(WholesalerActivity.this,
                        WholesalerShowActivity.class);
                intent.putExtra("wholesaler", clicked);
                WholesalerActivity.this.startActivity(intent);

            }
        });

        FloatingActionButton fab = findViewById(R.id.fab2);
        FloatingActionButton floationButtonAdd = findViewById(R.id.fab4);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WholesalerActivity.this.finish();
            }
        });

        floationButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WholesalerActivity.this,
                        WholeSalerNewActivity.class);
                WholesalerActivity.this.startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            wholesalerRepository = getHelper().getWholesalerRepository();
            wholesalers=wholesalerRepository.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        wholesalerAdapter = new WholesalerAdapter(this, wholesalers);
        listView.setAdapter(wholesalerAdapter);
    }

    private void fiterWholesalers(String query) {

        List<Wholesaler> filteredList = new ArrayList<>();

        for (Wholesaler wholesaler : wholesalers) {
            if (wholesaler.getName() != null && wholesaler.getName().toLowerCase().startsWith(query.toLowerCase())) {
                filteredList.add(wholesaler);
            } else if (wholesaler.getSurname() != null && wholesaler.getSurname().toLowerCase().startsWith(query.toLowerCase())) {
                filteredList.add(wholesaler);
            } else if (wholesaler.getPhone() != null && wholesaler.getPhone().startsWith(query)) {
                filteredList.add(wholesaler);
            }
        }

        if (filteredList.size() == 0) {
            filteredList.add(new Wholesaler("Sonuc bulamadi", "", new BigDecimal(0)));
        }

        wholesalerAdapter = new WholesalerAdapter(this, filteredList);
        listView.setAdapter(wholesalerAdapter);
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
                fiterWholesalers(s.trim().toLowerCase());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                if (wholesalers.size() < 1000) {
                    if (s.isEmpty()) {
                        wholesalerAdapter = new WholesalerAdapter(WholesalerActivity.this, wholesalers);
                        listView.setAdapter(wholesalerAdapter);
                    } else {
                        fiterWholesalers(s.trim().toLowerCase());

                    }
                } else if (s.isEmpty()) {
                    wholesalerAdapter = new WholesalerAdapter(WholesalerActivity.this, wholesalers);
                    listView.setAdapter(wholesalerAdapter);
                }

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}

package com.dogukanhan.kayitlar.activity.income;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.dogukanhan.kayitlar.R;
import com.dogukanhan.kayitlar.config.DatabaseHelper;
import com.dogukanhan.kayitlar.model.Customer;
import com.dogukanhan.kayitlar.model.Income;
import com.dogukanhan.kayitlar.model.Sale;
import com.dogukanhan.kayitlar.repository.CustomerRepository;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IncomeActivity extends OrmLiteBaseActivity<DatabaseHelper> {

    private GridView gridView;

    private TextView textViewTotalCount;

    private TextView textViewTotalIncome;

    private CustomerRepository customerRepository;

    private List<Income> incomes;

    private Button btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);

        gridView = findViewById(R.id.gridViewIncome);
        textViewTotalCount = findViewById(R.id.textViewTotalCount);
        textViewTotalIncome = findViewById(R.id.textViewTotalIncome);
        btn=findViewById(R.id.button2);

        try {
            customerRepository = getHelper().getCustomerRepository();
        } catch (SQLException e) {
            Log.e("Customer Repository", "Customer Repository can't get", e);
            throw new RuntimeException(e);
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IncomeActivity.this.finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        incomes = new ArrayList<>();

        try {

            List<Customer> customers = customerRepository.findAll();

            int totalIncome = 0;

            BigDecimal totalCount = BigDecimal.ZERO;

            for (Customer customer : customers) {
                for (Sale sale : customer.getSales()) {
                    incomes.addAll(sale.getIncomes());
                    for (Income income : sale.getIncomes()) {
                        totalCount = totalCount.add(income.getAmount());
                        totalIncome++;
                    }
                }
            }

            IncomeAdapter incomeAdapter = new IncomeAdapter(this, incomes);
            gridView.setAdapter(incomeAdapter);

            textViewTotalIncome.setText(String.valueOf(totalIncome));
            textViewTotalCount.setText(totalCount.toString()+" TL");

        } catch (SQLException e) {
            e.printStackTrace();
        }



    }
}

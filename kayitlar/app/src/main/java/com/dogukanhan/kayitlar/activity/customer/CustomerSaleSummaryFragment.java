package com.dogukanhan.kayitlar.activity.customer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;

import com.dogukanhan.kayitlar.OrmLiteFragment;
import com.dogukanhan.kayitlar.R;
import com.dogukanhan.kayitlar.activity.customer.ui.main.CustomerSaleSummaryAdapter;
import com.dogukanhan.kayitlar.model.Customer;
import com.dogukanhan.kayitlar.model.Income;
import com.dogukanhan.kayitlar.model.Sale;
import com.dogukanhan.kayitlar.repository.CustomerRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerSaleSummaryFragment extends OrmLiteFragment {

    private View root;
    private GridView gridView;
    private CustomerSaleSummaryAdapter saleSummaryAdapter;
    private Customer customerDao;
    private Customer customer;
    private CustomerRepository customerRepository;

    List<Income> incomes = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customer = (Customer) getActivity().getIntent().getExtras().getSerializable("customer");
        try {
            customerRepository = getHelper().getCustomerRepository();
            customerDao = customerRepository.findById(customer.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (Sale sale : customerDao.getSales()) {
            for (Income income : sale.getIncomes()) {
                incomes.add(income);
            }
        }

    }



    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {


        root = inflater.inflate(R.layout.fragment_customer_salesummary, container, false);


        gridView = root.findViewById(R.id.gridViewCustomerSaleSummary);
        saleSummaryAdapter = new CustomerSaleSummaryAdapter(getContext(), incomes);
        gridView.setAdapter(saleSummaryAdapter);


        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    }

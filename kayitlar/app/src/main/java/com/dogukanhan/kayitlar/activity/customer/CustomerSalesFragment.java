package com.dogukanhan.kayitlar.activity.customer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.dogukanhan.kayitlar.OrmLiteFragment;
import com.dogukanhan.kayitlar.R;
import com.dogukanhan.kayitlar.activity.customer.ui.main.CustomerSalesAdapter;
import com.dogukanhan.kayitlar.model.Customer;
import com.dogukanhan.kayitlar.model.Income;
import com.dogukanhan.kayitlar.model.Sale;
import com.dogukanhan.kayitlar.repository.CustomerRepository;
import com.dogukanhan.kayitlar.repository.IncomeRepository;
import com.dogukanhan.kayitlar.repository.SaleRepository;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerSalesFragment extends OrmLiteFragment {
    private View root;

    private Customer customer;
    private Customer customerDao;

    private Income income;

    private GridView gridView;

    private CustomerRepository customerRepository;
    private SaleRepository saleRepository;
    private IncomeRepository incomeRepository;

    private CustomerSalesAdapter salesAdapter;
    private  Sale clicked;
    private EditText input;

    private String m_Text = "";

    private List<Sale> sales = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        customer= (Customer) getActivity().getIntent().getExtras().getSerializable("customer");
        try {
            customerRepository = getHelper().getCustomerRepository();
            saleRepository=getHelper().getSaleRepository();
            incomeRepository=getHelper().getIncomeRepository();
            customerDao=customerRepository.findById(customer.getId());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        sales.addAll(customerDao.getSales());

        root = inflater.inflate(R.layout.fragment_customer_sales, container, false);

        gridView= root.findViewById(R.id.gridViewCustomerSales);
        salesAdapter = new CustomerSalesAdapter(getContext(),sales);
        gridView.setAdapter(salesAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                clicked = (Sale) salesAdapter.getItem(i);

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Ödenecek Miktarı Giriniz. \n Satış Tutarı:"+ clicked.getCost().toString());

                input = new EditText(getContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT);

                builder.setView(input);


                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        try {
                            if ((clicked.getCost().compareTo( new BigDecimal(m_Text))==1 ||
                                    clicked.getCost().compareTo( new BigDecimal(m_Text))==0) &&
                                    (new BigDecimal(m_Text).compareTo(new BigDecimal(0))==1)){

                                customerDao.setCurrentIncome(customerDao.getCurrentIncome().subtract(new BigDecimal(m_Text)));
                                clicked.setCost(clicked.getCost().subtract(new BigDecimal(m_Text)));
                                income=new Income();
                                income.setSale(clicked);
                                income.setAmount(new BigDecimal(m_Text));

                                java.util.Date now = new java.util.Date();
                                java.sql.Date sqlDate = new java.sql.Date(now.getTime());
                                income.setDate(sqlDate);
                                income.setSale(clicked);
                            }
                            else{
                                Toast.makeText(getContext(),"Satış Tutarından fazla ödeme giremezsiniz...",Toast.LENGTH_LONG).show();
                            }
                        } catch (NumberFormatException exception) {
                            Toast.makeText(getContext(),"Ödenecek tutar bölümüne sadece rakam girilebilir...",Toast.LENGTH_LONG).show();
                        }

                        try {
                            customerDao.getSales().update(clicked);
                            customerRepository.update(customerDao);
                            saleRepository.update(clicked);
                            incomeRepository.create(income);

                        } catch (SQLException e) {
                            e.printStackTrace();
                            throw new RuntimeException(e);

                        }
                    }
                });



                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();


            }
        });
        return root;
    }
}


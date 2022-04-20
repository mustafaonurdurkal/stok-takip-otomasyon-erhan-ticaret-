package com.dogukanhan.kayitlar.activity.wholesaler;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
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
import com.dogukanhan.kayitlar.activity.wholesaler.main.WholesalerPurchaseAdapter;
import com.dogukanhan.kayitlar.model.Customer;
import com.dogukanhan.kayitlar.model.Income;
import com.dogukanhan.kayitlar.model.Payout;
import com.dogukanhan.kayitlar.model.Purchase;
import com.dogukanhan.kayitlar.model.Sale;
import com.dogukanhan.kayitlar.model.Wholesaler;
import com.dogukanhan.kayitlar.repository.IncomeRepository;
import com.dogukanhan.kayitlar.repository.PayoutRepository;
import com.dogukanhan.kayitlar.repository.PurchaseRepository;
import com.dogukanhan.kayitlar.repository.SaleRepository;
import com.dogukanhan.kayitlar.repository.WholesalerRepository;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WholesalerPurchasesFragment extends OrmLiteFragment {
    private View root;

    private Wholesaler wholesaler;
    private Wholesaler wholesalerDao;

    private Payout payout;

    private GridView gridView;

    private WholesalerRepository wholesalerRepository;
    private PurchaseRepository purchaseRepository;
    private PayoutRepository payoutRepository;

    private WholesalerPurchaseAdapter purchaseAdapter;
    private Purchase clicked;
    private EditText input;

    private String m_Text = "";


    List<Purchase> purchases = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

}

    @Override
    public View onCreateView(

            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        wholesaler= (Wholesaler) getActivity().getIntent().getExtras().getSerializable("wholesaler");
        try {
            wholesalerRepository = getHelper().getWholesalerRepository();
            purchaseRepository=getHelper().getPurchaseRepository();
            payoutRepository=getHelper().getPayoutRepository();
            wholesalerDao=wholesalerRepository.findById(wholesaler.getId());

        } catch (SQLException e) {
            e.printStackTrace();
        }
        purchases.addAll(wholesalerDao.getPurchases());

        root = inflater.inflate(R.layout.fragment_wholesaler_purchases, container, false);

        gridView= root.findViewById(R.id.gridViewWholesalerSales);
        purchaseAdapter = new WholesalerPurchaseAdapter(getContext(),purchases);
        gridView.setAdapter(purchaseAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                clicked = (Purchase) purchaseAdapter.getItem(i);
                Toast.makeText(getContext(),String.valueOf(clicked.getCost()),Toast.LENGTH_LONG).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Ödenecek Miktarı Giriniz. \n Satın Alım Tutarı:"+clicked.getCost().toString());

                input = new EditText(getContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT);

                builder.setView(input);


                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        try {
                            if ((clicked.getCost().compareTo( new BigDecimal(m_Text))==1 ||
                                    clicked.getCost().compareTo( new BigDecimal(m_Text))==0)&&
                                    (new BigDecimal(m_Text).compareTo(new BigDecimal(0))==1)){
                                wholesalerDao.setCurrentPayout(wholesalerDao.getCurrentPayout().subtract(new BigDecimal(m_Text)));
                                clicked.setCost(clicked.getCost().subtract(new BigDecimal(m_Text)));
                                payout=new Payout();
                                payout.setPurchase(clicked);
                                payout.setAmount(new BigDecimal(m_Text));

                                java.util.Date now = new java.util.Date();
                                java.sql.Date sqlDate = new java.sql.Date(now.getTime());
                                payout.setDate(sqlDate);
                                payout.setPurchase(clicked);

                            }
                            else{
                                Toast.makeText(getContext(),"Satış Tutarından fazla ödeme giremezsiniz...",Toast.LENGTH_LONG).show();
                            }
                        } catch (NumberFormatException exception) {
                            Toast.makeText(getContext(),"Ödenecek tutar bölümüne sadece rakam girilebilir...",Toast.LENGTH_LONG).show();
                        }

                        try {
                            wholesalerDao.getPurchases().update(clicked);
                            wholesalerRepository.update(wholesalerDao);
                            purchaseRepository.update(clicked);
                            payoutRepository.create(payout);

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

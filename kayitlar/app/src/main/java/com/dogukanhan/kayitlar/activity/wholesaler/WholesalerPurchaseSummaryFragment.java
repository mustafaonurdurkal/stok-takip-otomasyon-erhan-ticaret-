package com.dogukanhan.kayitlar.activity.wholesaler;

import androidx.annotation.NonNull;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.dogukanhan.kayitlar.OrmLiteFragment;
import com.dogukanhan.kayitlar.R;
import com.dogukanhan.kayitlar.activity.wholesaler.main.WholesalerPurchaseSummaryAdapter;
import com.dogukanhan.kayitlar.model.Payout;
import com.dogukanhan.kayitlar.model.Purchase;
import com.dogukanhan.kayitlar.model.Wholesaler;
import com.dogukanhan.kayitlar.repository.WholesalerRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WholesalerPurchaseSummaryFragment extends OrmLiteFragment {


    private View root;
    private GridView gridView;


    private WholesalerPurchaseSummaryAdapter purchaseSummaryAdapter;


    private Wholesaler wholesalerDao;
    private Wholesaler wholesaler;
    private WholesalerRepository wholesalerRepository;

    List<Payout> payouts = new ArrayList<>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wholesaler = (Wholesaler) getActivity().getIntent().getExtras().getSerializable("wholesaler");
        try {
            wholesalerRepository = getHelper().getWholesalerRepository();
            wholesalerDao = wholesalerRepository.findById(wholesaler.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (Purchase purchase : wholesalerDao.getPurchases()) {
            for (Payout payout : purchase.getPayouts()) {
                payouts.add(payout);
            }
        }
    }


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {


        root = inflater.inflate(R.layout.activity_wholesaler_purchase_summary_fragment, container, false);


        gridView = root.findViewById(R.id.gridViewWholesalerPurchaseSummary);
        purchaseSummaryAdapter = new WholesalerPurchaseSummaryAdapter(getContext(), payouts);
        gridView.setAdapter(purchaseSummaryAdapter);


        return root;
    }
}

package com.dogukanhan.kayitlar.activity.Payout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.dogukanhan.kayitlar.R;
import com.dogukanhan.kayitlar.activity.income.IncomeActivity;
import com.dogukanhan.kayitlar.activity.income.IncomeAdapter;
import com.dogukanhan.kayitlar.config.DatabaseHelper;
import com.dogukanhan.kayitlar.model.Customer;
import com.dogukanhan.kayitlar.model.Income;
import com.dogukanhan.kayitlar.model.Payout;
import com.dogukanhan.kayitlar.model.Purchase;
import com.dogukanhan.kayitlar.model.Sale;
import com.dogukanhan.kayitlar.model.Wholesaler;
import com.dogukanhan.kayitlar.repository.CustomerRepository;
import com.dogukanhan.kayitlar.repository.WholesalerRepository;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PayoutActivity extends OrmLiteBaseActivity<DatabaseHelper> {

    private GridView gridView;

    private TextView textViewTotalCount;

    private TextView textViewTotalPayout;

    private WholesalerRepository wholesalerRepository;

    private List<Payout> payouts;

    private Button btn;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payout);

        gridView = findViewById(R.id.gridViewPayout);
        textViewTotalCount = findViewById(R.id.textViewTotalCount);
        textViewTotalPayout = findViewById(R.id.textViewTotalPayout);
        btn=findViewById(R.id.button2);


        try {
            wholesalerRepository = getHelper().getWholesalerRepository();
        } catch (SQLException e) {
            Log.e("WholeSaler Repository", "Wholesaler Repository can't get", e);
            throw new RuntimeException(e);
        }


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PayoutActivity.this.finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        payouts = new ArrayList<>();

        try {

            List<Wholesaler> wholesalers = wholesalerRepository.findAll();

            int totalIncome = 0;

            BigDecimal totalCount = BigDecimal.ZERO;

            for (Wholesaler wholesaler : wholesalers) {
                for (Purchase purchase : wholesaler.getPurchases()) {
                    payouts.addAll(purchase.getPayouts());
                    for (Payout payout : purchase.getPayouts()) {
                        totalCount = totalCount.add(payout.getAmount());
                        totalIncome++;
                    }
                }
            }

            PayoutAdapter payoutAdapter = new PayoutAdapter(this, payouts);
            gridView.setAdapter(payoutAdapter);

            textViewTotalPayout.setText(String.valueOf(totalIncome));
            textViewTotalCount.setText(totalCount.toString()+" TL");

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}

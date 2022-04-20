package com.dogukanhan.kayitlar.activity.Payout;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dogukanhan.kayitlar.model.Payout;
import com.dogukanhan.kayitlar.model.Wholesaler;

import java.util.List;

public class PayoutAdapter extends BaseAdapter {

    private static final LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT, 1);

    private Context context;

    private List<Payout> payouts;

    public PayoutAdapter(Context context, List<Payout> payouts) {
        this.context = context;
        this.payouts = payouts;
    }

    @Override
    public int getCount() {
        return payouts.size();
    }

    @Override
    public Object getItem(int i) {

        return payouts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private TextView textViewForMe(View view, String text) {

        LinearLayout layout = (LinearLayout) view;

        TextView textView = new TextView(context);
        textView.setText(text);
        textView.setLayoutParams(param);

        layout.addView(textView);

        return textView;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        View layout;

        final Payout payout = payouts.get(i);

        final Wholesaler wholesaler = payout.getPurchase().getWholesaler();

        layout = new LinearLayout(context);

        textViewForMe(layout, wholesaler.getName() + " " + wholesaler.getSurname());
        textViewForMe(layout, String.valueOf(payout.getPurchase().getId()));
        textViewForMe(layout, payout.getDate().toString());
        textViewForMe(layout, payout.getAmount().toString() +" TL");


        if (i % 2 == 0) {
            layout.setBackgroundColor(Color.GRAY);
        }


        return layout;
    }
}


package com.dogukanhan.kayitlar.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.dogukanhan.kayitlar.R;
import com.dogukanhan.kayitlar.activity.Notes.NotesActivity;
import com.dogukanhan.kayitlar.activity.Payout.PayoutActivity;
import com.dogukanhan.kayitlar.activity.Purchase.PurchaseActivity;
import com.dogukanhan.kayitlar.activity.Product.CategoryActivity;
import com.dogukanhan.kayitlar.activity.Sales.SalesActivity;
import com.dogukanhan.kayitlar.activity.customer.CustomerActivity;
import com.dogukanhan.kayitlar.activity.income.IncomeActivity;
import com.dogukanhan.kayitlar.activity.wholesaler.WholesalerActivity;
import com.dogukanhan.kayitlar.config.DatabaseHelper;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

import java.io.File;
import java.io.FileInputStream;

import java.io.FileOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainScreen extends OrmLiteBaseActivity<DatabaseHelper> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        Button button=findViewById(R.id.buttonCustomer);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainScreen.this, CustomerActivity.class);
                MainScreen.this.startActivity(intent);

            }
        });

        Button button2=findViewById(R.id.buttonWholesalers);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainScreen.this, WholesalerActivity.class);
                MainScreen.this.startActivity(intent);

            }
        });

        Button button3=findViewById(R.id.buttonProduct);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainScreen.this, CategoryActivity.class);
                MainScreen.this.startActivity(intent);

            }
        });
        Button button4=findViewById(R.id.buttonSales);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainScreen.this, SalesActivity.class);
                MainScreen.this.startActivity(intent);

            }
        });

        Button button5=findViewById(R.id.buttonPayout);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainScreen.this, PurchaseActivity.class);
                MainScreen.this.startActivity(intent);

            }
        });
        Button button6=findViewById(R.id.buttonNotes);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainScreen.this, NotesActivity.class);
                MainScreen.this.startActivity(intent);

            }
        });
        Button button7=findViewById(R.id.button3);
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainScreen.this, IncomeActivity.class);
                MainScreen.this.startActivity(intent);

            }
        });

        Button button8=findViewById(R.id.button5);
        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainScreen.this, PayoutActivity.class);
                MainScreen.this.startActivity(intent);

            }
        });



        Button button10=findViewById(R.id.button13);
        button10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             System.exit(0);

            }
        });

        Button button12 = findViewById(R.id.button6);
        button12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HHmmss", Locale.getDefault());
                String currentDateandTime = sdf.format(new Date());

                final File dbRoot = new File(MainScreen.this.getFilesDir().getParentFile(), "databases");
                final File outRoot = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                        , "yedek/" + currentDateandTime);

                if (!hasWritePermissions()) {
                    requestAppPermissions();
                }

                if (!outRoot.exists()) {
                    if (!outRoot.mkdirs()) {
                       // throw new RuntimeException("Can't create yedek directory");
                    }
                }

                try {


                    File[] files = dbRoot.listFiles();

                    for (File f : files) {
                        copy(f, new File(outRoot, f.getName()));
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Bakup", "error", e);
                }

                Toast.makeText(MainScreen.this, "Yedek Alindi", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void requestAppPermissions() {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }

        if (hasReadPermissions() && hasWritePermissions()) {
            return;
        }

        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, 111); // your request code
    }

    private boolean hasReadPermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    private boolean hasWritePermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    public static int copy(File src, File dst) throws Exception, IOException {

        InputStream input = new FileInputStream(src);
        OutputStream output = new FileOutputStream(dst);

        int BUFFER_SIZE = 100_000;

        byte[] buffer = new byte[BUFFER_SIZE];

        BufferedInputStream in = new BufferedInputStream(input, BUFFER_SIZE);
        BufferedOutputStream out = new BufferedOutputStream(output, BUFFER_SIZE);
        int count = 0, n = 0;
        try {
            while ((n = in.read(buffer, 0, BUFFER_SIZE)) != -1) {
                out.write(buffer, 0, n);
                count += n;
            }
            out.flush();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                Log.e(e.getMessage(), "Erorr" + e);
            }
            try {
                in.close();
            } catch (IOException e) {
                Log.e(e.getMessage(), "Erorr", e);
            }
        }
        return count;
    }

}


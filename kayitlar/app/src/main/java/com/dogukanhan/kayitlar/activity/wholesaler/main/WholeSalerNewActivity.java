package com.dogukanhan.kayitlar.activity.wholesaler.main;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dogukanhan.kayitlar.R;
import com.dogukanhan.kayitlar.activity.customer.CustomerNewActivity;
import com.dogukanhan.kayitlar.config.DatabaseHelper;
import com.dogukanhan.kayitlar.model.Customer;
import com.dogukanhan.kayitlar.model.Wholesaler;
import com.dogukanhan.kayitlar.repository.WholesalerRepository;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

import java.math.BigDecimal;
import java.sql.SQLException;


public class WholeSalerNewActivity extends OrmLiteBaseActivity<DatabaseHelper> {
    private Button buttonWholesalerSave;

    private EditText editTextName, editTextSurname,
            editTextPhone, editTextEmail, editTextAddress;

    private WholesalerRepository wholesalerRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whole_saler_new);
        buttonWholesalerSave = findViewById(R.id.buttonWholesalerSave);
        editTextName = findViewById(R.id.editTextName);
        editTextSurname = findViewById(R.id.editTextSurname);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextAddress = findViewById(R.id.editTextAddress);

        try {
            wholesalerRepository = getHelper().getWholesalerRepository();
        } catch (SQLException e) {
            Toast.makeText(this, "Bir hata olustu", Toast.LENGTH_LONG).show();
            Log.e("Sql Hatasi", "Wholesaler Repository Cagrilirken Hata olustu", e);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        buttonWholesalerSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Wholesaler wholesaler = fromEditTexts();

                try {

                    Boolean flag =false;

                    for (Wholesaler wholesaler1 :wholesalerRepository.findAll()){
                        if ((wholesaler1.getName().equals(wholesaler.getName()))&&
                                wholesaler1.getSurname().equals(wholesaler.getSurname())){
                            flag=true;
                        }
                    }
                    if(!wholesaler.getName().isEmpty()&&
                            !wholesaler.getSurname().isEmpty()){
                        if(!flag){
                            wholesalerRepository.create(wholesaler);
                            WholeSalerNewActivity.this.finish();
                        }else {
                            Toast.makeText(getApplicationContext(),"Bu isim ve Soyisimle Kayıtlı Kullanıcı zaten Var...",Toast.LENGTH_SHORT).show();

                        }

                    }
                    else{
                        Toast.makeText(getApplicationContext(),"İsim ve soyisim kısmı boş kalamaz.",Toast.LENGTH_SHORT).show();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    Toast.makeText(WholeSalerNewActivity.this, "Bir hata olustu", Toast.LENGTH_LONG).show();
                    Log.e("Sql Hatasi", "Wholesaler Repository Create Cagrilirken Hata olustu", e);
                }
            }
        });

    }

    private Wholesaler fromEditTexts() {

        Wholesaler wholesaler = new Wholesaler();

        wholesaler.setName(editTextName.getText().toString());
        wholesaler.setSurname(editTextSurname.getText().toString());
        wholesaler.setPhone(editTextPhone.getText().toString());
        wholesaler.setEmail(editTextEmail.getText().toString());
        wholesaler.setAddress(editTextAddress.getText().toString());
        wholesaler.setCurrentPayout(new BigDecimal(0));

        return wholesaler;
    }
}

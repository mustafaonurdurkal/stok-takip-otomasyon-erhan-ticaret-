package com.dogukanhan.kayitlar.activity.customer;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dogukanhan.kayitlar.R;
import com.dogukanhan.kayitlar.config.DatabaseHelper;
import com.dogukanhan.kayitlar.model.Customer;
import com.dogukanhan.kayitlar.repository.CustomerRepository;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class CustomerNewActivity extends OrmLiteBaseActivity<DatabaseHelper> {

    private Button buttonCustomerSave;

    private EditText editTextName, editTextSurname,
            editTextPhone, editTextEmail, editTextAddress;

    private CustomerRepository customerRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_new);

        buttonCustomerSave = findViewById(R.id.buttonWholesalerSave);
        editTextName = findViewById(R.id.editTextName);
        editTextSurname = findViewById(R.id.editTextSurname);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextAddress = findViewById(R.id.editTextAddress);

        try {
            customerRepository = getHelper().getCustomerRepository();
        } catch (SQLException e) {
            Toast.makeText(this, "Bir hata olustu", Toast.LENGTH_LONG).show();
            Log.e("Sql Hatasi", "Customer Repository Cagrilirken Hata olustu", e);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        buttonCustomerSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Customer customer = fromEditTexts();

                try {
                    Boolean flag =false;

                    for (Customer customer1 :customerRepository.findAll()){
                        if ((customer1.getName().equals(customer.getName()))&&
                             customer1.getSurname().equals(customer.getSurname())){
                            flag=true;
                        }
                    }
                    if(!customer.getName().isEmpty()&&
                    !customer.getSurname().isEmpty()){
                        if(!flag){
                            customerRepository.create(customer);
                            CustomerNewActivity.this.finish();
                        }else {
                            Toast.makeText(getApplicationContext(),"Bu isim ve Soyisimle Kayıtlı Kullanıcı zaten Var...",Toast.LENGTH_SHORT).show();

                        }

                    }
                    else{
                        Toast.makeText(getApplicationContext(),"İsim ve soyisim kısmı boş kalamaz.",Toast.LENGTH_SHORT).show();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    Toast.makeText(CustomerNewActivity.this, "Bir hata olustu", Toast.LENGTH_LONG).show();
                    Log.e("Sql Hatasi", "Customer Repository Create Cagrilirken Hata olustu", e);
                }
            }
        });
    }


    private Customer fromEditTexts() {

        Customer customer = new Customer();

        customer.setName(editTextName.getText().toString());
        customer.setSurname(editTextSurname.getText().toString());
        customer.setPhone(editTextPhone.getText().toString());
        customer.setEmail(editTextEmail.getText().toString());
        customer.setAddress(editTextAddress.getText().toString());
        customer.setCurrentIncome(new BigDecimal(0));

        return customer;
    }

}

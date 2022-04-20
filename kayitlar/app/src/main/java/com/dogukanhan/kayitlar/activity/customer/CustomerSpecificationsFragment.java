package com.dogukanhan.kayitlar.activity.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.dogukanhan.kayitlar.OrmLiteFragment;
import com.dogukanhan.kayitlar.R;
import com.dogukanhan.kayitlar.model.Customer;
import com.dogukanhan.kayitlar.repository.CustomerRepository;

import java.sql.SQLException;
import java.util.List;

public class CustomerSpecificationsFragment extends OrmLiteFragment {

    private View root;

    private EditText nameEditText;
    private  EditText surnameEditText;
    private  EditText phoneEditText;
    private  EditText emailEditText;
    private  EditText adresEditText;
    private ImageButton editButton;
    private ImageButton deleteButton;

    private CustomerRepository customerRepository;
    private List<Customer> customers;
    private Customer customer;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        try {
            customerRepository = getHelper().getCustomerRepository();
            customers=customerRepository.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }



        root = inflater.inflate(R.layout.fragment_customer_specifications, container, false);

        nameEditText=root.findViewById(R.id.editTextName);
        surnameEditText=root.findViewById(R.id.editTextSurname);
        phoneEditText=root.findViewById(R.id.editTextPhone);
        emailEditText=root.findViewById(R.id.editTextEmail);
        adresEditText=root.findViewById(R.id.editTextAddress);
        editButton=root.findViewById(R.id.imageButtonApply);
        deleteButton=root.findViewById(R.id.imageButtonDelete);

         customer= (Customer) getActivity().getIntent().getExtras().getSerializable("customer");

        nameEditText.setText(customer.getName());
        surnameEditText.setText(customer.getSurname());
        phoneEditText.setText(customer.getPhone());
        emailEditText.setText(customer.getEmail());
        adresEditText.setText(customer.getAddress());

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Müşteri Bilgilerini Değistir");
                builder.setMessage(customer.getName() + " "+customer.getSurname()+" İsimli Müşterinin bilgilerini güncellemek istediğinize emin misiniz? ");
                builder.setNegativeButton("Hayır", null);
                builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(!nameEditText.getText().toString().isEmpty()&&
                                !surnameEditText.getText().toString().isEmpty() ){
                            customer.setName(String.valueOf(nameEditText.getText()));
                            customer.setSurname(String.valueOf(surnameEditText.getText()));
                            customer.setPhone(String.valueOf(phoneEditText.getText()));
                            customer.setEmail(String.valueOf(emailEditText.getText()));
                            customer.setAddress(String.valueOf(adresEditText.getText()));
                            try {
                                customerRepository.update(customer);
                                Toast.makeText(getActivity().getApplicationContext(), "Müsteri bilgileri Düzenlendi...", Toast.LENGTH_SHORT).show();
                                getActivity().finish();

                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                        else{
                            Toast.makeText(getActivity().getApplicationContext(), "İsim ve soyisim boş kalamaz", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                builder.show();


            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Müşteri Sil");
                builder.setMessage(customer.getName() + " İsimli Müşteriyi Silmek istediğinize emin misiniz? \n" +
                        " Bu eylem Müsteriye ait satışlarıda siler ve Müşterinin Borç bilgisini Kaybedersiniz...");
                builder.setNegativeButton("Hayır", null);
                builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                            try {
                                customerRepository.remove(customer);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }

                            Toast.makeText(getActivity().getApplicationContext(), customer.getName() + " silindi.", Toast.LENGTH_LONG).show();
                            getActivity().finish();


                    }
                });
                builder.show();
            }
        });

        return root;
    }

}

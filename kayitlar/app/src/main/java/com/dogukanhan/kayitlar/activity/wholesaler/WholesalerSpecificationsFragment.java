package com.dogukanhan.kayitlar.activity.wholesaler;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.dogukanhan.kayitlar.OrmLiteFragment;
import com.dogukanhan.kayitlar.R;
import com.dogukanhan.kayitlar.model.Customer;
import com.dogukanhan.kayitlar.model.Wholesaler;
import com.dogukanhan.kayitlar.repository.CustomerRepository;
import com.dogukanhan.kayitlar.repository.WholesalerRepository;

import java.sql.SQLException;
import java.util.List;

public class WholesalerSpecificationsFragment extends OrmLiteFragment {
    private EditText nameEditText;
    private  EditText surnameEditText;
    private  EditText phoneEditText;
    private  EditText emailEditText;
    private  EditText adresEditText;
    private ImageButton editButton;
    private ImageButton deleteButton;
    private View root;

    private WholesalerRepository wholesalerRepository;
    private List<Wholesaler> wholesalers;
    private Wholesaler wholesaler;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        try {
            wholesalerRepository = getHelper().getWholesalerRepository();
            wholesalers=wholesalerRepository.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }


         root = inflater.inflate(R.layout.fragment_wholesaler_specifications, container, false);

        nameEditText=root.findViewById(R.id.editTextName);
        surnameEditText=root.findViewById(R.id.editTextSurname);
        phoneEditText=root.findViewById(R.id.editTextPhone);
        emailEditText=root.findViewById(R.id.editTextEmail);
        adresEditText=root.findViewById(R.id.editTextAddress);
        editButton=root.findViewById(R.id.imageButtonApply);
        deleteButton=root.findViewById(R.id.imageButtonDelete);

        wholesaler= (Wholesaler) getActivity().getIntent().getExtras().getSerializable("wholesaler");

        nameEditText.setText(wholesaler.getName());
        surnameEditText.setText(wholesaler.getSurname());
        phoneEditText.setText(wholesaler.getPhone());
        emailEditText.setText(wholesaler.getEmail());
        adresEditText.setText(wholesaler.getAddress());

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Toptancı Bilgilerini Değistir");
                builder.setMessage(wholesaler.getName() + " "+wholesaler.getSurname()+" İsimli Toptancının bilgilerini güncellemek istediğinize emin misiniz? ");
                builder.setNegativeButton("Hayır", null);
                builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(!nameEditText.getText().toString().isEmpty()&&
                                !surnameEditText.getText().toString().isEmpty() ){
                            wholesaler.setName(String.valueOf(nameEditText.getText()));
                            wholesaler.setSurname(String.valueOf(surnameEditText.getText()));
                            wholesaler.setPhone(String.valueOf(phoneEditText.getText()));
                            wholesaler.setEmail(String.valueOf(emailEditText.getText()));
                            wholesaler.setAddress(String.valueOf(adresEditText.getText()));
                            try {
                                wholesalerRepository.update(wholesaler);
                                Toast.makeText(getActivity().getApplicationContext(), "Toptancı bilgileri Düzenlendi...", Toast.LENGTH_SHORT).show();
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
                builder.setTitle("Toptancı Sil");
                builder.setMessage(wholesaler.getName() + " İsimli Toptancıyı Silmek istediğinize emin misiniz? \n" +
                        " Bu eylem Toptancıya ait satışlarıda siler ve Toptancıya olan borç bilgisinide kaybedersiniz..");
                builder.setNegativeButton("Hayır", null);
                builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        try {
                            wholesalerRepository.remove(wholesaler);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(getActivity().getApplicationContext(), wholesaler.getName() + " silindi.", Toast.LENGTH_LONG).show();
                        getActivity().finish();


                    }
                });
                builder.show();
            }
        });


        return root;
    }

}

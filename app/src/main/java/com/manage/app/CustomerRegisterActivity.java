package com.manage.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CustomerRegisterActivity extends AppCompatActivity {
    TextInputLayout firstNameL,lastNameL,phoneL,emailL;
    Button addCustomer;
    DatabaseReference DBref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_register);
        getWindow().setStatusBarColor(ContextCompat.getColor(CustomerRegisterActivity.this,R.color.yelight));

        /*------------------------------Hooks start---------------------------------------*/
            firstNameL = findViewById(R.id.FirstNameCustomerRegister);
            lastNameL = findViewById(R.id.LastNameCustomerRegister);
            emailL = findViewById(R.id.emailCustomerRegister);
            phoneL = findViewById(R.id.phoneCustomerRegister);
            addCustomer = findViewById(R.id.addCustomerRegister);

        /*------------------------------Hooks end---------------------------------------*/


        /*------------------------------Variables---------------------------------------*/
        DBref= FirebaseDatabase.getInstance().getReference("customers");


        /*------------------------------Variables end---------------------------------------*/


        addCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 if(firstNameL.getEditText().getText().toString().equals("") || lastNameL.getEditText().getText().toString().equals("") || phoneL.getEditText().getText().toString().equals("") || emailL.getEditText().getText().toString().equals("")){
                    Toast.makeText(CustomerRegisterActivity.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }else {

                    String fname = firstNameL.getEditText().getText().toString();
                    String lname = lastNameL.getEditText().getText().toString();
                    String em = emailL.getEditText().getText().toString();
                    String ph = phoneL.getEditText().getText().toString();


                    HashMap<String, String> hmap = new HashMap<>();

                    hmap.put("firstName", fname);
                    hmap.put("lastName", lname);
                    hmap.put("email", em);
                    hmap.put("phone", ph);
                    hmap.put("bookingStatus","false");
                    /*hmap.put("serviceCount","0");*/



                    DatabaseReference DBr = FirebaseDatabase.getInstance().getReference("customers");
                    DBr.child(ph).setValue(hmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){
                                Toast.makeText(CustomerRegisterActivity.this, "Customer Added", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(CustomerRegisterActivity.this,CustomerDisplayActivity.class).putExtra("phone",ph));
                                finish();
                            }

                           }
                    });


                }


            }
        });




    }
}
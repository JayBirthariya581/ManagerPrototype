package com.manage.app;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;

import android.icu.util.Calendar;
import android.os.Build;
import android.widget.DatePicker;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class CustomerServiceActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    TextInputLayout serviceDateL, serviceTimeL, serviceVehicleNoL, serviceAddressL;
    String phone;
    Button addService;
    Integer serviceCount;
    String longitude, latitude;
    String serviceId;
    long maxiID=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_service);
        getWindow().setStatusBarColor(ContextCompat.getColor(CustomerServiceActivity.this, R.color.yelight));


        /*------------------------------Hooks start---------------------------------------*/
        serviceDateL = findViewById(R.id.serviceDateL);
        serviceTimeL = findViewById(R.id.serviceTimeL);
        addService = findViewById(R.id.addService);
        serviceVehicleNoL = findViewById(R.id.serviceVehicleNoL);
        serviceAddressL = findViewById(R.id.serviceAddressL);





        /*------------------------------Hooks end---------------------------------------*/

        /*------------------------------Variables---------------------------------------*/
        phone = getIntent().getStringExtra("phone");

        /*------------------------------Variables end---------------------------------------*/

        DatabaseReference DBref = FirebaseDatabase.getInstance().getReference("customers").child(phone).child("services");
        DBref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    maxiID = snapshot.getChildrenCount();
                }

            }


            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


        ActivityResultLauncher<Intent> lau = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode()== Activity.RESULT_OK){
                    Intent data= result.getData();

                    latitude = data.getStringExtra("Lat");
                    longitude = data.getStringExtra("Lng");
                    serviceAddressL.getEditText().setText(data.getStringExtra("address"));

                }
            }
        });






        serviceAddressL.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i = new Intent(CustomerServiceActivity.this, PlacePickerActivity.class);
                lau.launch(i);

            }
        });





        addService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    createBooking();
                    finish();

            }
        });


        serviceDateL.getEditText().setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        serviceTimeL.getEditText().setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                showTimePickerDialog();
            }
        });




    }



    private void createBooking() {
        String date = serviceDateL.getEditText().getText().toString();
        String time = serviceTimeL.getEditText().getText().toString();
        String vehicleNo = serviceVehicleNoL.getEditText().getText().toString();
        String address = serviceAddressL.getEditText().getText().toString();




        DatabaseReference DBref2 = FirebaseDatabase.getInstance().getReference("customers").child(phone);


        AddressHelper addressHelper = new AddressHelper();

        addressHelper.setTxt(address);
        addressHelper.setLat(latitude);
        addressHelper.setLng(longitude);


        serviceId= DBref2.child("services").push().getKey();
        Service service = new Service(phone,date,vehicleNo,addressHelper,time,"On Hold",serviceId);
        DBref2.child("services").child(serviceId).setValue(service).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override

            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(CustomerServiceActivity.this, "Service Added Successfully", Toast.LENGTH_SHORT).show();

                    DBref2.child("bookingStatus").setValue("true");





                    DatabaseReference TB_DB = FirebaseDatabase.getInstance().getReference("Bookings_on_hold").child(phone);


                    HashMap<String,String> m = new HashMap<>();
                    m.put("serviceID",serviceId);
                    m.put("status","On Hold");
                    m.put("mechanic","No Mechanic");
                    TB_DB.child(serviceId).setValue(m);

                    finish();


                }
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = dayOfMonth +"/"+ month + "/"  + year;

        serviceDateL.getEditText().setText(date);

    }




    @RequiresApi(api = Build.VERSION_CODES.N)
    public void showTimePickerDialog(){
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, this, mHour, mMinute, false);
        timePickerDialog.show();

    }


    @Override
    public void onTimeSet(TimePicker timePicker, int hr, int min) {

        if((hr-12)>0){
            serviceTimeL.getEditText().setText((hr-12) + ":" + min);
        }else{
            serviceTimeL.getEditText().setText(hr + ":" + min);
        }


    }
}
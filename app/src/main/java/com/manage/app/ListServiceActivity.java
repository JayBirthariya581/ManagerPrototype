package com.manage.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ListServiceActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ServiceAdapter adapter;
    private ArrayList<ModelService> modelServiceList;
    DatabaseReference DBr ;
    SwipeRefreshLayout srl;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_service);
        getWindow().setStatusBarColor(ContextCompat.getColor(ListServiceActivity.this,R.color.yelight));
        srl=findViewById(R.id.swiperefresh);
        progressBar = findViewById(R.id.pro_list_sv);


        /*------------------------------Variables---------------------------------------*/
        String phone = getIntent().getStringExtra("phone");
        DBr = FirebaseDatabase.getInstance().getReference("customers").child(phone).child("services");

        /*------------------------------Variables end---------------------------------------*/

        recyclerView=findViewById(R.id.recyclerViewListService);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ListServiceActivity.this));


        modelServiceList = new ArrayList<>();
        adapter = new ServiceAdapter(modelServiceList,ListServiceActivity.this);

        recyclerView.setAdapter(adapter);




        makelist();

        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                makelist();
                srl.setRefreshing(false);
            }
        });



    }

    private void makelist() {
        progressBar.setVisibility(View.VISIBLE);
        modelServiceList.clear();
        DBr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                modelServiceList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ModelService modelCustomer =dataSnapshot.getValue(ModelService.class);
                    modelServiceList.add(modelCustomer);
                }
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NotNull DatabaseError error) {

            }
        });
    }
}
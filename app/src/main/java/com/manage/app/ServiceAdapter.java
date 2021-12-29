package com.manage.app;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.MyViewHolder> {


    ArrayList<ModelService> mlist;
    Context context;

    public ServiceAdapter(ArrayList<ModelService> mlist, Context context) {
        this.mlist = mlist;
        this.context = context;
    }


    @NotNull
    @Override
    public ServiceAdapter.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.card_service, parent, false);

        return new ServiceAdapter.MyViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NotNull ServiceAdapter.MyViewHolder holder, int position) {
        ModelService m = mlist.get(position);
        holder.date.setText(m.getDate());
        holder.time.setText(m.getTime());
        holder.vehicleNo.setText(m.getVehicle_no());
        holder.address.setText(m.getAddress().getTxt());

        holder.getPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, GenerateImageActivity.class);

                i.putExtra("phone", m.getPhone());
                i.putExtra("serviceID", m.getServiceID());


                context.startActivity(i);

            }
        });


        holder.address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("LatLng", m.address.getLat() + "," + m.address.getLng());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(context, "LatLng copied to clip board", Toast.LENGTH_SHORT).show();


            }
        });

        holder.seeImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ServiceImagesActivity.class);

                intent.putExtra("phone", m.getPhone());
                intent.putExtra("serviceID", m.getServiceID());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView date, time, vehicleNo, address;
        Button seeImages, getPdf;


        public MyViewHolder(@NotNull View itemView) {
            super(itemView);

            //Hooks

            date = itemView.findViewById(R.id.serviceCard_date);
            time = itemView.findViewById(R.id.serviceCard_time);
            vehicleNo = itemView.findViewById(R.id.serviceCard_vehicleNo);
            address = itemView.findViewById(R.id.serviceCard_Address);
            seeImages = itemView.findViewById(R.id.cardMechanic_seeImages_btn);
            getPdf = itemView.findViewById(R.id.getPdf);


        }
    }


}

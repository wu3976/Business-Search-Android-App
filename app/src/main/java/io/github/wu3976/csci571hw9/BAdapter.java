package io.github.wu3976.csci571hw9;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BAdapter extends RecyclerView.Adapter<BAdapter.MyViewHolder> {
    Context context;
    ArrayList<BusinessRowModel> bus_models;
    OnItemClickListener click_listener;
    public BAdapter(Context context, ArrayList<BusinessRowModel> bus_models,
                    OnItemClickListener listener) {
        this.context = context;
        this.bus_models = bus_models;
        this.click_listener = listener;
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView serial_num_view, name_view, rating_view, distance_view;
        private ImageView bus_img_view;
        private RelativeLayout clickable_layout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            serial_num_view = itemView.findViewById(R.id.serial_num);
            bus_img_view = itemView.findViewById(R.id.bus_img);
            name_view = itemView.findViewById(R.id.name);
            rating_view = itemView.findViewById(R.id.rating);
            distance_view = itemView.findViewById(R.id.distance);
            clickable_layout = itemView.findViewById(R.id.row_rela);
        }
    }
    @NonNull
    @Override
    public BAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.business_row, parent, false);

        return new BAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BAdapter.MyViewHolder holder, int position) {
        holder.serial_num_view.setText(bus_models.get(position).serialnum);
        Picasso.get().load(bus_models.get(position).img_src).into(holder.bus_img_view);
        holder.name_view.setText(bus_models.get(position).name);
        holder.rating_view.setText(bus_models.get(position).rating);
        holder.distance_view.setText(bus_models.get(position).distance);
        holder.clickable_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click_listener.OnItemClick(bus_models.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return bus_models.size();
    }
}

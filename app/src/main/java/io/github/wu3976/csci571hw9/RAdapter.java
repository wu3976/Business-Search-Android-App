package io.github.wu3976.csci571hw9;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RAdapter extends RecyclerView.Adapter<RAdapter.MyViewHolder> {
    Context context;
    ArrayList<ReservationRowModel> row_models;
    OnItemTouchListener touch_listener;

    public RAdapter(Context context, ArrayList<ReservationRowModel> row_models,
                   OnItemTouchListener touch_listener) {
        this.context = context;
        this.row_models = row_models;
        this.touch_listener = touch_listener;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.reservation_row, parent, false);
        return new RAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.r_serial_num.setText(row_models.get(position).serial_num);
        holder.r_name.setText(row_models.get(position).name);
        holder.r_time.setText(row_models.get(position).time);
        holder.r_date.setText(row_models.get(position).date);
        holder.r_email.setText(row_models.get(position).email);
        holder.swapper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                touch_listener.OnItemTouch(position, row_models.get(position), motionEvent);
                return false;
            }

        });
    }

    @Override
    public int getItemCount() { return row_models.size(); }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView r_serial_num, r_name, r_date, r_time, r_email;
        private ConstraintLayout swapper;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            r_serial_num = itemView.findViewById(R.id.r_serial_num);
            r_name = itemView.findViewById(R.id.r_name);
            r_date = itemView.findViewById(R.id.r_date);
            r_time = itemView.findViewById(R.id.r_time);
            r_email = itemView.findViewById(R.id.r_email);
            swapper = itemView.findViewById(R.id.swapper);
        }
    }
}

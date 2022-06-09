package com.example.scannerqrapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderListChildAdapter extends RecyclerView.Adapter<OrderListChildAdapter.ViewHolder>{

    private List<ConfiguraitonNestedData> confNestedData;
    private Context context;

    public OrderListChildAdapter(List<ConfiguraitonNestedData> confNestedData, Context context) {
        this.confNestedData = confNestedData;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recyclerview_childrow, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ConfiguraitonNestedData item = confNestedData.get(position);
        holder.parentNameTextView.setText(item.getParentName());
        holder.valueNameTextView.setText(item.getValueName());
    }

    @Override
    public int getItemCount() {
        return confNestedData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView parentNameTextView, valueNameTextView;

        ViewHolder(View itemView){
            super(itemView);

            parentNameTextView = itemView.findViewById(R.id.parentNameTextView);
            valueNameTextView = itemView.findViewById(R.id.valueNameTextView);
        }
    }
}

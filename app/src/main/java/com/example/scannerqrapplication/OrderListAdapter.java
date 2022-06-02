package com.example.scannerqrapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder>{
    private List<OrdersData> parsingData;
    private LayoutInflater myInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    OrderListAdapter(Context context, List<OrdersData> data) {
        this.myInflater = LayoutInflater.from(context);
        this.parsingData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        String animal = mData.get(position);
//        holder.elementTextView.setText(animal);
        OrdersData ordersDataAdapter = parsingData.get(position);
        holder.productNameTextView.setText(ordersDataAdapter.getProductName());
        holder.statusTextView.setText(ordersDataAdapter.getStatus());
        holder.productionOrderNumber.setText(ordersDataAdapter.getProductionOrderNumber());
        holder.completionTerm.setText(ordersDataAdapter.getCompletionTerm());
        holder.operationName.setText(ordersDataAdapter.getOperationName());
        holder.remainingCount.setText(ordersDataAdapter.getRemainingCount());
        holder.totalCount.setText(ordersDataAdapter.getTotalCount());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return parsingData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView productNameTextView;
        TextView statusTextView;
        TextView productionOrderNumber;
        TextView completionTerm;
        TextView operationName;
        TextView remainingCount;
        TextView totalCount;

        ViewHolder(View itemView) {
            super(itemView);
            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            productionOrderNumber = itemView.findViewById(R.id.productionOrderNumberTextView);
            completionTerm = itemView.findViewById(R.id.completionTerm);
            operationName = itemView.findViewById(R.id.operationNameTextView);
            remainingCount = itemView.findViewById(R.id.remainingCountTextView);
            totalCount = itemView.findViewById(R.id.totalCountTextView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return String.valueOf(parsingData.get(id));
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}


/*

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<List_Data>list_data;
    private Context context;

    public MyAdapter(List<List_Data> list_data, Context context) {
        this.list_data = list_data;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_data,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        List_Data listData=list_data.get(position);

        Picasso.with(context)
                .load(listData
                        .getImage_url())
                .into(holder.img);

        holder.txtname.setText(listData.getName());

    }

    @Override
    public int getItemCount() {
        return list_data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView img;
        private TextView txtname;
        public ViewHolder(View itemView) {
            super(itemView);
            img=(ImageView)itemView.findViewById(R.id.image_view);
            txtname=(TextView)itemView.findViewById(R.id.text_name);
        }
    }
}

*/





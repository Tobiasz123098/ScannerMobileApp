package com.example.scannerqrapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.puretech.scanner.api.definition.Api;
import pl.puretech.scanner.api.response.OperationDto;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {
    private List<ExtendedOperationDto> parsingData;
    private LayoutInflater myInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    OrderListAdapter(Context context, List<ExtendedOperationDto> data) {
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
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ExtendedOperationDto extendedOperationDto = parsingData.get(position);

        //cutting off minutes and seconds of date
        String date = extendedOperationDto.getProductionPlanDateTime();
        String parsedDate[] = date.split(" ");
        String finalDateResult = parsedDate[0];

        OrderListChildAdapter childAdapter = new OrderListChildAdapter(extendedOperationDto.getConfiguration(), myInflater.getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(myInflater.getContext());
        holder.child_rv.setLayoutManager(layoutManager);
        holder.child_rv.setAdapter(childAdapter);

        holder.productNameTextView.setText(extendedOperationDto.getProductName());
        holder.statusTextView.setText(extendedOperationDto.getStatus());
        holder.productionOrderNumber.setText(extendedOperationDto.getProductionOrderNumber());
        holder.productionPlanDateTime.setText(finalDateResult);
        holder.operationName.setText(extendedOperationDto.getOperationName());
        holder.remainingCount.setText(String.valueOf(extendedOperationDto.getRemainingCount()));
        holder.totalCount.setText(String.valueOf(extendedOperationDto.getTotalCount()));

        holder.productNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                extendedOperationDto.setExpanded(!extendedOperationDto.isExpanded());
                notifyItemChanged(position);
            }
        });

        boolean isExpandedL = parsingData.get(position).isExpanded();
        holder.expandableLayout.setVisibility(isExpandedL ? View.VISIBLE : View.GONE);

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return parsingData.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView productNameTextView;
        TextView statusTextView;
        TextView productionOrderNumber;
        TextView productionPlanDateTime;
        TextView operationName;
        TextView remainingCount;
        TextView totalCount;
        RecyclerView child_rv;
        ConstraintLayout expandableLayout;

        ViewHolder(View itemView) {
            super(itemView);
            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            productionOrderNumber = itemView.findViewById(R.id.productionOrderNumberTextView);
            productionPlanDateTime = itemView.findViewById(R.id.productionPlanDateTime);
            operationName = itemView.findViewById(R.id.operationNameTextView);
            remainingCount = itemView.findViewById(R.id.remainingCountTextView);
            totalCount = itemView.findViewById(R.id.totalCountTextView);
            child_rv = itemView.findViewById(R.id.child_rv);
            expandableLayout = itemView.findViewById(R.id.expandableLayout);
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





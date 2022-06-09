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

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {
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
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        OrdersData ordersDataAdapter = parsingData.get(position);

        //cutting off minutes and seconds of date
        String date = ordersDataAdapter.getProductionPlanDateTime();
        String parsedDate[] = date.split(" ");
        String finalDateResult = parsedDate[0];

        List<ConfiguraitonNestedData> configuraitonList = new ArrayList<>();
        holder.child_rv.setHasFixedSize(true);
        OrderListChildAdapter childAdapter = new OrderListChildAdapter(configuraitonList, myInflater.getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(myInflater.getContext());
        holder.child_rv.setLayoutManager(layoutManager);
        holder.child_rv.setAdapter(childAdapter);
        childAdapter.notifyDataSetChanged();


        UnsafeOkHttpClient.nuke();
        RequestQueue listQueue = Volley.newRequestQueue(myInflater.getContext());

        String url = "https://192.168.42.182:8443" + Api.Service.PREFIX + "/in_queue?station_code=" + StateHolder.INSTANCE.getStationCode() + "&page=1" + "&size=10";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("content");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        JSONArray nestedArray = object.getJSONArray("configuration");
                        for (int j = 0; j < nestedArray.length(); j++) {
                            JSONObject resultObject = nestedArray.getJSONObject(j);
                            ConfiguraitonNestedData ordersData = new ConfiguraitonNestedData(resultObject.getString("parentName"), resultObject.getString("valueName"));
                            configuraitonList.add(ordersData);
                            childAdapter.notifyDataSetChanged();
                        }
                    }

//                    holder.child_rv.setAdapter(new OrderListChildAdapter(configuraitonList, myInflater.getContext()));
//                    childAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(myInflater.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = new String(response.data, StandardCharsets.UTF_8);
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-Auth-Token", "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqa293YWwifQ.fG6fPXANjS3aqd_gslMh57AE56rakPiqvEXyqEO9zUqys2ArpuVziJvPZ1rpWpezdiD4hXgy5MTuJQhKlM6Dhg");
                return params;
            }
        };

        listQueue.add(stringRequest);

        holder.productNameTextView.setText(ordersDataAdapter.getProductName());
        holder.statusTextView.setText(ordersDataAdapter.getStatus());
        holder.productionOrderNumber.setText(ordersDataAdapter.getProductionOrderNumber());
        holder.productionPlanDateTime.setText(finalDateResult);
        holder.operationName.setText(ordersDataAdapter.getOperationName());
        holder.remainingCount.setText(ordersDataAdapter.getRemainingCount());
        holder.totalCount.setText(ordersDataAdapter.getTotalCount());

        holder.productNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ordersDataAdapter.setExpanded(!ordersDataAdapter.isExpanded());
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





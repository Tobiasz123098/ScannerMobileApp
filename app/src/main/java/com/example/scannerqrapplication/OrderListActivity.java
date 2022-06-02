package com.example.scannerqrapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    OrderListAdapter orderListAdapter;
    ArrayList<OrdersData> jsonArrayList;
    String url = "https://192.168.42.182:8443/scanner/api/v2/in_queue?station_code=006&page=1&size=10";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        UnsafeOkHttpClient.nuke();

        jsonArrayList = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderListAdapter = new OrderListAdapter(this, jsonArrayList);
        orderListAdapter.setClickListener(new OrderListAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getApplicationContext(), "You clicked, test", Toast.LENGTH_SHORT).show();
            }
        });

        getData();
    }

    private void getData() {

        UnsafeOkHttpClient.nuke();
        RequestQueue listQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray array=jsonObject.getJSONArray("content");
                    for (int i=0; i<array.length(); i++){
                        JSONObject object = array.getJSONObject(i);
                        OrdersData ordersData = new OrdersData(object.getString("operationName"),object.getString("productName"), object.getString("productionOrderNumber"), object.getString("completionTerm"));
                        jsonArrayList.add(ordersData);
                    }
                    recyclerView.setAdapter(orderListAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-Auth-Token", "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqa293YWwifQ.fG6fPXANjS3aqd_gslMh57AE56rakPiqvEXyqEO9zUqys2ArpuVziJvPZ1rpWpezdiD4hXgy5MTuJQhKlM6Dhg");
                return params;
            }
        };

        listQueue.add(stringRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
package com.example.scannerqrapplication;

import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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
import com.fasterxml.jackson.core.type.TypeReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.puretech.scanner.api.definition.Api;

public class OrderListActivity extends AppCompatActivity{

    RecyclerView recyclerView;
    OrderListAdapter orderListAdapter;
    List<ExtendedOperationDto> operations;
    ArrayList<ExtendedOperationDto> arrayList;

    EndlessRecyclerViewScrollListener scrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        UnsafeOkHttpClient.nuke();

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        operations = new ArrayList<>();
        arrayList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list

                getData(page + 1);
            }
        };

        recyclerView.setOnScrollListener(scrollListener);
        orderListAdapter = new OrderListAdapter(this, arrayList);
        recyclerView.setAdapter(orderListAdapter);

        /*orderListAdapter.setClickListener(new OrderListAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getApplicationContext(), "You clicked, test", Toast.LENGTH_SHORT).show();
            }
        });*/

        getData(1);
    }

    private void getData(int pageNumber) {

        UnsafeOkHttpClient.nuke();
        RequestQueue listQueue = Volley.newRequestQueue(this);

        String url = "https://192.168.42.182:8443" + Api.Service.PREFIX + "/in_queue?station_code=" + StateHolder.INSTANCE.getStationCode() + "&page=" + pageNumber + "&size=10";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {



                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray array=jsonObject.getJSONArray("content");
//                    JSONArray arrayConfiguration = jsonObject.getJSONArray("configuration");

                    arrayList.addAll(JSONUtils.mapToPojoList(array, new TypeReference<List<ExtendedOperationDto>>() {}));
                    operations.addAll(JSONUtils.mapToPojoList(array, new TypeReference<List<ExtendedOperationDto>>() {}));

                    Parcelable recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
                    orderListAdapter.notifyDataSetChanged();
                    recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        })
        {

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
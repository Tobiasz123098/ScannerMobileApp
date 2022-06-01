package com.example.scannerqrapplication;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import pl.puretech.scanner.api.definition.Api;

public class OrderListActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        UnsafeOkHttpClient.nuke();

        RequestQueue listQueue = Volley.newRequestQueue(this);
        String url = "https://192.168.42.182:8443/scanner/api/v2/in_queue?station_code=006&page=1&size=10";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
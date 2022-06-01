package com.example.scannerqrapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.puretech.scanner.api.definition.Api;
import pl.puretech.scanner.api.enums.ProductionType;
import pl.puretech.scanner.api.enums.ScannerMode;
import pl.puretech.scanner.api.enums.ScannerOperationType;
import pl.puretech.scanner.api.request.ScannerPacket;

public class EmployeeScanActivity extends AppCompatActivity implements View.OnClickListener {
/*
try {
        // if your response is { },you can use JSONObject
        JSONObject jsonObject = new JSONObject(response);
        // then find the foods tag in your json data


        JSONArray employeeArray = jsonObject.getJSONArray("employee");
        // loop for the JSONArray
        for (int j = 0; j < employeeArray.length(); j++) {
            // getJSONObject from the index again
            JSONObject jsonObject2 = employeeArray.getJSONObject(j);
            // get value
            String value = jsonObject2.getString("name");
        }

    } catch (JSONException e) {
        e.printStackTrace();
    }*/
    Button employeeScan;
    Button listButton;
    TextView employeeTextView;
    TextView employeeStaticText;
    TextView stationStaticText;
    TextView stationTextView;
    TextView payloadStaticView;
    TextView payloadTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_scan);
        UnsafeOkHttpClient.nuke();

        employeeScan = findViewById(R.id.employeeButton);
        employeeScan.setOnClickListener(this);

        employeeStaticText = findViewById(R.id.employeeStaticText);
        stationStaticText = findViewById(R.id.stationStaticText);
        employeeTextView = findViewById(R.id.employeeTextView);
        stationTextView = findViewById(R.id.stationTextView);
        payloadStaticView = findViewById(R.id.payloadStaticText);
        payloadTextView = findViewById(R.id.payloadTextView);
        listButton = findViewById(R.id.listButton);
        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent listIntent = new Intent(getApplicationContext(), OrderListActivity.class);
                startActivity(listIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    void scan() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setPrompt("Scan a barcode or QR code");
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.initiateScan();
    }

    @Override
    public void onClick(View v) {
        scan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        RequestQueue queue = Volley.newRequestQueue(this);
        String scan_code = IntentIntegrator.parseActivityResult(requestCode, resultCode, data).getContents();

        //regex wyciągający z --> *.0.003.*  --> 003
        final String regex = "\\...(.*)\\.";
        Pattern p = Pattern.compile(regex, Pattern.MULTILINE);
        Matcher matcher = p.matcher(scan_code);
        if (matcher.find()) {
            System.out.println("Full match: " + matcher.group(1));
        }

        //regex wyciągający z --> *.0.003.* --> 0
        final String checkingRegex = "^\\*\\.([0-9])\\.";
        Pattern checking_p = Pattern.compile(checkingRegex, Pattern.MULTILINE);
        Matcher checkingMatcher = checking_p.matcher(scan_code);
        if (checkingMatcher.find()) {
            System.out.println("Full match: " + checkingMatcher.group(1));
        }

        Long timestamp = System.currentTimeMillis();

        if (Integer.valueOf(checkingMatcher.group(1)) == 0) {
            String employee = matcher.group(1);
            StateHolder.INSTANCE.setEmployeeCode(employee);

            String url = "https://192.168.42.182:8443" + Api.Service.PREFIX + "/scan";
            //request body
            ScannerPacket packet_for_employee = new ScannerPacket();
            packet_for_employee.setUuid(UUID.fromString("cb953a41-eccb-48ce-9376-f65ce6c7c910"));
            packet_for_employee.setScannerMode(ScannerMode.valueOf("PRODUCTION"));
            packet_for_employee.setOperationType(ScannerOperationType.valueOf("SCAN"));
            packet_for_employee.setProductionType(ProductionType.valueOf("PIECEWORK"));
            packet_for_employee.setTimestamp(timestamp);
            packet_for_employee.setEmployeeCode(StateHolder.INSTANCE.getEmployeeCode()); //   ^\*\.([0-9])\.([0-9]{3,4})\/.+\*$     -->poprawiony -->   ^\*\.([0-9])\.

            final String mRequestBody = new Gson().toJson(packet_for_employee);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
                Toast.makeText(EmployeeScanActivity.this, response, Toast.LENGTH_LONG).show();

                try {
                    JSONObject json = new JSONObject(response);
                    JSONObject employeeObject =  json.getJSONObject("employee");
                    String name = employeeObject.getString("name");
                    employeeTextView.setText(name);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Toast.makeText(EmployeeScanActivity.this, "Something went wrong:  " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                        return null;
                    }
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
                    params.put("X-Heartbeat-Interval-In-Seconds", "999");
                    params.put("X-Port-Number", "999");
                    params.put("X-Scanner-Session-Id", "e01e7d40-f09e-4135-aecf-35ea1d5294a3");

                    return params;
                }
            };

            queue.add(stringRequest);

        } else if (Integer.valueOf(checkingMatcher.group(1)) == 1) {

            String workStation = matcher.group(1);
            StateHolder.INSTANCE.setStationCode(workStation);

            String url = "https://192.168.42.182:8443" + Api.Service.PREFIX + "/scan";
            ScannerPacket packet_for_employee = new ScannerPacket();
            packet_for_employee.setUuid(UUID.fromString("cb953a41-eccb-48ce-9376-f65ce6c7c910"));
            packet_for_employee.setScannerMode(ScannerMode.valueOf("PRODUCTION"));
            packet_for_employee.setOperationType(ScannerOperationType.valueOf("SCAN"));
            packet_for_employee.setProductionType(ProductionType.valueOf("PIECEWORK"));
            packet_for_employee.setTimestamp(timestamp);
            packet_for_employee.setEmployeeCode(StateHolder.INSTANCE.getEmployeeCode());
            packet_for_employee.setStationCode(StateHolder.INSTANCE.getStationCode()); //problem : co dać w argumencie tej funkcji setStation()

            final String mRequestBody = new Gson().toJson(packet_for_employee);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
                Toast.makeText(EmployeeScanActivity.this, response, Toast.LENGTH_LONG).show();

                try {
                    JSONObject json = new JSONObject(response);
                    JSONObject workStationObject = json.getJSONObject("station");
                    String workStationObjectString = workStationObject.getString("name");
                    stationTextView.setText(workStationObjectString);
                }catch(JSONException e) {
                    e.printStackTrace();
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Toast.makeText(EmployeeScanActivity.this, "Something went wrong: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                        return null;
                    }
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
                    params.put("X-Heartbeat-Interval-In-Seconds", "999");
                    params.put("X-Port-Number", "999");
                    params.put("X-Scanner-Session-Id", "e01e7d40-f09e-4135-aecf-35ea1d5294a3");

                    return params;
                }
            };

            queue.add(stringRequest);

        } else if (Integer.valueOf(checkingMatcher.group(1)) == 2) {

            String payload = matcher.group(1);
            StateHolder.INSTANCE.setPayloadCode(payload);

            String url = "https://192.168.42.182:8443" + Api.Service.PREFIX + "/scan";
            ScannerPacket packet_for_employee = new ScannerPacket();
            packet_for_employee.setUuid(UUID.fromString("cb953a41-eccb-48ce-9376-f65ce6c7c910"));
            packet_for_employee.setScannerMode(ScannerMode.valueOf("PRODUCTION"));
            packet_for_employee.setOperationType(ScannerOperationType.valueOf("SCAN"));
            packet_for_employee.setProductionType(ProductionType.valueOf("PIECEWORK"));
            packet_for_employee.setTimestamp(timestamp);
            packet_for_employee.setEmployeeCode(StateHolder.INSTANCE.getEmployeeCode());
            packet_for_employee.setStationCode(StateHolder.INSTANCE.getStationCode());
            packet_for_employee.setProductionOrderNumber(StateHolder.INSTANCE.getPayloadCode()); // payload jest w formie "*.2.KOD.*" a my chcemy miec z tego sam "KOD"

            final String mRequestBody = new Gson().toJson(packet_for_employee);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
                Toast.makeText(EmployeeScanActivity.this, response, Toast.LENGTH_LONG).show();

                try {
                    JSONObject json = new JSONObject(response);
                    JSONObject payloadObject = json.getJSONObject("operation");
                    String payloadObjectString = payloadObject.getString("productName");
                    payloadTextView.setText(payloadObjectString);
                }catch(JSONException e) {
                    e.printStackTrace();
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Toast.makeText(EmployeeScanActivity.this, "Something went wrong: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                        return null;
                    }
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
                    params.put("X-Heartbeat-Interval-In-Seconds", "999");
                    params.put("X-Port-Number", "999");
                    params.put("X-Scanner-Session-Id", "e01e7d40-f09e-4135-aecf-35ea1d5294a3");

                    return params;
                }
            };

            queue.add(stringRequest);

        } else {
            Toast.makeText(getApplicationContext(), "Incorrect Barcode, Try again!", Toast.LENGTH_SHORT).show();
        }
    }
}

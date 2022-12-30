package com.example.truckoutorder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class SearchPage extends AppCompatActivity {

    static boolean checkISO;
    private String clickedData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);

        TextView lblSearch = (TextView) findViewById(R.id.tvSearch);
        Button btnSearch = (Button) findViewById(R.id.btnSearch);
        TextView etSearch = (TextView) findViewById(R.id.etContainerNo);
        ListView lvSearch = findViewById(R.id.listViewSearch);


        ArrayList<String> shippingList = new ArrayList<String>();
        lblSearch.setInputType(4096);
        Connection connection = SqlServerConnection.connectionClass();

        String loginSelect = "Select id,container_no,Invoice, Check_ISO_Tank from shipping where (shipping_post = 1 and warehouse_Post  = 1 and security_post is null) or (shipping_post = 1 and Check_ISO_Tank = 1 and security_post is null) order by id desc";

        try {
            Statement st = connection.createStatement();
            ResultSet rt = st.executeQuery(loginSelect);
            while(rt.next()){
                String id = rt.getString("id");
                String container = rt.getString("Container_No");
                String invoice = rt.getString("Invoice");
                String allInformation = id + ", " + container + ", " + invoice;
                shippingList.add(allInformation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1,shippingList);
        lvSearch.setAdapter(arrayAdapter);


        lvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                clickedData = (String)adapterView.getItemAtPosition(i);
                clickedData = clickedData.substring(0,5);
                SqlServerConnection.shippingID = Integer.parseInt(clickedData);
                String checkDriver = "select shipping_id, check_ISO_Tank from security right join shipping on shipping.id = security.Shipping_ID where shipping_id = '" + clickedData + "'";

                try {
                    Statement st = connection.createStatement();
                    ResultSet rt1 = st.executeQuery(checkDriver);
                    if(rt1.next()){
//                                            Boolean Security_Check = rt1.getBoolean("Security_Check");
//                                            Boolean driver_Check = rt1.getBoolean("driver_check");
//                        Toast.makeText(SearchPage.this,"Found",Toast.LENGTH_LONG).show();
                        checkISO = rt1.getBoolean("check_ISO_Tank");
                        startActivity(new Intent(SearchPage.this,NetCargoCheck.class));
                    }
                    else{
//                        Toast.makeText(SearchPage.this,"Found",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(SearchPage.this,DriverCheck.class));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                TableLayout table = findViewById(R.id.tableSearch);
                shippingList.clear();
                ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, shippingList);
                lvSearch.setAdapter(arrayAdapter);
//
                try {
                    if (connection != null) {
                        String loginSelect = "Select id,container_no,Invoice, Check_ISO_Tank from shipping where container_no = '" + etSearch.getText().toString() + "' and security_post is null";
//                        ArrayList<ArrayList<String>> shippingList = new ArrayList<ArrayList<String>>();
//                        int i = 0;
//                        while(rt.next()){
//                            String id = rt.getString("id");
//                            String container = rt.getString("Container_No");
//                            String invoice = rt.getString("Invoice");
//                            shippingList.add(new ArrayList<String>());
//                            shippingList.get(i).add(id);
//                            shippingList.get(i).add(container);
//                            shippingList.get(i).add(invoice);
//                            i++;
                        Statement st = connection.createStatement();
                        ResultSet rt2 = st.executeQuery(loginSelect);
                            while(rt2.next()){
                                String id = rt2.getString("id");
                                String container = rt2.getString("Container_No");
                                String invoice = rt2.getString("Invoice");
                                String allInformation = id + ", " + container + ", " + invoice;
                                shippingList.add(allInformation);
                            }

//                            checkISO = rt.getBoolean("Check_ISO_Tank");
//                            SqlServerConnection.shippingID = Integer.parseInt(clickedData);
//                            lblSearch.setText(String.valueOf(SqlServerConnection.shippingID));
//                            String checkDriver = "Select driver_check,Security_Check from security where Shipping_ID = '" + clickedData + "'";
//
//                            ResultSet rt1 = st.executeQuery(checkDriver);


//                            if(rt1.next()){
//                                Boolean Security_Check = rt1.getBoolean("Security_Check");
//                                Boolean driver_Check = rt1.getBoolean("driver_check");
//                                Toast.makeText(SearchPage.this,"Found",Toast.LENGTH_LONG).show();
////                                if((driver_Check == true) && (Security_Check == true)){
////                                    startActivity(new Intent(SearchPage.this,PostPage.class));
////                                }
////                                else{
//                                    startActivity(new Intent(SearchPage.this,NetCargoCheck.class));
////                                }
//                            }
//                            else{
//                                Toast.makeText(SearchPage.this,"Found",Toast.LENGTH_LONG).show();
//                                startActivity(new Intent(SearchPage.this,DriverCheck.class));
//                            }
                        }
                        else{
                            Toast.makeText(SearchPage.this,"Not Found",Toast.LENGTH_LONG).show();
                        }

//                        System.out.println(shippingList.size());
//                        for(int j = 0; j < shippingList.size();j++){
//
//
//                            TableRow row = new TableRow(SearchPage.this);
//                            for(int k = 0; k< shippingList.get(j).size();k++){
//
//                                TextView list = new TextView(SearchPage.this);
//                                String listContent = shippingList.get(j).get(k);
//                                list.setText(listContent + "     ");
//                                row.addView(list);
//                                row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.MATCH_PARENT
//                                ));
//
//
//                            }
//                            table.addView(row);
//                        }

                    } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });

    }
}

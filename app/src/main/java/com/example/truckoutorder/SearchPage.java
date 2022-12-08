package com.example.truckoutorder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


public class SearchPage extends AppCompatActivity {

    static boolean checkISO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);

        TextView lblSearch = (TextView) findViewById(R.id.tvSearch);
        Button btnSearch = (Button) findViewById(R.id.btnSearch);
        TextView etSearch = (TextView) findViewById(R.id.etContainerNo);


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                TableLayout table = findViewById(R.id.tableSearch);
                Connection connection = SqlServerConnection.connectionClass();
//
                try {
                    if (connection != null) {
                        String loginSelect = "Select id, Check_ISO_Tank from shipping where container_no = '" + etSearch.getText().toString() + "' and warehouse_post = 'YES' and security_post is null";
                        Statement st = connection.createStatement();
                        ResultSet rt = st.executeQuery(loginSelect);


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
                        if(rt.next()){
                            checkISO = rt.getBoolean("Check_ISO_Tank");
                            SqlServerConnection.shippingID = rt.getInt("id");
                            lblSearch.setText(String.valueOf(SqlServerConnection.shippingID));
                            Toast.makeText(SearchPage.this,"Found",Toast.LENGTH_LONG).show();
                            String checkDriver = "Select driver_check from security where Shipping_ID = '" + SqlServerConnection.shippingID + "'";
                            Statement st1 = connection.createStatement();
                            ResultSet rt1 = st.executeQuery(checkDriver);

                            if(rt1.next()){
                                startActivity(new Intent(SearchPage.this, NetCargoCheck.class));
                                if(checkISO){
                                    Toast.makeText(SearchPage.this,"Found",Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(SearchPage.this,ISOTankCheck.class));
                                }
                                else{
                                    Toast.makeText(SearchPage.this,"Found",Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(SearchPage.this,NetCargoCheck.class));
                                }
                            }
                            else{
                                Toast.makeText(SearchPage.this,"Found",Toast.LENGTH_LONG).show();
                                startActivity(new Intent(SearchPage.this,DriverCheck.class));
                            }
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

                    }
                } catch (Exception exception) {
                    Log.e("Error",exception.getMessage());
                }
            }
        });

    }
}

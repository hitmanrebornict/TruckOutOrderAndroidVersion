package com.example.truckoutorder;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class SearchPage extends AppCompatActivity {

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
                Connection connection = connectionClass();
                try {
                    if (connection != null) {
                        String loginSelect = "Select * from shipping where container_no = '" + etSearch.getText().toString() + "' and warehouse_post = 'YES'";
                        Statement st = connection.createStatement();
                        ResultSet rt = st.executeQuery(loginSelect);

                        if(rt.next()){
                            String checkDriver = "Select driver_check from security where container_no = '" + etSearch.getText().toString() + "'";
                            Statement st1 = connection.createStatement();
                            ResultSet rt1 = st.executeQuery(checkDriver);

                            Toast.makeText(SearchPage.this,"Found",Toast.LENGTH_LONG).show();
//                            startActivity(new Intent(LoginPage.this,SearchPage.class));
                        }
                        else{
                            Toast.makeText(SearchPage.this,"Login Failed",Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (Exception exception) {
                    Log.e("Error",exception.getMessage());
                }
            }
        });

    }
    @SuppressLint("NewApi")
    public Connection connectionClass() {
        Connection con = null;
        String ip = "10.10.12.1", port = "1433", username = "too", password = "admin", databasename = "TooSystem";
        StrictMode.ThreadPolicy tp = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(tp);
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            String connectionUrl = "jdbc:jtds:sqlserver://" + ip + ":" + port + ";databasename=" + databasename + ";User=" + username + ";password=" + password + ";";
            con = DriverManager.getConnection(connectionUrl);

        } catch (Exception exception) {
            Log.e("Error", exception.getMessage());
        }
        return con;
    }
}
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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginPage extends AppCompatActivity {

    static String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        TextView lblTruckOutOrder = (TextView) findViewById(R.id.tvTOOSystem);
        TextView username = (TextView) findViewById(R.id.etUsername);
        TextView lblUser = (TextView) findViewById(R.id.tvUsername);
        TextView lblPassword = (TextView) findViewById(R.id.tvPassword);
        TextView password = (TextView) findViewById(R.id.etPassword);
        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connection connection = SqlServerConnection.connectionClass();
                try {
                    if (connection != null) {
                        String loginSelect = "Select username, password from login where username = '" + username.getText().toString() + "' and password = '" + password.getText().toString() + "'";
                        Statement st = connection.createStatement();
                        ResultSet rt = st.executeQuery(loginSelect);

                        if(rt.next()){
                            userName = username.getText().toString();
                            Toast.makeText(LoginPage.this,"Login Success",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(LoginPage.this,SearchPage.class));
                        }
                        else{
                            Toast.makeText(LoginPage.this,"Login Failed",Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (Exception exception) {
                    Log.e("Error",exception.getMessage());
                }
            }
        });
    }


//    @SuppressLint("NewApi")
//    public static Connection connectionClass() {
//        Connection con = null;
//        String ip = "10.10.12.1", port = "1433", username = "too", password = "admin", databasename = "TooSystem";
//        StrictMode.ThreadPolicy tp = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(tp);
//        try {
//            Class.forName("net.sourceforge.jtds.jdbc.Driver");
//            String connectionUrl = "jdbc:jtds:sqlserver://" + ip + ":" + port + ";databasename=" + databasename + ";User=" + username + ";password=" + password + ";";
//            con = DriverManager.getConnection(connectionUrl);
//
//        } catch (Exception exception) {
//            Log.e("Error", exception.getMessage());
//        }
//        return con;
//    }
}
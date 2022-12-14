package com.example.truckoutorder;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;

public class SqlServerConnection {

    public static Integer shippingID;

    @SuppressLint("NewApi")
    public static Connection connectionClass() {
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

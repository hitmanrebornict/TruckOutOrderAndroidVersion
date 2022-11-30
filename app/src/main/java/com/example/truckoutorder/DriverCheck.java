package com.example.truckoutorder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.net.IDN;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.xml.transform.Result;

public class DriverCheck extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_check);

        TextView lblFullName = findViewById(R.id.tvFullName);
        TextView lblPMCode = findViewById(R.id.tvPMCode);
        TextView lblRegistrationPlate = findViewById(R.id.tvRegistrationPlate);
        Button btnCheck = findViewById(R.id.btnDriverCheck);
        Spinner spinnerFullName = findViewById(R.id.spinnerFullName);
        Spinner spinnerPMCode = findViewById(R.id.spinnerPMCode);
        Spinner spinnerRegistrationPlate = findViewById(R.id.spinnerRegistratoinPlate);
        Connection connection = SqlServerConnection.connectionClass();
        try {
            if (connection != null) {
                String DriverSelect = "SELECT Full_Name, PM_Code,PM_Registration_Plate from Driver_Info ";
                Statement st = connection.createStatement();
                ResultSet rt = st.executeQuery(DriverSelect);

                ArrayList<String> fullName = new ArrayList<String>();
                ArrayList<String> PMCode = new ArrayList<String>();
                ArrayList<String> RegistrationPlate = new ArrayList<String>();

                while(rt.next()){
                    String name = rt.getString("full_Name");
                    String code = rt.getString("PM_CODE");
                    String plate = rt.getString("PM_REGISTRATION_PLATE");
                    fullName.add(name);
                    PMCode.add(code);
                    RegistrationPlate.add(plate);
                }

                ArrayAdapter nameForSpinner = new ArrayAdapter(this, android.R.layout.simple_list_item_1,fullName);
                ArrayAdapter codeForSpinner = new ArrayAdapter(this, android.R.layout.simple_list_item_1,PMCode);
                ArrayAdapter plateForSpinner = new ArrayAdapter(this, android.R.layout.simple_list_item_1,RegistrationPlate);

                spinnerFullName.setAdapter(nameForSpinner);
                spinnerPMCode.setAdapter(codeForSpinner);
                spinnerRegistrationPlate.setAdapter((plateForSpinner));

            }
        } catch (Exception exception) {
            Log.e("Error",exception.getMessage());
        }

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Connection connection = SqlServerConnection.connectionClass();
                String DriverCheckSelect = "Select full_name, pm_code, pm_registration_plate from driver_info where full_name = '"+ spinnerFullName.getSelectedItem().toString()+"' and PM_Code = '"+spinnerPMCode.getSelectedItem().toString()+"'and PM_Registration_Plate = '"+spinnerRegistrationPlate.getSelectedItem().toString()+"'";
                try {
                    if (connection!= null){
                        Statement st1 = connection.createStatement();
                        ResultSet rt1 = st1.executeQuery(DriverCheckSelect);

                        if(rt1.next()){
//                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                            Date date = new Date();
                            String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

                            String update = "Insert Into Security (Shipping_ID,Driver_Full_Name,PM_CODE,PM_REGISTRATION_PLATE,DRIVER_CHECK,Update_Time,Update_User) values('"+ SearchPage.shippingID +"','"+ spinnerFullName.getSelectedItem().toString()+"','"+spinnerPMCode.getSelectedItem().toString()+"','"+spinnerRegistrationPlate.getSelectedItem().toString()+"','YES', ? ,'"+LoginPage.userName.toString()+"')";
                            PreparedStatement st2 = connection.prepareStatement(update);
                            st2.setString(1,timeStamp);
                            st2.executeUpdate();


                                Toast.makeText(DriverCheck.this,"Update Complete",Toast.LENGTH_LONG).show();
                                startActivity(new Intent(DriverCheck.this,ContainerCheck.class));

                        }
                        else{
                            Toast.makeText(DriverCheck.this,"Driver Verification Failed",Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (Exception exception) {
                    Log.e("Error",exception.getMessage());
                }




            }
        });
    }
}
package com.example.truckoutorder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.Console;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;

import javax.xml.transform.Result;
import java.text.ParseException;

public class NetCargoCheck extends AppCompatActivity{

    private Boolean checkCargoWeight, checkAllowToPost, checkSecurityCheck;
    private Integer checkNetCargoWeight,netCargoWeight;
    private double checkISOTankWeight;
    private String containerNo,EsSealNo,LinerSealNo,InternalSealNo,TemporarySealNo;
    private double ISOWeightLower,ISOWeightUpper;
    private final String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
    private final String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private String ISOTruckOutDate;

    Connection conn = SqlServerConnection.connectionClass();

    public int compareDate(String a, String b) throws ParseException {
        Date firstDate = format.parse(a);
        Date secondDate = format.parse(b);

        return(firstDate.compareTo(secondDate));
    }

    public int isNullEmpty(String a) {
        if(a == null) return 0;

        if(a.trim().isEmpty()) return 1;

        return -1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_cargo_check);

        EditText tbContainerNo = findViewById(R.id.containerNo);
        TextView tbEsSealNo = findViewById(R.id.esSealNo);
        TextView tbLinerSealNo = findViewById(R.id.linerSealNo);
        TextView tbInternalSealNo = findViewById(R.id.InternalSealNo);
        TextView tbTempSealNo = findViewById(R.id.tempSealNo);
        TextView tbNetCargoWeight = findViewById(R.id.netCargoWeight);
        Button btnCheck = findViewById(R.id.btnContainerCheck);
        TextView tbISOTankWeight = findViewById(R.id.ISOTank);
        Button btnNext = findViewById(R.id.btnNextPage);
        TextView lblSecurityCheck = findViewById(R.id.securityCheckStatus);
        TextView lblPostCheck = findViewById(R.id.allowToPostStatus);
        TextView lblTruckOutNumber = findViewById(R.id.tvTruckOutOrderNumber);

        lblTruckOutNumber.setText("Truck Out Order Number: " + SqlServerConnection.shippingID );

        tbContainerNo.setInputType(4096);
        tbEsSealNo.setInputType(4096);
        tbInternalSealNo.setInputType(4096);
        tbTempSealNo.setInputType(4096);
        tbLinerSealNo.setInputType(4096);


        try {
            conn = SqlServerConnection.connectionClass();
            String securityCheck = "SELECT CONTAINER_NO, Warehouse.ES_SEAL_NO as ES_SEAL_NO, LINER_SEA_NO, INTERNAL_SEAL_NO, TEMPORARY_SEAL_NO, Net_Cargo_Weight,ISO_Truck_Out_Date, ISO_Tank_Weight_Lower, ISO_Tank_Weight_Upper, Security_Check, Allow_To_Post, Security_Check_ISO_Tank_Weight, Cargo_Weight_Check_Value " +
                    "FROM Shipping left join Warehouse " +
                    "ON shipping.id = warehouse.shipping_id " +
                    "left join Security " +
                    "on shipping.id = Security.Shipping_ID " +
                    "Where id = '" + SqlServerConnection.shippingID + "'";
            Statement st = conn.createStatement();
            ResultSet rt = st.executeQuery(securityCheck);

            if(rt.next()){
                containerNo = rt.getString("CONTAINER_NO");
                EsSealNo = rt.getString("ES_SEAL_NO");
                LinerSealNo = rt.getString("Liner_Sea_No");
                InternalSealNo = rt.getString("Internal_Seal_No");
                TemporarySealNo = rt.getString("Temporary_Seal_No");
                netCargoWeight = rt.getInt("Net_Cargo_Weight");
                ISOTruckOutDate = rt.getString("ISO_Truck_Out_Date");
                ISOWeightLower = rt.getDouble("ISO_Tank_Weight_Lower");
                ISOWeightUpper = rt.getDouble("ISO_Tank_Weight_Upper");
                checkAllowToPost = rt.getBoolean("Allow_To_Post");
                checkSecurityCheck = rt.getBoolean("Security_Check");
                Double securityCheckISOTankWeight = rt.getDouble("Security_Check_ISO_Tank_Weight");
                Integer cargoWeightCheckValue = rt.getInt("Cargo_Weight_Check_Value");

                if(checkAllowToPost) lblPostCheck.setText("This Number is Allowed To Be Posted");
                else if(!checkAllowToPost) lblPostCheck.setText("This Number is Not Allow To Post");

                if(checkSecurityCheck) lblSecurityCheck.setText("This Number Was Checked");
                else lblSecurityCheck.setText("This Number Is Not Yet Checked");

                if(SearchPage.checkISO){
                    if(checkSecurityCheck){
                        tbContainerNo.setText(containerNo);
                        tbInternalSealNo.setText(InternalSealNo);
                        tbISOTankWeight.setText(Double.toString(securityCheckISOTankWeight));
                    }
                    else{
                        Log.i("This is ISO","ISO");
                        tbEsSealNo.setEnabled(false);
                        tbEsSealNo.setHint("NULL");
                        tbLinerSealNo.setEnabled(false);
                        tbLinerSealNo.setHint("NULL");
                        tbTempSealNo.setEnabled(false);
                        tbTempSealNo.setHint("NULL");
                        tbNetCargoWeight.setHint("NULL");
                        tbNetCargoWeight.setEnabled(false);
                    }

                }
                else{
                    Log.i("This is not ISO","ISO");
                    if(checkSecurityCheck){
                        tbContainerNo.setText(containerNo);
                        tbInternalSealNo.setText(InternalSealNo);
                        tbEsSealNo.setText(EsSealNo);
                        tbTempSealNo.setText(TemporarySealNo);
                        tbLinerSealNo.setText(LinerSealNo);
                        tbNetCargoWeight.setText(Integer.toString(cargoWeightCheckValue));
//
                    }
                    if (isNullEmpty(containerNo) == 0 || isNullEmpty(containerNo) == 1 ){
                        tbContainerNo.setEnabled(false);
                        tbContainerNo.setHint("NULL");
                    }
                    if (isNullEmpty(EsSealNo) == 0 || isNullEmpty(EsSealNo) == 1){
                        tbEsSealNo.setEnabled(false);
                        tbEsSealNo.setHint("NULL");
                    }
                    if (isNullEmpty(InternalSealNo) == 0 || isNullEmpty(InternalSealNo) == 1){
                        tbInternalSealNo.setEnabled(false);
                        tbInternalSealNo.setHint("NULL");
                    }
                    if (isNullEmpty(TemporarySealNo) == 0 || isNullEmpty(TemporarySealNo) == 1){
                        tbTempSealNo.setEnabled(false);
                        tbTempSealNo.setHint("NULL");
                    }
                    if ( isNullEmpty(LinerSealNo) == 0 || isNullEmpty(LinerSealNo) == 1){
                        tbLinerSealNo.setEnabled(false);
                        tbLinerSealNo.setHint("NULL");
                    }

                    tbISOTankWeight.setHint("NULL");
                    tbISOTankWeight.setEnabled(false);



                }
            }
        } catch (Exception exception) {
            Log.e("Error",exception.getMessage());
            finish();
        }

        btnNext.setOnClickListener(view -> {
            if(checkSecurityCheck) startActivity(new Intent(NetCargoCheck.this, CapturePhoto.class));
            else Toast.makeText(NetCargoCheck.this,"Please Check Before Proceed To Next Page",Toast.LENGTH_LONG).show();
        });


        btnCheck.setOnClickListener(view -> {

            String checkContainerNo = tbContainerNo.getText().toString();
            String checkEsSealNo = tbEsSealNo.getText().toString();
            String checkLinerSealNo = tbLinerSealNo.getText().toString();
            String checkInternalSealNo = tbInternalSealNo.getText().toString();
            String checkTempSealNo = tbTempSealNo.getText().toString();


            if(SearchPage.checkISO){
                try {
//                if (isNullEmpty(tbISOTankWeight.getText().toString()) == -1)
                    checkISOTankWeight = Double.parseDouble(tbISOTankWeight.getText().toString());
                } catch (Exception exception) {
                    Log.e("Error",exception.getMessage());
                    Toast.makeText(NetCargoCheck.this,"Please Check ISO Tank Weight Field",Toast.LENGTH_LONG).show();
                }
            }
            else{
//                try {
//                } catch (Exception exception) {
//                    Log.e("Error",exception.getMessage());
//                    Toast.makeText(NetCargoCheck.this,"Please Check Net Cargo Tank Weight Field",Toast.LENGTH_LONG).show();
//                }
                checkNetCargoWeight = Integer.parseInt(tbNetCargoWeight.getText().toString());
            }



                    if (!checkContainerNo.equals(containerNo)){
                        Toast.makeText(NetCargoCheck.this,
                                "Container No. Checking Failed",Toast.LENGTH_LONG).show();
                        Log.i("cn","hi");
                    }

                    else if ( isNullEmpty(EsSealNo) == -1 && !checkEsSealNo.equals(EsSealNo)){
                        Toast.makeText(NetCargoCheck.this,"ES Seal No. Checking Failed",Toast.LENGTH_LONG).show();
                        Log.i("es","hi");
                    }

                    else if (isNullEmpty(LinerSealNo) == -1 && !checkLinerSealNo.equals(LinerSealNo)){
                        Toast.makeText(NetCargoCheck.this,"Liner Seal No. Checking Failed",Toast.LENGTH_LONG).show();
                        Log.i("ls","hi");
                    }
                    else if (!checkInternalSealNo.equals(InternalSealNo)){
                        Toast.makeText(NetCargoCheck.this,"Internal Seal No. Checking Failed",Toast.LENGTH_LONG).show();
                        Log.i("is","hi");
                    }

                    else if (isNullEmpty(TemporarySealNo) == -1 && !checkTempSealNo.equals(TemporarySealNo)) {
                        Toast.makeText(NetCargoCheck.this, "Temporary Seal No. Checking Failed", Toast.LENGTH_LONG).show();
                        Log.i("ts", "hi");
                    }
                    else{
                        if(SearchPage.checkISO){
                            try {
                                if((compareDate(currentDate,ISOTruckOutDate) == 0 || compareDate(currentDate,ISOTruckOutDate) > 0) && (checkISOTankWeight >= ISOWeightLower) && (checkISOTankWeight <= ISOWeightUpper)){
                                    Log.e("no problem","hi");
                                    checkAllowToPost = true;
                                    Toast.makeText(NetCargoCheck.this,"Checking Complete",Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    checkAllowToPost = false;
                                    Toast.makeText(NetCargoCheck.this,"Checking Failed",Toast.LENGTH_LONG).show();
                                }
                                String ISOSuccessUpdate = "Update Security set Update_Time = ?," +
                                        "Update_User = ?, " +
                                        "Security_Check = ?," +
                                        " Allow_To_Post = ?, " +
                                        "Security_Check_ISO_Tank_Weight = ?, " +
                                        "Security_Check_ISO_Truck_Out_Date = ? " +
                                        "WHERE Shipping_ID = ? ";
                                PreparedStatement st = conn.prepareStatement(ISOSuccessUpdate);
                                st.setString(1, timestamp);
                                st.setString(2,LoginPage.userName);
                                st.setBoolean(3,true);
                                st.setBoolean(4,checkAllowToPost);
                                st.setDouble(5,checkISOTankWeight);
                                st.setString(6,timestamp);
                                st.setInt(7,SqlServerConnection.shippingID);

                                st.executeUpdate();

//                            } catch (ParseException e) {
//                                e.printStackTrace();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        else{
                            if(checkNetCargoWeight < netCargoWeight){
                                Log.i("aa","hi");
                                checkCargoWeight = Boolean.FALSE;
                                checkAllowToPost = Boolean.FALSE;
                                Toast.makeText(NetCargoCheck.this,"Please Fill In Net Cargo Check Value / Only FIll In Number In Net Cargo Field",Toast.LENGTH_LONG).show();
                            }
                            else{
                                Log.i("bb","hi");
                                checkCargoWeight = Boolean.TRUE;
                                checkAllowToPost = Boolean.TRUE;
                                Toast.makeText(NetCargoCheck.this,"Check Complete",Toast.LENGTH_LONG).show();
                            }
                            String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
                            String update = "Update Security set Update_Time = ?, " +
                                    "Update_User = ?, Security_Check = ?, " +
                                    "Cargo_Weight_Check = ?, " +
                                    "Cargo_Weight_Check_Value = ?, " +
                                    "Allow_To_Post = ? " +
                                    "Where Shipping_ID = ?";
                            try {
                                PreparedStatement st2 = conn.prepareStatement(update);
                                st2.setString(1,timeStamp);
                                st2.setString(2,LoginPage.userName);
                                st2.setBoolean(3,true);
                                st2.setBoolean(4,checkCargoWeight);
                                st2.setInt(5, checkNetCargoWeight);
                                st2.setBoolean(6,checkAllowToPost);
                                st2.setString(7,SqlServerConnection.shippingID.toString());
                                st2.executeUpdate();
                            }catch (Exception exception) {

                            }
                        }
                    }
        });
    }
}
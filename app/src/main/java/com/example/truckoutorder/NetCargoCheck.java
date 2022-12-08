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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.xml.transform.Result;

public class NetCargoCheck extends AppCompatActivity {

    private Boolean checkCargoWeight;
    private Boolean checkAllowToPost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_cargo_check);

        EditText tbContainerNo = (EditText) findViewById(R.id.containerNo);
        TextView tbEsSealNo = (TextView) findViewById(R.id.esSealNo);
        TextView tbLinerSealNo = (TextView) findViewById(R.id.linerSealNo);
        TextView tbInternalSealNo = (TextView) findViewById(R.id.InternalSealNo);
        TextView tbTempSealNo = (TextView) findViewById(R.id.tempSealNo);
        TextView tbNetCargoWeight = (TextView) findViewById(R.id.netCargoWeight);
        Button btnCheck = (Button) findViewById(R.id.btnContainerCheck);

        tbContainerNo.setInputType(4096);

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String checkContainerNo = tbContainerNo.getText().toString();
                String checkEsSealNo = tbEsSealNo.getText().toString();
                String checkLinerSealNo = tbLinerSealNo.getText().toString();
                String checkInternalSealNo = tbInternalSealNo.getText().toString();
                String checkTempSealNo = tbTempSealNo.getText().toString();
                Integer checkNetCargoWeight = Integer.parseInt(tbNetCargoWeight.getText().toString());


                try {
                    Connection conn = SqlServerConnection.connectionClass();
                    String securityCheck = "SELECT CONTAINER_NO, Warehouse.ES_SEAL_NO as ES_SEAL_NO, LINER_SEA_NO, INTERNAL_SEAL_NO, TEMPORARY_SEAL_NO, Net_Cargo_Weight from Shipping left join warehouse on shipping.id = warehouse.shipping_id Where id = '" + SqlServerConnection.shippingID + "'";
                    Statement st = conn.createStatement();
                    ResultSet rt = st.executeQuery(securityCheck);

                    if(rt.next()){
                        String containerNo = rt.getString("CONTAINER_NO");
                        String EsSealNo = rt.getString("ES_SEAL_NO");
                        String LinerSealNo = rt.getString("Liner_Sea_No");
                        String InternalSealNo = rt.getString("Internal_Seal_No");
                        String TemporarySealNo = rt.getString("Temporary_Seal_No");
                        Integer netCargoWeight = rt.getInt("Net_Cargo_Weight");

                        if (!checkContainerNo.equals(containerNo)){
                            Toast.makeText(NetCargoCheck.this,"Container No. Checking Failed",Toast.LENGTH_LONG).show();
                            Log.i("cn","hi");
                        }
                        else if (!EsSealNo.trim().isEmpty() & !checkEsSealNo.equals(EsSealNo)){
                            Toast.makeText(NetCargoCheck.this,"ES Seal No. Checking Failed",Toast.LENGTH_LONG).show();
                            Log.i("es","hi");
                        }
                        else if (!LinerSealNo.trim().isEmpty() & !checkLinerSealNo.equals(LinerSealNo)){
                            Toast.makeText(NetCargoCheck.this,"Liner Seal No. Checking Failed",Toast.LENGTH_LONG).show();
                            Log.i("ls","hi");
                        }
                        else if (!checkInternalSealNo.equals(InternalSealNo)){
                            Toast.makeText(NetCargoCheck.this,"Internal Seal No. Checking Failed",Toast.LENGTH_LONG).show();
                            Log.i("is","hi");
                        }
                        else if(!TemporarySealNo.trim().isEmpty() & !checkTempSealNo.equals(TemporarySealNo)){
                            Toast.makeText(NetCargoCheck.this,"Temporary Seal No. Checking Failed",Toast.LENGTH_LONG).show();
                            Log.i("ts","hi");
                        }
                        else if(tbNetCargoWeight.getText().toString().isEmpty()){
                            Toast.makeText(NetCargoCheck.this,"Temporary Seal No. Checking Failed",Toast.LENGTH_LONG).show();
                            Log.i("nc","hi");
                        }
                        else{
                            if(checkNetCargoWeight < netCargoWeight){
                                Log.i("aa","hi");
                                checkCargoWeight = Boolean.FALSE;
                                checkAllowToPost = Boolean.FALSE;
                            }
                            else{
                                Log.i("bb","hi");
                                checkCargoWeight = Boolean.TRUE;
                                checkAllowToPost = Boolean.TRUE;
                            }
                            String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
                            String update = "Update Security set Update_Time = ?, Update_User = ?, Security_Check = ?, Cargo_Weight_Check = ?, Cargo_Weight_Check_Value = ?, Allow_To_Post = ? Where Shipping_ID = ?";
                            PreparedStatement st2 = conn.prepareStatement(update);

                            st2.setString(1,timeStamp);
                            st2.setString(2,LoginPage.userName);
                            st2.setString(3,"YES");
                            st2.setBoolean(4,checkCargoWeight);
                            st2.setInt(5, checkNetCargoWeight);
                            st2.setBoolean(6,checkAllowToPost);
                            st2.setString(7,SqlServerConnection.shippingID.toString());
                            st2.executeUpdate();
                            Toast.makeText(NetCargoCheck.this,"Temporary Seal No. Checking Failed",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(NetCargoCheck.this, CapturePhoto.class));
                            finish();
                        }
                    }




                } catch (Exception exception) {
                    Log.e("Error",exception.getMessage());
                }

            }

        });
    }
}
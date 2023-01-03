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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PostPage extends AppCompatActivity {
    Boolean allowToPost;
    TextView lblAllowToPost, lblCondition;
    Button btnPost;
    Statement st;
    ResultSet rt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_page);

    lblAllowToPost = findViewById(R.id.labelAllowToPost);
    lblCondition = findViewById(R.id.lblCondition);
    btnPost = findViewById(R.id.btnPost);

        Connection conn = SqlServerConnection.connectionClass();
        String selectStatement = "SELECT Allow_To_Post FROM Security WHERE Shipping_ID = '"+SqlServerConnection.shippingID+"'";

        try {
            st = conn.createStatement();
            rt = st.executeQuery(selectStatement);
            if (rt.next()){
                allowToPost = rt.getBoolean("Allow_To_Post");
                if((SearchPage.checkISO) && (allowToPost)){
                    lblAllowToPost.setText("This Number Is Allow To Post");
                }else if ((SearchPage.checkISO) && !(allowToPost)){
                    lblAllowToPost.setText("This Number Is Not Allow To Post");
                    lblCondition.setText("Inform Shipping Admin");
                }else if(!(SearchPage.checkISO) && (allowToPost)){
                    lblAllowToPost.setText("This Number Is Allow To Post");
                    lblCondition.setVisibility(View.INVISIBLE);
                }else if(!(SearchPage.checkISO) && !(allowToPost)){
                    lblAllowToPost.setText("This Number Is Not Allow To Post");
                    lblCondition.setText("Inform Warehouse Admin");
                }


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String update = "Select Allow_To_Post From Security where Shipping_id = '"+SqlServerConnection.shippingID+"'";
                    st = conn.createStatement();
                    rt = st.executeQuery(update);
                    rt.next();
                    allowToPost = rt.getBoolean("Allow_To_Post");

                } catch (SQLException e) {
                    e.printStackTrace();
                    Log.i("hi","hi");
                }

                if(allowToPost){
                    String update = "Update Shipping set Security_Post = ?, Security_Post_Time = ?, Security_Post_User = ? Where id = ? ";
                    try {
                        PreparedStatement st2 = conn.prepareStatement(update);
                        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
                        st2.setBoolean(1,true);
                        st2.setString(2,timeStamp);
                        st2.setString(3,LoginPage.userName);
                        st2.setInt(4,SqlServerConnection.shippingID);
                        st2.executeUpdate();
                        Toast.makeText(PostPage.this,"Post Completed",  Toast.LENGTH_LONG).show();
                        startActivity(new Intent(PostPage.this, SearchPage.class));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(PostPage.this,"This Number Is Not Allow To Post",  Toast.LENGTH_LONG).show();
                }


            }
        });

    }


}
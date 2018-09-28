package dcom.citycab12;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class customer_call extends AppCompatActivity {

    Button btnCancal,btnAccept;
    double lat,lng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_call);


        btnAccept = (Button)findViewById(R.id.btnAccept);
        btnCancal =  (Button)findViewById(R.id.btnDecline);


        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(customer_call.this,DriverTracking.class);
                intent.putExtra("lat",lat);
                intent.putExtra("lng",lng);

                startActivity(intent);
                finish();
            }
        });
    }
}

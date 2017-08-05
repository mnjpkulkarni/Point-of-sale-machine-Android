package com.HarmanInternational.foodie.foodie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PrintActivity extends AppCompatActivity {

    TextView itemId,itemName,itemQuantity,t0talItemPrice;
    String itId,itName;
    int itQuantity;
    Button printBill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);

        itemId=(TextView)findViewById(R.id.item_id);
        itemName=(TextView)findViewById(R.id.item_name);
        itemQuantity=(TextView)findViewById(R.id.item_quantity);
        t0talItemPrice=(TextView)findViewById(R.id.total_item_price);
        printBill=(Button)findViewById(R.id.bill);

        Intent intent=getIntent();
        Bundle extras1=intent.getExtras();
        itId=extras1.getString("ItemId");



        printBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(PrintActivity.this,OrderActivity.class);

        startActivity(intent);
        finish();

    }
}

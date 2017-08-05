package com.HarmanInternational.foodie.foodie;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity  {

    Button qty,add,del,print,settings;
    TextView selection,selection2,selection3,textnum,enternum;
    int digit,quantity;
    long number;
    Bundle saveb;
    String itemId,itemName,price;
    String printdetails="";
    String details="";
    int flag=0;
    int blankflag=0;
    String num="",directadd="";
    LinearLayout numberdisplay,displaymain;
    ArrayList<String> printdetailsnew=new ArrayList<>();
    ArrayList<String> quantitynum=new ArrayList<>();
    ArrayList<String> amount=new ArrayList<>();
    Cursor discounttax;

   static final Uri CONTENT_URL=Uri.parse("content://com.HarmanInternational.foodie.foodie.Foodymenuprovider/details");
    CursorLoader cursorLoader;
    ContentResolver contentResolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        /*Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);*/
     /*   android.app.ActionBar actionBar = getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.BLUE));
        actionBar.show();*/
        saveb=savedInstanceState;
        qty=(Button)findViewById(R.id.qty);
        add=(Button)findViewById(R.id.add);
        del=(Button)findViewById(R.id.del);
        print=(Button)findViewById(R.id.print);
        settings=(Button)findViewById(R.id.Settings);
        numberdisplay=(LinearLayout)findViewById(R.id.numberdisplay);
        displaymain=(LinearLayout)findViewById(R.id.displaymain);

        textnum=(TextView) findViewById(R.id.num);
        enternum=(TextView) findViewById(R.id.enter);

        selection=(TextView) findViewById(R.id.selection1);
        selection2=(TextView) findViewById(R.id.selection2);
        selection3=(TextView) findViewById(R.id.selection3);

        //Get the content resolver
        contentResolver=getContentResolver();
        numberdisplay.setVisibility(View.INVISIBLE);
        displaymain.setVisibility(View.VISIBLE);


        //Get the quantity


        qty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              selectQuantity();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberdisplay.setVisibility(View.INVISIBLE);
                displaymain.setVisibility(View.VISIBLE);
                showandadd();


            }
        });

        del.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       deleteitem();
                                   }
                               });


        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);


            }
        });

        //Print the bill

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            /*    Intent intent=new Intent(OrderActivity.this,PDFActivity.class);
                intent.putExtra("Items",printdetailsnew);
                intent.putExtra("Quantity",quantitynum);
                intent.putExtra("Amount",amount);
                startActivity(intent);*/
            goToBilling();

            }
        });

    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }*/

/*    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_password:
                // User chose the "Settings" item, show the app settings UI...
                Intent intent=new Intent(this.getApplicationContext(),LoginActivity.class);
                startActivity(intent);

                return true;

            case R.id.action_favorite:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }*/


   @Override
    public void onResume(){
        super.onResume();

        Intent i=getIntent();
        int quantity=i.getIntExtra("Quantity",0);

        if(quantity!=0){
            this.selection.setText(details +" : "+quantity);
        }



    }



    public void selectQuantity()
    {
        /*FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        transaction.replace(R.id.select_food, QuantityFragment.newInstance());
        transaction.addToBackStack(null);
        transaction.commit();*/
        flag=1;
        num="";
        number=0;
        enternum.setText("Quantity :");
        textnum.setText("");
        /*getItem(num);
        int updatedPrice=Integer.parseInt(this.price);
        int data= Integer.parseInt(this.num);
        int d=updatedPrice * data;
        this.price=d+"";private void deleteitem() {
    }
        String str="Item:"+ itemName + " -- " +"Price:"+price;
        printdetailsnew.add(str);
        num="";*/

    }



    public String numberPressed(View view)
    {


        displaymain.setVisibility(View.INVISIBLE);
        Button b=(Button)view;
        digit=Integer.parseInt(((Button) view).getText().toString());
//        number=Long.parseLong(selection.getText().toString());
        number=digit;
        //selection.setText(String.valueOf(number));

        num=num + String.valueOf(number);
        if(flag!=1){
            enternum.setText("Item num :");
            numberdisplay.setVisibility(View.VISIBLE);
        directadd=num;

        }else if(directadd.isEmpty() && flag ==1){
            Toast.makeText(this,"Wrong input!! Press item number before quantity, to continue press next and use number pad",Toast.LENGTH_LONG ).show();
            numberdisplay.setVisibility(View.INVISIBLE);
            displaymain.setVisibility(View.VISIBLE);
        }else if (flag ==1){

            enternum.setText("Qunatity :");
            numberdisplay.setVisibility(View.VISIBLE);
        }
        textnum.setText(num);

        Log.d("Num", "numberPressed: "+num);
        return num;


    }

    public void getItem(String num)
    {
        String str="";
        String[] projection = new String[]{"button", "itemname", "price"};
        String selection = "button =?";
        String[] selectionArgs = new String[]{num};
        Cursor cursor=contentResolver.query(CONTENT_URL,projection,selection,selectionArgs,null);

        if(cursor.moveToFirst()){

            do{

                itemId = cursor.getString(cursor.getColumnIndex("button"));
                itemName = cursor.getString(cursor.getColumnIndex("itemname"));
                price = cursor.getString(cursor.getColumnIndex("price"));
                //details = details + "Item:"+ itemName + " -- " +"Price:"+price + "\n";
                Log.d("Item ID", "getItem: "+itemId);
                Log.d("Item Name", "getItem: "+itemName);
                Log.d("Price", "getItem: "+price);

            }while (cursor.moveToNext());


        }else {
            itemId = "";
            itemName = "";
            price ="";
            blankflag=1;
        }
        //this.selection.setText(details);
       /* if(flag!=1) {
            printdetailsnew.add(str);
        }*/
        number=0;

    }

    public void goToBilling()
    {
        checkbusiness();
        if(discounttax!=null) {
            Intent intent = new Intent(OrderActivity.this, PDFActivity.class);
            intent.putExtra("Items", printdetailsnew);
            intent.putExtra("Quantity", quantitynum);
            intent.putExtra("Amount", amount);

        /*Bundle extras=new Bundle();
        extras.putStringArrayList("ItemName",printdetailsnew);
        extras.putStringArrayList("Quantity",quantitynum);
        extras.putStringArrayList("amount",amount);*/
            // extras.putFloat("sum",sum);
            // intent.putExtras(extras);
            float sum=printitem();
            intent.putExtra("Sum", sum);
            startActivity(intent);
            Log.d("Price", "goToBilling: " + price);
        }else{
            Toast.makeText(this,"Submit 'Discount' & 'Tax' under BusinessInfo in 'Settings' and try again",Toast.LENGTH_LONG).show();
        }
    }


 //   @Override
/*    public void onDataPass(int data) {
        int updatedPrice=Integer.parseInt(this.price);
        int d=updatedPrice * data;
        this.price=d+"";
       // collectdetails();
        //onCreate(saveb);
       // setContentView(R.layout.activity_order);
        collectdetails();
      //  onResume();


    }*/

  /*  public void collectdetails(){
        printdetails = printdetails+ "Item:"+ itemName + " -- " +"Price:"+price + "\n";

        this.selection.setText(printdetails);
       // printdetails.add(details);


    }*/

    private void showandadd() {
        /*String num="";
        num=num+String.valueOf(number);*/
        String string1="";
        String string2="";
        String string3="";
        if(num==""){
            Toast.makeText(this,"Wrong input!! try again",Toast.LENGTH_LONG).show();
        }
        if(flag==1) {
            getItem(directadd);
            if(blankflag!=1) {
                int updatedPrice = Integer.parseInt(this.price);
                int data = Integer.parseInt(this.num);
                if(data==0){
                    Toast.makeText(this,"Wrong input!! insert different quantity number",Toast.LENGTH_LONG).show();
                }else {
                    int d = updatedPrice * data;
                    this.price = d + "";

                    // String str = itemName + "      \t" + num + " No" + "      \t" + "Rs " + price;
                    printdetailsnew.add(itemName);
                    quantitynum.add(num);
                    amount.add(price);
                    num = "";
                    directadd = "";
                    flag = 0;
                    blankflag = 0;
                }
            }
            num = "";
            directadd = "";
            flag = 0;
            blankflag = 0;

        }else {
            getItem(directadd);
            if(blankflag!=1) {
                //String str = itemName + "      \t" + num + " No" + "      \t" + "Rs " + price;
                printdetailsnew.add(itemName);
                quantitynum.add("1");
                amount.add(price);
                num = "";
            }
            directadd="";
            blankflag=0;
        }
        for(int i=0;i<printdetailsnew.size();i++){
            string1=string1 + printdetailsnew.get(i)+"\n";
            string2=string2+quantitynum.get(i)+"\t"+"No."+"\n";
            string3=string3+amount.get(i)+"\n";
        }

        this.selection.setText(string1);
        this.selection2.setText(string2);
        this.selection3.setText(string3);
        num="";
    }

    private void deleteitem() {
        String string1="";
        String string2="";
        String string3="";

        if(printdetailsnew.size()!=0) {
            printdetailsnew.remove(printdetailsnew.size() - 1);
            quantitynum.remove(quantitynum.size() - 1);
            amount.remove(amount.size() - 1);
            for (int i = 0; i < printdetailsnew.size(); i++) {
                string1 = string1 + printdetailsnew.get(i) + "\n";
                string2 = string2 + quantitynum.get(i)+"\t"+"No." + "\n";
                string3 = string3 + amount.get(i) + "\n";
            }

            this.selection.setText(string1);
            this.selection2.setText(string2);
            this.selection3.setText(string3);
        }
    }

    public float printitem(){
        float sum=0f;
        float discount=0f;
        float gst=0f;
        Foodymenuprovider.mydb mydb=new Foodymenuprovider.mydb(getApplicationContext());
        Cursor cursor=mydb.getbusinessinfo();
            cursor.moveToFirst();
            if (cursor != null) {
                do {
                    if (cursor.getColumnName(cursor.getColumnIndex("discount")).equalsIgnoreCase("discount")) {
                        discount = Float.parseFloat(cursor.getString(cursor.getColumnIndex("discount")));

                    }
                    if (cursor.getColumnName(cursor.getColumnIndex("tax")).equalsIgnoreCase("tax")) {
                        gst = Float.parseFloat(cursor.getString(cursor.getColumnIndex("tax")));

                    }


                } while (cursor.moveToNext());
            }



        for(int i=0;i<amount.size();i++){
            int amt=Integer.parseInt(amount.get(i));
            sum=sum+amt;
        }

        sum=sum-((discount/100)*sum);
        sum=sum+((gst/100)*sum);

        Toast.makeText(this,"final sum"+sum,Toast.LENGTH_LONG).show();


        return sum;
    }

    private void checkbusiness(){
        Foodymenuprovider.mydb mydb=new Foodymenuprovider.mydb(getApplicationContext());
        discounttax=mydb.getbusinessinfo();
    }

}

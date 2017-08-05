package com.HarmanInternational.foodie.foodie;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class PasswordActivity extends AppCompatActivity {

    EditText button;
    EditText item;
    EditText price;
    Button pwdrst,items,dayreport,businessinfo,createAccount,submitbusiness;
    ScrollView mainlinear,scrollView;
    LinearLayout savebuttonlayout;
    FrameLayout pwdframe,businessframe;
    EditText createPassword,confirmPassword;
    ContentResolver contentResolver;
    String PASSWORD="";
    String crPassword=null,coPassword=null;
    ArrayList<CharSequence> Buttonlist = new ArrayList<>();
    ArrayList<CharSequence> Pricelist = new ArrayList<>();
    ArrayList<CharSequence> Itemlist = new ArrayList<>();
    ArrayList<Button> Buttonnum=new ArrayList<>();

    int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        button=(EditText)findViewById(R.id.editTextnew);
        item=(EditText)findViewById(R.id.editText);
        price=(EditText)findViewById(R.id.editText10);
        pwdrst=(Button)findViewById(R.id.pwdrst);
        items=(Button)findViewById(R.id.show);
      //  dayreport=(Button)findViewById(R.id.dayreport);
        businessinfo=(Button)findViewById(R.id.businessinfo);
        mainlinear=(ScrollView)findViewById(R.id.mainlinear);
        pwdframe=(FrameLayout)findViewById(R.id.passwordframe);
        businessframe=(FrameLayout)findViewById(R.id.Businessframe);
        createPassword=(EditText)findViewById(R.id.create_password1);
        confirmPassword=(EditText)findViewById(R.id.confirm_password1);
        createAccount=(Button)findViewById(R.id.create_account1);
        scrollView=(ScrollView)findViewById(R.id.scroll);
        savebuttonlayout=(LinearLayout)findViewById(R.id.savebuttonlayout);
        submitbusiness=(Button)findViewById(R.id.business_submit);


        pwdframe.setVisibility(View.GONE);
        mainlinear.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);
        businessframe.setVisibility(View.GONE);

        items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    itemlists();


            }
        });

        businessinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addbusinessinfo();


            }
        });


        pwdrst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout d = (LinearLayout) findViewById(R.id.addnewbutton);
                d.setVisibility(View.GONE);
                businessframe.setVisibility(View.GONE);
                mainlinear.setVisibility(View.GONE);
                pwdframe.setVisibility(View.VISIBLE);
                savebuttonlayout.setVisibility(View.GONE);
                scrollView.setVisibility(View.GONE);
                retainPassword();

                createAccount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        crPassword=createPassword.getText().toString();
                        coPassword=confirmPassword.getText().toString();

                        if(crPassword.equals(PASSWORD))
                        {
                            PASSWORD=coPassword;

                            ContentValues contentValues=new ContentValues();
                            contentValues.put(DBAdapter.CREATE_PASSWORD,PASSWORD);
                            contentValues.put(DBAdapter.CONFIRM_PASSWORD,PASSWORD);
                            getContentResolver().insert(DBAdapter.CONTENT_URL, contentValues);
                            Toast.makeText(getApplicationContext(),"Password changed",Toast.LENGTH_LONG).show();

                        }
                    }
                });

            }
        });



    }

    public void retainPassword(){
        contentResolver=getContentResolver();

        String[] projection = new String[]{"password1", "password2"};
        Cursor cursor = contentResolver.query(DBAdapter.CONTENT_URL, projection, null, null, null);
        if(cursor.moveToFirst()){

            do{

                PASSWORD = cursor.getString(cursor.getColumnIndex("password1"));

            }while (cursor.moveToNext());

        }

    }

    public void getContacts() {
        int num=0;



        // Projection contains the columns we want
        String[] projection = new String[]{"button", "itemname", "price"};

        // Pass the URL, projection and I'll cover the other options below
        Cursor cursor = getContentResolver().query(Foodymenuprovider.Content_URL, projection, null, null, null);

        //String details = "";
        if(cursor.getCount()==0){
            Toast.makeText(this,"No items found",Toast.LENGTH_LONG).show();
        }
        // Cycle through and display every row of data
        if (cursor.moveToFirst()) {

            do {
                String details = "";

                String button = cursor.getString(cursor.getColumnIndex("button"));
                String itemname = cursor.getString(cursor.getColumnIndex("itemname"));
                String price = cursor.getString(cursor.getColumnIndex("price"));
                //details = "Button:"+ button + " -- " +"Item:"+ itemname + " -- " +"Price:"+price;
                Itemlist.add(itemname);
                Buttonlist.add(button);
                Pricelist.add(price);

            } while (cursor.moveToNext());

           /* final CharSequence[] todo = new CharSequence[itemlist.size()];
            for (int count = 0; count < itemlist.size(); count++) {
                todo[count] = itemlist.get(count);
            }

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(PasswordActivity.this);
            alertDialog.setTitle("Item Details");
            alertDialog.setItems(todo, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            AlertDialog alert = alertDialog.create();
            alert.show();*/


        }
    }



    public void Save(View view){
        String button=this.button.getText().toString();
        String item =this.item.getText().toString();
        String price =this.price.getText().toString();
        ContentValues contentValues=new ContentValues();
        contentValues.put(Foodymenuprovider.BUTTON,button);
        contentValues.put(Foodymenuprovider.ITEMNAME,item);
        contentValues.put(Foodymenuprovider.PRICE,price);
        String st[]={Foodymenuprovider.BUTTON};
        String but[]={button};

        if(button.isEmpty()||item.isEmpty()||price.isEmpty()){
            Toast.makeText(this,"Put values Before Saving",Toast.LENGTH_LONG).show();
        }
        else if(getContentResolver().query(Foodymenuprovider.Content_URL,st,Foodymenuprovider.BUTTON +"=?",but,null).getCount()!=0){

            Toast.makeText(this,"Button is present",Toast.LENGTH_LONG).show();
        }else {
            Uri uri = getContentResolver().insert(Foodymenuprovider.Content_URL, contentValues);
        }
        this.button.setText("");
        this.item.setText("");
        this.price.setText("");

    }

    public void Delete(View view){
        getContentResolver().delete(Foodymenuprovider.Content_URL,null,null);
    }

    public void show(View view){
        getContacts();

    }

    public void itemlists(){

            getContacts();
            pwdframe.setVisibility(View.GONE);
            mainlinear.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
        savebuttonlayout.setVisibility(View.GONE);
        businessframe.setVisibility(View.GONE);
            LinearLayout a = (LinearLayout) findViewById(R.id.itemlinear);
        LinearLayout b = (LinearLayout) findViewById(R.id.itemlinear2);
        LinearLayout c = (LinearLayout) findViewById(R.id.itemlinear3);
        LinearLayout e = (LinearLayout) findViewById(R.id.itemlinear4);
        LinearLayout f = (LinearLayout) findViewById(R.id.itemlinear5);

            LinearLayout d = (LinearLayout) findViewById(R.id.addnewbutton);
        d.setVisibility(View.VISIBLE);
            final Button button = new Button(getApplicationContext());
            Button [] deletebuttons= new Button[Itemlist.size()];
            final Button [] editbuttons= new Button[Itemlist.size()];

            button.setText("ADD NEW ITEM");
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout.LayoutParams buttonparam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        if(count==0)  {
            d.addView(button, lp);

            for (int i = 0; i < Itemlist.size(); i++) {
                String j=i+"";
                TextView textView1 = new TextView(getApplicationContext());
                textView1.setText(Buttonlist.get(i));
                textView1.setTextSize(20);
                textView1.setTextColor(Color.BLACK);
                a.addView(textView1, lp);

                TextView textView2 = new TextView(getApplicationContext());
                textView2.setText(Itemlist.get(i));
                textView2.setTextSize(20);
                textView2.setTextColor(Color.BLACK);
                b.addView(textView2, lp);

                TextView textView3 = new TextView(getApplicationContext());
                textView3.setText(Pricelist.get(i));
                textView3.setTextColor(Color.BLACK);
                textView3.setTextSize(20);
                c.addView(textView3, lp);

                deletebuttons[i]= new Button(getApplicationContext());
                deletebuttons[i].setText("d");
                deletebuttons[i].setTextSize(10);
                deletebuttons[i].setId(Integer.parseInt(Buttonlist.get(i).toString()));
                buttonparam.width=70;
                buttonparam.height=70;
                e.addView(deletebuttons[i],buttonparam);
                deletebuttons[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        deletebuttons(v.getId());
                        itemlists();
                    }
                });

                editbuttons[i]= new Button(getApplicationContext());
                editbuttons[i].setText("e");
                editbuttons[i].setTextSize(10);
                editbuttons[i].setId(Integer.parseInt(Buttonlist.get(i).toString()));
                buttonparam.width=70;
                buttonparam.height=70;
                f.addView(editbuttons[i],buttonparam);
                editbuttons[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        editbuttons(v.getId());
                    }
                });






            }
            Itemlist.clear();
            Buttonlist.clear();
            Pricelist.clear();
            count++;
        }else{
            a.removeAllViews();
            b.removeAllViews();
            c.removeAllViews();
            e.removeAllViews();
            f.removeAllViews();
            d.removeAllViews();
            d.addView(button, lp);
            TextView item= new TextView(getApplicationContext());
            TextView id=new TextView(getApplicationContext());
            TextView price=new TextView(getApplicationContext());
            TextView action=new TextView(getApplicationContext());
            TextView space=new TextView(getApplicationContext());



            id.setText("ID");
            id.setTextColor(Color.BLACK);
            a.addView(id, lp);
            item.setText("Item");
            item.setTextColor(Color.BLACK);
            b.addView(item, lp);
            price.setText("Price");
            price.setTextColor(Color.BLACK);
            c.addView(price, lp);
            action.setText("Action");
            action.setTextColor(Color.BLACK);
            e.addView(action, lp);
            space.setText("");
            f.addView(space,lp);


            for (int i = 0; i < Itemlist.size(); i++) {
                TextView textView1 = new TextView(getApplicationContext());
                textView1.setText(Buttonlist.get(i));
                textView1.setTextColor(Color.BLACK);
                textView1.setTextSize(20);
                a.addView(textView1, lp);

                TextView textView2 = new TextView(getApplicationContext());
                textView2.setText(Itemlist.get(i));
                textView2.setTextColor(Color.BLACK);
                textView2.setTextSize(20);
                b.addView(textView2, lp);

                TextView textView3 = new TextView(getApplicationContext());
                textView3.setText(Pricelist.get(i));
                textView3.setTextColor(Color.BLACK);
                textView3.setTextSize(20);
                c.addView(textView3, lp);

                deletebuttons[i]= new Button(getApplicationContext());
                deletebuttons[i].setText("d");
                deletebuttons[i].setTextSize(8);
                deletebuttons[i].setId(Integer.parseInt(Buttonlist.get(i).toString()));
                buttonparam.width=70;
                buttonparam.height=70;
                e.addView(deletebuttons[i],buttonparam);
                deletebuttons[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        deletebuttons(v.getId());
                        itemlists();
                    }
                });

                editbuttons[i]= new Button(getApplicationContext());
                editbuttons[i].setText("e");
                editbuttons[i].setTextSize(10);
                editbuttons[i].setId(Integer.parseInt(Buttonlist.get(i).toString()));
                buttonparam.width=70;
                buttonparam.height=70;
                f.addView(editbuttons[i],buttonparam);
                editbuttons[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        editbuttons(v.getId());
                    }
                });

            }
            Itemlist.clear();
            Buttonlist.clear();
            Pricelist.clear();
        }



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addnewitem();
                button.setVisibility(View.GONE);

            }
        });


            }
    public void addnewitem(){
        scrollView.setVisibility(View.GONE);
        pwdframe.setVisibility(View.GONE);
        mainlinear.setVisibility(View.VISIBLE);
        savebuttonlayout.setVisibility(View.VISIBLE);
        businessframe.setVisibility(View.GONE);

    }

    public void addbusinessinfo() {
        LinearLayout d = (LinearLayout) findViewById(R.id.addnewbutton);
        d.setVisibility(View.GONE);
        scrollView.setVisibility(View.GONE);
        pwdframe.setVisibility(View.GONE);
        mainlinear.setVisibility(View.GONE);
        savebuttonlayout.setVisibility(View.GONE);
        businessframe.setVisibility(View.VISIBLE);

        submitbusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                EditText busname = (EditText) findViewById(R.id.Businessname);
                EditText businfo = (EditText) findViewById(R.id.business_info);
                EditText tax = (EditText) findViewById(R.id.tax);
                EditText discount = (EditText) findViewById(R.id.discount);

                Foodymenuprovider.mydb mydb = new Foodymenuprovider.mydb(getApplicationContext());

                mydb.createlist();
                mydb.addlist(busname.getText().toString(), businfo.getText().toString(),tax.getText().toString(),discount.getText().toString());

                Cursor cursor = mydb.getbusinessinfo();
                cursor.moveToFirst();
                if (cursor != null) {
                    do {
                        for (int i = 0; i < cursor.getColumnCount(); i++) {

                            Toast.makeText(getApplicationContext(), cursor.getString(i), Toast.LENGTH_LONG).show();

                        }
                    } while (cursor.moveToNext());


                }

                String bName=busname.getText().toString();
                String bInfo=businfo.getText().toString();
                Log.d("Business", "onClick: "+bName);

                ContentValues contentValues=new ContentValues();
                contentValues.put(DBAdapter1.BUSINESS_NAME,bName);
                contentValues.put(DBAdapter1.BUSINESS_INFO,bInfo);
                Uri uri = getContentResolver().insert(DBAdapter1.CONTENT_URL, contentValues);
            }
        });
    }

    public void deletebuttons( int button){
        String st=button+"";
        String str[]={st};

        getContentResolver().delete(Foodymenuprovider.Content_URL,Foodymenuprovider.BUTTON +"=?",str);

    }

    public void editbuttons(int button){
        String[] projection = new String[]{"button", "itemname", "price"};
        String st=button+"";
        String str[]={st};
        Cursor cursor=getContentResolver().query(Foodymenuprovider.Content_URL,projection,Foodymenuprovider.BUTTON +"=?",str,null);
        cursor.moveToFirst();


        String button1 = cursor.getString(cursor.getColumnIndex("button"));
        String itemname = cursor.getString(cursor.getColumnIndex("itemname"));
        String price = cursor.getString(cursor.getColumnIndex("price"));

        EditText id=(EditText)findViewById(R.id.editTextnew);
        EditText item=(EditText)findViewById(R.id.editText);
        EditText itemprice=(EditText)findViewById(R.id.editText10);

        scrollView.setVisibility(View.GONE);
        pwdframe.setVisibility(View.GONE);
        mainlinear.setVisibility(View.VISIBLE);
        savebuttonlayout.setVisibility(View.VISIBLE);
        businessframe.setVisibility(View.GONE);

        id.setText(button1);
        item.setText(itemname);
        itemprice.setText(price);



        deletebuttons(button);




    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(PasswordActivity.this,OrderActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }


}


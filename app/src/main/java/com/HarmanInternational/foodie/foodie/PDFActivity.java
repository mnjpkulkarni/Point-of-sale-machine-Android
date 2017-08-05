package com.HarmanInternational.foodie.foodie;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.tom_roush.pdfbox.util.PDFBoxResourceLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class PDFActivity extends Activity {

    private static final int PERMISSION_REQUEST_CODE = 1;

    static final Uri CONTENT_URL = Uri.parse(DBAdapter1.PROVIDER_URL);
    ContentResolver contentResolver;

    String busName,busInfo;

    Button printFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        printFinal=(Button)findViewById(R.id.print_final);

        contentResolver=getContentResolver();

        ArrayList<String> items=(ArrayList<String>)getIntent().getSerializableExtra("Items");
        ArrayList<String> quantity=(ArrayList<String>)getIntent().getSerializableExtra("Quantity");
        ArrayList<String> amount=(ArrayList<String>)getIntent().getSerializableExtra("Amount");

        checkPermission();

        requestPermission();

        getDbValues();

        createPDF(items,quantity,amount);


        printFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                printJob();
                Intent i=new Intent(PDFActivity.this,BluetoothActivity.class);
                startActivity(i);
            }
        });



    }

//------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(PDFActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(PDFActivity.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can save image .");
                } else {
                    Log.e("value", "Permission Denied, You cannot save image.");
                }
                break;
        }
    }

//------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    public void getDbValues()
    {
        String[] projection = new String[]{"bname", "binfo"};
        Cursor cursor=contentResolver.query(CONTENT_URL,projection,null,null,null);

        if(cursor.moveToFirst()){

            do{

                busName = cursor.getString(cursor.getColumnIndex("bname"));
                busInfo = cursor.getString(cursor.getColumnIndex("binfo"));
                Log.d("Business Name", "getDbValues: "+busName);
                Log.d("Business Info", "getDbValues: "+busInfo);

            }while (cursor.moveToNext());

        }

    }
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------


    public void createPDF(ArrayList<String> items,ArrayList<String> quantity,ArrayList<String> amount)
    {
        String FILE = Environment.getExternalStorageDirectory().toString()
                + "/PDF1/" + "MyPdf.pdf";
        Document document = new Document(PageSize.A4);

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/PDF1");
        myDir.mkdirs();


        // Create Pdf Writer for Writting into New Created Document
        try {
            PdfWriter.getInstance(document, new FileOutputStream(FILE));

            document.open();
            addTitlePage(document,items,quantity,amount);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Close Document after writting all content
        document.close();

        Toast.makeText(this, "PDF File is Created. Location : " + FILE,
                Toast.LENGTH_LONG).show();

    }


    public void addTitlePage(Document document,ArrayList<String> items,ArrayList<String> quantity,ArrayList<String> amount) throws DocumentException {

        Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
        Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 22, Font.BOLD
                | Font.UNDERLINE, BaseColor.GRAY);
        Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
        Font normal = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.NORMAL);

        Paragraph prHead = new Paragraph();
        prHead.setFont(titleFont);
        prHead.add(busName+"\n\n\n");

        // Create Table into Document with 1 Row
        PdfPTable myTable = new PdfPTable(1);
        // 100.0f mean width of table is same as Document size
        myTable.setWidthPercentage(100.0f);

        PdfPCell myCell = new PdfPCell(new Paragraph(""));
        myCell.setBorder(Rectangle.BOTTOM);

        // Add Cell into Table
        myTable.addCell(myCell);

        prHead.setFont(catFont);
        prHead.add("\n"+busInfo+"\n");
        prHead.setAlignment(Element.ALIGN_CENTER);

        Paragraph menuItems=new Paragraph();

        menuItems.setFont(normal);
        menuItems.setAlignment(Element.ALIGN_CENTER);

        for(int i=0;i<items.size();i++)
        {
            menuItems.add(items.get(i)+"    "+quantity.get(i)+"No.      Rs."+amount.get(i)+"\n\n");
        }

        document.add(prHead);
        document.add(myTable);
        document.add(menuItems);


    }


    public void printJob()
    {
        String FILE = Environment.getExternalStorageDirectory().toString()
                + "/PDF1/" + "MyPdf.pdf";

        PDFBoxResourceLoader.init(getApplicationContext());


    }


    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(PDFActivity.this,OrderActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }


}

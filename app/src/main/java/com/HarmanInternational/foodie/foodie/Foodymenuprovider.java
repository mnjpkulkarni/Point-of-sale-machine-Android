package com.HarmanInternational.foodie.foodie;

/**
 * Created by Nithin on 6/14/2017.
 */

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by Nithin on 6/12/2017.
 */

public class Foodymenuprovider extends ContentProvider {
    static final String Provider_name="com.HarmanInternational.foodie.foodie.Foodymenuprovider";
    static final String URL="content://"+Provider_name+"/details";
    static final Uri Content_URL=Uri.parse(URL);
    static final String BUTTON="button";
    static final String ITEMNAME="itemname";
    static final String PRICE ="price";
    static final int uricode=1;

    private  static HashMap<String, String> values;
    static final UriMatcher urimatcher;

    static {
        urimatcher=new UriMatcher(UriMatcher.NO_MATCH);
        urimatcher.addURI(Provider_name,"details",uricode);
    }

    private SQLiteDatabase db;
    static final String DATABASE_NAME="Foody";
    static final String Table_NAME="foodytable";
    static final String Table_NAME_BUSINESS="businesstable";
    static final int DATABASE_VERSION=1;
    static final String CREATE_TABLE="CREATE TABLE "+Table_NAME+" (_id INTEGER PRIMARY KEY AUTOINCREMENT,button TEXT, itemname TEXT, price TEXT);";
    static final String CREATE_TABLE_BUSINESS="CREATE TABLE "+Table_NAME_BUSINESS+" (_id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,Info TEXT,tax TEXT,discount TEXT);";

    @Override
    public boolean onCreate() {
        mydb mydb=new mydb(getContext());
        db=mydb.getWritableDatabase();
        if(db==null){
            return  true;
        }
        return  false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder sqLiteQueryBuilder =new SQLiteQueryBuilder();
        sqLiteQueryBuilder.setTables(Table_NAME);
        switch(urimatcher.match(uri)){

            case uricode:sqLiteQueryBuilder.setProjectionMap(values);
                break;
            default: throw new IllegalArgumentException("unknown url"+uri);
        }
        Cursor cursor = sqLiteQueryBuilder.query(db, projection, selection, selectionArgs, null,
                null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long rowID = db.insert(Table_NAME, null, values);

        // Verify a row has been added
        if (rowID > 0) {

            // Append the given id to the path and return a Builder used to manipulate URI
            // references
            Uri _uri = ContentUris.withAppendedId(Content_URL, rowID);

            // getContentResolver provides access to the content model
            // notifyChange notifies all observers that a row was updated
            getContext().getContentResolver().notifyChange(_uri, null);

            // Return the Builder used to manipulate the URI
            Toast.makeText(getContext(), "Item added Successfully", Toast.LENGTH_LONG).show();
            return _uri;
        }
        Toast.makeText(getContext(), "Item addition Failed", Toast.LENGTH_LONG).show();

        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
       // db.execSQL("DROP TABLE "+Table_NAME);
       // db.delete(Table_NAME, selection, selectionArgs);
     //   db.execSQL(CREATE_TABLE);
        int rowsDeleted = 0;

        // Used to match uris with Content Providers
        switch (urimatcher.match(uri)) {
            case uricode:
                rowsDeleted = db.delete(Table_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        // getContentResolver provides access to the content model
        // notifyChange notifies all observers that a row was updated
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
        //Toast.makeText(getContext(), "All Items Deleted", Toast.LENGTH_LONG).show();
       // return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    public static class mydb extends SQLiteOpenHelper {


        public mydb(Context context){
            super(context,DATABASE_NAME,null,DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS"+Table_NAME);
            db.execSQL(CREATE_TABLE);

        }

        public void createlist() {

            SQLiteDatabase db = getWritableDatabase();
            db.execSQL("DROP TABLE IF EXISTS "+Table_NAME_BUSINESS);
            db.execSQL(CREATE_TABLE_BUSINESS);
            db.close();
        }

        public void addlist(String name,String business_info, String Tax, String disount)

        {

            ContentValues values=new ContentValues(2);

            values.put("name", name);

            values.put("Info", business_info);

            values.put("tax", Tax);

            values.put("discount", disount);

            getWritableDatabase().insert(Table_NAME_BUSINESS,null, values);





        }

        public Cursor getbusinessinfo()

        {
            try {



                Cursor cursor = getReadableDatabase().rawQuery("SELECT name,Info,tax,discount FROM " + Table_NAME_BUSINESS, null);
                return cursor;
            }catch (  SQLiteException ex) {
                return null;
        }

        }


    }
}

package com.animelabs.asheeshsharma.dressupapp.Data;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.animelabs.asheeshsharma.dressupapp.Model.Combination;
import com.animelabs.asheeshsharma.dressupapp.Model.Product;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.CheckedOutputStream;

/**
 * Created by Asheesh.Sharma on 04-12-2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "productManager";

    // Products table name
    private static final String TABLE_CONTACTS = "products";

     // Products Table Columns names
    private static final String KEY_ID = "articleId";
    private static final String KEY_TYPE = "article_type";
    private static final String KEY_PATH = "image_path";
    private static final String KEY_DATE = "article_date";
    private static final String KEY_TITLE = "article_title";
    private static final String KEY_PRICE = "article_price";

    //Favrouite table name
    private static final String TABLE_FAV = "liked";

    //Favrouite table column
    private static final String KEY_SHIRT_PATH  = "shirt_path";
    private static final String KEY_TROUSER_PATH = "trouser_path";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MESSAGE_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + " ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+ KEY_PATH + " TEXT," + KEY_TITLE + " TEXT," + KEY_DATE + " TEXT," + KEY_PRICE + " TEXT,"
                + KEY_ID + " TEXT," + KEY_TYPE + " TEXT" + ")";
        String CREATE_LIKED_TABLE = "CREATE TABLE " + TABLE_FAV + "("
                + " ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+ KEY_SHIRT_PATH + " TEXT," + KEY_TROUSER_PATH + " TEXT" + ")";
        db.execSQL(CREATE_MESSAGE_TABLE);
        db.execSQL(CREATE_LIKED_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAV);
        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    public void addProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PATH, product.getPathToImage());
        values.put(KEY_TITLE, product.getArticleType());
        values.put(KEY_DATE, product.getDateOfCreation());
        values.put(KEY_PRICE, product.getPrice());
        values.put(KEY_ID, product.getArticleNumber());
        values.put(KEY_TYPE, product.getArticleType());
        // Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        db.close(); // Closing database connection
    }

    //Adding Liked Combination
    public void addCombination(Combination combination){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_SHIRT_PATH, combination.getShirtPath());
        values.put(KEY_TROUSER_PATH, combination.getTrouserPath());
        db.insert(TABLE_FAV, null, values);
        db.close();
    }

    // Getting single contact
    public Product getProduct(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_PATH, KEY_TITLE, KEY_DATE, KEY_PRICE,
                        KEY_ID, KEY_TYPE}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Product product = new Product(cursor.getString(0),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
        // return contact
        return product;
    }

    // Getting All Contacts
    public List<Product> getAllProducts(String type) {
        List<Product> productList = new ArrayList<Product>();
        // Select All Query
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_PATH, KEY_TITLE, KEY_DATE, KEY_PRICE,
                        KEY_ID, KEY_TYPE}, KEY_TYPE + "=?",
                new String[] { String.valueOf(type) }, null, null, null, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Log.d(" Values ", cursor.getString(0) + "-" + cursor.getString(1) + "-" + cursor.getString(2) + "-" + cursor.getString(3)+ "-" + cursor.getString(4)+ "-" + cursor.getString(5));
                Product product = new Product();
                product.setPathToImage(cursor.getString(0));
                product.setTitleProduct(cursor.getString(1));
                product.setDateOfCreation(cursor.getString(2));
                product.setPrice(cursor.getString(3));
                product.setArticleNumber(cursor.getString(4));
                product.setArticleType(cursor.getString(5));
                // Adding contact to list
                productList.add(product);
            } while (cursor.moveToNext());
        }

        // return contact list
        return productList;
    }

    // Updating single contact
    public int updateContact(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PATH, product.getPathToImage());
        values.put(KEY_TITLE, product.getArticleType());
        values.put(KEY_DATE, product.getDateOfCreation());
        values.put(KEY_PRICE, product.getPrice());
        values.put(KEY_ID, product.getArticleNumber());
        values.put(KEY_TYPE, product.getArticleType());
        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(product.getArticleNumber()) });
    }

    // Deleting single contact
    public void deleteProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[] { String.valueOf(product.getArticleNumber()) });
        db.close();
    }


    // Getting contacts Count
    public int getProductsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        // return count
        return cursor.getCount();
    }

    public int getCombinationCount(){
        String countQuery = "SELECT  * FROM " + TABLE_FAV;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        // return count
        return cursor.getCount();
    }

}

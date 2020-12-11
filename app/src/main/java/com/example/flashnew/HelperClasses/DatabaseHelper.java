package com.example.flashnew.HelperClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.flashnew.Modals.TableOneDelivererModal;
import com.example.flashnew.Modals.TableThreeDeliveryModal;
import com.example.flashnew.Modals.TableTwoListModal;

import java.io.ByteArrayOutputStream;

import static android.content.ContentValues.TAG;

public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "Flash.db";
    private static final int VERSION = 1;

    //Table names
    public static final String TABLE_DELIVER_DETAILS = "tbl_deliverer_details";
    public static final String TABLE_TOTAL_LIST_DETAILS = "tbl_list_details";
    public static final String TABLE_DELIVERY_DETAILS = "tbl_delivery_details";
    public static final String TABLE_HAWB_CODES = "tbl_hawb_code";


    //Table 1 columns & query:
    private static final String ID = "id";
    private static final String FRANCHISE = "franchise";
    private static final String LIST = "lists";
    private static final String DELIVERY_ID = "delivery_id";
    public static final String DELIVERER_NAME = "deliverer_name";
    private static final String TOTAL_DOCUMENTS = "total_documents";
    private static final String CREATE_TABLE_DELIVERER_DETAILS = "CREATE TABLE " + TABLE_DELIVER_DETAILS + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + FRANCHISE + " TEXT, "
            + LIST + " INTEGER, " + DELIVERY_ID + " INTEGER, " + DELIVERER_NAME + " TEXT, " + TOTAL_DOCUMENTS + " TEXT)";

    //Table 2 columns & query:
    private static final String CUSTOMER_ID = "customer_id";
    private static final String CONTRACT_ID = "contract_id";
    private static final String HAWB_CODE = "hawb_code";
    private static final String NUMBER_ORDER_CLIENT = "number_order";
    private static final String RECIPIENT_NAME = "recipient_name";
    private static final String DNA = "dna";
    private static final String ATTEMPTS = "attempts";
    private static final String SPECIAL_PHOTO = "special_photo";
    private static final String SCORE = "score";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    public static final String TICK_MARK = "tick_mark";
    private static final String CREATE_TABLE_TOTAL_LIST_DETAILS = "CREATE TABLE " + TABLE_TOTAL_LIST_DETAILS + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CUSTOMER_ID + " INTEGER, "
            + CONTRACT_ID + " INTEGER, " + HAWB_CODE + " TEXT, " + NUMBER_ORDER_CLIENT + " TEXT, " + RECIPIENT_NAME + " TEXT, " + DNA + " INTEGER, "
            + ATTEMPTS + " INTEGER, " + SPECIAL_PHOTO + " TEXT, " + SCORE + " INTEGER, " + LATITUDE + " FLOAT, " + LONGITUDE + " FLOAT, " + TICK_MARK + " TEXT)";


    //Table 3 columns & query:
    private static final String H_CODE = "h_code";
    private static final String RELATIONSHIP = "relation";
    private static final String NO_OF_ATTEMPTS = "attempts";
    private static final String DATE_TIME = "date_time";
    private static final String BATTERY_LEVEL = "battery_level";
    private static final String LOW_TYPE = "low_type";
    private static final String DELIVERY_IMAGE = "image";
    private static final String PERIMETER = "perimeter";
    private static final String PHOTO_BOOLEAN = "photo_boolean";
    private static final String CREATE_TABLE_TABLE_DELIVERY_DETAILS = "CREATE TABLE " + TABLE_DELIVERY_DETAILS + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            H_CODE + " TEXT, " + RELATIONSHIP + " TEXT, " + NO_OF_ATTEMPTS + " INTEGER, " + DATE_TIME + " TEXT, " + BATTERY_LEVEL + " INTEGER, " + LOW_TYPE + " TEXT, " + PHOTO_BOOLEAN + " TEXT, " + LATITUDE + " FLOAT, " + LONGITUDE + " FLOAT, " + DELIVERY_IMAGE + " BLOB)";

    //Table 4 query:
    private static final String CREATE_TABLE_HAWB_CODES = "CREATE TABLE " + TABLE_HAWB_CODES + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            HAWB_CODE + " TEXT)";


    private ByteArrayOutputStream objectByteArrayOutputStream;
    private byte[] imageInByte;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_DELIVERER_DETAILS);
        db.execSQL(CREATE_TABLE_TOTAL_LIST_DETAILS);
        db.execSQL(CREATE_TABLE_TABLE_DELIVERY_DETAILS);
        db.execSQL(CREATE_TABLE_HAWB_CODES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DELIVER_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOTAL_LIST_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DELIVERY_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HAWB_CODES);

        //create new tables on upgrade
        onCreate(db);
    }

    public void truncateAllTablesOnLogout() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_DELIVER_DETAILS);
        db.execSQL("DELETE FROM " + TABLE_TOTAL_LIST_DETAILS);
        db.execSQL("DELETE FROM " + TABLE_DELIVERY_DETAILS);
        db.execSQL("DELETE FROM " + TABLE_HAWB_CODES);
        db.close();
    }

    /**
     * TABLE QUERYING START
     */

    public boolean addDataToTableOne(TableOneDelivererModal delivererModal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(FRANCHISE, delivererModal.getFranchise());
        contentValues.put(LIST, delivererModal.getLists());
        contentValues.put(DELIVERY_ID, delivererModal.getDeliveryID());
        contentValues.put(DELIVERER_NAME, delivererModal.getDelivererName());
        contentValues.put(TOTAL_DOCUMENTS, delivererModal.getTotalDocuments());

        long result = db.insert(TABLE_DELIVER_DETAILS, null, contentValues);
        db.close();
        if (result == -1) {
            return false;
        } else {
            return true;
        }

    }

    public boolean addDataToTableTwo(TableTwoListModal twoListModal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(CUSTOMER_ID, twoListModal.getCustomerID());
        contentValues.put(CONTRACT_ID, twoListModal.getContractID());
        contentValues.put(HAWB_CODE, twoListModal.getHawbCode());
        contentValues.put(NUMBER_ORDER_CLIENT, twoListModal.getNumberOrder());
        contentValues.put(RECIPIENT_NAME, twoListModal.getRecipientName());
        contentValues.put(DNA, twoListModal.getDna());
        contentValues.put(ATTEMPTS, twoListModal.getAttempts());
        contentValues.put(SPECIAL_PHOTO, twoListModal.getSpecialPhoto());
        contentValues.put(SCORE, twoListModal.getScore());
        contentValues.put(LATITUDE, twoListModal.getLatitude());
        contentValues.put(LONGITUDE, twoListModal.getLongitude());
        contentValues.put(TICK_MARK, "false");

        long result = db.insert(TABLE_TOTAL_LIST_DETAILS, null, contentValues);
        db.close();
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_TOTAL_LIST_DETAILS;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public void ValidateDataWithSecondTable(String code) {
        SQLiteDatabase db = this.getWritableDatabase();
        //String query= "SELECT * FROM " + TABLE_TOTAL_LIST_DETAILS + " WHERE " + HAWB_CODE + " ='" + code + "'";
        String query = "UPDATE " + TABLE_TOTAL_LIST_DETAILS + " SET " + TICK_MARK + "=' true  ' WHERE " + HAWB_CODE + " ='" + code + "'";
        db.execSQL(query);
    }

    public void DeleteDataFromTableTwo() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_TOTAL_LIST_DETAILS;
        db.execSQL(query);
    }

    public void addDataToTableThree(TableThreeDeliveryModal deliveryModal) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Bitmap imageToStoreBitmap = deliveryModal.getImage();
            objectByteArrayOutputStream = new ByteArrayOutputStream();
            imageToStoreBitmap.compress(Bitmap.CompressFormat.JPEG, 100, objectByteArrayOutputStream);
            imageInByte = objectByteArrayOutputStream.toByteArray();

            ContentValues contentValues = new ContentValues();
            contentValues.put(H_CODE, deliveryModal.getHawbCode());
            contentValues.put(RELATIONSHIP, deliveryModal.getRelationship());
            contentValues.put(NO_OF_ATTEMPTS, deliveryModal.getAttempts());
            contentValues.put(DATE_TIME, deliveryModal.getDateTime());
            contentValues.put(BATTERY_LEVEL, deliveryModal.getBatteryLevel());
            contentValues.put(LOW_TYPE, deliveryModal.getLowType());
            contentValues.put(PHOTO_BOOLEAN, deliveryModal.getPhotoBoolean());
            contentValues.put(LATITUDE, deliveryModal.getLatitude());
            contentValues.put(LONGITUDE, deliveryModal.getLongitude());
            contentValues.put(DELIVERY_IMAGE, imageInByte);

            long result = db.insert(TABLE_DELIVERY_DETAILS, null, contentValues);

            if (result == -1) {
                Log.e(TAG, "storeDeliveryDetails: " + result);
            } else {
                Log.e(TAG, "storeDeliveryDetails1: " + result);
            }
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Cursor getDeliveryData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_DELIVERY_DETAILS;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public void DeleteFromTableThreeUponSync() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_DELIVERY_DETAILS;
        db.execSQL(query);
    }

    public boolean addDataToTableFour(String item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(HAWB_CODE, item);
        long result = db.insert(TABLE_HAWB_CODES, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getDataFromTableFour() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_HAWB_CODES;
        return db.rawQuery(query, null);
    }

    public void deleteHawbFromTableFour(String h_code) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_HAWB_CODES + " WHERE " + HAWB_CODE + " = '" + h_code + "'";
        db.execSQL(query);
    }

    public boolean CheckHawbCode(String cod) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_HAWB_CODES + " WHERE " + HAWB_CODE + " = '" + cod + "'";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
}

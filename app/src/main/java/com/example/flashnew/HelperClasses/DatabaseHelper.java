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
    private static final String CREATE_TABLE_TOTAL_LIST_DETAILS = "CREATE TABLE " + TABLE_TOTAL_LIST_DETAILS + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CUSTOMER_ID + " INTEGER, "
            + CONTRACT_ID + " INTEGER, " + HAWB_CODE + " TEXT, " + NUMBER_ORDER_CLIENT + " TEXT, " + RECIPIENT_NAME + " TEXT, " + DNA + " INTEGER, "
            + ATTEMPTS + " INTEGER, " + SPECIAL_PHOTO + " TEXT, " + SCORE + " INTEGER, " + LATITUDE + " FLOAT, " + LONGITUDE + " FLOAT)";


    //Table 3 columns & query:
    public static final String H_CODE = "h_code";
    public static final String RELATIONSHIP = "relation";
    public static final String NO_OF_ATTEMPTS = "attempts";
    public static final String DATE_TIME = "date_time";
    public static final String BATTERY_LEVEL = "battery_level";
    public static final String LOW_TYPE = "low_type";
    public static final String DELIVERY_IMAGE = "image";
    public static final String PERIMETER = "perimeter";
    public static final String CREATE_TABLE_TABLE_DELIVERY_DETAILS = "CREATE TABLE " + TABLE_DELIVERY_DETAILS + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            H_CODE + " TEXT, " + RELATIONSHIP + " TEXT, " + NO_OF_ATTEMPTS + " INTEGER, " + DATE_TIME + " TEXT, " + BATTERY_LEVEL + " INTEGER, " + LOW_TYPE + " TEXT, " + DELIVERY_IMAGE + " BLOB)";


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

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DELIVER_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOTAL_LIST_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DELIVERY_DETAILS);

        //create new tables on upgrade
        onCreate(db);
    }

    public void truncateAllTablesOnLogout() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_DELIVER_DETAILS);
        db.execSQL("DELETE FROM " + TABLE_TOTAL_LIST_DETAILS);
        db.execSQL("DELETE FROM " + TABLE_DELIVERY_DETAILS);
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

    public void storeDeliveryDetails(TableThreeDeliveryModal deliveryModal) {
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
            contentValues.put(DELIVERY_IMAGE, imageInByte);

            long result = db.insert(TABLE_DELIVERY_DETAILS, null, contentValues);

            if (result == -1) {
                Log.e(TAG, "storeDeliveryDetails: " + result);
            } else {
                Log.e(TAG, "storeDeliveryDetails1: " + result);
            }
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

}

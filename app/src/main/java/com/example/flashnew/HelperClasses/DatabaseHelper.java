package com.example.flashnew.HelperClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.flashnew.Modals.TableFiveModel;
import com.example.flashnew.Modals.TableOneDelivererModal;
import com.example.flashnew.Modals.TableSevenNotCollectedModal;
import com.example.flashnew.Modals.TableSixCollectModal;
import com.example.flashnew.Modals.TableThreeDeliveryModal;
import com.example.flashnew.Modals.TableTwoListModal;
import com.google.zxing.common.StringUtils;

import java.io.ByteArrayOutputStream;

import static android.content.ContentValues.TAG;

public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "Flash.db";
    private static final int VERSION = 1;

    //Table names
    private static final String TABLE_DELIVER_DETAILS = "tbl_deliverer_details";
    private static final String TABLE_TOTAL_LIST_DETAILS = "tbl_list_details";
    private static final String TABLE_DELIVERY_DETAILS = "tbl_delivery_details";
    private static final String TABLE_HAWB_CODES = "tbl_hawb_code";
    private static final String TABLE_SCANNER_DETAILS = "tbl_scanner_details";
    private static final String TABLE_COLETA_DETAILS = "tbl_coleta_details";
    private static final String TABLE_NOT_COLLECTED_DETAILS = "tbl_not_collected_details";


    //Table 1 columns & query:
    private static final String ID = "id";
    private static final String FRANCHISE = "franchise";
    private static final String LIST = "lists";
    private static final String DELIVERY_ID = "delivery_id";
    private static final String DELIVERER_NAME = "deliverer_name";
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
    private static final String CLIENT_NUMBER = "client_number";
    private static final String TICK_MARK = "tick_mark";
    private static final String CREATE_TABLE_TOTAL_LIST_DETAILS = "CREATE TABLE " + TABLE_TOTAL_LIST_DETAILS + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CUSTOMER_ID + " INTEGER, "
            + CONTRACT_ID + " INTEGER, " + HAWB_CODE + " TEXT, " + NUMBER_ORDER_CLIENT + " TEXT, " + RECIPIENT_NAME + " TEXT, " + DNA + " INTEGER, "
            + ATTEMPTS + " INTEGER, " + SPECIAL_PHOTO + " TEXT, " + SCORE + " INTEGER, " + CLIENT_NUMBER + " TEXT, " + TICK_MARK + " TEXT)";


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

    //Table 5 columns & query:
    private static final String COLETA_ID = "coleta_id";
    private static final String STREET_NAME = "street_name";
    private static final String APT_NO = "apt_no";
    private static final String CITY = "city";
    private static final String STATE = "state";
    private static final String PINCODE = "pincode";
    private static final String CREATE_TABLE_SCANNER_DETAILS = "CREATE TABLE " + TABLE_SCANNER_DETAILS + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLETA_ID + " TEXT, " + STREET_NAME + " TEXT, " + APT_NO + " TEXT, " + CITY + " TEXT, " + STATE + " TEXT, " +
            PINCODE + " INTEGER, " + TICK_MARK + " TEXT)";

    //Table 6 columns & query:
    public static final String IDENTIFICATION_NO = "identification_no";
    public static final String TYPE_PROCESS = "type_process";
    public static final String CREATE_TABLE_COLETA_DETAILS = "CREATE TABLE " + TABLE_COLETA_DETAILS + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLETA_ID + " TEXT, " + RECIPIENT_NAME + " TEXT, " + IDENTIFICATION_NO + " TEXT, " + DATE_TIME + " TEXT," + TYPE_PROCESS + " TEXT," +
            LATITUDE + " FLOAT, " + LONGITUDE + " FLOAT, " + BATTERY_LEVEL + " INTEGER)";


    //Table 7 columns & query
    public static final String COLLECT_IMAGE = "collect_image";
    public static final String CREATE_TABLE_NOT_COLLECTED_DETAILS = "CREATE TABLE " + TABLE_NOT_COLLECTED_DETAILS + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLETA_ID + " TEXT, " + DATE_TIME + " TEXT, " + TYPE_PROCESS + " TEXT, " + LATITUDE + " FLOAT, " + LONGITUDE + " FLOAT, " + BATTERY_LEVEL + " INTEGER, " + COLLECT_IMAGE + " BLOB)";

    private ByteArrayOutputStream objectByteArrayOutputStream, objectByteArrayOutputStream2;
    private byte[] imageInByte, imageInByte2;

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
        db.execSQL(CREATE_TABLE_SCANNER_DETAILS);
        db.execSQL(CREATE_TABLE_COLETA_DETAILS);
        db.execSQL(CREATE_TABLE_NOT_COLLECTED_DETAILS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DELIVER_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOTAL_LIST_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DELIVERY_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HAWB_CODES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCANNER_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COLETA_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOT_COLLECTED_DETAILS);
        //create new tables on upgrade
        onCreate(db);
    }

    public void truncateAllTablesOnLogout() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_DELIVER_DETAILS);
        db.execSQL("DELETE FROM " + TABLE_TOTAL_LIST_DETAILS);
        db.execSQL("DELETE FROM " + TABLE_DELIVERY_DETAILS);
        db.execSQL("DELETE FROM " + TABLE_HAWB_CODES);
        db.execSQL("DELETE FROM " + TABLE_SCANNER_DETAILS);
        db.execSQL("DELETE FROM " + TABLE_COLETA_DETAILS);
        db.execSQL("DELETE FROM " + TABLE_NOT_COLLECTED_DETAILS);
        db.close();
    }

    /**
     * TABLE QUERYING START
     */
    //Table one
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

    //List screen
    //Table two
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
        contentValues.put(CLIENT_NUMBER, twoListModal.getClientNumber());
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
        return db.rawQuery(query, null);
    }

    public void ValidateDataWithSecondTable(String code) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_TOTAL_LIST_DETAILS + " SET " + TICK_MARK + "=' true  ' WHERE " + HAWB_CODE + " ='" + code + "'";
        db.execSQL(query);
    }

    public void DeleteDataFromTableTwo() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_TOTAL_LIST_DETAILS;
        db.execSQL(query);
    }

    public void DeleteDataUponUpload(String code_hawb) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_TOTAL_LIST_DETAILS + " WHERE " + HAWB_CODE + " = '" + code_hawb + "'";
        db.execSQL(query);
    }

    //Table three
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
        return db.rawQuery(query, null);
    }

    public void DeleteFromTableThreeUponSync() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_DELIVERY_DETAILS;
        db.execSQL(query);
    }

    //Table four
    public boolean addDataToTableFour(String item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(HAWB_CODE, item);
        long result = db.insert(TABLE_HAWB_CODES, null, contentValues);
        db.close();
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

    public void DeleteTableFour() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_HAWB_CODES;
        db.execSQL(query);
    }

    //Collect Screen
    //Table five
    public boolean AddDateToTableFive(TableFiveModel tableFiveModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLETA_ID, tableFiveModel.getColetaID());
        contentValues.put(STREET_NAME, tableFiveModel.getStreetName());
        contentValues.put(APT_NO, tableFiveModel.getAptNo());
        contentValues.put(CITY, tableFiveModel.getCity());
        contentValues.put(STATE, tableFiveModel.getState());
        contentValues.put(PINCODE, tableFiveModel.getPincode());
        contentValues.put(TICK_MARK, "false");

        long result = db.insert(TABLE_SCANNER_DETAILS, null, contentValues);

        db.close();
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor GetDataFromTableFive() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_SCANNER_DETAILS;
        return db.rawQuery(query, null);
    }

    public boolean CheckColetaData(String coletaID) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_SCANNER_DETAILS + " WHERE " + COLETA_ID + " = '" + coletaID + "'";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public void CheckTickMarkInTableFive(String tick) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_SCANNER_DETAILS + " SET " + TICK_MARK + "=' true  ' WHERE " + COLETA_ID + " ='" + tick + "'";
        db.execSQL(query);
    }

    public void DeleteFromTableFiveUponUpload(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_SCANNER_DETAILS + " WHERE " + COLETA_ID + " = '" + id + "'";
        db.execSQL(query);
    }

    //Table six
    public boolean AddDataToTableSix(TableSixCollectModal collectModal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLETA_ID, collectModal.getCollectID());
        contentValues.put(RECIPIENT_NAME, collectModal.getName());
        contentValues.put(IDENTIFICATION_NO, collectModal.getIdentification());
        contentValues.put(DATE_TIME, collectModal.getDateTime());
        contentValues.put(TYPE_PROCESS, collectModal.getType());
        contentValues.put(LATITUDE, collectModal.getLatitude());
        contentValues.put(LONGITUDE, collectModal.getLongitude());
        contentValues.put(BATTERY_LEVEL, collectModal.getBatteryPercentage());

        long result = db.insert(TABLE_COLETA_DETAILS, null, contentValues);
        db.close();
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor GetDataFromTableSix() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_COLETA_DETAILS;
        return db.rawQuery(query, null);
    }

    public void DeleteFromTableSixUponSync() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_COLETA_DETAILS;
        db.execSQL(query);
    }

    //Table seven
    public boolean AddDataToTableSeven(TableSevenNotCollectedModal notCollectedModal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Bitmap imageToStoreBitmap = notCollectedModal.getImage();
        objectByteArrayOutputStream2 = new ByteArrayOutputStream();
        imageToStoreBitmap.compress(Bitmap.CompressFormat.JPEG, 100, objectByteArrayOutputStream2);
        imageInByte2 = objectByteArrayOutputStream2.toByteArray();

        contentValues.put(COLETA_ID, notCollectedModal.getCollectID());
        contentValues.put(DATE_TIME, notCollectedModal.getDateTime());
        contentValues.put(TYPE_PROCESS, notCollectedModal.getType());
        contentValues.put(LATITUDE, notCollectedModal.getLatitude());
        contentValues.put(LONGITUDE, notCollectedModal.getLongitude());
        contentValues.put(BATTERY_LEVEL, notCollectedModal.getBatteryPercentage());
        contentValues.put(COLLECT_IMAGE, imageInByte2);

        long result = db.insert(TABLE_NOT_COLLECTED_DETAILS, null, contentValues);
        db.close();
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor GetDataFromTableSeven() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NOT_COLLECTED_DETAILS;
        Cursor data = db.rawQuery(query, null);
        //return db.rawQuery(query, null);
        return data;
    }

    public void DeleteFromTableSevenUponSync() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NOT_COLLECTED_DETAILS;
        db.execSQL(query);
    }

}

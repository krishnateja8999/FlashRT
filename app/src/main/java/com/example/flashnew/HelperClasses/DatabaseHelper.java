package com.example.flashnew.HelperClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.flashnew.Modals.ResearchListModal;
import com.example.flashnew.Modals.ListImageModal;
import com.example.flashnew.Modals.SaveResearchDetailsModal;
import com.example.flashnew.Modals.TableFiveModel;
import com.example.flashnew.Modals.TableOneDelivererModal;
import com.example.flashnew.Modals.TableSevenNotCollectedModal;
import com.example.flashnew.Modals.TableSixCollectModal;
import com.example.flashnew.Modals.TableThreeDeliveryModal;
import com.example.flashnew.Modals.TableTwoListModal;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class DatabaseHelper extends SQLiteOpenHelper {

    private final Context context;
    private static final String DATABASE_NAME = "Flash.db";
    private static final int VERSION = 4;

    //Table names
    private static final String TABLE_DELIVER_DETAILS = "tbl_deliverer_details";
    private static final String TABLE_TOTAL_LIST_DETAILS = "tbl_list_details";
    private static final String TABLE_DELIVERY_DETAILS = "tbl_delivery_details";
    private static final String TABLE_HAWB_CODES = "tbl_hawb_code";
    private static final String TABLE_SCANNER_DETAILS = "tbl_scanner_details";
    private static final String TABLE_COLETA_DETAILS = "tbl_coleta_details";
    private static final String TABLE_NOT_COLLECTED_DETAILS = "tbl_not_collected_details";
    private static final String TABLE_TOTAL_DELIVERY_RETURNS = "tbl_total_delivery_returns";
    private static final String TABLE_TOTAL_COLLECT_DONE = "tbl_total_collect_done";
    private static final String TABLE_RESEARCH_LIST = "tbl_research_list";
    private static final String TABLE_SAVE_RESEARCH_DETAILS = "tbl_save_research_details";
    private static final String TABLE_RESEARCH_IMAGES = "tbl_research_images";

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
    public static final String IMAGE_NAME = "image_name";
    private static final String CREATE_TABLE_TABLE_DELIVERY_DETAILS = "CREATE TABLE " + TABLE_DELIVERY_DETAILS + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            H_CODE + " TEXT, " + RELATIONSHIP + " TEXT, " + NO_OF_ATTEMPTS + " INTEGER, " + DATE_TIME + " TEXT, " + BATTERY_LEVEL + " INTEGER, " + LOW_TYPE + " TEXT, " + PHOTO_BOOLEAN + " TEXT, " + LATITUDE + " FLOAT, " + LONGITUDE + " FLOAT, " + DELIVERY_IMAGE + " TEXT, " + IMAGE_NAME + " TEXT)";

    //Table 4 query:
    private static final String CREATE_TABLE_HAWB_CODES = "CREATE TABLE " + TABLE_HAWB_CODES + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            HAWB_CODE + " TEXT, " + CLIENT_NUMBER + " TEXT)";

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

    //Table 8 columns & query
    public static final String TOTAL_LIST_COUNT = "list_count";
    public static final String CREATE_TABLE_TOTAL_DELIVERY_RETURNS = "CREATE TABLE " + TABLE_TOTAL_DELIVERY_RETURNS + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TOTAL_LIST_COUNT + " TEXT)";

    //Table 9 columns & query
    public static final String TOTAL_COLLECT_COUNT = "collect_count";
    public static final String CREATE_TABLE_TOTAL_COLLECT_DONE = "CREATE TABLE " + TABLE_TOTAL_COLLECT_DONE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TOTAL_COLLECT_COUNT + " TEXT)";

    //Table 10 columns & query
    public static final String PUBLIC_PLACE = "public_place";
    public static final String CREATE_TABLE_RESEARCH_LIST = "CREATE TABLE " + TABLE_RESEARCH_LIST + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            HAWB_CODE + " TEXT, " + NUMBER_ORDER_CLIENT + " TEXT, " + RECIPIENT_NAME + " TEXT, " + DNA + " INTEGER, " + APT_NO + " TEXT, " +
            PUBLIC_PLACE + " TEXT, " + STREET_NAME + " TEXT, " + CITY + " TEXT, " + STATE + " TEXT, " + PINCODE + " INTEGER, " + TICK_MARK + " TEXT, " + CUSTOMER_ID + " TEXT, " + CONTRACT_ID + " TEXT, " + LIST + " INTEGER)";

    //Table 11 columns & query
    public static final String RESEARCH_ONE = "research_one";
    public static final String RESEARCH_TWO = "research_two";
    public static final String RESEARCH_THREE = "research_three";
    public static final String XML_RESEARCH = "xml_research";
    public static final String CREATE_TABLE_SAVE_RESEARCH_DETAILS = "CREATE TABLE " + TABLE_SAVE_RESEARCH_DETAILS + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            HAWB_CODE + " TEXT, " + DATE_TIME + " TEXT, " + BATTERY_LEVEL + " INTEGER, " + LATITUDE + " FLOAT, " + LONGITUDE + " FLOAT, " + XML_RESEARCH + " TEXT, " + LIST + " INTEGER)";

    //Table 12 columns & query
    public static final String RESEARCH_IMAGES = "research_image_path";
    public static final String CREATE_TABLE_RESEARCH_IMAGES = "CREATE TABLE " + TABLE_RESEARCH_IMAGES + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            RESEARCH_IMAGES + " TEXT, " + IMAGE_NAME + " TEXT)";
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
        db.execSQL(CREATE_TABLE_TOTAL_DELIVERY_RETURNS);
        db.execSQL(CREATE_TABLE_TOTAL_COLLECT_DONE);
        db.execSQL(CREATE_TABLE_RESEARCH_LIST);
        db.execSQL(CREATE_TABLE_SAVE_RESEARCH_DETAILS);
        db.execSQL(CREATE_TABLE_RESEARCH_IMAGES);
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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOTAL_DELIVERY_RETURNS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOTAL_COLLECT_DONE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESEARCH_LIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SAVE_RESEARCH_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESEARCH_IMAGES);
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
        db.execSQL("DELETE FROM " + TABLE_TOTAL_DELIVERY_RETURNS);
        db.execSQL("DELETE FROM " + TABLE_TOTAL_COLLECT_DONE);
        db.execSQL("DELETE FROM " + TABLE_RESEARCH_LIST);
        db.execSQL("DELETE FROM " + TABLE_SAVE_RESEARCH_DETAILS);
        db.execSQL("DELETE FROM " + TABLE_RESEARCH_IMAGES);
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
        return result != -1;

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
        return result != -1;
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_TOTAL_LIST_DETAILS;
        return db.rawQuery(query, null);
    }

    public void ValidateDataWithSecondTable(String code) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_TOTAL_LIST_DETAILS + " SET " + TICK_MARK + "= 'true' WHERE " + HAWB_CODE + " ='" + code + "' OR " +
                CLIENT_NUMBER + " ='" + code + "'";
        db.execSQL(query);
    }

    public void DeleteDataFromTableTwo() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_TOTAL_LIST_DETAILS;
        db.execSQL(query);
    }

    public void DeleteDataUponUpload(String code_hawb) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_TOTAL_LIST_DETAILS + " WHERE " + HAWB_CODE + " = '" + code_hawb + "' OR " +
                CLIENT_NUMBER + " ='" + code_hawb + "'";
        db.execSQL(query);
    }

    public int DashSyncCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_TOTAL_LIST_DETAILS + " WHERE " + TICK_MARK + " = 'true' ";
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public List<ListImageModal> GetImgDetails(String codes) {
        List<ListImageModal> listImage = new ArrayList<ListImageModal>();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + CUSTOMER_ID + ", " + CONTRACT_ID + ", " + NUMBER_ORDER_CLIENT + " FROM " + TABLE_TOTAL_LIST_DETAILS +
                " WHERE " + HAWB_CODE + " ='" + codes + "' OR " + NUMBER_ORDER_CLIENT + " ='" + codes + "'";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                ListImageModal modal = new ListImageModal();
                try {
                    modal.setCustomerCode(Integer.parseInt(cursor.getString(0)));
                    modal.setContractCode(Integer.parseInt(cursor.getString(1)));
                    modal.setCustomerNumber(cursor.getString(2));
                } catch (NumberFormatException e) {
                    Log.e(TAG, "GetImgDetails: " + e.getMessage());
                }
                listImage.add(modal);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return listImage;
    }

    //Table three
    public void addDataToTableThree(TableThreeDeliveryModal deliveryModal) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
//            Bitmap imageToStoreBitmap = deliveryModal.getImage();
//            objectByteArrayOutputStream = new ByteArrayOutputStream();
//            imageToStoreBitmap.compress(Bitmap.CompressFormat.JPEG, 100, objectByteArrayOutputStream);
//            imageInByte = objectByteArrayOutputStream.toByteArray();

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
            contentValues.put(DELIVERY_IMAGE, deliveryModal.getImage());
            contentValues.put(IMAGE_NAME, deliveryModal.getImageName());

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

    public void DeleteTheHawbDetailsThree(String c_hawb) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_DELIVERY_DETAILS + " WHERE " + H_CODE + " = '" + c_hawb + "'";
        db.execSQL(query);
    }

    public int DashImageSync() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + DELIVERY_IMAGE + " FROM " + TABLE_DELIVERY_DETAILS;
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    //Table four
    public boolean addDataToTableFour(String item, String clientNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(HAWB_CODE, item);
        contentValues.put(CLIENT_NUMBER, clientNumber);
        long result = db.insert(TABLE_HAWB_CODES, null, contentValues);
        db.close();
        return result != -1;
    }

    public Cursor getDataFromTableFour() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_HAWB_CODES;
        return db.rawQuery(query, null);
    }

    public void deleteHawbFromTableFour(String h_code) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_HAWB_CODES + " WHERE " + HAWB_CODE + " = '" + h_code + "' OR " + CLIENT_NUMBER + " = '" + h_code + "'";
        db.execSQL(query);
    }

    public boolean CheckHawbCode(String cod) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_HAWB_CODES + " WHERE " + HAWB_CODE + " = '" + cod + "' OR " + CLIENT_NUMBER + " = '" + cod + "'";
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

    public String CheckClientNumber(String cli_num) {
        String text = "";
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + HAWB_CODE + " FROM " + TABLE_HAWB_CODES + " WHERE " + CLIENT_NUMBER + " = '" + cli_num + "'";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                text = cursor.getString(cursor.getColumnIndex(HAWB_CODE));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return text;
    }

    public boolean CheckHawbCode1(String cod) {
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
        return result != -1;
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
        String query = "UPDATE " + TABLE_SCANNER_DETAILS + " SET " + TICK_MARK + "= 'true' WHERE " + COLETA_ID + " ='" + tick + "'";
        db.execSQL(query);
    }

    public void DeleteFromTableFiveUponUpload(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_SCANNER_DETAILS + " WHERE " + COLETA_ID + " = '" + id + "'";
        db.execSQL(query);
    }

    public int DashCollectSyncCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_SCANNER_DETAILS + " WHERE " + TICK_MARK + " = 'true' ";
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
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
        return result != -1;
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
//        Bitmap imageToStoreBitmap = notCollectedModal.getImage();
//        objectByteArrayOutputStream2 = new ByteArrayOutputStream();
//        imageToStoreBitmap.compress(Bitmap.CompressFormat.JPEG, 100, objectByteArrayOutputStream2);
//        imageInByte2 = objectByteArrayOutputStream2.toByteArray();

        contentValues.put(COLETA_ID, notCollectedModal.getCollectID());
        contentValues.put(DATE_TIME, notCollectedModal.getDateTime());
        contentValues.put(TYPE_PROCESS, notCollectedModal.getType());
        contentValues.put(LATITUDE, notCollectedModal.getLatitude());
        contentValues.put(LONGITUDE, notCollectedModal.getLongitude());
        contentValues.put(BATTERY_LEVEL, notCollectedModal.getBatteryPercentage());
        contentValues.put(COLLECT_IMAGE, notCollectedModal.getImage());

        long result = db.insert(TABLE_NOT_COLLECTED_DETAILS, null, contentValues);
        db.close();
        return result != -1;
    }

    public Cursor GetDataFromTableSeven() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NOT_COLLECTED_DETAILS;
        return db.rawQuery(query, null);
    }

    public void DeleteFromTableSevenUponSync() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NOT_COLLECTED_DETAILS;
        db.execSQL(query);
    }

    public int DashNotCollectImageSync() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLLECT_IMAGE + " FROM " + TABLE_NOT_COLLECTED_DETAILS;
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    //Dashboard Screen
    //Table eight
    public boolean AddDeliveryType(String d_type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TOTAL_LIST_COUNT, d_type);

        long result = db.insert(TABLE_TOTAL_DELIVERY_RETURNS, null, contentValues);
        db.close();
        return result != -1;
    }

    public int TotalDeliveryCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_TOTAL_DELIVERY_RETURNS + " WHERE " + TOTAL_LIST_COUNT + " = 'ENTREGA'";
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public int TotalReturnCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_TOTAL_DELIVERY_RETURNS + " WHERE " + TOTAL_LIST_COUNT + " = 'DEVOLUCAO'";
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public void DeleteTableEight() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_TOTAL_DELIVERY_RETURNS;
        db.execSQL(query);
    }

    //Table nine
    public boolean AddCollectsForCount(String c_type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TOTAL_COLLECT_COUNT, c_type);

        long result = db.insert(TABLE_TOTAL_COLLECT_DONE, null, contentValues);
        db.close();
        return result != -1;
    }

    public int TotalCollectCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_TOTAL_COLLECT_DONE + " WHERE " + TOTAL_COLLECT_COUNT + " = 'COLECTA'";
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public void DeleteTableNine() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_TOTAL_COLLECT_DONE;
        db.execSQL(query);
    }

    //Research Screen
    //Table ten
    public boolean AddResearchList(ResearchListModal listModal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(HAWB_CODE, listModal.getHawbCode());
        contentValues.put(NUMBER_ORDER_CLIENT, listModal.getClientNumber());
        contentValues.put(RECIPIENT_NAME, listModal.getRecipientName());
        contentValues.put(DNA, listModal.getDna());
        contentValues.put(APT_NO, listModal.getAptNo());
        contentValues.put(PUBLIC_PLACE, listModal.getPublicPlace());
        contentValues.put(STREET_NAME, listModal.getStreetName());
        contentValues.put(CITY, listModal.getCity());
        contentValues.put(STATE, listModal.getState());
        contentValues.put(PINCODE, listModal.getPinCode());
        contentValues.put(TICK_MARK, "false");
        contentValues.put(CUSTOMER_ID, listModal.getClientID());
        contentValues.put(CONTRACT_ID, listModal.getContractID());
        contentValues.put(LIST, listModal.getListCode());

        long result = db.insert(TABLE_RESEARCH_LIST, null, contentValues);
        db.close();
        return result != -1;
    }

    public Cursor GetResearchList() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_RESEARCH_LIST;
        return db.rawQuery(query, null);
    }

    public void CheckTickMarkInResearchLists() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_RESEARCH_LIST + " SET " + TICK_MARK + "= 'true'";
        db.execSQL(query);
    }

    public void DeleteDataFromResearchList(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_RESEARCH_LIST + " WHERE " + HAWB_CODE + " = '" + id + "'";
        db.execSQL(query);
    }

    public void DeleteResearchList() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_RESEARCH_LIST;
        db.execSQL(query);
    }

    public int TotalResearchPendingCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_RESEARCH_LIST + " WHERE " + TICK_MARK + " = 'false'";
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public boolean CheckResearchListData(String researchList) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_RESEARCH_LIST + " WHERE " + HAWB_CODE + " = '" + researchList + "'";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    //Table eleven
    public boolean AddResearchDetails(SaveResearchDetailsModal researchDetailsModal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(HAWB_CODE, researchDetailsModal.getHawb_code());
        contentValues.put(DATE_TIME, researchDetailsModal.getDateTime());
        contentValues.put(BATTERY_LEVEL, researchDetailsModal.getBatteryLevel());
        contentValues.put(LATITUDE, researchDetailsModal.getLatitude());
        contentValues.put(LONGITUDE, researchDetailsModal.getLongitude());
        contentValues.put(XML_RESEARCH, researchDetailsModal.getXmlBody());
        contentValues.put(LIST, researchDetailsModal.getListCodes());

        long result = db.insert(TABLE_SAVE_RESEARCH_DETAILS, null, contentValues);
        db.close();
        return result != -1;
    }

    public Cursor GetResearchDetails() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_SAVE_RESEARCH_DETAILS;
        return db.rawQuery(query, null);
    }

    public void DeleteFromResearchDetails() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_SAVE_RESEARCH_DETAILS;
        db.execSQL(query);
    }

    //Table twelve
    public boolean AddResearchImages(String imagePath, String imageName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(RESEARCH_IMAGES, imagePath);
        contentValues.put(IMAGE_NAME, imageName);
        long result = db.insert(TABLE_RESEARCH_IMAGES, null, contentValues);
        db.close();
        return result != -1;
    }

    public Cursor GetResearchImages() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_RESEARCH_IMAGES;
        return db.rawQuery(query, null);
    }

    public void DeleteResearchImages() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_RESEARCH_IMAGES;
        db.execSQL(query);
    }

}

package com.example.servicemanager;

import static com.example.servicemanager.Utils.*;
import static com.example.servicemanager.Constants.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

  public static final String DATABASE_NAME = "SMDEV.db";
  public static final String CUSTOMERS = "CUSTOMERS";
  public static final String ID = "ID";
  public static final String NAME = "NAME";
  public static final String ADDRESS = "ADDRESS";
  public static final String CONTACT_NO = "CONTACT_NO";
  public static final String EMAIL_ID = "EMAIL_ID";
  public static final String PRODUCT_NAME = "PRODUCT_NAME";
  public static final String PRODUCT_MODEL_NO = "PRODUCT_MODEL_NO";
  public static final String PRODUCT_PRICE = "PRODUCT_PRICE";
  public static final String SELLING_DATE = "SELLING_DATE";
  public static final String LAST_SERVICE_DATE = "LAST_SERVICE_DATE";
  public static final String NEXT_SERVICE_DATE = "NEXT_SERVICE_DATE";
  public static final String TOTAL_SERVICE_COUNT = "TOTAL_SERVICE_COUNT";
  public static final String TIME_STAMP = "TIME_STAMP";
  public static final String IS_OBSOLATE = "IS_OBSOLATE";
  public static final String SERVICE_NO = "SERVICENO";
  public static final String SERVICE_DATE = "SERVICE_DATE";
  public static final String SERVICES = "SERVICES";
  public static final String GLOBAL_PARAMS = "GLOBAL_PARAMS";
  public static final String PARAM_NAME = "PARAM_NAME";
  public static final String PARAM_VALUE = "PARAM_VALUE";
  public static final String COMMENT = "COMMENT";

  public DBHelper(Context context) {
    super(context, DATABASE_NAME, null, 1);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    createCustomersTable(db);
    createServicesTable(db);
    createProductsTable(db);
    createGlobalParamsTable(db);
    insertDefaultData(db);
  }

  private void insertDefaultData(SQLiteDatabase db) {

    insertGlobalParam(db, USER_NAME);
    insertGlobalParam(db, PASSWORD);
    insertGlobalParam(db, DRIVE_FOLDER_ID);
    insertGlobalParam(db, DRIVE_FILE_ID);
  }

  private void insertGlobalParam(SQLiteDatabase db, String paramName) {
    ContentValues contentValues = new ContentValues();
    contentValues.put(PARAM_NAME, paramName);
    contentValues.put(PARAM_VALUE, "");
    contentValues.put(COMMENT, "");
    contentValues.put(IS_OBSOLATE, 0);
    contentValues.put(TIME_STAMP, new Date().toString());
    db.insert(GLOBAL_PARAMS, null, contentValues);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + CUSTOMERS);
    db.execSQL("DROP TABLE IF EXISTS " + SERVICES);
    db.execSQL("DROP TABLE IF EXISTS " + GLOBAL_PARAMS);
    onCreate(db);
  }

  public void createCustomersTable(SQLiteDatabase db) {
    db.execSQL("CREATE TABLE CUSTOMERS (" + "ID INTEGER PRIMARY KEY, " + "NAME TEXT, "
        + "ADDRESS TEXT, " + "CONTACT_NO NUMERIC, " + "EMAIL_ID TEXT, " + "PRODUCT_NAME TEXT, "
        + "PRODUCT_MODEL_NO TEXT, " + "PRODUCT_PRICE NUMERIC, " + "SELLING_DATE TEXT, "
        + "LAST_SERVICE_DATE TEXT, " + "NEXT_SERVICE_DATE TEXT, "/* + "SERVICE_DURATION TEXT, " */ // based
                                                                                                   // on
                                                                                                   // service_duration
                                                                                                   // from
                                                                                                   // PRODUCTS
                                                                                                   // table
        + "TOTAL_SERVICE_COUNT NUMERIC, " + "TIME_STAMP TEXT, " + "IS_OBSOLATE INTEGER " + ")");
  }

  public void createServicesTable(SQLiteDatabase db) {
    db.execSQL("CREATE TABLE SERVICES (" + "ID INTEGER, " + "SERVICE_NO INTEGER, "
        + "SERVICE_DATE TEXT, " + "SERVICE_AMOUNT NUMERIC, " + "SERVICE_NOTES TEXT, "
        + "TIME_STAMP TEXT, " + "IS_OBSOLATE INTEGER, " + "PRIMARY KEY (ID, SERVICE_NO), "
        + "FOREIGN KEY (ID) REFERENCES CUSTOMERS(ID) " + ")");
  }

  private void createGlobalParamsTable(SQLiteDatabase db) {
    db.execSQL("CREATE TABLE " + GLOBAL_PARAMS + " ( " + "PARAM_NAME TEXT PRIMARY KEY, "
        + "PARAM_VALUE TEXT, " + "COMMENT TEXT, " + "IS_OBSOLATE INTEGER, " + "TIME_STAMP TEXT "
        + ")");
  }

  public void createProductsTable(SQLiteDatabase db) {
    db.execSQL("CREATE TABLE PRODUCTS (" + "ID INTEGER, " + "PRODUCT_NAME TEXT, "
        + "PRODUCT_MODEL_NO TEXT, " + "SERVICE_DURATION NUMERIC, " + "SERVICE_NOTES TEXT, "
        + "TIME_STAMP TEXT, " + "IS_OBSOLATE INTEGER, "
        + "PRIMARY KEY (PRODUCT_NAME, PRODUCT_MODEL_NO) " + ")");
  }

  public boolean updateGlobalParam(String name, String value, String comment) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put(PARAM_VALUE, value);
    contentValues.put(COMMENT, comment);

    db.update(GLOBAL_PARAMS, contentValues, "PARAM_NAME = ? ", new String[] {name});
    return true;
  }

  public boolean insertCustomer(Customer customer) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put(NAME, customer.getName());
    contentValues.put(ADDRESS, customer.getAddress());
    contentValues.put(CONTACT_NO, customer.getContactNo());
    contentValues.put(EMAIL_ID, customer.getEmailId());
    contentValues.put(CONTACT_NO, customer.getContactNo());
    contentValues.put(PRODUCT_NAME, customer.getProductName());
    contentValues.put(PRODUCT_MODEL_NO, customer.getProductModelNo());
    contentValues.put(PRODUCT_PRICE, customer.getProductPrice());
    contentValues.put(SELLING_DATE, customer.getSellingDate());
    contentValues.put(LAST_SERVICE_DATE, customer.getLastServiceDate());
    contentValues.put(NEXT_SERVICE_DATE, customer.getNextServiceDate());
    contentValues.put(TOTAL_SERVICE_COUNT, customer.getTotalServiceCount());
    contentValues.put(TIME_STAMP, customer.getTimeStamp());
    contentValues.put(IS_OBSOLATE, customer.getIsObsolate());
    db.insert(CUSTOMERS, null, contentValues);
    return true;
  }

  public boolean updateCustomerData(Customer customer) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();

    contentValues.put(NAME, customer.getName());
    contentValues.put(ADDRESS, customer.getAddress());
    contentValues.put(CONTACT_NO, customer.getContactNo());
    contentValues.put(EMAIL_ID, customer.getEmailId());
    contentValues.put(CONTACT_NO, customer.getContactNo());
    contentValues.put(PRODUCT_NAME, customer.getProductName());
    contentValues.put(PRODUCT_MODEL_NO, customer.getProductModelNo());
    contentValues.put(PRODUCT_PRICE, customer.getProductPrice());
    contentValues.put(SELLING_DATE, customer.getSellingDate());
    contentValues.put(LAST_SERVICE_DATE, customer.getLastServiceDate());
    contentValues.put(NEXT_SERVICE_DATE, customer.getNextServiceDate());
    contentValues.put(TOTAL_SERVICE_COUNT, customer.getTotalServiceCount());
    contentValues.put(TIME_STAMP, customer.getTimeStamp());
    contentValues.put(IS_OBSOLATE, customer.getIsObsolate());

    db.update(CUSTOMERS, contentValues, "ID = ? ",
        new String[] {Integer.toString(customer.getId())});
    return true;
  }

  public Integer deleteCustomer(Customer customer) {
    SQLiteDatabase db = this.getWritableDatabase();
    deleteServices(customer);
    return db.delete(CUSTOMERS, "ID = ? ", new String[] {Integer.toString(customer.getId())});
  }

  public Integer deleteServices(Customer customer) {
    SQLiteDatabase db = this.getWritableDatabase();
    return db.delete(SERVICES, "ID = ? ", new String[] {Integer.toString(customer.getId())});
  }

  public List<Customer> getAllCustomers() {
    List<Customer> customers = new LinkedList<Customer>();
    String query = "SELECT  * FROM " + CUSTOMERS;

    SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor = db.rawQuery(query, null);

    if (cursor.moveToFirst()) {
      do {
        customers.add(getCustomer(cursor));
      } while (cursor.moveToNext());
    }
    return customers;
  }

  private Customer getCustomer(Cursor cursor) {
    Customer customer = new Customer();
    customer.setId(Integer.parseInt(cursor.getString(0)));
    customer.setName(cursor.getString(1));
    customer.setAddress(cursor.getString(2));
    customer.setContactNo(cursor.getString(3));
    customer.setEmailId(cursor.getString(4));
    customer.setProductName(cursor.getString(5));
    customer.setProductModelNo(cursor.getString(6));
    customer.setProductPrice(Integer.parseInt(cursor.getString(7)));
    customer.setSellingDate(cursor.getString(8));
    customer.setLastServiceDate(cursor.getString(9));
    customer.setNextServiceDate(cursor.getString(10));
    customer.setTotalServiceCount(Integer.parseInt(cursor.getString(11)));
    customer.setTimeStamp(cursor.getString(12));
    customer.setIsObsolate(Integer.parseInt(cursor.getString(13)));
    return customer;
  }

  public List<Customer> getDueServices() throws java.text.ParseException {
    List<Customer> customers = new LinkedList<Customer>();
    String query = "SELECT  * FROM " + CUSTOMERS;

    SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor = db.rawQuery(query, null);

    Calendar oldDate = Calendar.getInstance();
    oldDate.setTime(new Date());
    oldDate = getFormatedDate(oldDate);
    oldDate.add(Calendar.MONTH, -3);

    for (int i = 0; i < cursor.getCount(); i++) {
      cursor.moveToNext();
      Calendar lastService = getDate(cursor.getString(9));

      // TODO max limit of services
      //int serviceCount = Integer.parseInt(cursor.getString(8));

      if (lastService.compareTo(oldDate) <= 0 /*&& serviceCount <= 4*/) {
        customers.add(getCustomer(cursor));
      }
    }
    return customers;
  }

  // TODO need to correct logic, just copied form above method
  public List<Customer> getUpcomingServices() throws java.text.ParseException {
    List<Customer> customers = new LinkedList<Customer>();
    String query = "SELECT  * FROM " + CUSTOMERS;

    SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor = db.rawQuery(query, null);

    Calendar oldDate = Calendar.getInstance();
    oldDate.setTime(new Date());
    oldDate = getFormatedDate(oldDate);
    oldDate.add(Calendar.MONTH, -3);
    oldDate.add(Calendar.DATE, 10);

    for (int i = 0; i < cursor.getCount(); i++) {
      cursor.moveToNext();
      Calendar lastService = getDate(cursor.getString(9));

      //int serviceCount = Integer.parseInt(cursor.getString(8));

      if (lastService.compareTo(oldDate) <= 0/* && serviceCount <= 4*/) {
        customers.add(getCustomer(cursor));
      }
    }
    return customers;
  }

  /**
   * @param id
   * @return TODO
   */
  public List<List<String>> getServiceHistory(int id) {

    String query = "SELECT  * FROM " + SERVICES + " WHERE " + ID + " = " + id;

    SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor = db.rawQuery(query, null);

    List<String> serviceNos = new ArrayList<String>();
    List<String> serviceDates = new ArrayList<String>();
    if (cursor.moveToFirst()) {
      do {
        serviceNos.add(cursor.getString(1));
        serviceDates.add(cursor.getString(2));
      } while (cursor.moveToNext());
    }

    List<List<String>> services = new ArrayList<List<String>>();
    services.add(serviceNos);
    services.add(serviceDates);

    return services;
  }

  public boolean insertService(Customer customer) {

    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put(ID, customer.getId());
    contentValues.put(SERVICE_NO, customer.getTotalServiceCount());
    contentValues.put(SERVICE_DATE, customer.getLastServiceDate());
    db.execSQL("INSERT INTO SERVICES(ID, SERVICE_NO, SERVICE_DATE) VALUES(" + "'" + customer.getId()
        + "', " + customer.getTotalServiceCount() + ", '" + customer.getLastServiceDate() + "');");
    return true;
  }

  public String getGlobalParam(String paramName) {

    String query =
        "SELECT  * FROM " + GLOBAL_PARAMS + " WHERE " + PARAM_NAME + " = '" + paramName + "'";

    SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor = db.rawQuery(query, null);
    if (cursor.moveToFirst()) {
      return cursor.getString(1);
    }
    return null;
  }
}

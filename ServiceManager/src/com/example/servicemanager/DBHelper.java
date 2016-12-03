package com.example.servicemanager;

import static com.example.servicemanager.Utils.*;

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

	public static final String DATABASE_NAME = "MyDBName.db";
	public static final String CUSTOMERS = "CUSTOMERS";
	public static final String ID = "ID";
	public static final String NAME = "NAME";
	public static final String ADDRESS = "ADDRESS";
	public static final String MOBILE = "MOBILE";
	public static final String MODEL = "MODEL";
	public static final String PRICE = "PRICE";
	public static final String SELLING_DATE = "SELLING_DATE";
	public static final String LAST_SERVICE = "LAST_SERVICE";
	public static final String SERVICE_COUNT = "SERVICE_COUNT";
	public static final String SERVICE_NO = "SERVICENO";
	public static final String SERVICE_DATE = "SERVICE_DATE";
	public static final String SERVICES = "SERVICES";

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE CUSTOMERS " + "(ID INTEGER PRIMARY KEY, " + "NAME TEXT UNIQUE, " + "ADDRESS TEXT, "
				+ "MOBILE NUMERIC, " + "MODEL TEXT, " + "PRICE NUMERIC, " + "SELLING_DATE TEXT , "
				+ "LAST_SERVICE TEXT, " + "SERVICE_COUNT NUMERIC )");

		db.execSQL("CREATE TABLE SERVICES " + "(ID INTEGER, " + "SERVICE_NO INTEGER, " + "SERVICE_DATE TEXT, "
				+ "PRIMARY KEY (ID, SERVICE_NO), " + "FOREIGN KEY (ID) REFERENCES CUSTOMERS(ID) ) ");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS CUSTOMERS");
		db.execSQL("DROP TABLE IF EXISTS SERVICES");
		onCreate(db);
	}

	public boolean insertCustomer(Customer customer) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(NAME, customer.getName());
		contentValues.put(ADDRESS, customer.getAddress());
		contentValues.put(MOBILE, customer.getMobile());
		contentValues.put(MODEL, customer.getModel());
		contentValues.put(PRICE, customer.getPrice());
		contentValues.put(SELLING_DATE, customer.getSellingDate());
		contentValues.put(LAST_SERVICE, customer.getLastService());
		contentValues.put(SERVICE_COUNT, customer.getServiceCount());
		db.insert(CUSTOMERS, null, contentValues);
		return true;
	}

	public boolean updateCustomerData(Customer customer) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(NAME, customer.getName());
		contentValues.put(ADDRESS, customer.getAddress());
		contentValues.put(MOBILE, customer.getMobile());
		contentValues.put(MODEL, customer.getModel());
		contentValues.put(PRICE, customer.getPrice());
		contentValues.put(SELLING_DATE, customer.getSellingDate());
		contentValues.put(LAST_SERVICE, customer.getLastService());
		contentValues.put(SERVICE_COUNT, customer.getServiceCount());
		db.update(CUSTOMERS, contentValues, "ID = ? ", new String[] { Integer.toString(customer.getId()) });
		return true;
	}

	public Integer deleteCustomer(Customer customer) {
		SQLiteDatabase db = this.getWritableDatabase();
		deleteServices(customer);
		return db.delete(CUSTOMERS, "ID = ? ", new String[] { Integer.toString(customer.getId()) });
	}

	public Integer deleteServices(Customer customer) {
		SQLiteDatabase db = this.getWritableDatabase();
		return db.delete(SERVICES, "ID = ? ", new String[] { Integer.toString(customer.getId()) });
	}

	public List<Customer> getAllCustomers() {
		List<Customer> customers = new LinkedList<Customer>();
		String query = "SELECT  * FROM " + CUSTOMERS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		Customer customer = null;
		if (cursor.moveToFirst()) {
			do {
				customer = new Customer();
				customer.setId(Integer.parseInt(cursor.getString(0)));
				customer.setName(cursor.getString(1));
				customer.setAddress(cursor.getString(2));
				customer.setMobile(cursor.getString(3));
				customer.setModel(cursor.getString(4));
				customer.setPrice(Integer.parseInt(cursor.getString(5)));
				customer.setSellingDate(cursor.getString(6));
				customer.setLastService(cursor.getString(7));
				customer.setServiceCount(Integer.parseInt(cursor.getString(8)));
				customers.add(customer);
			} while (cursor.moveToNext());
		}
		return customers;
	}

	public List<Customer> getDueServices() throws java.text.ParseException {
		List<Customer> customers = new LinkedList<Customer>();
		String query = "SELECT  * FROM " + CUSTOMERS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		Customer customer = new Customer();

		Calendar oldDate = Calendar.getInstance();
		oldDate.setTime(new Date());
		oldDate = getFormatedDate(oldDate);
		oldDate.add(Calendar.MONTH, -3);

		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToNext();
			Calendar lastService = getDate(cursor.getString(7));

			int serviceCount = Integer.parseInt(cursor.getString(8));

			if (lastService.compareTo(oldDate) <= 0 && serviceCount <= 4) {
				customer = new Customer();
				customer.setId(Integer.parseInt(cursor.getString(0)));
				customer.setName(cursor.getString(1));
				customer.setAddress(cursor.getString(2));
				customer.setMobile(cursor.getString(3));
				customer.setModel(cursor.getString(4));
				customer.setPrice(Integer.parseInt(cursor.getString(5)));
				customer.setSellingDate(cursor.getString(6));
				customer.setLastService(cursor.getString(7));
				customer.setServiceCount(serviceCount);
				customers.add(customer);
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

		Customer customer = new Customer();

		Calendar oldDate = Calendar.getInstance();
		oldDate.setTime(new Date());
		oldDate = getFormatedDate(oldDate);
		oldDate.add(Calendar.MONTH, -3);

		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToNext();
			Calendar lastService = getDate(cursor.getString(7));

			int serviceCount = Integer.parseInt(cursor.getString(8));

			if (lastService.compareTo(oldDate) <= 0 && serviceCount <= 4) {
				customer = new Customer();
				customer.setId(Integer.parseInt(cursor.getString(0)));
				customer.setName(cursor.getString(1));
				customer.setAddress(cursor.getString(2));
				customer.setMobile(cursor.getString(3));
				customer.setModel(cursor.getString(4));
				customer.setPrice(Integer.parseInt(cursor.getString(5)));
				customer.setSellingDate(cursor.getString(6));
				customer.setLastService(cursor.getString(7));
				customer.setServiceCount(serviceCount);
				customers.add(customer);
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
		contentValues.put(SERVICE_NO, customer.getServiceCount());
		contentValues.put(SERVICE_DATE, customer.getLastService());
		db.execSQL("INSERT INTO SERVICES(ID, SERVICE_NO, SERVICE_DATE) VALUES(" + "'" + customer.getId() + "', " + customer.getServiceCount() + ", '" + customer.getLastService() + "');");
		return true;
	}
}
package com.example.servicemanager;

import android.os.Parcel;
import android.os.Parcelable;

public class Customer implements Parcelable {

	private int id;
	private String name;
	private String address;
	private String mobile;
	private String model;
	private int price;
	private String sellingDate;
	private String lastService;
	private int serviceCount;

	public Customer(int id, String name, String address, String mobile, String model, int price, String sellingDate,
			String lastService, int serviceCount) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.mobile = mobile;
		this.model = model;
		this.price = price;
		this.sellingDate = sellingDate;
		this.lastService = lastService;
		this.serviceCount = serviceCount;
	}

	public Customer() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getSellingDate() {
		return sellingDate;
	}

	public void setSellingDate(String sellingDate) {
		this.sellingDate = sellingDate;
	}

	public String getLastService() {
		return lastService;
	}

	public void setLastService(String lastService) {
		this.lastService = lastService;
	}

	// getters & setters
	//
	// @Override
	// public String toString() {
	// return "Book [id=" + id + ", title=" + title + ", author=" + author
	// + "]";
	// }

	public int getServiceCount() {
		return serviceCount;
	}

	public void setServiceCount(int serviceCount) {
		this.serviceCount = serviceCount;
	}

	/* everything below here is for implementing Parcelable */

	// 99.9% of the time you can just ignore this
	public int describeContents() {
		return 0;
	}

	// write your object's data to the passed-in Parcel
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(id);
		out.writeString(name);
		out.writeString(address);
		out.writeString(mobile);
		out.writeString(model);
		out.writeInt(price);
		out.writeString(sellingDate);
		out.writeString(lastService);
		out.writeInt(serviceCount);
	}

	// this is used to regenerate your object. All Parcelables must have a
	// CREATOR that implements these two methods
	public static final Parcelable.Creator<Customer> CREATOR = new Parcelable.Creator<Customer>() {
		public Customer createFromParcel(Parcel in) {
			return new Customer(in);
		}

		public Customer[] newArray(int size) {
			return new Customer[size];
		}
	};

	// example constructor that takes a Parcel and gives you an object populated
	// with it's values
	private Customer(Parcel in) {
		id = in.readInt();
		name = in.readString();
		address = in.readString();
		mobile = in.readString();
		model = in.readString();
		price = in.readInt();
		sellingDate = in.readString();
		lastService = in.readString();
		serviceCount = in.readInt();
	}
}
package com.example.servicemanager;

import android.os.Parcel;
import android.os.Parcelable;

public class Customer implements Parcelable {

  private int id;
  private String name;
  private String address;
  private String contactNo;
  private String emailId;
  private String productName;
  private String productModelNo;
  private int productPrice;
  private String sellingDate;
  private String lastServiceDate;
  private String nextServiceDate;
  private int totalServiceCount;
  private String timeStamp;
  private int isObsolate;
  
  public Customer(int id, String name, String address, String contactNo, String emailId, 
		          String productName, String productModelNo,
                  int productPrice, String sellingDate, String lastServiceDate, String nextServiceDate, 
                  int totalServiceCount, String timeStamp, int isObsolate) {
    super();
    this.id = id;
    this.name = name;
    this.address = address;
    this.contactNo = contactNo;
    this.emailId = emailId;
    this.productName = productName;
    this.productModelNo = productModelNo;
    this.productPrice = productPrice;
    this.sellingDate = sellingDate;
    this.lastServiceDate = lastServiceDate;
    this.nextServiceDate = nextServiceDate;
    this.totalServiceCount = totalServiceCount;
    this.timeStamp = timeStamp;
    this.isObsolate = isObsolate;
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

  public String getEmailId() {
    return emailId;
  }

  public void setEmailId(String emailId) {
    this.emailId = emailId;
  }

  public String getContactNo() {
    return contactNo;
  }

  public void setContactNo(String contactNo) {
    this.contactNo = contactNo;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public String getProductModelNo() {
    return productModelNo;
  }

  public void setProductModelNo(String productModelNo) {
    this.productModelNo = productModelNo;
  }

  public int getProductPrice() {
    return productPrice;
  }

  public void setProductPrice(int productPrice) {
    this.productPrice = productPrice;
  }

  public String getSellingDate() {
    return sellingDate;
  }

  public void setSellingDate(String sellingDate) {
    this.sellingDate = sellingDate;
  }

  public String getLastServiceDate() {
    return lastServiceDate;
  }

  public void setLastServiceDate(String lastServiceDate) {
    this.lastServiceDate = lastServiceDate;
  }

  public String getNextServiceDate() {
    return nextServiceDate;
  }

  public void setNextServiceDate(String nextServiceDate) {
    this.nextServiceDate = nextServiceDate;
  }

  // getters & setters
  //
  // @Override
  // public String toString() {
  // return "Book [id=" + id + ", title=" + title + ", author=" + author
  // + "]";
  // }

  public int getTotalServiceCount() {
    return totalServiceCount;
  }

  public void setTotalServiceCount(int totalServiceCount) {
    this.totalServiceCount = totalServiceCount;
  }
  
  public String getTimeStamp() {
    return timeStamp;
  }

  public void setTimeStamp(String timeStamp) {
    this.timeStamp = timeStamp;
  }
  
  public int getIsObsolate() {
    return isObsolate;
  }

  public void setIsObsolate(int isObsolate) {
    this.isObsolate = isObsolate;
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
    out.writeString(contactNo);
    out.writeString(emailId);
    out.writeString(productName);
    out.writeString(productModelNo);
    out.writeInt(productPrice);
    out.writeString(sellingDate);
    out.writeString(lastServiceDate);
    out.writeString(nextServiceDate);
    out.writeInt(totalServiceCount);
    out.writeString(timeStamp);
    out.writeInt(isObsolate);
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
    contactNo = in.readString();
    emailId = in.readString();
    productName = in.readString();
    productModelNo = in.readString();
    productPrice = in.readInt();
    sellingDate = in.readString();
    lastServiceDate = in.readString();
    nextServiceDate = in.readString();
    totalServiceCount = in.readInt();
    timeStamp = in.readString();
    isObsolate = in.readInt();
  }
}
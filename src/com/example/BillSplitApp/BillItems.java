package com.example.BillSplitApp;

import android.os.Parcel;
import android.os.Parcelable;

public class BillItems implements Parcelable {

	private String name = "";
	private boolean checked = true;

	public BillItems() {
	}

	public BillItems(String name) {
		this.name = name;
	}

	public BillItems(String name, boolean checked) {
		this.name = name;
		this.checked = checked;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String toString() {
		return name;
	}

	public void toggleChecked() {
		checked = !checked;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(name);
		dest.writeInt(checked ? 1 : 0);
	}
	
	private BillItems(Parcel in){
        this.name = in.readString();
        this.checked = (in.readInt() == 0) ? false : true;
	}
	
	public static final Parcelable.Creator<BillItems> CREATOR = new Parcelable.Creator<BillItems>() {
		 
        @Override
        public BillItems createFromParcel(Parcel source) {
            return new BillItems(source);
        }
 
        @Override
        public BillItems[] newArray(int size) {
            return new BillItems[size];
        }
    };
}


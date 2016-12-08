package com.example.servicemanager;

import static com.example.servicemanager.Constants.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter {

	// Declare Variables
	Context mContext;
	LayoutInflater inflater;
	private List<Customer> searchCustomerList = null;
	private ArrayList<Customer> arraylist;

	public ListViewAdapter(Context context, List<Customer> customersList) {
		mContext = context;
		this.searchCustomerList = customersList;
		inflater = LayoutInflater.from(mContext);
		this.arraylist = new ArrayList<Customer>();
		this.arraylist.addAll(customersList);
	}

	public class ViewHolder {
		TextView name;
		TextView address;
		TextView mobile;
	}

	@Override
	public int getCount() {
		return searchCustomerList.size();
	}

	@Override
	public Customer getItem(int position) {
		return searchCustomerList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.listview_item, null);
			// Locate the TextViews in listview_item.xml
			holder.name = (TextView) view.findViewById(R.id.displayName);
			holder.address = (TextView) view.findViewById(R.id.displayAddress);
			holder.mobile = (TextView) view.findViewById(R.id.displayMobile);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		// Set the results into TextViews
		holder.name.setText(searchCustomerList.get(position).getName());
		holder.address.setText(searchCustomerList.get(position).getAddress());
		holder.mobile.setText(String.valueOf(searchCustomerList.get(position).getContactNo()));

		// Listen for ListView Item Click
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// Send single item click data to SingleItemView Class
				Intent intent = new Intent(mContext, com.example.servicemanager.SingleItemView.class);
				intent.putExtra(CUSTOMER, searchCustomerList.get(position));

				mContext.startActivity(intent);
			}
		});

		return view;
	}

	// Filter Class
	public void filter(String charText, String calledFrom) {
		charText = charText.toLowerCase(Locale.getDefault());
		searchCustomerList.clear();
		if (charText.length() == 0) {
			searchCustomerList.addAll(arraylist);
		} else {
			for (Customer wp : arraylist) {
				if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
					searchCustomerList.add(wp);
				} else if (wp.getAddress().toLowerCase(Locale.getDefault()).contains(charText)) {
					searchCustomerList.add(wp);
				}else if (wp.getContactNo().toLowerCase(Locale.getDefault()).contains(charText)) {
					searchCustomerList.add(wp);
				}
			}
		}
		notifyDataSetChanged();
	}

}

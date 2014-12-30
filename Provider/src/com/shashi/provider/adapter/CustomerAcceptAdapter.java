package com.shashi.provider.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shashi.provider.R;
import com.shashi.provider.db.DataBaseHelper;
import com.shashi.provider.db.ProviderDatabase;

public class CustomerAcceptAdapter extends BaseAdapter {

	Context context;
	Helper helper;
	List<ProviderDatabase> list;
	DataBaseHelper dataBaseHelper;

	public CustomerAcceptAdapter(Context context,
			DataBaseHelper dataBaseHelper, List<ProviderDatabase> list) {
		this.context = context;
		this.dataBaseHelper = dataBaseHelper;
		this.list = list;
	}

	public void update() {
		list = dataBaseHelper.getCustomerAccepted();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.customer_accept, arg2, false);
			helper = new Helper();
			helper.customer = (TextView) view.findViewById(R.id.customer_name);
			helper.time = (TextView) view.findViewById(R.id.time_to);
			helper.location = (TextView) view.findViewById(R.id.location_to);
			view.setTag(helper);
		} else {
			helper = (Helper) view.getTag();
		}
		helper.customer.setText(list.get(position).getCustomerName());
		helper.time.setText(list.get(position).getTimeToService());
		helper.location.setText(list.get(position).getLocationToService());
		return view;
	}

	private static class Helper {
		TextView time, customer, location;
	}

}

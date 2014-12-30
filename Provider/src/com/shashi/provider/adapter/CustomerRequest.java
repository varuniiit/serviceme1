package com.shashi.provider.adapter;

import java.util.List;

import com.shashi.provider.R;
import com.shashi.provider.db.DataBaseHelper;
import com.shashi.provider.db.ProviderDatabase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class CustomerRequest extends BaseAdapter {

	Context context;
	Helper helper;
	List<ProviderDatabase> list;
	DataBaseHelper dataBaseHelper;

	public CustomerRequest(Context context, DataBaseHelper dataBaseHelper,
			List<ProviderDatabase> list) {
		this.context = context;
		this.dataBaseHelper = dataBaseHelper;
		this.list = list;
	}

	public void update() {
		list = dataBaseHelper.getAllEntries();
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
			view = inflater.inflate(R.layout.customer_support, arg2, false);
			helper = new Helper();
			helper.check = (CheckBox) view.findViewById(R.id.check);
			helper.customer = (TextView) view.findViewById(R.id.customer);
			helper.time = (TextView) view.findViewById(R.id.time);
			view.setTag(helper);
		} else {
			helper = (Helper) view.getTag();
		}
		helper.customer.setText(list.get(position).getCustomerName());
		helper.time.setText("" + list.get(position).getTimeToService());
		// System.out.println(list.get(position).getTimeToService());
		if (list.get(position).getProviderAcceptedStatus().equalsIgnoreCase("true")) {
			helper.check.setChecked(true);
		} else {
			helper.check.setChecked(false);
		}
		return view;
	}

	private static class Helper {
		TextView time, customer;
		CheckBox check;
	}

}

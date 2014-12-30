package com.shashi.provider;

import java.util.List;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;

import com.shashi.provider.adapter.CustomerAcceptAdapter;
import com.shashi.provider.db.DataBaseHelper;
import com.shashi.provider.db.ProviderDatabase;

public class CustomerAccept extends ActionBarActivity {
	public static boolean isAcceptAppOpend = false;
	static ListView listView;
	static CustomerAcceptAdapter adapter;
	static DataBaseHelper dataBaseHelper;
	static List<ProviderDatabase> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_customer_accept);
		listView = (ListView) findViewById(R.id.customeracceptlist);
		dataBaseHelper = new DataBaseHelper(this);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		isAcceptAppOpend = false;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		isAcceptAppOpend = false;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		isAcceptAppOpend = true;
		list = dataBaseHelper.getCustomerAccepted();
		adapter = new CustomerAcceptAdapter(this, dataBaseHelper, list);
		listView.setAdapter(adapter);
	}

	public static void updateListView() {
		adapter.update();
		list = dataBaseHelper.getAllEntries();
		listView.setAdapter(adapter);
	}
}

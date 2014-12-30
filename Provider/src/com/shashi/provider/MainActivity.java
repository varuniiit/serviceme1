package com.shashi.provider;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.shashi.provider.adapter.CustomerRequest;
import com.shashi.provider.db.DataBaseHelper;
import com.shashi.provider.db.ProviderDatabase;

public class MainActivity extends ActionBarActivity implements OnClickListener,
		OnItemClickListener {
	public static boolean isAppOpend = false;
	static ListView listView;
	static CustomerRequest adapter;
	Button button, listButton;
	static DataBaseHelper dataBaseHelper;
	static List<ProviderDatabase> list;
	List<Integer> checkedStatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		isAppOpend = true;
		listView = (ListView) findViewById(R.id.listView1);
		listView.setOnItemClickListener(this);
		button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(this);
		listButton = (Button) findViewById(R.id.list);
		listButton.setOnClickListener(this);
		dataBaseHelper = new DataBaseHelper(this);
		checkedStatus = new ArrayList<Integer>();
		String name = getSharedPreferences("name", Context.MODE_PRIVATE)
				.getString("customername", null);
		if (name == null)
			showDialog();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		isAppOpend = false;
	}

	public static void updateListView() {
		adapter.update();
		list = dataBaseHelper.getAllEntries();
		listView.setAdapter(adapter);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		isAppOpend = false;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		isAppOpend = true;
		list = dataBaseHelper.getAllEntries();
		adapter = new CustomerRequest(this, dataBaseHelper, list);
		listView.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		if (R.id.button1 == v.getId()) {
			ParseObject parseObject;
			for (Integer i : checkedStatus) {
				parseObject = new ParseObject("ProviderResponse");
				list.get(i).setProviderAcceptedStatus("true");
				dataBaseHelper.updateProviderStatus(list.get(i));
				parseObject.add("customername", list.get(i).getCustomerName());
				parseObject
						.add("timetoservice", list.get(i).getTimeToService());
				parseObject.add("loctiontoservice", list.get(i)
						.getLocationToService());
				parseObject.add("customerid", list.get(i).getRequestId());
				parseObject.saveInBackground();
				sendPushNotification(list.get(i));
			}
		} else if (R.id.list == v.getId()) {
			Intent intent = new Intent(this, CustomerAccept.class);
			startActivity(intent);
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position,
			long arg3) {
		CheckBox checkBox = (CheckBox) view.findViewById(R.id.check);
		if (list.get(position).getProviderAcceptedStatus()
				.equalsIgnoreCase("false")) {
			if (checkBox.isChecked()) {
				checkBox.setChecked(false);
				checkedStatus.remove((Integer) position);
			} else {
				checkedStatus.add(position);
				checkBox.setChecked(true);
			}
		}
	}

	public void sendPushNotification(ProviderDatabase database) {
		try {
			ParsePush parsePush = new ParsePush();
			ParseQuery<ParseInstallation> query = ParseInstallation.getQuery();
			query.whereEqualTo("installationId", database.getInstallationId());
			parsePush.setQuery(query);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("providername", "Jhon");
			jsonObject.put("requestid", database.getRequestId());
			jsonObject.put("messagetype", "request");
			jsonObject.put("installationid", GlobalApplication.installationId);
			parsePush.setMessage(jsonObject.toString());
			parsePush.sendInBackground();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void showDialog() {
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		final View view = layoutInflater.inflate(R.layout.customer_name, null);
		final EditText editText = (EditText) view.findViewById(R.id.editText1);
		AlertDialog.Builder builder = new AlertDialog.Builder(this)
				.setTitle("Provider Name")
				.setIcon(R.drawable.ic_launcher)
				.setView(view)
				.setPositiveButton("Okay",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (editText.getText().toString().trim()
										.length() == 0) {
									showDialog();
								}
								getSharedPreferences("name",
										Context.MODE_PRIVATE)
										.edit()
										.putString("customername",
												editText.getText().toString())
										.commit();
							}
						})
				.setNegativeButton("Exit",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								MainActivity.this.finish();
							}
						});
		builder.create();
		builder.setCancelable(false);
		builder.show();
	}

}

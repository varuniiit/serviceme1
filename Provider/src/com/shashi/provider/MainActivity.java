package com.shashi.provider;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.shashi.provider.adapter.CustomerRequest;
import com.shashi.provider.db.DataBaseHelper;
import com.shashi.provider.db.ProviderDatabase;

public class MainActivity extends ActionBarActivity implements OnClickListener,
		OnItemClickListener, OnItemSelectedListener {
	public static boolean isAppOpend = false;
	static ListView listView;
	static CustomerRequest adapter;
	Button button, listButton;
	static DataBaseHelper dataBaseHelper;
	static List<ProviderDatabase> list;
	List<Integer> checkedStatus;
	List<Integer> selectedItems = new ArrayList<Integer>();
	boolean isClearClicked = false;
	String address;
	LatLng location;
	String service;
	List<String> arrayList = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		isAppOpend = true;
		listView = (ListView) findViewById(android.R.id.list);
		TextView textView = (TextView) findViewById(android.R.id.empty);
		listView.setEmptyView(textView);
		listView.setOnItemClickListener(this);
		button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(this);
		listButton = (Button) findViewById(R.id.listCustomer);
		listButton.setOnClickListener(this);
		dataBaseHelper = new DataBaseHelper(this);
		checkedStatus = new ArrayList<Integer>();
		String name = getSharedPreferences("name", Context.MODE_PRIVATE)
				.getString("customername", null);
		if (name == null)
			new Background().execute();
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
		System.out.println(MapActivity.address + " ");
		System.out.println(" " + MapActivity.finalLoc);
		if (MapActivity.address != null) {
			address = MapActivity.address;
			this.locationText.setText("Address: " + MapActivity.address);
		}
		if (MapActivity.finalLoc != null) {
			location = MapActivity.finalLoc;
		}
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
		} else if (R.id.listCustomer == v.getId()) {
			Intent intent = new Intent(this, CustomerAccept.class);
			startActivity(intent);
		} else if (R.id.map == v.getId()) {
			startActivity(new Intent(this, MapActivity.class));
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position,
			long arg3) {
		if (!isClearClicked) {
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
		} else {
			CheckedTextView checkedTextView = (CheckedTextView) view;
			if (checkedTextView.isChecked()) {
				checkedTextView.setChecked(true);
				selectedItems.add((Integer) position);
			} else {
				checkedTextView.setChecked(false);
				selectedItems.remove((Integer) position);
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
			jsonObject.put("providername",
					getSharedPreferences("name", Context.MODE_PRIVATE)
							.getString("customername", null));
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

	TextView locationText;

	private void showDialog(final List<String> arrayList) {
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		final View view = layoutInflater.inflate(R.layout.customer_name, null);
		final EditText editText = (EditText) view.findViewById(R.id.editText1);
		final Spinner spinner = (Spinner) view.findViewById(R.id.spinner1);
		locationText = (TextView) view.findViewById(R.id.locationtext);
		spinner.setOnItemSelectedListener(this);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, arrayList);
		spinner.setAdapter(adapter);
		if (arrayList.size() > 0)
			service = arrayList.get(0);
		final ImageButton map = (ImageButton) view.findViewById(R.id.map);
		map.setOnClickListener(this);
		AlertDialog.Builder builder = new AlertDialog.Builder(this)
				.setTitle("Provider Name")
				.setIcon(R.drawable.ic_launche)
				.setView(view)
				.setPositiveButton("Okay",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (editText.getText().toString().trim()
										.length() == 0) {
									showDialog(arrayList);
									return;
								}
								System.out.println(location);
								System.out.println(address);
								System.out.println(service);
								if (location != null && address != null
										&& service != null) {
									saveInDatabase(editText.getText()
											.toString().trim(), service);
									Toast.makeText(MainActivity.this,
											"Saved Succesfully.",
											Toast.LENGTH_LONG).show();
									getSharedPreferences("name",
											Context.MODE_PRIVATE)
											.edit()
											.putString(
													"customername",
													editText.getText()
															.toString())
											.commit();
								} else {
									showDialog(arrayList);
								}
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.customer_accept, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getTitle().toString().equals("Clear")) {
			selectedItems.clear();
			isClearClicked = true;
			item.setTitle("Delete");
			listView.setOnItemClickListener(this);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_checked);
			for (ProviderDatabase provider : list) {
				adapter.add(provider.getCustomerName() + "\n"
						+ provider.getTimeToService());
			}
			listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
			listView.setAdapter(adapter);
			return true;
		} else if (item.getTitle().toString().equals("Delete")) {
			isClearClicked = false;
			for (Integer i : selectedItems) {
				System.out.println("Index " + i);
				dataBaseHelper.delete(list.get(i));
			}
			updateListView();
			item.setTitle("Clear");
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private class Background extends AsyncTask<Void, Void, List<ParseObject>> {

		@Override
		protected List<ParseObject> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			ParseQuery<ParseObject> query = ParseQuery.getQuery("ServiceType");
			query.whereExists("servicetype");
			try {
				List<ParseObject> list = query.find();
				return list;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog = new ProgressDialog(MainActivity.this);
			dialog.setMessage("Loading...");
			dialog.setCancelable(false);
			dialog.show();
		}

		ProgressDialog dialog = null;

		@Override
		protected void onPostExecute(List<ParseObject> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			dialog.dismiss();

			for (ParseObject parseObject : result) {
				arrayList.add(parseObject.getString("servicetype"));
			}
			showDialog(arrayList);
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		service = arrayList.get(arg2);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	private void saveInDatabase(String providerName, String serviceType) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("address", MapActivity.address);
			jsonObject.put("latitude", location.latitude);
			jsonObject.put("longitude", location.longitude);
			ParseObject parseObject;
			parseObject = new ParseObject("ProviderDetails");
			parseObject.add("providername", providerName);
			parseObject.add("servicetype", serviceType);
			parseObject.add("location", jsonObject.toString());
			parseObject.saveInBackground();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

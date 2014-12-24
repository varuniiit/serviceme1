package com.shashi.customer;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.shashi.customer.db.DataBaseHelper;
import com.shashi.customer.db.ProviderAcceptedList;

public class ProviderDetails extends ActionBarActivity implements
		OnClickListener {

	TextView textView;
	Button button;
	ProviderAcceptedList database;
	DataBaseHelper baseHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_provider_details);
		textView = (TextView) findViewById(R.id.providername);
		button = (Button) findViewById(R.id.submit);
		button.setOnClickListener(this);
		baseHelper = new DataBaseHelper(this);
		String data = getIntent().getStringExtra("data");
		try {
			database = new ProviderAcceptedList();
			JSONObject jsonObject = new JSONObject(data);
			database.setProviderName(jsonObject.getString("providername"));
			database.setRequestId(jsonObject.getString("requestid"));
			database.setInstallationId(jsonObject.getString("installationid"));
			database.setId(Integer.parseInt(jsonObject.getString("id")));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View arg0) {
		sendPushNotification(database);
	}

	public void sendPushNotification(ProviderAcceptedList database) {
		try {
			database.setCustomerAcceptedStatus("true");
			baseHelper.updateCustomerAcceptStatus(database);
			ParsePush parsePush = new ParsePush();
			ParseQuery<ParseInstallation> query = ParseInstallation.getQuery();
			query.whereEqualTo("installationId", database.getInstallationId());
			parsePush.setQuery(query);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("requestid", database.getRequestId());
			jsonObject.put("messagetype", "accept");
			parsePush.setMessage(jsonObject.toString());
			parsePush.sendInBackground();
			Toast.makeText(this, "Response Sent...", Toast.LENGTH_LONG).show();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}

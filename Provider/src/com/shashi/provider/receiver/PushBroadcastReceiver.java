package com.shashi.provider.receiver;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.parse.ParsePushBroadcastReceiver;

import com.shashi.provider.CustomerAccept;
import com.shashi.provider.MainActivity;
import com.shashi.provider.R;
import com.shashi.provider.db.DataBaseHelper;
import com.shashi.provider.db.ProviderDatabase;

public class PushBroadcastReceiver extends ParsePushBroadcastReceiver {

	Context context;
	Intent emptyIntent = null;
	JSONObject jsonObject = null;

	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;
		Toast.makeText(context, "Recived", Toast.LENGTH_LONG).show();
		System.out.println("Channel  "
				+ intent.getExtras().getString("com.parse.Channel"));
		if (intent.getAction().equals("com.parse.push.intent.RECEIVE")) {
			try {
				JSONObject jsonObject = new JSONObject(intent.getExtras()
						.getString("com.parse.Data"));
				String data = jsonObject.getString("alert");
				System.out.println("Data  " + data);
				decodeMessage(data);
				if (MainActivity.isAppOpend) {
					MainActivity.updateListView();
				} else if (CustomerAccept.isAcceptAppOpend) {
					CustomerAccept.updateListView();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

	}

	private void decodeMessage(String message) {

		try {
			jsonObject = new JSONObject(message);
			DataBaseHelper baseHelper = new DataBaseHelper(context);
			String title = null;
			if (jsonObject.getString("messagetype").equalsIgnoreCase("request")) {
				ProviderDatabase database = new ProviderDatabase();
				database.setCustomerAcceptedStatus("false");
				database.setRequestId(jsonObject.getString("requestid"));
				database.setCustomerName(jsonObject.getString("customername"));
				database.setTimeToService(jsonObject.getString("timetoservice"));
				database.setLocationToService(jsonObject
						.getString("locationtoservice"));
				database.setProviderAcceptedStatus("false");
				database.setInstallationId(jsonObject
						.getString("installationid"));
				baseHelper.insert(database);
				emptyIntent = new Intent(context, MainActivity.class);
				title = "Customer Service Request";
				showNotification(title, jsonObject.getString("customername"),
						jsonObject.getString("timetoservice"));
			} else {
				ProviderDatabase database = new ProviderDatabase();
				database.setCustomerAcceptedStatus("true");
				database.setRequestId(jsonObject.getString("requestid"));
				baseHelper.updateCustomerStatus(database);
				List<ProviderDatabase> list = baseHelper
						.getCustomerByRequestId(jsonObject
								.getString("requestid"));
				emptyIntent = new Intent(context, CustomerAccept.class);
				title = "Customer Accepted Service";
				showNotification(title, list.get(0).getCustomerName(),
						"Confirmed");
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	public void showNotification(String title, String first, String last)
			throws JSONException {

		PendingIntent pendingIntent = PendingIntent.getActivity(context, 9,
				emptyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(title)
				.setContentText(first + ".\n" + last + ".")
				.setContentIntent(pendingIntent);
		mBuilder.setSound(RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(99, mBuilder.build());
	}
}

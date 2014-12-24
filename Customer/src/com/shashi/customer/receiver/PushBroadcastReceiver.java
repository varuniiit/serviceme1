package com.shashi.customer.receiver;

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
import com.shashi.customer.MainActivity;
import com.shashi.customer.R;
import com.shashi.customer.db.DataBaseHelper;
import com.shashi.customer.db.ProviderAcceptedList;

public class PushBroadcastReceiver extends ParsePushBroadcastReceiver {

	Context context;
	Intent emptyIntent = null;
	JSONObject jsonObject = null;

	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;
		Toast.makeText(context, "Recived", Toast.LENGTH_LONG).show();
		if (intent.getAction().equals("com.parse.push.intent.RECEIVE")) {
			try {
				JSONObject jsonObject = new JSONObject(intent.getExtras()
						.getString("com.parse.Data"));
				String data = jsonObject.getString("alert");
				System.out.println("Data  " + data);
				decodeMessage(data);

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
				ProviderAcceptedList acceptedList = new ProviderAcceptedList();
				acceptedList.setCustomerAcceptedStatus("false");
				acceptedList.setProviderName(jsonObject
						.getString("providername"));
				acceptedList.setRequestId(jsonObject.getString("requestid"));
				acceptedList.setInstallationId(jsonObject
						.getString("installationid"));
				baseHelper.insertProvider(acceptedList);
				emptyIntent = new Intent(context, MainActivity.class);
				title = "Provider Accepted";
			}
			showNotification(title);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void showNotification(String title) throws JSONException {

		PendingIntent pendingIntent = PendingIntent.getActivity(context, 9,
				emptyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(title)
				.setContentText(jsonObject.getString("providername") + ".")
				.setContentIntent(pendingIntent);
		mBuilder.setSound(RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(99, mBuilder.build());
	}
}

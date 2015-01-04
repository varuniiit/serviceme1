package com.shashi.provider;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends ActionBarActivity {

	GoogleMap googleMap;
	MarkerOptions markerOptions;
	Marker marker;
	LatLng tempLocation;
	public static LatLng finalLoc;
	public static String address;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		address = null;
		finalLoc = null;
		initialMap();
	}

	private void initialMap() {
		if (googleMap == null) {
			SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map);
			googleMap = fragment.getMap();
			if (googleMap == null) {
				Toast.makeText(this,
						"Google Play service is not supported in this device",
						Toast.LENGTH_LONG).show();
				MapActivity.finalLoc = null;
				MapActivity.address = null;
				finish();
			}
			googleMap.setMyLocationEnabled(true);
			googleMap.setOnMyLocationChangeListener(myLocationChangeListener);
			googleMap.setOnMapClickListener(mapClickListener);
		}
	}

	private GoogleMap.OnMapClickListener mapClickListener = new OnMapClickListener() {

		@Override
		public void onMapClick(LatLng loc) {
			if (marker != null) {
				marker.remove();
			}
			tempLocation = loc;
			markerOptions = new MarkerOptions().position(loc).title(
					"Current Location");
			marker = googleMap.addMarker(markerOptions);
			if (googleMap != null) {
				googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc,
						16.0f));
			}

		}
	};
	private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
		@Override
		public void onMyLocationChange(Location location) {
			LatLng loc = new LatLng(location.getLatitude(),
					location.getLongitude());
			tempLocation = loc;
			if (marker != null)
				marker.remove();
			MarkerOptions markerOptions = new MarkerOptions().position(loc)
					.title("Current Location");
			marker = googleMap.addMarker(markerOptions);
			if (googleMap != null) {
				googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc,
						16.0f));
			}
			googleMap.setOnMyLocationChangeListener(null);
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.map, menu);
		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.okay) {
			MapActivity.finalLoc = tempLocation;
			try {
				new Background().execute();
			} catch (Exception e) {
				Toast.makeText(this,
						"Network problem. Please check network connection",
						Toast.LENGTH_LONG).show();
				MapActivity.finalLoc = null;
				MapActivity.address = null;
				e.printStackTrace();
				finish();
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public String getLocationCityName(double lat, double lon) {
		JSONObject result = getLocationFormGoogle(lat + "," + lon);
		return getCityAddress(result);
	}

	protected static JSONObject getLocationFormGoogle(String placesName) {

		String apiRequest = "https://maps.googleapis.com/maps/api/geocode/json?latlng="
				+ placesName; // + "&ka&sensor=false"
		HttpGet httpGet = new HttpGet(apiRequest);
		HttpClient client = new DefaultHttpClient();
		HttpResponse response;
		StringBuilder stringBuilder = new StringBuilder();

		try {
			response = client.execute(httpGet);
			HttpEntity entity = response.getEntity();
			InputStream stream = entity.getContent();
			int b;
			while ((b = stream.read()) != -1) {
				stringBuilder.append((char) b);
			}
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		}

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject = new JSONObject(stringBuilder.toString());
		} catch (JSONException e) {

			e.printStackTrace();
		}

		return jsonObject;
	}

	protected String getCityAddress(JSONObject result) {
		if (result.has("results")) {
			try {
				JSONArray array = result.getJSONArray("results");
				if (array.length() > 0) {

					JSONObject place = array.getJSONObject(1);
					return place.getString("formatted_address");
				}
			} catch (JSONException e) {
				Toast.makeText(MapActivity.this,
						"Network problem. Please check network connection",
						Toast.LENGTH_LONG).show();
				MapActivity.finalLoc = null;
				MapActivity.address = null;
				e.printStackTrace();
				finish();
			}
		}

		return null;
	}

	private class Background extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String addressDetail = getLocationCityName(finalLoc.latitude,
					finalLoc.longitude);
			return addressDetail;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			MapActivity.address = result;
			Toast.makeText(MapActivity.this, "Address: " + result,
					Toast.LENGTH_LONG).show();
			MapActivity.this.finish();
		}

	}

}

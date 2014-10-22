package remoteeyes.bsp.vn.common;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class GPSCurrent {

	public Location location = null;
	public Activity contex;
	public String locationString = "";
	public String idUser = "19";
	LocationManager locman;
	public GPSCurrent(Activity context) {
		this.contex = context;
		locman=(LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
	}

	public void getGPS() {

		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW);

		String provider = locman.getBestProvider(criteria, true);
		if (provider != null) {
			location = locman.getLastKnownLocation(provider);

			locman.requestLocationUpdates(provider, 100, 1, locationListener);
			
			updateWithNewLocation(location);
		}

	}

	private final LocationListener locationListener = new LocationListener() {

		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		public void onProviderDisabled(String provider) {
			updateWithNewLocation(null);
		}

		public void onLocationChanged(Location location) {
			updateWithNewLocation(location);
			
		}

	};

	public void updateWithNewLocation(final Location location) {

		// String latLongString;
		if (location != null) {
			contex.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					//Toast.makeText(contex, String.valueOf(location.getLatitude()), Toast.LENGTH_LONG).show();
				}
			});
			
			this.location = location;
			double latitude = location.getLatitude();
			double logitude = location.getLongitude();

//			if (!idUser.equals("")) {
//				new UpdateMyLocationAsyncTask().execute(
//						String.valueOf(latitude), String.valueOf(logitude));
//			}
			// get address from latitude longitude
			Geocoder gc = new Geocoder(contex, Locale.getDefault());
			try {

				List<Address> address = gc.getFromLocation(latitude, logitude,
						1);

				StringBuilder sb = new StringBuilder();
				if (address.size() > 0) {
					Address add = address.get(0);
					for (int i = 0; i < add.getMaxAddressLineIndex(); i++)
						sb.append(add.getAddressLine(i)).append("\n");
					sb.append(add.getLocality()).append("\n");
					sb.append(add.getPostalCode()).append("\n");
					sb.append(add.getCountryName());
					locationString = sb.toString();

					// Toast.makeText(this.contex, locationString,
					// Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

		} else {
			locationString = "No location found";

		}
		// return latLongString;
	}

	public String getAddressFromLatLong() {
		String address = "";
		double latitude = location.getLatitude();
		double logitude = location.getLongitude();
		Geocoder gc = new Geocoder(contex, Locale.getDefault());
		try {

			List<Address> listaddress = gc.getFromLocation(latitude, logitude,
					1);

			StringBuilder sb = new StringBuilder();
			if (listaddress.size() > 0) {
				Address add = listaddress.get(0);
				for (int i = 0; i < add.getMaxAddressLineIndex(); i++)
					sb.append(add.getAddressLine(i)).append("\n");
				sb.append(add.getLocality()).append("\n");
				sb.append(add.getPostalCode()).append("\n");
				sb.append(add.getCountryName());
				address = sb.toString();

				// Toast.makeText(this.contex, locationString,
				// Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			address = "location not found";
		}
		return address;
	}

	class UpdateMyLocationAsyncTask extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			ArrayList<NameValuePair> arrNameValuePairs = new ArrayList<NameValuePair>();
			arrNameValuePairs.add(new BasicNameValuePair("lat", params[0]));
			arrNameValuePairs.add(new BasicNameValuePair("lng", params[1]));
			arrNameValuePairs.add(new BasicNameValuePair("userID", idUser));
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(Config.UPDATE_LOCALTION);

			try {

				httpPost.setEntity(new UrlEncodedFormEntity(arrNameValuePairs));
				HttpResponse httpResponse = httpClient.execute(httpPost);
				HttpEntity httpEntity = httpResponse.getEntity();
				Log.d("sww", httpEntity.toString());

			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

	}

}

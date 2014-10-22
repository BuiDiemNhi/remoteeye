package remoteeyes.bsp.vn.asynctask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import remoteeyes.bsp.vn.CreateChallengeActivity;
import remoteeyes.bsp.vn.R;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

public class ReverseGeocodingTask extends AsyncTask<LatLng, Void, String> {
	Context mContext;
	
	AutoCompleteTextView atvPlaces;
	String sAddress;
	Spinner spArea;
	ArrayList<String> areaList = new ArrayList<String>();

	public ReverseGeocodingTask(Context context, AutoCompleteTextView atvPlaces,
			Spinner spArea) {
		super();
		mContext = context;
		this.atvPlaces = atvPlaces;
		this.spArea = spArea;
	}

	// Finding address using reverse geocoding
	@Override
	protected String doInBackground(LatLng... params) {
		Geocoder geocoder = new Geocoder(mContext);
		double latitude = params[0].latitude;
		double longitude = params[0].longitude;

		List<Address> addresses = null;
		String addressText = "";

		try {
			addresses = geocoder.getFromLocation(latitude, longitude, 1);

			if (addresses != null && addresses.size() > 0) {
				Address address = addresses.get(0);
				addressText = address.getAddressLine(0);
				areaList.clear();
				for (int i = 1; i < address.getMaxAddressLineIndex(); i++) {
					String tmp = address.getAddressLine(i);
					if (i == 1) {
						tmp += " ward";
					}
					addressText += ", " + tmp;
					areaList.add(tmp);
				}
				addressText += ", " + address.getCountryName();
				areaList.add(address.getCountryName());

				Log.e("Test", address.toString());
				/*
				 * addressText = String.format("%s, %s, %s",
				 * address.getMaxAddressLineIndex() > 0 ?
				 * address.getAddressLine(0) : "", address.getLocality(),
				 * address.getCountryName());
				 */
				// sAddress = address.getMaxAddressLineIndex() > 0 ?
				// address.getAddressLine(0) : "";

				sAddress = addressText;
				((CreateChallengeActivity) mContext).fmCreateChallenge.selectedLatLng = new LatLng(
						latitude, longitude);
			} else {
				((CreateChallengeActivity) mContext).fmCreateChallenge.selectedLatLng = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return addressText;
	}

	@Override
	protected void onPostExecute(String addressText) {
		atvPlaces.setText(sAddress);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
				R.layout.item_category, R.id.tv_category, areaList);
		spArea.setAdapter(adapter);
	}
}

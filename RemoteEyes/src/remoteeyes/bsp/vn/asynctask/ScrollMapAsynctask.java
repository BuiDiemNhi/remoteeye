package remoteeyes.bsp.vn.asynctask;

import remoteeyes.bsp.vn.CreateChallengeActivity;
import remoteeyes.bsp.vn.MyAreaActivity;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.common.Get_LatLong_FromAddress;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ScrollMapAsynctask extends AsyncTask<String, Void, Void> {

	Context context;
	LatLng latLng;
	
	public ScrollMapAsynctask(Context context){
		this.context = context;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}
	
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		if(latLng != null){
			((CreateChallengeActivity) context).setMapVisible(View.VISIBLE);
			((CreateChallengeActivity) context).getGoogleMap().clear();
			((CreateChallengeActivity) context).getGoogleMap().addMarker(new MarkerOptions()
					.icon(BitmapDescriptorFactory.fromBitmap(((CreateChallengeActivity) context).createPin()))
					.position(latLng).anchor(0.5f, 1.0f));
			CameraPosition cameraPosition = new CameraPosition.Builder()
			.target(latLng).zoom(15).tilt(45)
			.build();

			((CreateChallengeActivity)context).getGoogleMap().animateCamera(CameraUpdateFactory
			.newCameraPosition(cameraPosition));
			((CreateChallengeActivity)context).enableLocationOkButton(true);
			((CreateChallengeActivity)context).fmCreateChallenge.selectedLatLng = latLng;
		} else{
			((CreateChallengeActivity)context).enableLocationOkButton(false);
			((CreateChallengeActivity)context).fmCreateChallenge.selectedLatLng = null;
		}
	}
	
	@Override
	protected Void doInBackground(String... params) {
		Get_LatLong_FromAddress latlong = new Get_LatLong_FromAddress();
		latlong.getLocationInfo(params[0].trim());
		if (latlong.getLatLong()) {
			latLng = new LatLng(latlong.latitude, latlong.longitute);
		}
		
		return null;
	}

	
}

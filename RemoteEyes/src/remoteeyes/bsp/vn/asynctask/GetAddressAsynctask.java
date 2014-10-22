package remoteeyes.bsp.vn.asynctask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import remoteeyes.bsp.vn.R;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.common.JSONParser;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

public class GetAddressAsynctask extends AsyncTask<String, Void, String>{
Context context;

	public GetAddressAsynctask(Context context) {
	this.context= context;
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		ArrayList<NameValuePair> arrNameValuePairs = new ArrayList<NameValuePair>();
		arrNameValuePairs.add(new BasicNameValuePair("lat", params[0]));
		arrNameValuePairs.add(new BasicNameValuePair("long", params[1]));
		JSONParser jsonParser = new JSONParser();
		String json = jsonParser.makeHttpRequest(Config.API_GET_CUR_LOCATION,
				arrNameValuePairs);
		return json;
		
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(result);
			
			String intersection= jsonObject.getString("intersection");
			String point_of_interest= jsonObject.getString("point_of_interest");
			String park= jsonObject.getString("park");
			String airport= jsonObject.getString("airport");
			String natural_feature= jsonObject.getString("natural_feature");
			String subpremise= jsonObject.getString("subpremise");
			String premise= jsonObject.getString("premise");
			String neighborhood= jsonObject.getString("neighborhood");
			String sublocality= jsonObject.getString("sublocality");
			String locality= jsonObject.getString("locality");
			String administrative_area_level_3= jsonObject.getString("administrative_area_level_3");
			String administrative_area_level_2=jsonObject.getString("administrative_area_level_2");
			String administrative_area_level_1= jsonObject.getString("administrative_area_level_1");
			String country= jsonObject.getString("country");


			 final AlertDialog alertDialog = new AlertDialog.Builder(context)
			.create();

	// Setting Dialog Title
	alertDialog.setTitle("Information for your locaton");

	// get information location
	
		alertDialog.setMessage("Your current location is:"+"\n" + administrative_area_level_2 +"\n"+ administrative_area_level_1+"\n"
				+ country+"\n");
	

	// Setting Icon to Dialog

	alertDialog.setIcon(context.getResources().getDrawable(
			R.drawable.ic_info_alert));

	// Setting OK Button
	alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			alertDialog.cancel();
		}
	});

	// Showing Alert Message
	alertDialog.show();
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}

}

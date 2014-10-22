package remoteeyes.bsp.vn.asynctask;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import remoteeyes.bsp.vn.CreateChallengeActivity;
import remoteeyes.bsp.vn.MyAreaActivity;
import remoteeyes.bsp.vn.R;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.common.JSONParser;
import remoteeyes.bsp.vn.common.UploadImageToServer;
import remoteeyes.bsp.vn.model.ChallengeObject;
import remoteeyes.bsp.vn.type.ShowingChallengeType;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

public class CreateChallengeAsynctask extends
		AsyncTask<ChallengeObject, Void, String> {

	Context context;
	ProgressDialog progressDialog;
	ChallengeObject obj;

	public CreateChallengeAsynctask(Context context) {
		this.context = context;
		this.progressDialog = new ProgressDialog(context);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		try {
			progressDialog.setMessage("Loading");
			progressDialog.show();
		} catch (Exception x) {
		}
	}

	@Override
	protected String doInBackground(ChallengeObject... co) {
		// Upload image
		obj = co[0];
		ArrayList<String> imageServerList = new ArrayList<String>();
		for (int i = 0; i < co[0].getImagesList().size(); i++) {
			String path = co[0].getImagesList().get(i);
			File file = new File(path);
			UploadImageToServer upload = new UploadImageToServer("image",
					Config.API_UPLOAD_IMAGE_CHALLENG);
			upload.uploadFile(path, file.getName());
			if (!upload.urlImage.equals("-1") || !upload.urlImage.equals(""))
				imageServerList.add(upload.urlImage);
		}

		co[0].setImagesList(imageServerList);

		String result = "";
		NumberFormat formator = new DecimalFormat("00");
		try {
			JSONObject jsonChallenge = new JSONObject();
			jsonChallenge.put("budget", co[0].getReward());
			jsonChallenge.put("category", co[0].getCategory());
			jsonChallenge.put("dateStart", co[0].getStarting_on_year() + "-"
					+ formator.format(co[0].getStarting_on_month()) + "-"
					+ formator.format(co[0].getStarting_on_day()) + " "
					+ formator.format(co[0].getStarting_on_hour()) + ":"
					+ formator.format(co[0].getStarting_on_minute()) + ":"
					+ formator.format(co[0].getStarting_on_second()));
			jsonChallenge.put("description", co[0].getDescription());
			jsonChallenge.put(
					"duration",
					formator.format(co[0].getDuration_day()) + ":"
							+ formator.format(co[0].getDuration_hour()) + ":"
							+ formator.format(co[0].getDuration_minute()));
			jsonChallenge.put("public", co[0].getiPublic());
			jsonChallenge.put("title", co[0].getTitle());
			jsonChallenge.put("userID", Integer.parseInt(Config.IdUser));

			JSONArray jsonArrImages = new JSONArray();
			for (int i = 0; i < co[0].getImagesList().size(); i++) {
				jsonArrImages.put(co[0].getImagesList().get(i));
			}

			JSONArray jsonArrLocation = new JSONArray();
			for (int j = 0; j < co[0].getLocationsList().size(); j++) {
				JSONObject jsonLocation = new JSONObject();
				jsonLocation.put("address", co[0].getLocationsList().get(j)
						.getAddress());
				jsonLocation.put("city", "");
				jsonLocation.put("country", "");
				jsonLocation.put("area", co[0].getLocationsList().get(j)
						.getArea());
				jsonLocation.put("lat", co[0].getLocationsList().get(j)
						.getLat());
				jsonLocation.put("lng", co[0].getLocationsList().get(j)
						.getLng());

				jsonArrLocation.put(jsonLocation);
			}

			JSONObject jsonRequest = new JSONObject();
			jsonRequest.put("challenge", jsonChallenge);
			jsonRequest.put("images", jsonArrImages);
			jsonRequest.put("location", jsonArrLocation);

			try {
				ArrayList<NameValuePair> arrNameValuePairs = new ArrayList<NameValuePair>();
				arrNameValuePairs.add(new BasicNameValuePair("json", URLEncoder
						.encode(jsonRequest.toString(), "UTF-8")));
				JSONParser jsonParser = new JSONParser();
				result = jsonParser.makeHttpRequest(Config.API_CREATE_CHALLENG,
						arrNameValuePairs);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		try {
			if (progressDialog.isShowing())
				progressDialog.dismiss();
		} catch (Exception ex) {
		}
		if (ShowingChallengeType.STATUS_SHOW_CURRENT == ShowingChallengeType.CHALLENGE_SHOW_NEARBY) {
			Intent intentArea = new Intent(context, MyAreaActivity.class);
			intentArea.putExtra("run", "MyArea");
			context.startActivity(intentArea);
		} else if (ShowingChallengeType.STATUS_SHOW_CURRENT == ShowingChallengeType.CHALLENGE_SHOW_GLOBAL) {
			Intent Global = new Intent(context, MyAreaActivity.class);
			Global.putExtra("run", "Global");
			context.startActivity(Global);
		}
		((CreateChallengeActivity) context).finish();
		if (result.contains("1")) {
			Toast.makeText(
					context,
					context.getResources().getString(
							R.string.create_challenge_successfully),
					Toast.LENGTH_SHORT).show();
		} else if (result.contains("-1")) {
			Toast.makeText(
					context,
					context.getResources().getString(
							R.string.create_challenge_failed),
					Toast.LENGTH_SHORT).show();
		} else if (result.contains("-2")) {
			Toast.makeText(
					context,
					context.getResources().getString(R.string.not_enough_money),
					Toast.LENGTH_SHORT).show();
		} else if (result.contains("0"))
			Toast.makeText(context,
					context.getResources().getString(R.string.long_detail),
					Toast.LENGTH_SHORT).show();
	}
}
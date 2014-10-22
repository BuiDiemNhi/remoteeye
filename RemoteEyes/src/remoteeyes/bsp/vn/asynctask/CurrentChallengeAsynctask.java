package remoteeyes.bsp.vn.asynctask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import remoteeyes.bsp.vn.MyAreaActivity;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.common.JSONParser;
import remoteeyes.bsp.vn.custom.view.BubbleChallenge;
import remoteeyes.bsp.vn.fragments.DetailChallengeFragment;
import remoteeyes.bsp.vn.model.ChallengeObject;
import remoteeyes.bsp.vn.model.ImageObject;
import remoteeyes.bsp.vn.model.LocationObject;
import remoteeyes.bsp.vn.type.ColorChallengeType;
import remoteeyes.bsp.vn.type.ShowingChallengeType;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class CurrentChallengeAsynctask extends AsyncTask<String, Void, String> {

	Context context;
	ArrayList<ChallengeObject> challengeList;
	ProgressDialog progressDialog;

	public CurrentChallengeAsynctask(Context context) {
		this.context = context;
		challengeList = new ArrayList<ChallengeObject>();
		this.progressDialog = new ProgressDialog(context);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progressDialog.setMessage("Loading");
		progressDialog.show();

	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (progressDialog.isShowing())
			progressDialog.dismiss();
		try {
			JSONArray jarr = new JSONArray(result);
			for (int i = 0; i < jarr.length(); i++) {
				JSONObject jObj = jarr.getJSONObject(i);
				ChallengeObject obj = new ChallengeObject();
				obj.setId(jObj.getString("ID"));
				obj.setTitle(jObj.getString("title"));
				obj.setAccept(Integer.parseInt(jObj.getString("accept")));
				obj.setReward(Integer.parseInt(jObj.getString("budget")));
				obj.setiPublic(Integer.parseInt(jObj.getString("public")));

				obj.setWinner_id(jObj.getInt("winner_id"));
				obj.setType_win(jObj.getInt("type_win"));
				obj.setMedia_id(jObj.getInt("media_id"));

				int countAvatar = Integer.parseInt(jObj
						.getString("count_avatar"));
				int countPhoto = Integer
						.parseInt(jObj.getString("count_photo"));
				int countVideo = Integer
						.parseInt(jObj.getString("count_video"));
				obj.setCountImage(countAvatar + countPhoto + countVideo);
				obj.setRemainStart(jObj.getString("remain_start"));

				obj.setTitle(jObj.getString("title"));
				obj.setIspaid(jObj.getInt("is_paid"));
				obj.setIsShare(jObj.getInt("is_shared"));
				obj.setInterval(jObj.getString("interval"));
				obj.setIsExpired(jObj.getInt("is_expired"));
				obj.setCountAccept(Integer.parseInt(jObj
						.getString("count_accept")));

				obj.setEmail(jObj.getString("author_email"));
				obj.setName(jObj.getString("author_name"));
				obj.setDescription(jObj.getString("description"));

				String duration = jObj.getString("duration");
				String[] duration1 = duration.split(":");

				String start_date = jObj.getString("start_date");
				String[] start_date1 = start_date.split(" ");
				String[] ngay = start_date1[0].split("-");
				String[] gio = start_date1[1].split(":");

				obj.setStarting_on_year(Integer.parseInt(ngay[0]));
				obj.setStarting_on_month(Integer.parseInt(ngay[1]));
				obj.setStarting_on_day(Integer.parseInt(ngay[2]));
				obj.setStarting_on_hour(Integer.parseInt(gio[0]));
				obj.setStarting_on_minute(Integer.parseInt(gio[1]));
				obj.setStarting_on_second(Integer.parseInt(gio[2]));

				obj.setDuration_day(Integer.parseInt(duration1[0]));
				obj.setDuration_hour(Integer.parseInt(duration1[1]));
				obj.setDuration_minute(Integer.parseInt(duration1[2]));
				obj.setAvatar(jObj.getString("author_avatar"));
				if (Config.EMAILUSER.equals(jObj.getString("author_email"))) {
					obj.setMine(true);
					obj.setAvatar(null);
				} else {
					obj.setMine(false);
				}

				JSONArray jarrExpImage = jObj.getJSONArray("avatar");
				ArrayList<String> imagesList = new ArrayList<String>();
				for (int k = 0; k < jarrExpImage.length(); k++) {
					String url = jarrExpImage.getString(k);
					imagesList.add(url);
				}
				obj.setImagesList(imagesList);

				JSONArray jsonLocation = jObj.getJSONArray("locations");
				ArrayList<LocationObject> locationMyList = new ArrayList<LocationObject>();
				for (int j = 0; j < jsonLocation.length(); j++) {
					LocationObject locObj = new LocationObject();
					JSONObject jsObj = jsonLocation.getJSONObject(j);
					locObj.setAddress(jsObj.getString("address"));
					locObj.setChallengeId(jsObj.getString("challenge_ID"));
					locObj.setArea(jsObj.getString("area"));
					try {
						locObj.setLat(Double.parseDouble(jsObj.getString("lat")));
						locObj.setLng(Double.parseDouble(jsObj.getString("lng")));
					} catch (Exception ex) {
					}
					locObj.updateDistance();
					locationMyList.add(locObj);
				}
				obj.setLocationsList(locationMyList);
				challengeList.add(obj);
			}
			try {
				if (challengeList.size() >= 5) {
					challengeList.add(createChallenge());
				}
				((MyAreaActivity) context).showChallengeList(challengeList);
				((MyAreaActivity) context)
						.setCurrentChallengeList(challengeList);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			// if (DetailChallengeFragment.challengeUploadId.equals("-1")) {
			// new LoadDetailChallengeAsynctask(context)
			// .execute(DetailChallengeFragment.challengeUploadId);
			// DetailChallengeFragment.challengeUploadId = "-1";
			// }
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	JSONObject parserJson(String json) {
		JSONObject job = null;
		try {
			job = new JSONObject(json);
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}
		return job;
	}

	@Override
	protected String doInBackground(String... params) {

		JSONParser jsonParser = new JSONParser();

		String url = Config.GET_CURC + "?lat=" + params[0] + "&lng="
				+ params[1] + "&userID=" + params[2] + "&type=" + params[3];
		String result = jsonParser.getStringFromUrl(url);
		// JSONObject jsonObject = jsonParser.getJSONFromUrl(url);
		// if (jsonObject != null) {
		// return jsonObject.toString();
		// } else {
		// return null;
		// }
		return result;
	}

	public ChallengeObject createChallenge() {
		ChallengeObject obj = new ChallengeObject();
		obj.setId("-1");
		obj.setAccept(3);
		obj.setReward(0);
		obj.setiPublic(2);
		obj.setCountImage(0);
		obj.setRemainStart("");
		obj.setAvatar(null);
		obj.setTitle("");
		obj.setIsShare(2);
		obj.setInterval("");
		obj.setIsExpired(2);

		ArrayList<String> imagesList = new ArrayList<String>();
		String url = "";
		imagesList.add(url);
		obj.setImagesList(imagesList);

		ArrayList<LocationObject> loList = new ArrayList<LocationObject>();
		LocationObject lo = new LocationObject();
		lo.setAddress("");
		lo.setArea("");
		lo.setLat(Double.parseDouble("-1.0"));
		lo.setLng(Double.parseDouble("-1.0"));
		lo.updateDistance();
		loList.add(lo);
		obj.setLocationsList(loList);
		return obj;
	}
}

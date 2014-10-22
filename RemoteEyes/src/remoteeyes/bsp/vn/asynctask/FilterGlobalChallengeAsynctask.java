package remoteeyes.bsp.vn.asynctask;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import remoteeyes.bsp.vn.MyAreaActivity;
import remoteeyes.bsp.vn.adapter.ChallengeListAdapter;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.common.JSONParser;
import remoteeyes.bsp.vn.model.ChallengeObject;
import remoteeyes.bsp.vn.model.LocationObject;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class FilterGlobalChallengeAsynctask extends
		AsyncTask<String, Void, String> {
	Context context;
	String isMine, isUpcoming, isIgnore, isHistory, isToday, filter = "",
			userID;
	ArrayList<ChallengeObject> challengeList = new ArrayList<ChallengeObject>();
	ProgressDialog progressDialog;
	ChallengeListAdapter adapter;

	public FilterGlobalChallengeAsynctask(Context context) {
		this.context = context;
		this.progressDialog = new ProgressDialog(context);
	}

	public FilterGlobalChallengeAsynctask(Context context,
			ChallengeListAdapter adapter) {
		this.context = context;
		this.adapter = adapter;
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
			JSONObject jsonAllObj = parserJson(result);
			// Config.total = jsonAllObj.getInt("total");
			JSONArray jsonDataObj = jsonAllObj.getJSONArray("data");
			if (jsonDataObj != null) {
				for (int i = 0; i < jsonDataObj.length(); i++) {
					JSONObject jObj = jsonDataObj.getJSONObject(i);
					ChallengeObject obj = GetChallengeFromJsonObjet(jObj);
					challengeList.add(obj);
				}
			} else {
			}
			try {
				if (challengeList.size() >= 5) {
					challengeList.add(createChallenge());
				}
				// Config.data.addAll(challengeList);
				// if (MyAreaActivity.page == 1) {
				// ((MyAreaActivity) context).showChallengeList(challengeList);
				// } else {
				// ((MyAreaActivity) context).showChallengeList(Config.data);
				// // ((MyAreaActivity)
				// context).challengeListFragment.adapter.appendItems(Config.data);
				// }
				((MyAreaActivity) context).showChallengeList(challengeList);
				((MyAreaActivity) context)
						.setCurrentChallengeList(challengeList);
			} catch (Exception ex) {
			}
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
		ArrayList<NameValuePair> arrNameValuePairs = new ArrayList<NameValuePair>();
		isMine = params[1];
		isUpcoming = params[2];
		isIgnore = params[3];
		isHistory = params[4];
		isToday = params[5];
		if (isMine.equals("true")) {
			filter += "filter_by_user_id|";
		}
		if (isUpcoming.equals("true")) {
			filter += "filter_upcoming|";
		}
		if (isIgnore.equals("true")) {
			filter += "filter_ignore|";
		}
		if (isHistory.equals("true")) {
			filter += "filter_historical|";
		}
		if (isToday.equals("true")) {
			filter += "filter_today|";
		}
		filter = filter.substring(0, filter.lastIndexOf("|"));
		arrNameValuePairs.add(new BasicNameValuePair("user_id", params[0]));
		arrNameValuePairs.add(new BasicNameValuePair("filter", filter));
		arrNameValuePairs.add(new BasicNameValuePair("sort_field", params[6]));
		try {
			arrNameValuePairs.add(new BasicNameValuePair("page", params[7]));
		} catch (Exception e) {
			e.printStackTrace();
		}
		userID = params[0];
		JSONParser jsonParser = new JSONParser();
		String json = jsonParser.makeHttpRequest(
				Config.GET_FILTER_GLOBAL_CHALLENGE, arrNameValuePairs);
		return json;
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

	public ChallengeObject GetChallengeFromJsonObjet(JSONObject jObj) {
		ChallengeObject obj = new ChallengeObject();
		try {
			obj.setId(jObj.getString("ID"));
			obj.setTitle(jObj.getString("title"));
			obj.setAccept(jObj.getInt("accept"));
			obj.setReward(Integer.parseInt(jObj.getString("budget")));
			obj.setiPublic(Integer.parseInt(jObj.getString("public")));

			int countAvatar = jObj.getInt("count_avatar");
			int countPhoto = jObj.getInt("count_photo");
			int countVideo = jObj.getInt("count_video");
			obj.setCountImage(countAvatar + countPhoto + countVideo);
			obj.setRemainStart(jObj.getString("remain_start"));

			obj.setWinner_id(jObj.getInt("winner_id"));
			obj.setType_win(jObj.getInt("type_win"));
			obj.setMedia_id(jObj.getInt("media_id"));

			obj.setTitle(jObj.getString("title"));
			obj.setIspaid(jObj.getInt("is_paid"));
			obj.setIsShare(jObj.getInt("is_shared"));
			obj.setInterval(jObj.getString("interval"));
			obj.setIsExpired(jObj.getInt("is_expired"));
			obj.setCountAccept(Integer.parseInt(jObj.getString("count_accept")));

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
				obj.setAvatar(jObj.getString("author_avatar"));
			} else {
				obj.setMine(false);
			}

			JSONArray jarrExpImage = jObj.getJSONArray("avatar");
			ArrayList<String> imagesList = new ArrayList<String>();
			for (int z = 0; z < jarrExpImage.length(); z++) {
				String url = jarrExpImage.getString(z);
				imagesList.add(url);
			}
			obj.setImagesList(imagesList);

			JSONArray jsonLocation = jObj.getJSONArray("locations");
			ArrayList<LocationObject> locationMyList = new ArrayList<LocationObject>();
			for (int j = 0; j < jsonLocation.length(); j++) {
				JSONObject jl = jsonLocation.getJSONObject(j);
				LocationObject lo = new LocationObject();
				lo.setAddress(jl.getString("address"));
				lo.setArea(jl.getString("area"));
				lo.setLat(Double.parseDouble(jl.getString("lat")));
				lo.setLng(Double.parseDouble(jl.getString("lng")));
				lo.updateDistance();
				locationMyList.add(lo);
			}
			obj.setLocationsList(locationMyList);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}
}

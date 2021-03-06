package remoteeyes.bsp.vn.asynctask;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import remoteeyes.bsp.vn.MyAreaActivity;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.common.JSONParser;
import remoteeyes.bsp.vn.model.ChallengeObject;
import remoteeyes.bsp.vn.model.ImageObject;
import remoteeyes.bsp.vn.model.LocationObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

public class AcceptedChallengeAsynctask extends AsyncTask<String, Void, String> {

	Activity activity;
	ProgressDialog progressDialog;

	public AcceptedChallengeAsynctask(Activity activity) {
		this.activity = activity;
		this.progressDialog = new ProgressDialog(activity);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progressDialog.setMessage("Loading");
		progressDialog.show();

	}

	// run UI alter
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (progressDialog.isShowing())
			progressDialog.dismiss();
		ArrayList<ChallengeObject> coList = new ArrayList<ChallengeObject>();
		try {
			JSONArray jarr = new JSONArray(result);
			for (int i = 0; i < jarr.length(); i++) {
				JSONObject json = jarr.getJSONObject(i);
				ChallengeObject obj = new ChallengeObject();
				obj.setId(json.getString("ID"));
				obj.setAccept(json.getInt("accept"));
				obj.setReward(Integer.parseInt(json.getString("budget")));
				obj.setiPublic(Integer.parseInt(json.getString("public")));

				obj.setWinner_id(json.getInt("winner_id"));
				obj.setType_win(json.getInt("type_win"));
				obj.setMedia_id(json.getInt("media_id"));

				int countAvatar = Integer.parseInt(json
						.getString("count_avatar"));
				int countPhoto = Integer
						.parseInt(json.getString("count_photo"));
				int countVideo = Integer
						.parseInt(json.getString("count_video"));
				obj.setCountImage(countAvatar + countPhoto + countVideo);
				obj.setRemainStart(json.getString("remain_start"));

				String[] duration = json.getString("duration").split(":");
				obj.setDuration_day(Integer.parseInt(duration[0]));
				obj.setDuration_hour(Integer.parseInt(duration[1]));
				obj.setDuration_minute(Integer.parseInt(duration[2]));

				obj.setStarting_on_year(Integer.parseInt(json.getString(
						"start_date").substring(0, 4)));
				obj.setStarting_on_month(Integer.parseInt(json.getString(
						"start_date").substring(5, 7)));
				obj.setStarting_on_day(Integer.parseInt(json.getString(
						"start_date").substring(8, 10)));
				obj.setStarting_on_hour(Integer.parseInt(json.getString(
						"start_date").substring(11, 13)));
				obj.setStarting_on_minute(Integer.parseInt(json.getString(
						"start_date").substring(14, 16)));
				obj.setAvatar(json.getString("author_avatar"));
				obj.setTitle(json.getString("title"));
				obj.setIsShare(json.getInt("is_shared"));
				obj.setInterval(json.getString("interval"));
				obj.setIsExpired(json.getInt("is_expired"));
				obj.setIspaid(json.getInt("is_paid"));
				obj.setCountAccept(Integer.parseInt(json
						.getString("count_accept")));
				if (Config.EMAILUSER.equals(json.getString("author_email"))) {
					obj.setMine(true);
					obj.setAvatar(null);
				} else {
					obj.setMine(false);
				}

				JSONArray jarrExpImage = json.getJSONArray("avatar");
				ArrayList<String> imagesList = new ArrayList<String>();
				for (int k = 0; k < jarrExpImage.length(); k++) {
					String url = jarrExpImage.getString(k);
					imagesList.add(url);
				}
				obj.setImagesList(imagesList);

				ArrayList<LocationObject> loList = new ArrayList<LocationObject>();
				JSONArray jarrLocation = json.getJSONArray("location");
				for (int j = 0; j < jarrLocation.length(); j++) {
					JSONObject jl = jarrLocation.getJSONObject(j);
					LocationObject lo = new LocationObject();
					lo.setAddress(jl.getString("address"));
					lo.setArea(jl.getString("area"));
					lo.setLat(Double.parseDouble(jl.getString("lat")));
					lo.setLng(Double.parseDouble(jl.getString("lng")));
					lo.updateDistance();
					loList.add(lo);
				}

				obj.setLocationsList(loList);
				coList.add(obj);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		try {
			if (coList.size() >= 5) {
				coList.add(createChallenge());
			}
			((MyAreaActivity) activity).showChallengeList(coList);
			((MyAreaActivity) activity).setCurrentChallengeList(coList);

		} catch (Exception ex) {
		}
	}

	@Override
	protected String doInBackground(String... params) {
		String result = "";
		JSONParser jsonParser = new JSONParser();
		String url = String
				.format(Config.API_GET_ACCEPTED_CHALLENGE, params[0]);
		result = jsonParser.getStringFromUrl(url);
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

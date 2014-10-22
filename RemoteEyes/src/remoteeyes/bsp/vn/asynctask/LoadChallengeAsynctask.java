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
import remoteeyes.bsp.vn.model.LocationObject;
import remoteeyes.bsp.vn.type.ColorChallengeType;
import remoteeyes.bsp.vn.type.ShowingChallengeType;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;

public class LoadChallengeAsynctask extends AsyncTask<String, Void, String> {
	Context context;
	Map<String, BubbleChallenge> bubbleChallengeMap;
	ArrayList<ChallengeObject> challengeList;
	// ArrayList<ChallengeObject> globalChallengeList;
	// ArrayList<ChallengeObject> myCountryChallengeList;
	// ArrayList<ChallengeObject> myCityChallengeList;
	public BubbleChallenge bubbleChallenge;

	public LoadChallengeAsynctask(Context context) {
		this.context = context;
		bubbleChallengeMap = new HashMap<String, BubbleChallenge>();
		challengeList = new ArrayList<ChallengeObject>();
		// globalChallengeList = new ArrayList<ChallengeObject>();
		// myCountryChallengeList = new ArrayList<ChallengeObject>();
		// myCityChallengeList = new ArrayList<ChallengeObject>();
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		try {
			Log.e("Test", result);
			JSONObject jsonObj = parserJson(result);

			JSONArray jsonArrMyChallenge = jsonObj.getJSONArray("my_challenge");
			JSONArray jsonArrOthers = jsonObj.getJSONArray("orther_challenge");

			for (int i = 0; i < jsonArrMyChallenge.length(); i++) {
				JSONObject jObj = jsonArrMyChallenge.getJSONObject(i);

				JSONArray jsonLocation = jObj.getJSONArray("locations");
				int sizeLocation = jsonLocation.length();

				BubbleChallenge bubbleChallenge = GetBubbleChallengeFromJsonObjet(jObj);
				if (bubbleChallenge.getIsExpired() != 1) {
					if (sizeLocation > 1) {
						bubbleChallenge
								.setColorBubble(ColorChallengeType.MINE_MULTILOCATION);
					} else {
						bubbleChallenge.setColorBubble(ColorChallengeType.MINE);
					}
				} else {
					if (sizeLocation > 1) {
						bubbleChallenge
								.setColorBubble(ColorChallengeType.MINE_EXPIRED_MULTILOCATION);
					} else {
						bubbleChallenge
								.setColorBubble(ColorChallengeType.MINE_EXPIRED);
					}
				}
				bubbleChallenge.setType(ColorChallengeType.MINE);
				bubbleChallenge.setMessage("$" + bubbleChallenge.getReward(),
						bubbleChallenge.getInterval(),
						bubbleChallenge.getTitle());
				// bubbleChallenge.setMessage("$" + bubbleChallenge.getReward()
				// + " in " + bubbleChallenge.getDuration());
				bubbleChallengeMap.put(bubbleChallenge.getChallengeId(),
						bubbleChallenge);

				ChallengeObject obj = new ChallengeObject();
				obj.setAccept(Integer.parseInt(jObj.getString("accept")));
				obj.setAvatar(jObj.getString("author_avatar"));
				obj.setEmail(jObj.getString("author_email"));
				obj.setName(jObj.getString("author_name"));
				obj.setReward(Integer.parseInt(jObj.getString("budget")));
				obj.setCountAccept(Integer.parseInt(jObj
						.getString("count_accept")));
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

				obj.setiPublic(Integer.parseInt(jObj.getString("public")));
				obj.setTitle(jObj.getString("title"));
				obj.setId(jObj.getString("ID"));
				obj.setMine(true);

				ArrayList<LocationObject> locationMyList = new ArrayList<LocationObject>();
				for (int j = 0; j < jsonLocation.length(); j++) {
					LocationObject locObj = new LocationObject();
					JSONObject jsObj = jsonLocation.getJSONObject(j);
					locObj.setAddress(jsObj.getString("address"));
					locObj.setChallengeId(jsObj.getString("challenge_ID"));
					locObj.setArea(jsObj.getString("country"));
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

			for (int j = 0; j < jsonArrOthers.length(); j++) {
				JSONObject jObj = jsonArrOthers.getJSONObject(j);

				JSONArray jsonLocation = jObj.getJSONArray("locations");
				int sizeLocation = jsonLocation.length();

				BubbleChallenge bubbleChallenge = GetBubbleChallengeFromJsonObjet(jObj);

				if (bubbleChallenge.getAccept() == 0) {
					if (bubbleChallenge.getIsExpired() != 1) {
						if (sizeLocation > 1) {
							bubbleChallenge
									.setColorBubble(ColorChallengeType.ACCEPTED_YET_MULTILOCATION);
						} else {
							bubbleChallenge
									.setColorBubble(ColorChallengeType.ACCEPTED_YET);
						}
					} else {
						if (sizeLocation > 1) {
							bubbleChallenge
									.setColorBubble(ColorChallengeType.ACCEPTED_YET_EXPIRED_MULTILOCATION);
						} else {
							bubbleChallenge
									.setColorBubble(ColorChallengeType.ACCEPTED_YET_EXPIRED);
						}
					}
				} else if (bubbleChallenge.getAccept() == 1) {
					if (bubbleChallenge.getIsExpired() != 1) {
						if (sizeLocation > 1) {
							bubbleChallenge
									.setColorBubble(ColorChallengeType.ACCEPTED_MULTILOCATION);
						} else {
							bubbleChallenge
									.setColorBubble(ColorChallengeType.ACCEPTED);
						}
					} else {
						if (sizeLocation > 1) {
							bubbleChallenge
									.setColorBubble(ColorChallengeType.ACCEPTED_EXPIRED_MULTILOCATION);
						} else {
							bubbleChallenge
									.setColorBubble(ColorChallengeType.ACCEPTED_EXPIRED);
						}
					}
				} else if (bubbleChallenge.getAccept() == 2) {
					if (bubbleChallenge.getIsExpired() != 1) {
						if (sizeLocation > 1) {
							bubbleChallenge
									.setColorBubble(ColorChallengeType.IGNORED_MULTILOCATION);
						} else {
							bubbleChallenge
									.setColorBubble(ColorChallengeType.IGNORED);
						}
					} else {
						if (sizeLocation > 1) {
							bubbleChallenge
									.setColorBubble(ColorChallengeType.IGNORED_EXPIRED_MULTILOCATION);
						} else {
							bubbleChallenge
									.setColorBubble(ColorChallengeType.IGNORED_EXPIRED);
						}
					}
					bubbleChallenge.setType(ColorChallengeType.IGNORED);
				}

				// bubbleChallenge.setMessage(bubbleChallenge.getReward()
				// + "$ in " + bubbleChallenge.getDuration());
				bubbleChallenge.setMessage("$" + bubbleChallenge.getReward(),
						bubbleChallenge.getInterval(),
						bubbleChallenge.getTitle());
				if (bubbleChallenge.getAccept() != 2)
					bubbleChallengeMap.put(bubbleChallenge.getChallengeId(),
							bubbleChallenge);

				ChallengeObject obj = new ChallengeObject();
				obj.setAccept(Integer.parseInt(jObj.getString("accept")));
				obj.setAvatar(jObj.getString("author_avatar"));
				obj.setEmail(jObj.getString("author_email"));
				obj.setName(jObj.getString("author_name"));
				obj.setReward(Integer.parseInt(jObj.getString("budget")));
				obj.setCountAccept(Integer.parseInt(jObj
						.getString("count_accept")));
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

				obj.setiPublic(Integer.parseInt(jObj.getString("public")));
				obj.setTitle(jObj.getString("title"));
				obj.setId(jObj.getString("ID"));
				obj.setMine(false);

				ArrayList<LocationObject> locationList = new ArrayList<LocationObject>();

				for (int k = 0; k < jsonLocation.length(); k++) {
					JSONObject jsObj = jsonLocation.getJSONObject(k);
					LocationObject locObj = new LocationObject();
					locObj.setAddress(jsObj.getString("address"));
					locObj.setChallengeId(jsObj.getString("challenge_ID"));
					locObj.setArea(jsObj.getString("country"));
					try {
						locObj.setLat(Double.parseDouble(jsObj.getString("lat")));
						locObj.setLng(Double.parseDouble(jsObj.getString("lng")));
					} catch (Exception ex) {
					}
					locObj.updateDistance();
					locationList.add(locObj);
				}

				obj.setLocationsList(locationList);
				challengeList.add(obj);

			}

			if (ShowingChallengeType.STATUS_SHOW_CURRENT == ShowingChallengeType.CHALLENGE_SHOW_NEARBY) {
				((MyAreaActivity) context).showChallengeMap(bubbleChallengeMap);
				((MyAreaActivity) context)
						.setCurrentChallengeList(challengeList);
			}
			// else if (ShowingChallengeType.STATUS_SHOW_CURRENT ==
			// ShowingChallengeType.CHALLENGE_SHOW_MY_CITY){
			// ((MyAreaActivity)
			// context).showChallengeList(myCityChallengeList);
			// ((MyAreaActivity)
			// context).setCurrentChallengeList(myCityChallengeList);
			// }
			// else if (ShowingChallengeType.STATUS_SHOW_CURRENT ==
			// ShowingChallengeType.CHALLENGE_SHOW_MY_COUNTRY){
			// ((MyAreaActivity)
			// context).showChallengeList(myCountryChallengeList);
			// ((MyAreaActivity)
			// context).setCurrentChallengeList(myCountryChallengeList);
			// }
			// else if(ShowingChallengeType.STATUS_SHOW_CURRENT ==
			// ShowingChallengeType.CHALLENGE_SHOW_GLOBAL){
			// ((MyAreaActivity)
			// context).showChallengeList(globalChallengeList);
			// ((MyAreaActivity)
			// context).setCurrentChallengeList(globalChallengeList);
			// }

			if (DetailChallengeFragment.challengeUploadId.equals("-1")) {
				new LoadDetailChallengeAsynctask(context)
						.execute(DetailChallengeFragment.challengeUploadId);
				DetailChallengeFragment.challengeUploadId = "-1";
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

		JSONParser jsonParser = new JSONParser();

		String url = Config.GET_CURC + "?lat=" + params[0] + "&lng="
				+ params[1] + "&userID=" + params[2] + "&type=" + params[3];
		JSONObject jsonObject = jsonParser.getJSONFromUrl(url);
		if (jsonObject != null) {
			return jsonObject.toString();
		} else {
			return null;
		}

	}

	public BubbleChallenge GetBubbleChallengeFromJsonObjet(
			JSONObject joJsonObject) {
		Log.e("Test", joJsonObject.toString());
		BubbleChallenge bubbleChallenge = new BubbleChallenge(context);

		try {
			bubbleChallenge.setChallengeId(joJsonObject.getString("ID"));
			bubbleChallenge.setReward((Float.parseFloat(joJsonObject
					.getString("budget"))));
			bubbleChallenge.setInterval(joJsonObject.getString("interval"));
			bubbleChallenge.setTitle(joJsonObject.getString("title"));
			bubbleChallenge.setAccept(joJsonObject.getInt("accept"));
			bubbleChallenge.setIsExpired(joJsonObject.getInt("is_expired"));
			bubbleChallenge.setPublics(Integer.parseInt(joJsonObject
					.getString("public")));

			String duration = joJsonObject.getString("duration"); // 01:02:30
			String[] duration1 = duration.split(":");

			String start_date = joJsonObject.getString("start_date");// "2014-03-06 12:30:28"
			String[] start_date1 = start_date.split(" ");
			String[] ngay = start_date1[0].split("-");
			String[] gio = start_date1[1].split(":");

			bubbleChallenge.setStarting_on_year(Integer.parseInt(ngay[0]));
			bubbleChallenge.setStarting_on_month(Integer.parseInt(ngay[1]));
			bubbleChallenge.setStarting_on_day(Integer.parseInt(ngay[2]));
			bubbleChallenge.setStarting_on_hour(Integer.parseInt(gio[0]));
			bubbleChallenge.setStarting_on_minute(Integer.parseInt(gio[1]));
			bubbleChallenge.setStarting_on_second(Integer.parseInt(gio[2]));

			bubbleChallenge.setDuration_day(Integer.parseInt(duration1[0]));
			bubbleChallenge.setDuration_hour(Integer.parseInt(duration1[1]));
			bubbleChallenge.setDuration_minute(Integer.parseInt(duration1[2]));

			JSONArray jsonLocation = joJsonObject.getJSONArray("locations");
			for (int j = 0; j < jsonLocation.length(); j++) {
				LocationObject locObj = new LocationObject();
				JSONObject jsObj = jsonLocation.getJSONObject(j);
				locObj.setAddress(jsObj.getString("address"));
				locObj.setArea(jsObj.getString("country"));
				try {
					locObj.setLat(Double.parseDouble(jsObj.getString("lat")));
					locObj.setLng(Double.parseDouble(jsObj.getString("lng")));
				} catch (Exception ex) {
				}

				locObj.updateDistance();
				bubbleChallenge.setLat(locObj.getLat());
				bubbleChallenge.setLng(locObj.getLng());
				bubbleChallenge.setLocation(locObj);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// challengeObject.setId(surname);
		return bubbleChallenge;
	}

}
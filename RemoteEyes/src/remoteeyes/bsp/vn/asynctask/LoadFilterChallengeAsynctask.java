package remoteeyes.bsp.vn.asynctask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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

public class LoadFilterChallengeAsynctask extends
		AsyncTask<String, Void, String> {

	Context context;
	Map<String, BubbleChallenge> bubbleChallengeMap;
	public BubbleChallenge bubbleChallenge;
	String isMine, isUpcoming, isIgnore, isHistory, filter = "", userID;

	public LoadFilterChallengeAsynctask(Context context) {
		this.context = context;
		bubbleChallengeMap = new HashMap<String, BubbleChallenge>();
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		try {
			JSONObject jsonAllObj = parserJson(result);
			JSONObject jsonDataObj = jsonAllObj.getJSONObject("data");
			JSONArray jsonArrMyChallenge = null;
			JSONArray jsonArrHistory = null;
			JSONArray jsonArrUpcoming = null;
			JSONArray jsonArrIgnore = null;
			if (isMine.equals("true")) {
				jsonArrMyChallenge = jsonDataObj
						.getJSONArray("filter_by_user_id");
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
							bubbleChallenge
									.setColorBubble(ColorChallengeType.MINE);
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
					bubbleChallenge.setMessage(
							"$" + bubbleChallenge.getReward(),
							bubbleChallenge.getInterval(),
							bubbleChallenge.getTitle());
					// bubbleChallenge.setMessage("$"
					// + bubbleChallenge.getReward() + " in "
					// + bubbleChallenge.getDuration());
					bubbleChallengeMap.put(bubbleChallenge.getChallengeId(),
							bubbleChallenge);

				}
			}
			if (isUpcoming.equals("true")) {
				jsonArrUpcoming = jsonDataObj.getJSONArray("filter_upcoming");
				for (int k = 0; k < jsonArrUpcoming.length(); k++) {
					JSONObject jObj = jsonArrUpcoming.getJSONObject(k);

					JSONArray jsonLocation = jObj.getJSONArray("locations");
					int sizeLocation = jsonLocation.length();

					BubbleChallenge bubbleChallenge = GetBubbleChallengeFromJsonObjet(jObj);
					if (userID.equals(bubbleChallenge.getUserID())) {
						if (bubbleChallenge.getIsExpired() != 1) {
							if (sizeLocation > 1) {
								bubbleChallenge
										.setColorBubble(ColorChallengeType.MINE_MULTILOCATION);
							} else {
								bubbleChallenge
										.setColorBubble(ColorChallengeType.MINE);
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
					} else {
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
					}
					bubbleChallenge.setMessage(
							"$" + bubbleChallenge.getReward(),
							bubbleChallenge.getInterval(),
							bubbleChallenge.getTitle());
					// bubbleChallenge.setMessage(bubbleChallenge.getReward()
					// + "$ in " + bubbleChallenge.getDuration());
					if (bubbleChallenge.getAccept() != 2)
						bubbleChallengeMap.put(
								bubbleChallenge.getChallengeId(),
								bubbleChallenge);

				}
			}
			if (isIgnore.equals("true")) {
				jsonArrIgnore = jsonDataObj.getJSONArray("filter_ignore");
				for (int z = 0; z < jsonArrIgnore.length(); z++) {
					JSONObject jObj = jsonArrIgnore.getJSONObject(z);

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
					bubbleChallenge.setMessage(
							"$" + bubbleChallenge.getReward(),
							bubbleChallenge.getInterval(),
							bubbleChallenge.getTitle());
					// bubbleChallenge.setMessage(bubbleChallenge.getReward()
					// + "$ in " + bubbleChallenge.getDuration());
					bubbleChallengeMap.put(bubbleChallenge.getChallengeId(),
							bubbleChallenge);

				}
			}
			if (isHistory.equals("true")) {
				jsonArrHistory = jsonDataObj.getJSONArray("filter_historical");
				for (int j = 0; j < jsonArrHistory.length(); j++) {
					JSONObject jObj = jsonArrHistory.getJSONObject(j);

					JSONArray jsonLocation = jObj.getJSONArray("locations");
					int sizeLocation = jsonLocation.length();

					BubbleChallenge bubbleChallenge = GetBubbleChallengeFromJsonObjet(jObj);
					if (userID.equals(bubbleChallenge.getUserID())) {
						if (bubbleChallenge.getIsExpired() != 1) {
							if (sizeLocation > 1) {
								bubbleChallenge
										.setColorBubble(ColorChallengeType.MINE_MULTILOCATION);
							} else {
								bubbleChallenge
										.setColorBubble(ColorChallengeType.MINE);
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
					} else {
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
					}
					bubbleChallenge.setMessage(
							"$" + bubbleChallenge.getReward(),
							bubbleChallenge.getInterval(),
							bubbleChallenge.getTitle());
					// bubbleChallenge.setMessage(bubbleChallenge.getReward()
					// + "$ in " + bubbleChallenge.getDuration());
					if (bubbleChallenge.getAccept() != 2)
						bubbleChallengeMap.put(
								bubbleChallenge.getChallengeId(),
								bubbleChallenge);

				}
			}

			if (ShowingChallengeType.STATUS_SHOW_CURRENT == ShowingChallengeType.CHALLENGE_SHOW_MY_AREA_HISTORY)
				((MyAreaActivity) context).showChallengeMap(bubbleChallengeMap);
			if (ShowingChallengeType.STATUS_SHOW_CURRENT == ShowingChallengeType.CHALLENGE_SHOW_OPTION)
				((MyAreaActivity) context).showChallengeMap(bubbleChallengeMap);
			if (ShowingChallengeType.STATUS_SHOW_CURRENT == ShowingChallengeType.CHALLENGE_SHOW_NEARBY)
				((MyAreaActivity) context).showChallengeMap(bubbleChallengeMap);

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
		ArrayList<NameValuePair> arrNameValuePairs = new ArrayList<NameValuePair>();
		isMine = params[3];
		isUpcoming = params[4];
		isIgnore = params[5];
		isHistory = params[6];
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
		filter = filter.substring(0, filter.lastIndexOf("|"));
		arrNameValuePairs.add(new BasicNameValuePair("user_id", params[0]));
		userID = params[0];
		arrNameValuePairs.add(new BasicNameValuePair("filter", filter));
		arrNameValuePairs.add(new BasicNameValuePair("lat", params[1]));
		arrNameValuePairs.add(new BasicNameValuePair("long", params[2]));
		JSONParser jsonParser = new JSONParser();
		String json = jsonParser.makeHttpRequest(Config.GET_FILTER_CHALLENGE,
				arrNameValuePairs);
		return json;
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
			bubbleChallenge.setUserID(joJsonObject.getString("user_ID"));
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
			e.printStackTrace();
		}
		return bubbleChallenge;
	}
}

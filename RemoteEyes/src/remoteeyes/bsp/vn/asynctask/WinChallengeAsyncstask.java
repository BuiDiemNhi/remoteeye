package remoteeyes.bsp.vn.asynctask;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import remateeyes.bsp.vn.dialog.DialogResponsePhoto;
import remateeyes.bsp.vn.dialog.DialogResponseVideo;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.common.JSONParser;
import remoteeyes.bsp.vn.model.ImageObject;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

public class WinChallengeAsyncstask extends AsyncTask<String, Void, String> {

	Context context;
	Activity activity;
	String id = "";
	String user_id = "";
	String ui_media_id = "";
	String ui_type_win = "";

	public WinChallengeAsyncstask(Activity activity, String id, String user_id,
			String ui_media_id, String ui_type_win) {

		this.activity = activity;
		this.id = id;
		this.user_id = user_id;
		this.ui_media_id = ui_media_id;
		this.ui_type_win = ui_type_win;
	}

	// TODO Auto-generated constructor stub

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		try {
			JSONObject jsonObject = new JSONObject(result);
			if (jsonObject.getInt("status") == 200) {
				if (ui_type_win.equals("1")) {
					DialogResponsePhoto.isWined = true;
					DialogResponsePhoto.iv_winner.setVisibility(View.VISIBLE);
					DialogResponsePhoto.iv_set_winner.setVisibility(View.GONE);
					// DialogResponsePhoto.imgList.get(index).setWin(true);
				} else if (ui_type_win.equals("2")) {
					DialogResponseVideo.isWined = true;
					DialogResponseVideo.iv_winner.setVisibility(View.VISIBLE);
					DialogResponseVideo.ivSetWinner.setVisibility(View.GONE);
					// DialogResponsePhoto.imgList.get(index).setWin(true);
				}
			}
		} catch (Exception ex) {
		}
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... params) {
		// ui_challenge_id=14&ui_user_id=1&ui_media_id=1&ui_type_win=0
		ArrayList<NameValuePair> arrNameValuePairs = new ArrayList<NameValuePair>();

		arrNameValuePairs.add(new BasicNameValuePair("ui_challenge_id", id));
		arrNameValuePairs.add(new BasicNameValuePair("ui_user_id", user_id));
		arrNameValuePairs
				.add(new BasicNameValuePair("ui_media_id", ui_media_id));
		arrNameValuePairs
				.add(new BasicNameValuePair("ui_type_win", ui_type_win));

		JSONParser jsonParser = new JSONParser();
		String json = jsonParser.makeHttpRequest(Config.API_CHALLENGE_WINNER,
				arrNameValuePairs);
		return json;

	}
}

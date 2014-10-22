package remoteeyes.bsp.vn.asynctask;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import remoteeyes.bsp.vn.MyAreaActivity;
import remoteeyes.bsp.vn.MyGroupActivity;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.common.JSONParser;
import remoteeyes.bsp.vn.type.ShowingChallengeType;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class ShareChallengeAsynctask extends AsyncTask<String, Void, String> {

	Context context;

	public ShareChallengeAsynctask(Context context) {
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... params) {
		ArrayList<NameValuePair> arrNameValuePairs = new ArrayList<NameValuePair>();
		arrNameValuePairs.add(new BasicNameValuePair("ui_user_id", params[0]));
		arrNameValuePairs.add(new BasicNameValuePair("ui_challenge_id",
				params[1]));
		JSONParser jsonParser = new JSONParser();

		String result = jsonParser.makeHttpRequest(Config.API_SHARE_CHALLENGE,
				arrNameValuePairs);
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		try {
			JSONObject jsonObject = new JSONObject(result);
			if (jsonObject.getInt("status") == 200) {
				try {
					if ((MyAreaActivity) context != null) {
						if (((MyAreaActivity) context).detailChallengeFragment != null) {
							((MyAreaActivity) context).detailChallengeFragment
									.setGone();
						}
						if (ShowingChallengeType.STATUS_SHOW_CURRENT == 4) {
							new AcceptedChallengeAsynctask(
									(MyAreaActivity) context)
									.execute(Config.IdUser);
						}
						if (ShowingChallengeType.STATUS_SHOW_CURRENT == 3) {
							new GlobalChallengeAsynctask(
									(MyAreaActivity) context)
									.execute(Config.IdUser);
						}
						if (ShowingChallengeType.STATUS_SHOW_CURRENT == 6) {
							new PostedChallengeAsynctask(
									(MyAreaActivity) context)
									.execute(Config.IdUser);
						}
					}
				} catch (Exception ex) {
				}
			}
		} catch (Exception ex) {
		}
	}

}

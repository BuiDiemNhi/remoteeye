package remoteeyes.bsp.vn.asynctask;

import org.json.JSONObject;

import remoteeyes.bsp.vn.MyAreaActivity;
import remoteeyes.bsp.vn.MyGroupActivity;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.common.JSONParser;
import remoteeyes.bsp.vn.type.ShowingChallengeType;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class DeleteChallengeAsynctask extends AsyncTask<String, Void, String> {

	Context context;
	String idChall;

	public DeleteChallengeAsynctask(Context context) {
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... params) {
		JSONParser jsonParser = new JSONParser();
		String url = String.format(Config.API_DELETE_CHALLENGE, params[0]);
		String result = jsonParser.getStringFromUrl(url);
		idChall = params[0];
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		try {
			JSONObject jsonObject = new JSONObject(result);
			if (jsonObject.getInt("status") == 200) {
				Toast.makeText(context, "Deleted challenge successful",
						Toast.LENGTH_SHORT).show();
				try {
					if (ShowingChallengeType.STATUS_SHOW_CURRENT == ShowingChallengeType.CHALLENGE_SHOW_NEARBY) {
						new CurrentChallengeAsynctask((MyAreaActivity) context)
								.execute(
										Config.lat + "",
										Config.lng + "",
										Config.IdUser,
										ShowingChallengeType.STATUS_SHOW_CURRENT
												+ "");
					} else if (ShowingChallengeType.STATUS_SHOW_CURRENT == ShowingChallengeType.CHALLENGE_SHOW_POSTED) {
						new PostedChallengeAsynctask((MyAreaActivity) context)
								.execute(Config.IdUser);
					} else if (ShowingChallengeType.STATUS_SHOW_CURRENT == ShowingChallengeType.CHALLENGE_SHOW_GLOBAL) {
						new GlobalChallengeAsynctask((MyAreaActivity) context)
								.execute(Config.IdUser);
					}
				} catch (Exception e) {
				}

			} else {
				Toast.makeText(context, "Deleted challenge unsuccessful",
						Toast.LENGTH_SHORT).show();
			}
		} catch (Exception ex) {
		}
	}

}

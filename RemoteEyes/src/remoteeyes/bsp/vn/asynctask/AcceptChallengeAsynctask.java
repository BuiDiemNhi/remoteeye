package remoteeyes.bsp.vn.asynctask;

import remoteeyes.bsp.vn.MyAreaActivity;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.common.JSONParser;
import remoteeyes.bsp.vn.type.ShowingChallengeType;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class AcceptChallengeAsynctask extends AsyncTask<String, Void, String> {

	Context context;
	ProgressDialog dialog;
	String challengeID;

	public AcceptChallengeAsynctask(Context context) {
		this.context = context;
		dialog = new ProgressDialog(context);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		dialog.setMessage("Loading...");
		dialog.show();
	}

	@Override
	protected String doInBackground(String... params) {
		JSONParser jsonParser = new JSONParser();
		challengeID = params[1];
		String result = jsonParser.getStringFromUrl(String.format(
				Config.API_ACCEPT_CHALLENG, params[0], params[1]));
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (dialog.isShowing())
			dialog.dismiss();
		if (!result.equals("-1")) {

			try {
				if ((MyAreaActivity) context != null) {
					if (ShowingChallengeType.STATUS_SHOW_CURRENT == ShowingChallengeType.CHALLENGE_SHOW_GLOBAL) {
						new GlobalChallengeAsynctask((MyAreaActivity) context)
								.execute(Config.IdUser);
					}
					if (ShowingChallengeType.STATUS_SHOW_CURRENT == ShowingChallengeType.CHALLENGE_SHOW_NEARBY) {
						new CurrentChallengeAsynctask(context).execute(
								Config.lat + "", Config.lng + "",
								Config.IdUser,
								ShowingChallengeType.STATUS_SHOW_CURRENT + "");
					}
				}
			} catch (Exception ex) {
				new LoadDetailChallengeAsynctask(context).execute(challengeID);
			}
			
		}
	}
}

package remoteeyes.bsp.vn.asynctask;

import remoteeyes.bsp.vn.DetailChallengeActivity;
import remoteeyes.bsp.vn.MyAreaActivity;
import remoteeyes.bsp.vn.R;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.common.JSONParser;
import remoteeyes.bsp.vn.type.ShowingChallengeType;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class IgnoreChallengeAsynctask extends AsyncTask<String, Void, String> {

	Context context;
	ProgressDialog dialog;
	String challengeId;

	public IgnoreChallengeAsynctask(Context context) {
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
		challengeId = params[1];
		String url = String.format(Config.API_IGNORE_CHALLENGE, params[0],
				params[1]);
		String result = jsonParser.getStringFromUrl(url);
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (dialog.isShowing())
			dialog.dismiss();
		if (result.equals("1")) {
			try {
				if ((MyAreaActivity) context != null) {
					Toast.makeText(context, "Ignored challenge successful",
							Toast.LENGTH_SHORT).show();
					if (ShowingChallengeType.STATUS_SHOW_CURRENT == ShowingChallengeType.CHALLENGE_SHOW_ACCEPTED) {
						new AcceptedChallengeAsynctask((MyAreaActivity) context)
								.execute(Config.IdUser);
					}
					if (ShowingChallengeType.STATUS_SHOW_CURRENT == ShowingChallengeType.CHALLENGE_SHOW_NEARBY) {
						new CurrentChallengeAsynctask(context).execute(
								Config.lat + "", Config.lng + "",
								Config.IdUser,
								ShowingChallengeType.STATUS_SHOW_CURRENT + "");
					}
					if (ShowingChallengeType.STATUS_SHOW_CURRENT == ShowingChallengeType.CHALLENGE_SHOW_GLOBAL) {
						new GlobalChallengeAsynctask((MyAreaActivity) context)
								.execute(Config.IdUser);
					}
				}
			} catch (Exception ex) {
			}
		} else {
			Toast.makeText(context, "Ignored challenge fail",
					Toast.LENGTH_SHORT).show();
		}

	}
}

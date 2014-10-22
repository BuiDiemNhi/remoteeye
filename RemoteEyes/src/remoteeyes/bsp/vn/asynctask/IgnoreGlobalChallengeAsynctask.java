package remoteeyes.bsp.vn.asynctask;

import remoteeyes.bsp.vn.MyAreaActivity;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.common.JSONParser;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class IgnoreGlobalChallengeAsynctask extends AsyncTask<String, Void, String> {

	Context context;
	ProgressDialog dialog;
	String challengeId;

	public IgnoreGlobalChallengeAsynctask(Context context) {
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
		String url = String.format(
				Config.API_IGNORE_GLOBAL_CHALLENGE, params[0], params[1]);
		String result = jsonParser.getStringFromUrl(url);
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (dialog.isShowing())
			dialog.dismiss();
		if(result.equals("1")){
			((MyAreaActivity)context).refreshChallengeMapAfterIgnored(challengeId);
		}
		
	}
}

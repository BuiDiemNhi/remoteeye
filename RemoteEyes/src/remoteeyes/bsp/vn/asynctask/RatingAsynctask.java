package remoteeyes.bsp.vn.asynctask;

import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.common.JSONParser;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class RatingAsynctask extends AsyncTask<String, Void, String> {

	Context context;
	ProgressDialog dialog;

	public RatingAsynctask(Context context) {
		this.context = context;
		dialog = new ProgressDialog(context);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		dialog.setMessage("Loading....");
		dialog.show();
	}

	@Override
	protected String doInBackground(String... params) {
		JSONParser jsonParser = new JSONParser();
		String url = String.format(Config.API_RATING, params[0], params[1],
				params[2], params[3]);
		String result = jsonParser.getStringFromUrl(url);
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (dialog.isShowing())
			dialog.dismiss();
	}
}

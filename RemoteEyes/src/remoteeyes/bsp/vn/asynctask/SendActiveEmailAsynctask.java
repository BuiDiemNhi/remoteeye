package remoteeyes.bsp.vn.asynctask;

import remoteeyes.bsp.vn.MyAccountActivity;
import remoteeyes.bsp.vn.R;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.common.JSONParser;
import remoteeyes.bsp.vn.model.UserInfo;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class SendActiveEmailAsynctask extends AsyncTask<String, Void, String> {

	Context context;
	ProgressDialog dialog;
	int id;

	public SendActiveEmailAsynctask(Context context) {
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
		String result = "";
		try {
			JSONParser jsonParser = new JSONParser();
			String url = String.format(Config.API_SEND_ACTIVE_EMAIL, params[0]);
			id = Integer.parseInt(params[0]);
			result = jsonParser.getStringFromUrl(url);
			Log.e("Test", "Active: " + url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (dialog.isShowing())
			dialog.dismiss();
		if (result.equals("1")) {

			UserInfo info = new UserInfo().getInstance();
			// info.getOthersEmail().get(id).setActive(true);
			((MyAccountActivity) context).refreshOthersEmail();
			Toast.makeText(
					context,
					context.getResources().getString(
							R.string.toast_send_active_success),
					Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(
					context,
					context.getResources().getString(
							R.string.toast_send_active_failed),
					Toast.LENGTH_SHORT).show();
		}
	}
}

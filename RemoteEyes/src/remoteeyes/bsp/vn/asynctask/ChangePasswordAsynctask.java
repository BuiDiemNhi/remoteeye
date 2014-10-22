package remoteeyes.bsp.vn.asynctask;

import remoteeyes.bsp.vn.R;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.common.JSONParser;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class ChangePasswordAsynctask extends
		AsyncTask<String, Void, String> {

	Context context;
	ProgressDialog dialog;

	public ChangePasswordAsynctask(Context context) {
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
		String result = jsonParser
				.getStringFromUrl(String.format(Config.API_CHANGE_PASSWORD, params[0], params[1]));
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (dialog.isShowing())
			dialog.dismiss();
		if (result.equals("1")) {
			Toast.makeText(
					context,
					context.getResources().getString(
							R.string.toast_change_password_sucessfully),
					Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(
					context,
					context.getResources().getString(
							R.string.toast_change_password_failed),
					Toast.LENGTH_SHORT).show();
		}
	}
}

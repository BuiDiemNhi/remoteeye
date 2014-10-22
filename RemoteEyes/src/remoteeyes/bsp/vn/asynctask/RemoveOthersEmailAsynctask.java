package remoteeyes.bsp.vn.asynctask;

import remoteeyes.bsp.vn.MyAccountActivity;
import remoteeyes.bsp.vn.R;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.common.JSONParser;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class RemoveOthersEmailAsynctask extends
		AsyncTask<String, Void, String> {

	Context context;
	ProgressDialog dialog;

	public RemoveOthersEmailAsynctask(Context context) {
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
		String result = jsonParser.getStringFromUrl(String.format(
				Config.API_REMOVE_OTHERS_EMAIL, params[0]));
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (dialog.isShowing())
			dialog.dismiss();
		if (!result.equals("-1")) {
			((MyAccountActivity)context).removeEmailFromUserInfo(result);
			((MyAccountActivity)context).refreshOthersEmail();
			Toast.makeText(
					context,
					context.getResources().getString(
							R.string.toast_remove_others_email_success),
					Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(
					context,
					context.getResources().getString(
							R.string.toast_remove_others_email_failed),
					Toast.LENGTH_SHORT).show();
		}
	}
}

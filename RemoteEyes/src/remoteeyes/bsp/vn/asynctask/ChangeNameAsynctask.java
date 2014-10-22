package remoteeyes.bsp.vn.asynctask;

import java.net.URLEncoder;

import org.apache.http.client.utils.URLEncodedUtils;

import remoteeyes.bsp.vn.MyAccountActivity;
import remoteeyes.bsp.vn.R;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.common.JSONParser;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class ChangeNameAsynctask extends AsyncTask<String, Void, String> {

	Context context;
	ProgressDialog dialog;
	String idUser = "";
	String name = "";

	public ChangeNameAsynctask(Context context, String idUse, String name) {
		this.context = context;
		this.idUser = idUse;
		this.name = name;
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
		String url = String.format(Config.API_CHANGE_NAME, idUser, name,
				"UTF-8");
		String result = jsonParser.getStringFromUrl(url);
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (dialog.isShowing())
			dialog.dismiss();
		if (result.equals("1")) {
			((MyAccountActivity) context).updateNameAfterEdit(true);
			((MyAccountActivity) context).userInfo.setName(name);
			Toast.makeText(
					context,
					context.getResources().getString(
							R.string.toast_change_name_sucessfully),
					Toast.LENGTH_SHORT).show();
		} else {
			((MyAccountActivity) context).updateNameAfterEdit(false);
			Toast.makeText(
					context,
					context.getResources().getString(
							R.string.toast_change_name_failed),
					Toast.LENGTH_SHORT).show();
		}
	}
}

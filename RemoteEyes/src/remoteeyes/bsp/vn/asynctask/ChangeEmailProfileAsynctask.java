package remoteeyes.bsp.vn.asynctask;

import org.json.JSONException;
import org.json.JSONObject;

import remoteeyes.bsp.vn.MyAccountActivity;
import remoteeyes.bsp.vn.R;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.common.JSONParser;
import remoteeyes.bsp.vn.model.UserInfo;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class ChangeEmailProfileAsynctask extends
		AsyncTask<String, Void, String> {

	Context context;
	ProgressDialog dialog;
	int type;
	int type_api;
	String id;

	public ChangeEmailProfileAsynctask(Context context, int type, int type_api) {
		this.context = context;
		this.type = type;
		this.type_api = type_api;
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
		String result = "";
		if (type_api != 0) {
			result = jsonParser.getStringFromUrl(String
					.format(Config.API_CHANGE_PAYPAL_EMAIL2 + params[0]));
			id = params[0];
		} else {
			if (type == 0) {
				result = jsonParser.getStringFromUrl(String.format(
						Config.API_CHANGE_MAIN_EMAIL, params[0]));
				// id = params[0];
			} else if (type == 1) {
				result = jsonParser.getStringFromUrl(String.format(
						Config.API_CHANGE_PAYPAL_EMAIL, params[0], type + ""));
				id = params[0];
			} else if (type == 2) {
				result = jsonParser.getStringFromUrl(String.format(
						Config.API_CHANGE_PAYPAL_EMAIL, params[0], type + ""));
				id = params[0];
			}
		}
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (dialog.isShowing())
			dialog.dismiss();
		if (!result.equals("-1")) {

			if (type_api != 0) {

				if (type == 0) {
					int status = 0;
					try {
						JSONObject json = new JSONObject(result);
						status = json.getInt("status");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (status == 200) {

						((MyAccountActivity) context)
								.updateMainEmailAfterChange(id, type_api);
						((MyAccountActivity) context).refreshOthersEmail();
						Toast.makeText(
								context,
								context.getResources()
										.getString(
												R.string.toast_change_main_email_sucessfully),
								Toast.LENGTH_SHORT).show();
					}
					// MyAccountActivity.otherEmailItem
					// .remove(MyAccountActivity.otherEmailItem.size() - 1);
				} else if (type == 1) {
					int status = 0;
					try {
						JSONObject json = new JSONObject(result);
						status = json.getInt("status");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (status == 200) {
						((MyAccountActivity) context)
								.updatePaypalEmailAfterChange(id, type_api);

						((MyAccountActivity) context).refreshOthersEmail();
						Toast.makeText(
								context,
								context.getResources()
										.getString(
												R.string.toast_change_main_email_sucessfully),
								Toast.LENGTH_SHORT).show();
					}
					// MyAccountActivity.otherEmailItem
					// .remove(MyAccountActivity.otherEmailItem.size() - 1);

				}

			} else {
				// if (!UserInfo.getInstance().getEmail_paypal().equals("")) {
				// MyAccountActivity.otherEmailItem
				// .remove(MyAccountActivity.otherEmailItem.size() - 1);
				// }
				if (type == 0) {
					((MyAccountActivity) context).updateMainEmailAfterChange(
							result, 0);
					((MyAccountActivity) context).refreshOthersEmail();
					Toast.makeText(
							context,
							context.getResources()
									.getString(
											R.string.toast_change_main_email_sucessfully),
							Toast.LENGTH_SHORT).show();
				} else if (type == 1) {
					int status = 0;
					try {
						JSONObject json = new JSONObject(result);
						status = json.getInt("status");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (status == 200) {
						((MyAccountActivity) context)
								.updatePaypalEmailAfterChange(id, type_api);

						((MyAccountActivity) context).refreshOthersEmail();
						Toast.makeText(
								context,
								context.getResources()
										.getString(
												R.string.toast_change_main_email_sucessfully),
								Toast.LENGTH_SHORT).show();
					}

				} else if (type == 2) {
					int status = 0;
					try {
						JSONObject json = new JSONObject(result);
						status = json.getInt("status");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (status == 200) {
						((MyAccountActivity) context)
								.updateMainPaypalEmailAfterChange(id);

						((MyAccountActivity) context).refreshOthersEmail();
						Toast.makeText(
								context,
								context.getResources()
										.getString(
												R.string.toast_change_main_email_sucessfully),
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		} else {
			((MyAccountActivity) context).updateNameAfterEdit(false);
			Toast.makeText(
					context,
					context.getResources().getString(
							R.string.toast_change_main_email_failed),
					Toast.LENGTH_SHORT).show();
		}
	}
}

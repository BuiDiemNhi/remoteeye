package remoteeyes.bsp.vn.asynctask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import remateeyes.bsp.vn.dialog.DialogBlockedUser;
import remoteeyes.bsp.vn.R;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.common.JSONParser;
import remoteeyes.bsp.vn.model.Friend;
import remoteeyes.bsp.vn.model.UserInfo;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class GetInfoUserFromEmailAsynctacks extends
		AsyncTask<Void, Void, String> {
	// Context context;
	Activity activity;
	ProgressDialog dialog;
	boolean canConfigure;
	String email = "";
	DialogBlockedUser dialogBlockedUser;

	public GetInfoUserFromEmailAsynctacks(Activity activity, String email) {
		this.activity = activity;
		this.email = email;
		dialogBlockedUser = new DialogBlockedUser(activity);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		try {
			dialog = new ProgressDialog(activity);
			dialog.setMessage("Loading....");
			dialog.setIndeterminate(true);

			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
			this.canConfigure = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (canConfigure)
			dialog.dismiss();

		JSONObject jsonObject;
		String status;
		try {
			jsonObject = new JSONObject(result);
			status = jsonObject.getString("status");
			if (status.trim().equals("200")) {
				JSONObject jsonResult = jsonObject.getJSONObject("data");
				Friend friend = new Friend();
				friend.setAvatarUrl(jsonResult.getString("avatar"));
				friend.setName(jsonResult.getString("name"));
				friend.setId(jsonResult.getString("ID"));
				friend.setIdSocial(jsonResult.getString("facebookID"));
				friend.setEmail(jsonResult.getString("email"));
				friend.setFacebook(true);
				// JSONArray jarrLinked = jsonResult.getJSONArray("linked");
				// if (jarrLinked.length() > 0) {
				// friend.setFacebook(true);
				// } else {
				// friend.setFacebook(false);
				// }

				if (!dialogBlockedUser.CheckExistFriend(friend)) {
					dialogBlockedUser.arrBlockUser.add(friend.getId());
					friend.setStateBlock(true);
					dialogBlockedUser.friendList.add(friend);
					// dialogBlockedUser.memberAdapter.notifyDataSetChanged();
					try {
						dialogBlockedUser.RefeshListBlockUser();
					} catch (Exception e) {
						e.printStackTrace();
					}
					dialogBlockedUser.flagBlockUser = true;
					dialogBlockedUser.etEmail.setText("");
				} else {
					Toast.makeText(activity, "Email already exists",
							Toast.LENGTH_SHORT).show();
				}

			} else {
				Toast.makeText(activity, "Email not exist", Toast.LENGTH_SHORT)
						.show();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected String doInBackground(Void... params) {
		// TODO Auto-generated method stub
		JSONParser jsonParser = new JSONParser();
		// API_GET_USERINFO_FROM_EMAIL
		String result = "";
		try {

			String url = String.format(Config.API_GET_USERINFO_FROM_EMAIL,
					email);
			result = jsonParser.getStringFromUrl(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}

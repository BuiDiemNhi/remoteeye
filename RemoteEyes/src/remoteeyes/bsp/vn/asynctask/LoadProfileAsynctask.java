package remoteeyes.bsp.vn.asynctask;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import remoteeyes.bsp.vn.MainMenuAcitivity;
import remoteeyes.bsp.vn.MyAccountActivity;
import remoteeyes.bsp.vn.MyAreaActivity;
import remoteeyes.bsp.vn.SplashActivity;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.common.JSONParser;
import remoteeyes.bsp.vn.model.BlockUserItem;
import remoteeyes.bsp.vn.model.LinkedProfileItem;
import remoteeyes.bsp.vn.model.OtherEmailItem;
import remoteeyes.bsp.vn.model.UserInfo;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

public class LoadProfileAsynctask extends AsyncTask<String, Void, String> {

	Context context;
	Activity activity;

	public LoadProfileAsynctask(Context context, Activity activity) {
		this.context = context;
		this.activity = activity;
	}

	// run UI before
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	// run UI alter
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		Log.d("Test", result);

		UserInfo info = UserInfo.getInstance();
		try {
			JSONObject jsonObject = new JSONObject(result);
			info.setId(jsonObject.getString("ID"));
			info.setEmail(jsonObject.getString("email"));
			try {
				String email_paypal = jsonObject.getString("paypal_email");
				if (email_paypal == null || email_paypal.equals("null")) {
					info.setEmail_paypal("");
				} else {
					info.setEmail_paypal(jsonObject.getString("paypal_email"));
				}
			} catch (Exception e) {
				info.setEmail_paypal("");
				e.printStackTrace();
			}
			info.setGender(Integer.parseInt(jsonObject.getString("gender")));
			info.setBirth_year(Integer.parseInt(jsonObject
					.getString("birthday").substring(0, 4)));
			info.setBirth_month(Integer.parseInt(jsonObject.getString(
					"birthday").substring(5, 7)));
			info.setBirth_day(Integer.parseInt(jsonObject.getString("birthday")
					.substring(8, 10)));
			info.setiSocial(context.getSharedPreferences(Config.DATA_FIFE,
					Context.MODE_PRIVATE).getBoolean(Config.IS_SOCIAL_KEY,
					false));
			info.setAvatar(jsonObject.getString("avatar"));
			info.setName(jsonObject.getString("name"));
			info.setBalance(Float.parseFloat(jsonObject.getString("balance")));
			info.setTotal_posted(jsonObject.getInt("total_challenges"));
			info.setTotal_responded(jsonObject.getInt("total_response"));
			info.setTotal_images(jsonObject.getInt("total_images"));
			info.setTotal_rating_image(jsonObject.getInt("total_rating_image"));
			info.setTotal_video(jsonObject.getInt("total_video"));
			info.settotal_rating_video(jsonObject.getInt("total_rating_video"));

			JSONArray jarrBlockList = jsonObject.getJSONArray("block_list");
			ArrayList<BlockUserItem> blockUserList = new ArrayList<BlockUserItem>();
			for (int i = 0; i < jarrBlockList.length(); i++) {
				JSONObject jsonObj = jarrBlockList.getJSONObject(i);
				BlockUserItem item = new BlockUserItem();
				item.setId(jsonObj.getString("ID"));
				item.setName(jsonObj.getString("name"));
				item.setEmail(jsonObj.getString("email"));
				item.setAvatar(jsonObj.getString("avatar"));
				JSONArray jarrLinked = jsonObj.getJSONArray("linked");
				ArrayList<LinkedProfileItem> linkedProfileList = new ArrayList<LinkedProfileItem>();
				for (int j = 0; j < jarrLinked.length(); j++) {
					JSONObject jsonObjLinked = jarrLinked.getJSONObject(j);
					LinkedProfileItem linkedProfileItem = new LinkedProfileItem();
					linkedProfileItem.setId(jsonObjLinked.getString("ID"));
					String name = jsonObj.getString("name");
					linkedProfileItem.setName(name.substring(0, 1)
							.toUpperCase()
							+ name.substring(1, name.length()).toLowerCase());
					linkedProfileItem.setUserID(jsonObjLinked
							.getString("networkID"));
					linkedProfileItem.setStatus(jsonObjLinked
							.getString("status"));
					linkedProfileList.add(linkedProfileItem);
				}
				item.setLinkedProfileList(linkedProfileList);
				blockUserList.add(item);
			}
			info.setBlock_list(blockUserList);
			JSONArray jarrOtherEmail = jsonObject.getJSONArray("orther_email");
			ArrayList<OtherEmailItem> othersEmailList = new ArrayList<OtherEmailItem>();
			for (int i = 0; i < jarrOtherEmail.length(); i++) {
				JSONObject jsonObj = jarrOtherEmail.getJSONObject(i);
				OtherEmailItem item = new OtherEmailItem();
				item.setId(jsonObj.getString("ID"));
				item.setEmail(jsonObj.getString("email"));
				item.setActive(jsonObj.getString("status").equals("0") ? false
						: true);
				othersEmailList.add(item);
			}
			info.setOthersEmail(othersEmailList);

			JSONArray jarrLinkedProfile = jsonObject.getJSONArray("linked");
			ArrayList<LinkedProfileItem> linkedProfileList = new ArrayList<LinkedProfileItem>();
			for (int i = 0; i < jarrLinkedProfile.length(); i++) {
				JSONObject jsonObj = jarrLinkedProfile.getJSONObject(i);
				LinkedProfileItem item = new LinkedProfileItem();
				item.setId(jsonObj.getString("ID"));
				String name = jsonObj.getString("name");
				item.setName(name.substring(0, 1).toUpperCase()
						+ name.substring(1, name.length()).toLowerCase());
				item.setUserID(jsonObj.getString("networkID"));
				item.setStatus(jsonObj.getString("status"));
				linkedProfileList.add(item);
			}
			info.setLinkedProfileItem(linkedProfileList);

			try {
				info.setId_email_other(Integer.parseInt(jsonObject
						.getString("last_other_email_id")));

			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		try {
			((MyAreaActivity) context).loadAvatar();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		try {
			((MyAccountActivity) context).loadAccountInformation();
			((MyAccountActivity) context).refreshOthersEmail();
			((MyAccountActivity) context).refreshLinkedSocialList();
		} catch (Exception ex) {
			((MainMenuAcitivity) context).loadAvatar();
			ex.printStackTrace();

		}
	}

	@Override
	protected String doInBackground(String... params) {
		String result = "";
		try {
			JSONParser jsonParser = new JSONParser();
			String id = params[0].toString().trim();
			String get_Url = String.format(Config.API_GET_PROFILE, id);
			result = jsonParser.getStringFromUrl(get_Url);
		} catch (Exception ex) {
		}

		return result;
	}
}

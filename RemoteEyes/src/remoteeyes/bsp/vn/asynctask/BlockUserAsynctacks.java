package remoteeyes.bsp.vn.asynctask;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import remateeyes.bsp.vn.dialog.DialogBlockedUser;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.common.JSONParser;
import remoteeyes.bsp.vn.model.BlockUserItem;
import remoteeyes.bsp.vn.model.Friend;
import remoteeyes.bsp.vn.model.LinkedProfileItem;
import remoteeyes.bsp.vn.model.UserInfo;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

public class BlockUserAsynctacks extends AsyncTask<Void, Void, String> {

	Activity activity;
	ProgressDialog dialog;
	boolean canConfigure;
	String userID = "";
	String blockUserId = "";
	int type;
	DialogBlockedUser dialogBlockedUser;
	ArrayList<Friend> friendsList;

	public BlockUserAsynctacks(Activity activity, int type, String userID,
			String blockUserId) {
		this.activity = activity;
		this.type = type;
		this.userID = userID;
		this.blockUserId = blockUserId;
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
	protected String doInBackground(Void... params) {
		// TODO Auto-generated method stub
		JSONParser jsonParser = new JSONParser();
		// API_GET_USERINFO_FROM_EMAIL
		String result = "";
		try {
			ArrayList<NameValuePair> arrNameValuePairs = new ArrayList<NameValuePair>();
			arrNameValuePairs.add(new BasicNameValuePair("user_id", userID));
			arrNameValuePairs.add(new BasicNameValuePair("blocked_user_id",
					blockUserId));
			if (type == 1) {
				result = jsonParser.makeHttpRequest(Config.API_BLOCK_USER,
						arrNameValuePairs);
			} else if (type == 2) {
				result = jsonParser.makeHttpRequest(Config.API_UNBLOCK_USER,
						arrNameValuePairs);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (canConfigure)
			dialog.dismiss();
		JSONObject jsonObject;
		int status = 0;
		UserInfo info = new UserInfo().getInstance();
		ArrayList<BlockUserItem> blockUserList;
		JSONArray jarrError = null;
		try {
			jsonObject = new JSONObject(result);
			status = Integer.parseInt(jsonObject.getString("status"));
			try {
				jarrError = jsonObject.getJSONArray("errors");
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (status == 200) {
				JSONArray jarrBlockList = jsonObject.getJSONArray("data");

				blockUserList = new ArrayList<BlockUserItem>();
				try {
					for (int i = 0; i < jarrBlockList.length(); i++) {
						JSONObject jsonObj = jarrBlockList.getJSONObject(i);
						BlockUserItem item = new BlockUserItem();
						item.setId(jsonObj.getString("ID"));
						item.setName(jsonObj.getString("name"));
						item.setEmail(jsonObj.getString("email"));
						item.setAvatar(jsonObj.getString("avatar"));
						JSONArray jarrLinked = jsonObj.getJSONArray("linked");
						ArrayList<LinkedProfileItem> linkedProfileList = new ArrayList<LinkedProfileItem>();
						try {
							for (int j = 0; j < jarrLinked.length(); j++) {
								JSONObject jsonObjLinked = jarrLinked
										.getJSONObject(j);
								LinkedProfileItem linkedProfileItem = new LinkedProfileItem();
								linkedProfileItem.setId(jsonObjLinked
										.getString("ID"));
								String name = jsonObj.getString("name");
								linkedProfileItem.setName(name.substring(0, 1)
										.toUpperCase()
										+ name.substring(1, name.length())
												.toLowerCase());
								linkedProfileItem.setUserID(jsonObjLinked
										.getString("networkID"));
								linkedProfileItem.setStatus(jsonObjLinked
										.getString("status"));
								linkedProfileList.add(linkedProfileItem);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						item.setLinkedProfileList(linkedProfileList);
						blockUserList.add(item);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				info.setBlock_list(blockUserList);
				// friendsList = new ArrayList<Friend>();
				// JSONArray jarrFriendList = jsonObject.getJSONArray("data");
				// ArrayList<Friend> FriendList = new ArrayList<Friend>();
				// for (int i = 0; i < jarrFriendList.length(); i++) {
				// JSONObject jsonObj = jarrFriendList.getJSONObject(i);
				// Friend item = new Friend();
				// item.setId(jsonObj.getString("ID"));
				// item.setName(jsonObj.getString("name"));
				// item.setEmail(jsonObj.getString("email"));
				// item.setAvatarUrl(jsonObj.getString("avatar"));
				// JSONArray jarrLinked = jsonObj.getJSONArray("linked");
				// if (jarrLinked.length() > 0) {
				// item.setFacebook(true);
				// } else {
				// item.setFacebook(false);
				// }
				// friendsList.add(item);
				//
				// }
				// dialogBlockedUser.friendList = friendsList;
				// dialogBlockedUser.RefeshListBlockUser();

				if (type == 1) {
					DialogBlockedUser.BlockUser = "";
					DialogBlockedUser.arrBlockUser.clear();
					if (Config.flag_unblock_user == false) {
						Toast.makeText(activity, "Blocked user success !",
								Toast.LENGTH_SHORT).show();
					} else {
						Config.flag_unblock_user = false;
					}
				} else if (type == 2) {
					DialogBlockedUser.UnBlockUser = "";
					DialogBlockedUser.arrUnBlockUser.clear();
					Toast.makeText(activity, "UnBlocked user success !",
							Toast.LENGTH_SHORT).show();
				}
			} else if (status == 201) {
				if (type == 2) {
					blockUserList = new ArrayList<BlockUserItem>();
					DialogBlockedUser.UnBlockUser = "";
					DialogBlockedUser.arrUnBlockUser.clear();
					Toast.makeText(activity, "UnBlocked user success !",
							Toast.LENGTH_SHORT).show();
					info.setBlock_list(blockUserList);
				}
			} else if (status == 500) {
				String error = jarrError.getString(0);
				Toast.makeText(activity, error, Toast.LENGTH_SHORT).show();
				// DialogBlockedUser.memberAdapter.notifyDataSetChanged();
				try {
					DialogBlockedUser.RefeshListBlockUser();
				} catch (Exception e) {
					e.printStackTrace();
				}
				// DialogBlockedUser.Dismiss(activity);
				// {"status":500,"errors":["User 10 has not blocked before","User 10 has not blocked before"]}
			} else {
				if (type == 1) {
					Toast.makeText(activity, "Blocked user fall !",
							Toast.LENGTH_SHORT).show();
				} else if (type == 2) {
					Toast.makeText(activity, "Un   Blocked user fall !",
							Toast.LENGTH_SHORT).show();
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
}

package remoteeyes.bsp.vn.asynctask;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import remoteeyes.bsp.vn.MyGroupActivity;
import remoteeyes.bsp.vn.TellAFriendActivity;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.common.JSONParser;
import remoteeyes.bsp.vn.model.Friend;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class LoadMemberAsynctask extends AsyncTask<String, Void, String> {

	Context context;
	ProgressDialog dialog;

	public LoadMemberAsynctask(Context context) {
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
		ArrayList<NameValuePair> arrNameValuePairs = new ArrayList<NameValuePair>();
		arrNameValuePairs.add(new BasicNameValuePair("facebook", params[0]));
		arrNameValuePairs.add(new BasicNameValuePair("twitter", params[1]));
		arrNameValuePairs.add(new BasicNameValuePair("googleplus", params[2]));
		arrNameValuePairs.add(new BasicNameValuePair("linkedin", params[3]));
		arrNameValuePairs.add(new BasicNameValuePair("instagram", params[4]));
		arrNameValuePairs.add(new BasicNameValuePair("youtube", params[5]));
		JSONParser jsonParser = new JSONParser();

		String result = jsonParser.makeHttpRequest(Config.API_LOAD_MEMBER,
				arrNameValuePairs);
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (dialog.isShowing())
			dialog.dismiss();
		ArrayList<Friend> friendsList = new ArrayList<Friend>();
		try {
			JSONObject jsonObject = new JSONObject(result);
			JSONObject jsonResult = jsonObject.getJSONObject("result");
			if (jsonResult.getString("status").equals("200")) {
				JSONObject jsonData = jsonResult.getJSONObject("data");
				JSONArray jarrFacebook = jsonData.getJSONArray("facebook");
				addFriend(jarrFacebook, friendsList, "facebook");
				JSONArray jarrTwitter = jsonData.getJSONArray("twitter");
				addFriend(jarrTwitter, friendsList, "twitter");
				JSONArray jarrGooglePlus = jsonData.getJSONArray("google_plus");
				addFriend(jarrGooglePlus, friendsList, "google_plus");
				JSONArray jarrLinkedIn = jsonData.getJSONArray("linked_in");
				addFriend(jarrLinkedIn, friendsList, "linked_in");
				JSONArray jarrInstaram = jsonData.getJSONArray("instagram");
				addFriend(jarrInstaram, friendsList, "instagram");
				JSONArray jarrYoutube = jsonData.getJSONArray("youtube");
				addFriend(jarrYoutube, friendsList, "youtube");

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		try {
			((MyGroupActivity) context).addAdreadyFriends(friendsList);
		} catch (Exception ex) {
			try {
				((TellAFriendActivity) context).setText(friendsList.size());
				((TellAFriendActivity) context).setFriendAlreadyList(friendsList);
//				((TellAFriendActivity) context).dialog.addFriends(friendsList);
//				((TellAFriendActivity) context).setFriendAlreadyList(friendsList);
			} catch (Exception e) {
//				((TellAFriendActivity) context).setText(friendsList.size());
//				((TellAFriendActivity) context).setFriendAlreadyList(friendsList);
			}
		}
	}

	private void addFriend(JSONArray jarr, ArrayList<Friend> friends,
			String nameSocial) {
		try {
			for (int i = 0; i < jarr.length(); i++) {
				Friend friend = new Friend();
				JSONObject jsonObject = jarr.getJSONObject(i);
				friend.setId(jsonObject.getString("ID"));
				friend.setName(jsonObject.getString("name"));
				friend.setAvatarUrl(jsonObject.getString("avatar"));
				if (nameSocial.equals("facebook"))
					friend.setFacebook(true);
				else if (nameSocial.equals("twitter"))
					friend.setTwitter(true);
				else if (nameSocial.equals("google_plus"))
					friend.setGoogle(true);
				else if (nameSocial.equals("linked_in"))
					friend.setLinkedIn(true);
				else if (nameSocial.equals("instagram"))
					friend.setInstagram(true);
				else if (nameSocial.equals("youtube"))
					friend.setYoutube(true);
				friends.add(friend);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
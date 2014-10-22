package remoteeyes.bsp.vn.asynctask;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import remoteeyes.bsp.vn.MyGroupActivity;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.common.JSONParser;
import remoteeyes.bsp.vn.model.Friend;
import remoteeyes.bsp.vn.model.Group;
import android.content.Context;
import android.os.AsyncTask;

public class LoadGroupByUserAsynctask extends AsyncTask<String, Void, String> {

	Context context;

	public LoadGroupByUserAsynctask(Context context) {
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... params) {
		JSONParser jsonParser = new JSONParser();
		String url = String.format(
				Config.API_LOAD_GROUP_BY_USER, params[0]);
		String result = jsonParser.getStringFromUrl(url);
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		try{
			((MyGroupActivity)context).removeGroup();
			JSONObject jsonObject = new JSONObject(result);
			if(jsonObject.getString("status").equals("200")){
				JSONArray jarr = jsonObject.getJSONArray("data");
				ArrayList<Friend> friendList = new ArrayList<Friend>();
				for(int i = 0; i < jarr.length(); i++){
					JSONObject json = jarr.optJSONObject(i);
					Group group = new Group();
					JSONArray jfriend = json.getJSONArray("user");
					for(int j = 0; j < jfriend.length(); j++){
						JSONObject js = jfriend.optJSONObject(i);
						Friend f = new Friend();
						f.setId(js.getString("ID"));
						f.setAvatarUrl(js.getString("avatar"));
						f.setName(js.getString("name"));
						if(!js.getString("facebookID").equals(null))				
							f.setFacebook(true);
						else if(!js.getString("twitterID").equals(null))				
							f.setTwitter(true);
						else if(!js.getString("googleID").equals(null))				
							f.setGoogle(true);
						else if(!js.getString("linkedID").equals(null))				
							f.setLinkedIn(true);
						else if(!js.getString("instagramID").equals(null))				
							f.setInstagram(true);
						else if(!js.getString("youtubeID").equals(null))				
							f.setYoutube(true);
						friendList.add(f);
					}
					group.setFriendOfGroupList(friendList);
					group.setName(json.getString("name"));
					group.setId(json.getString("ID"));
					((MyGroupActivity)context).addGroup(group);
				}
				((MyGroupActivity)context).showMyGroups();
			}			
		} catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
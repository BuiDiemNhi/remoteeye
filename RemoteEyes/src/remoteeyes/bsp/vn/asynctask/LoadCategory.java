package remoteeyes.bsp.vn.asynctask;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import remoteeyes.bsp.vn.R;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.common.JSONParser;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

public class LoadCategory extends AsyncTask<Void, Void, String> {

	Activity activity;
	ProgressDialog dialog;
	ArrayList<String> categoryList;

	public LoadCategory(Activity activity) {
		this.activity = activity;
		this.dialog = new ProgressDialog(activity);
		categoryList = new ArrayList<String>();
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		dialog.setMessage("Loading....");
		dialog.show();
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (dialog.isShowing())
			dialog.dismiss();

		try {
			JSONObject jsonObject = new JSONObject(result);
			JSONArray jsonArray = null;
			jsonArray = jsonObject.getJSONArray("result");
			categoryList.add(activity.getResources().getString(
					R.string.select_category));
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonCategory = jsonArray.getJSONObject(i);
				categoryList.add(jsonCategory.getString("name"));
			}
			// try {
			// ((CreateGlobalChallengeActivity)
			// activity).createGlobalChallengeFragment
			// .setCategoryAdapter(categoryList);
			// ((CreateChallengeActivity) activity).fmCreateChallenge
			// .setCategoryAdapter(categoryList);
			// } catch (Exception ex) {
			// try {
			// ((MyAreaActivity)
			// activity).detailChallengeFragment.dialogeditReward
			// .setCategoryAdapter(categoryList);
			// } catch (Exception e) {
			// ((DetailChallengeActivity)
			// activity).detailChallengeFragment.dialogeditReward
			// .setCategoryAdapter(categoryList);
			// }
			// }
//			try{
//			((CreateChallengeActivity) activity).fmCreateChallenge
//					.setCategoryAdapter(categoryList);
//			}catch(Exception ex){
//				try{
//				((MyAreaActivity) activity).detailChallengeFragment.dialogeditReward.setCategoryAdapter(categoryList);
//				}catch(Exception e){
//					((DetailChallengeActivity) activity).detailChallengeFragment.dialogeditReward.setCategoryAdapter(categoryList);
//				}
//			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected String doInBackground(Void... params) {
		JSONParser jsonParser = new JSONParser();
		String jsonString = jsonParser
				.getStringFromUrl(Config.API_GET_CATEGORY);

		return jsonString;
	}
}

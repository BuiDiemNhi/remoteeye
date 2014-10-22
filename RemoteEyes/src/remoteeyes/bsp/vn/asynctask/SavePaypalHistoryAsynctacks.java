package remoteeyes.bsp.vn.asynctask;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import remoteeyes.bsp.vn.MyAccountActivity;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.common.JSONParser;
import remoteeyes.bsp.vn.model.UserInfo;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

public class SavePaypalHistoryAsynctacks extends
		AsyncTask<String, Void, String> {
	Context context;
	Activity activity;
	String info;
	String item_list;
	String user_id;
	String id_payment;

	public SavePaypalHistoryAsynctacks(Activity activity, String id_payment,
			String info, String item_list) {
		this.activity = activity;
		this.info = info;
		this.item_list = item_list;
		this.id_payment = id_payment;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		ArrayList<NameValuePair> arrNameValuePairs = new ArrayList<NameValuePair>();
		arrNameValuePairs
				.add(new BasicNameValuePair(" id_payment", id_payment));
		arrNameValuePairs.add(new BasicNameValuePair("info", info));
		arrNameValuePairs.add(new BasicNameValuePair("item_list", item_list));
		arrNameValuePairs.add(new BasicNameValuePair("user_id", UserInfo
				.getInstance().getId()));

		JSONParser jsonParser = new JSONParser();
		String json = jsonParser.makeHttpRequest(
				Config.API_SAVE_PAYPAL_HISTORY, arrNameValuePairs);
		return json;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		try {
			JSONObject jsonObject = new JSONObject(result);
			if (jsonObject.getInt("status") == 200) {
				setIsPaidChallenge(id_payment);
				// MyAccountActivity.id_payment = "";
			}
		} catch (Exception ex) {
		}
	}

	void setIsPaidChallenge(String id_payment) {
		UserInfo info = UserInfo.getInstance();
		String[] arr = id_payment.split(",");
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < info.getPaymentList().size(); j++) {
				if (Integer.parseInt(info.getPaymentList().get(j).getId()) == Integer
						.parseInt(arr[i])) {
					info.getPaymentList().remove(j);
				}
			}

		}
	}
}

package remoteeyes.bsp.vn.asynctask;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import remateeyes.bsp.vn.dialog.DialogBlockedUser;
import remateeyes.bsp.vn.dialog.DialogPaymentChallenge;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.common.JSONParser;
import remoteeyes.bsp.vn.model.BlockUserItem;
import remoteeyes.bsp.vn.model.PaymentItem;
import remoteeyes.bsp.vn.model.UserInfo;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

public class PaymentChallengeAsynctacks extends AsyncTask<Void, Void, String> {

	Activity activity;
	ProgressDialog dialog;
	boolean canConfigure;
	String idUser;

	public PaymentChallengeAsynctacks(Activity activity, String idUser) {
		this.activity = activity;
		this.idUser = idUser;
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
		JSONParser jsonParser = new JSONParser();
		String result = "";
		result = jsonParser
				.getStringFromUrl(Config.API_GET_LIST_CHALLENGE_NOT_PAYMENT
						+ idUser);
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (canConfigure)
			dialog.dismiss();
		UserInfo info = new UserInfo().getInstance();
		int status = 0;
		JSONObject jsonObject;
		ArrayList<PaymentItem> paymentList = new ArrayList<PaymentItem>();
		try {
			jsonObject = new JSONObject(result);
			status = Integer.parseInt(jsonObject.getString("status"));

			if (status == 200) {
				JSONArray jarrPaymentList = jsonObject.getJSONArray("data");
				paymentList = new ArrayList<PaymentItem>();
				for (int i = 0; i < jarrPaymentList.length(); i++) {
					JSONObject jsonObj = jarrPaymentList.getJSONObject(i);
					PaymentItem item = new PaymentItem();
					item.setId(jsonObj.getString("ID"));
					item.setTitle(jsonObj.getString("title"));
					item.setBudget(jsonObj.getString("budget"));
					item.setSelect(true);
					paymentList.add(item);
				}
				info.setPaymentList(paymentList);
				DialogPaymentChallenge.RefeshListPayment(paymentList);
			} else if (status == 201) {
				DialogPaymentChallenge.RefeshListPayment(paymentList);
				Toast.makeText(activity, "List Payment empty !",
						Toast.LENGTH_SHORT).show();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

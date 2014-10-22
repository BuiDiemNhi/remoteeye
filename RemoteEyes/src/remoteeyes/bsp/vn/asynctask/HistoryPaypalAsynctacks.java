package remoteeyes.bsp.vn.asynctask;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import remateeyes.bsp.vn.dialog.DialogHistoryPaypal;
import remateeyes.bsp.vn.dialog.DialogPaymentChallenge;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.common.JSONParser;
import remoteeyes.bsp.vn.model.HistoryPaypalItem;
import remoteeyes.bsp.vn.model.PaymentItem;
import remoteeyes.bsp.vn.model.UserInfo;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

public class HistoryPaypalAsynctacks extends AsyncTask<Void, Void, String> {

	Activity activity;
	ProgressDialog dialog;
	boolean canConfigure;
	String idUser;
	UserInfo info = new UserInfo().getInstance();

	public HistoryPaypalAsynctacks(Activity activity) {
		this.activity = activity;
		this.idUser = info.getId();
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
		String result = "";
		result = jsonParser.getStringFromUrl(Config.API_GET_PAYPAL_HISTORY
				+ idUser);
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (canConfigure)
			dialog.dismiss();

		int status = 0;
		JSONObject jsonObject;
		ArrayList<HistoryPaypalItem> arrayList = new ArrayList<HistoryPaypalItem>();
		try {
			jsonObject = new JSONObject(result);
			status = Integer.parseInt(jsonObject.getString("status"));

			if (status == 200) {
				JSONArray jarrPaymentList = jsonObject.getJSONArray("data");
				arrayList = new ArrayList<HistoryPaypalItem>();
				for (int i = 0; i < jarrPaymentList.length(); i++) {
					JSONObject jsonObj = jarrPaymentList.getJSONObject(i);
					HistoryPaypalItem item = new HistoryPaypalItem();
					item.setId(jsonObj.getString("id"));
					item.setPaypal_id(jsonObj.getString("paypal_id"));

					item.setType(jsonObj.getInt("type"));

					// SimpleDateFormat sdf1 = new SimpleDateFormat(
					// "yyyy-MM-dd hh:mm:ss");
					// String day = jsonObj.getString("date_created");
					// java.util.Date date = sdf1.parse(day);
					// SimpleDateFormat sdf = new
					// SimpleDateFormat("yyyy-mm-dd");
					//
					// item.setDate_created(sdf.format(date));

					item.setDate_created(jsonObj.getString("date_created"));
					item.setTitle_challenge(jsonObj
							.getString("short_description"));
					item.setTotal_amount(Float.parseFloat(jsonObj
							.getString("total_amount")));
					try {
						JSONArray ArrItem = jsonObj.getJSONArray("list_item");
						if (ArrItem.length() > 0) {
							item.setQuantity(ArrItem.length());
						} else {
							item.setQuantity(0);
						}
					} catch (Exception e) {
						item.setQuantity(0);
						e.printStackTrace();
					}
					arrayList.add(item);
				}
				// info.setPaymentList(arrayList);
				DialogHistoryPaypal.RefeshListHistory(arrayList);

			} else if (status == 201) {
				DialogHistoryPaypal.RefeshListHistory(arrayList);
				Toast.makeText(activity, "List History empty !",
						Toast.LENGTH_SHORT).show();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

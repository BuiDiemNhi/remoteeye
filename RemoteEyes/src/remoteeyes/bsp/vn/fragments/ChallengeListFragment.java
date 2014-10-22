package remoteeyes.bsp.vn.fragments;

import java.util.ArrayList;

import org.json.JSONException;

import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import remoteeyes.bsp.vn.DetailChallengeActivity;
import remoteeyes.bsp.vn.MyAreaActivity;
import remoteeyes.bsp.vn.R;
import remoteeyes.bsp.vn.adapter.ChallengeListAdapter;
import remoteeyes.bsp.vn.asynctask.FilterGlobalChallengeAsynctask;
import remoteeyes.bsp.vn.asynctask.PostedChallengeAsynctask;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.model.ChallengeObject;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

public class ChallengeListFragment extends Fragment {

	ListView lvChallenge;
	public ChallengeListAdapter adapter;
	Activity activity;
	ArrayList<ChallengeObject> objList;
	RelativeLayout layout;
	Boolean isList = false;
	Boolean loadingMore = true;
	public int lenght_arraylist_all;
	public ArrayList<ChallengeObject> arrayList_news_load;
	public ChallengeListAdapter ChallengeListAdapter;
	public static ArrayList<ChallengeObject> arrayList_news_all = new ArrayList<ChallengeObject>();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_challenge_list,
				container, false);
		// View mLoadMoreFooter = inflater.inflate(R.layout.loading_footer_item,
		// null);
		lvChallenge = (ListView) view.findViewById(R.id.lv_challenge);
		lvChallenge.setAdapter(new ChallengeListAdapter(activity,
				R.layout.item_list_challenge, this.objList));
		// lvChallenge.addFooterView(mLoadMoreFooter);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.height = (int) (Config.screenHeight - Config.screenWidth * 0.2);
		lvChallenge.setLayoutParams(params);
		// lvChallenge.setOnScrollListener(new EndlessScrollListener());
		lvChallenge.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:// New touch started
					isList = true;
					break;
				case MotionEvent.ACTION_MOVE:// Finger is moving
					isList = true;
					break;
				case MotionEvent.ACTION_UP:// Finger went up
					isList = true;
					break;
				default:
					isList = false;
					break;
				}
				return false;
			}
		});
		lvChallenge.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (!MyAreaActivity.isEnable && isList) {
					Intent intentDetail = new Intent(activity,
							DetailChallengeActivity.class);
					intentDetail.putExtra("id", objList.get(position).getId());
					ChallengeObject co = objList.get(position);
					intentDetail.putExtra(
							"area",
							co.getLocationsList()
									.get(co.getLocationsList().size() - 1)
									.getArea());
					intentDetail.putExtra(
							"lat",
							co.getLocationsList()
									.get(co.getLocationsList().size() - 1)
									.getLat()
									+ "");
					intentDetail.putExtra(
							"lng",
							co.getLocationsList()
									.get(co.getLocationsList().size() - 1)
									.getLng()
									+ "");
					activity.startActivity(intentDetail);
				}
				if (MyAreaActivity.isEnable && isList) {
					MyAreaActivity.isEnable = false;
				}
			}
		});
		// lvChallenge.setOnScrollListener(new OnScrollListener() {
		//
		// @Override
		// public void onScrollStateChanged(AbsListView view, int scrollState) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public void onScroll(AbsListView view, int firstVisibleItem,
		// int visibleItemCount, int totalItemCount) {
		// if (visibleItemCount != 0 && ((firstVisibleItem + visibleItemCount)
		// >= (totalItemCount))) {
		// MyAreaActivity.page +=1;
		// new FilterGlobalChallengeAsynctask(activity).execute(
		// Config.IdUser, String.valueOf(false),
		// String.valueOf(true), String.valueOf(false),
		// String.valueOf(false), String.valueOf(true), "",
		// MyAreaActivity.page + "");
		// // Update adapter Here
		//
		// }
		//
		// }
		// });
		return view;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		case Config.REQUEST_CODE_PAYMENT:
			if (resultCode == Activity.RESULT_OK) {
				PaymentConfirmation confirm = data
						.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
				if (confirm != null) {
					try {
						Log.i(Config.TAG, confirm.toJSONObject().toString(4));
						Log.i(Config.TAG, confirm.getPayment().toJSONObject()
								.toString(4));
						/**
						 * TODO: send 'confirm' (and possibly
						 * confirm.getPayment() to your server for verification
						 * or consent completion. See
						 * https://developer.paypal.com
						 * /webapps/developer/docs/integration
						 * /mobile/verify-mobile-payment/ for more details.
						 * 
						 * For sample mobile backend interactions, see
						 * https://github
						 * .com/paypal/rest-api-sdk-python/tree/master
						 * /samples/mobile_backend
						 */

						Toast.makeText(
								activity.getApplicationContext(),
								"PaymentConfirmation info received from PayPal",
								Toast.LENGTH_LONG).show();

					} catch (JSONException e) {
						Log.e(Config.TAG,
								"an extremely unlikely failure occurred: ", e);
					}
				}
			} else if (resultCode == Activity.RESULT_CANCELED) {
				Log.i(Config.TAG, "The user canceled.");
			} else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
				Log.i(Config.TAG,
						"An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
			}
			break;
		case Config.REQUEST_CODE_FUTURE_PAYMENT:
			if (resultCode == Activity.RESULT_OK) {
				PayPalAuthorization auth = data
						.getParcelableExtra(PayPalFuturePaymentActivity.EXTRA_RESULT_AUTHORIZATION);
				if (auth != null) {
					try {
						Log.i("FuturePaymentExample", auth.toJSONObject()
								.toString(4));

						String authorization_code = auth.getAuthorizationCode();
						Log.i("FuturePaymentExample", authorization_code);

						sendAuthorizationToServer(auth);
						Toast.makeText(activity.getApplicationContext(),
								"Future Payment code received from PayPal",
								Toast.LENGTH_LONG).show();

					} catch (JSONException e) {
						Log.e("FuturePaymentExample",
								"an extremely unlikely failure occurred: ", e);
					}
				}
			} else if (resultCode == Activity.RESULT_CANCELED) {
				Log.i("FuturePaymentExample", "The user canceled.");
			} else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
				Log.i("FuturePaymentExample",
						"Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
			}
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	private void sendAuthorizationToServer(PayPalAuthorization authorization) {

		/**
		 * TODO: Send the authorization response to your server, where it can
		 * exchange the authorization code for OAuth access and refresh tokens.
		 * 
		 * Your server must then store these tokens, so that your server code
		 * can execute payments for this user in the future.
		 * 
		 * A more complete example that includes the required app-server to
		 * PayPal-server integration is available from
		 * https://github.com/paypal/
		 * rest-api-sdk-python/tree/master/samples/mobile_backend
		 */

	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public void setLayout(RelativeLayout layout) {
		this.layout = layout;
	}

	public void setAdapter(ArrayList<ChallengeObject> objList) {
		this.objList = objList;
	}

	public void UpdateAdapter(ArrayList<ChallengeObject> objList) {
		for (int i = 0; i < objList.size(); i++) {
			this.objList.add(objList.get(i));
		}
		
		this.arrayList_news_all = objList;
		// this.lenght_arraylist_all = arrayList_news_all.size();
		this.lenght_arraylist_all = objList.size();

		if (Config.fitnews == true) {
			Config.fitnews = false;
			Config.total = 0;
			int rowmore = Config.total;
			if (Config.total +6  > lenght_arraylist_all)
				rowmore += lenght_arraylist_all - Config.total;
			else
				rowmore += 10;
			// int rowmore = Config.rowload + 5;
			for (int i = Config.total; i < rowmore; i++) {

				ChallengeObject newsItem = new ChallengeObject();

				if (objList.get(i) != null) {
					newsItem = objList.get(i);
					this.arrayList_news_load.add(newsItem);
				}

			}
			Config.total += 6;

		}
		
		
		lvChallenge.removeAllViews();
		lvChallenge.setAdapter(new ChallengeListAdapter(activity,
				R.layout.item_list_challenge, this.objList));
		
	}

	public class EndlessScrollListener implements OnScrollListener {

		private int visibleThreshold = 6;

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			
			if (lvChallenge.getLastVisiblePosition() >= lvChallenge
					.getCount() - visibleThreshold) {
				int totalPage = Config.total / 6;
				int du = Config.total % 6;
				if (du != 0) {
					totalPage += 1;
				}
				if (MyAreaActivity.page <= totalPage) {
					MyAreaActivity.page += 1;
					new PostedChallengeAsynctask(activity).execute(
							Config.IdUser, MyAreaActivity.page + "");
				}
			}
			/*int lastInScreen = firstVisibleItem + visibleItemCount;
			if ((lastInScreen == totalItemCount && Config.total < lvChallenge.getCount())) {

				try {

					// Log.e("test", "rowload : " + Config.rowload);
					// Log.e("test", "lengh all: " + lenght_arraylist_all);
					// Log.e("test", "lengh arrayList_news_load: "
					// + arrayList_news_load.size());
					int rowmore = Config.total;
					Log.e("test", "rowmore  : " + rowmore + "");
					if (Config.total + visibleThreshold > lenght_arraylist_all)
						rowmore += lenght_arraylist_all - Config.total;
					else
						rowmore += 6;
					for (int i = Config.total; i < rowmore; i++) {

						ChallengeObject newsItem = new ChallengeObject();

						// if (arrayList_news_all.get(i) != null) {
						newsItem = arrayList_news_all.get(i);
						arrayList_news_load.add(newsItem);
						// }
					}
					// Log.e("test", "lengh all 1 :" +
					// lenght_arraylist_all);
					// Log.e("test", "rowload 1 :" + Config.rowload);
					// customListAdapter.notifyDataSetChanged();
					if (Config.total <= lenght_arraylist_all - visibleThreshold) {

						Config.total += visibleThreshold;
						ChallengeListAdapter.notifyDataSetChanged();
						// Log.e("test", "lengh all 2 :" +
						// lenght_arraylist_all);
						// Log.e("test", "rowload 2 :" + Config.rowload);

					} else if (Config.total >= lenght_arraylist_all - 6
							&& Config.total <= lenght_arraylist_all) {
						Config.total += lenght_arraylist_all
								- Config.total;
						// Log.e("test", "lengh all 3 :" +
						// lenght_arraylist_all);
						// Log.e("test", "rowload 3 :" + Config.rowload);
						ChallengeListAdapter.notifyDataSetChanged();
					} else {
						Toast toast = Toast.makeText(
								activity.getApplicationContext(), "End news!!!...",
								Toast.LENGTH_SHORT);
					}
				} catch (Exception e) {
					Toast toast = Toast.makeText(activity.getApplicationContext(),
							"End news!!!...", Toast.LENGTH_SHORT);
				}
				
			}
			*/
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
/*
			if (scrollState == SCROLL_STATE_IDLE) {
				if (lvChallenge.getLastVisiblePosition() >= lvChallenge
						.getCount() - visibleThreshold) {
					int totalPage = Config.total / 6;
					int du = Config.total % 6;
					if (du != 0) {
						totalPage += 1;
					}
					if (MyAreaActivity.page <= totalPage) {
						MyAreaActivity.page += 1;
						new PostedChallengeAsynctask(activity).execute(
								Config.IdUser, MyAreaActivity.page + "");
					}
				}
			}*/
		}

	}


}

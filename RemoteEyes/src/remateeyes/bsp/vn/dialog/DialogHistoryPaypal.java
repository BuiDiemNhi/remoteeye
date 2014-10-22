package remateeyes.bsp.vn.dialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import remoteeyes.bsp.vn.R;
import remoteeyes.bsp.vn.adapter.HistoryListAdapter;
import remoteeyes.bsp.vn.adapter.PaymentListAdapter;
import remoteeyes.bsp.vn.asynctask.HistoryPaypalAsynctacks;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.model.HistoryPaypalItem;
import remoteeyes.bsp.vn.model.PaymentItem;
import remoteeyes.bsp.vn.uilt.ResizeUtils;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class DialogHistoryPaypal extends Dialog implements
		android.view.View.OnClickListener {
	static Activity activity;
	Context context;
	LinearLayout ll_lvHistory, ll_contentHistory, ll_ok_cancel;
	TextView tv_title, TvNote, tv_title_description, tv_title_amount,
			tv_title_balance;
	public static TextView tv_choose_date;
	ImageButton ib_prev_date, ib_next_date;
	Button btn_ok_history;
	static ListView lv_history;
	static HistoryListAdapter historyListAdapter;
	public static ArrayList<HistoryPaypalItem> historyPaypalItems;
	public static ArrayList<String> ArrDate;
	public static int index;

	public DialogHistoryPaypal(Context context) {
		super(context);
		this.context = context;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setContentView(R.layout.dialog_history_paypal);

		tv_title = (TextView) findViewById(R.id.tv_title);
		TvNote = (TextView) findViewById(R.id.TvNote);
		tv_choose_date = (TextView) findViewById(R.id.tv_choose_date);
		tv_title_description = (TextView) findViewById(R.id.tv_title_description);
		tv_title_amount = (TextView) findViewById(R.id.tv_title_amount);
		tv_title_balance = (TextView) findViewById(R.id.tv_title_balance);
		ib_prev_date = (ImageButton) findViewById(R.id.ib_prev_date);
		ib_next_date = (ImageButton) findViewById(R.id.ib_next_date);
		btn_ok_history = (Button) findViewById(R.id.btn_ok_history);
		lv_history = (ListView) findViewById(R.id.lv_history);
		ll_lvHistory = (LinearLayout) findViewById(R.id.ll_lvHistory);
		ll_contentHistory = (LinearLayout) findViewById(R.id.ll_contentHistory);
		ll_ok_cancel = (LinearLayout) findViewById(R.id.ll_ok_cancel);

		ib_prev_date.setOnClickListener(this);
		ib_next_date.setOnClickListener(this);
		btn_ok_history.setOnClickListener(this);
		index = 0;
		setFontTextView();
		new HistoryPaypalAsynctacks(activity).execute();
		adjustDialog();

	}

	private void setFontTextView() {
		Typeface typeface = Typeface.createFromAsset(activity.getAssets(),
				"fonts/HomeRunScript-roman.otf");
		tv_title.setTypeface(typeface);

	}

	private void adjustDialog() {
		Drawable cancelDrawable = context.getResources().getDrawable(
				R.drawable.cancel_button);

		ResizeUtils.resizeButton(btn_ok_history,
				(int) (Config.screenWidth * 0.1),
				(int) (Config.screenWidth * 0.1), 0, 0, 0, 0);
		ResizeUtils.resizeImageButton(ib_prev_date,
				(int) (Config.screenWidth * 0.05),
				(int) (Config.screenWidth * 0.05), 0,
				(int) (Config.screenWidth * 0.05), 0, 0);
		ResizeUtils.resizeImageButton(ib_next_date,
				(int) (Config.screenWidth * 0.05),
				(int) (Config.screenWidth * 0.05),
				(int) (Config.screenWidth * 0.05), 0, 0, 0);
		ResizeUtils.resizeLinearLayout(ll_lvHistory, Config.screenWidth,
				(int) (Config.screenHeight * 0.25), 0, 0, 0, 0);
		ResizeUtils.resizeLinearLayout(ll_contentHistory, Config.screenWidth,
				(int) (Config.screenHeight * 0.3), 0, 0, 0, 0);
		ResizeUtils.resizeLinearLayout(ll_ok_cancel, Config.screenWidth,
				(int) (Config.screenHeight * 0.1), 0, 0, 0, 0);

	}

	private static String ChangeDate(String d) {
		String day = "";
		java.util.Date date;
		try {
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
			date = sdf1.parse(d);
			SimpleDateFormat sdf = new SimpleDateFormat("MMMM,yyyy");
			day = sdf.format(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return day;
	}

	private static void getListDate(ArrayList<HistoryPaypalItem> arrayList) {
		ArrDate = new ArrayList<String>();
		ArrDate.add(arrayList.get(0).getDate_created().toString()
				.substring(0, 10));
		for (int i = 1; i < arrayList.size(); i++) {
			if (!arrayList
					.get(i)
					.getDate_created()
					.toString()
					.substring(0, 10)
					.equals(arrayList.get(i - 1).getDate_created().toString()
							.substring(0, 10))) {
				ArrDate.add(arrayList.get(i).getDate_created().toString()
						.substring(0, 10));
			}
		}
	}

	public static void RefeshListHistory(
			final ArrayList<HistoryPaypalItem> arrayList) {

		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {
					if (arrayList.size() > 0) {
						historyPaypalItems = arrayList;
						getListDate(arrayList);
						String date = arrayList.get(0).getDate_created();
						tv_choose_date.setText(ChangeDate(date.substring(0, 10)));
						loadHistoryFromDate(date.substring(0, 10),
								historyPaypalItems);
					} else {
						Date date = new Date();
						SimpleDateFormat sdf1 = new SimpleDateFormat(
								"yyyy-MM-dd");
						String d = sdf1.format(date);
						tv_choose_date.setText(ChangeDate(d.toString()
								.substring(0, 10)));
					}
					// historyListAdapter = new HistoryListAdapter(activity,
					// R.layout.item_history_paypal, arrayList);
					// lv_history.setAdapter(historyListAdapter);
					// paymentListAdapter.notifyDataSetChanged();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	public static void loadHistoryFromDate(String date,
			ArrayList<HistoryPaypalItem> arrayList) {
		ArrayList<HistoryPaypalItem> arrayListShow = new ArrayList<HistoryPaypalItem>();
		for (int i = 0; i < arrayList.size(); i++) {
			if (ChangeDate(
					arrayList.get(i).getDate_created().toString()
							.substring(0, 10)).equals(ChangeDate(date))) {
				arrayListShow.add(arrayList.get(i));
			}
		}
		try {
			historyListAdapter = new HistoryListAdapter(activity,
					R.layout.item_history_paypal, arrayListShow);
			lv_history.setAdapter(historyListAdapter);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.ib_prev_date:
			try {
				if (index < ArrDate.size() - 1) {
					index += 1;
					String date = ArrDate.get(index);
					tv_choose_date.setText(ChangeDate(date));
					loadHistoryFromDate(date.substring(0, 10),
							historyPaypalItems);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case R.id.ib_next_date:
			try {
				if (index > 0) {
					index -= 1;
					String date = ArrDate.get(index);
					tv_choose_date.setText(ChangeDate(date));
					loadHistoryFromDate(date.substring(0, 10),
							historyPaypalItems);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case R.id.btn_ok_history:
			dismiss();
			break;

		default:
			break;
		}
	}
}

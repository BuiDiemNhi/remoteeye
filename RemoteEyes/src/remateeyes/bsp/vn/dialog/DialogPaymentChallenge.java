package remateeyes.bsp.vn.dialog;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.paypal.android.sdk.payments.PayPalItem;

import remoteeyes.bsp.vn.MyAccountActivity;
import remoteeyes.bsp.vn.R;
import remoteeyes.bsp.vn.adapter.MemberAdapter;
import remoteeyes.bsp.vn.adapter.PaymentListAdapter;
import remoteeyes.bsp.vn.asynctask.BlockUserAsynctacks;
import remoteeyes.bsp.vn.asynctask.PaymentChallengeAsynctacks;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.model.PaymentItem;
import remoteeyes.bsp.vn.model.UserInfo;
import remoteeyes.bsp.vn.uilt.ResizeUtils;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DialogPaymentChallenge extends Dialog implements
		android.view.View.OnClickListener {
	Context context;
	Button btn_ok_payment, btn_cancel_payment;
	TextView tv_title_payment;
	public static TextView tv_message_empty;
	static TextView tv_total_money_payment;
	public static CheckBox cb_choose_all_payment;
	static ListView lv_payment;
	static Activity activity;
	public static PaymentListAdapter paymentListAdapter;
	static ArrayList<PaymentItem> PaymentItemList;
	public static int sum = 0;

	public DialogPaymentChallenge(Context context) {
		// TODO Auto-generated constructor stub
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
		setContentView(R.layout.dialog_payment_challenge);

		btn_ok_payment = (Button) findViewById(R.id.btn_ok_payment);
		btn_cancel_payment = (Button) findViewById(R.id.btn_cancel_payment);
		lv_payment = (ListView) findViewById(R.id.lv_payment);
		tv_title_payment = (TextView) findViewById(R.id.tv_title_payment);
		tv_message_empty = (TextView) findViewById(R.id.tv_message_empty);
		tv_total_money_payment = (TextView) findViewById(R.id.tv_total_money_payment);
		cb_choose_all_payment = (CheckBox) findViewById(R.id.cb_choose_all_payment);

		setFontTextView();
		adjustDialog();
		btn_ok_payment.setOnClickListener(this);
		btn_cancel_payment.setOnClickListener(this);
		cb_choose_all_payment.setOnClickListener(this);
		UserInfo info = new UserInfo().getInstance();
		new PaymentChallengeAsynctacks(activity, info.getId()).execute();

		// RefeshListPayment();

	}

	public static void RefeshListPayment(
			final ArrayList<PaymentItem> PaymentItemList) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {
					if (PaymentItemList.size() > 0) {
						paymentListAdapter = new PaymentListAdapter(activity,
								R.layout.item_payment_challenge,
								PaymentItemList);
						lv_payment.setAdapter(paymentListAdapter);
						lv_payment.setVisibility(View.VISIBLE);
						tv_message_empty.setVisibility(View.GONE);
						cb_choose_all_payment.setVisibility(View.VISIBLE);
						// paymentListAdapter.notifyDataSetChanged();
						SumTotalMoney();
					} else {
						cb_choose_all_payment.setVisibility(View.GONE);
						lv_payment.setVisibility(View.INVISIBLE);
						tv_message_empty.setVisibility(View.VISIBLE);

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	private void adjustDialog() {
		Drawable cancelDrawable = context.getResources().getDrawable(
				R.drawable.cancel_button);
		ResizeUtils.resizeButton(
				btn_cancel_payment,
				(int) (Config.screenWidth * 0.1),
				(int) (Config.screenWidth * 0.1
						* cancelDrawable.getIntrinsicHeight() / cancelDrawable
						.getIntrinsicWidth()), 0, 0, 0, 0);

		ResizeUtils.resizeButton(
				btn_ok_payment,
				(int) (Config.screenWidth * 0.1),
				(int) (Config.screenWidth * 0.1
						* cancelDrawable.getIntrinsicHeight() / cancelDrawable
						.getIntrinsicWidth()), 0, 0, 0, 0);
	}

	private void setFontTextView() {
		Typeface typeface = Typeface.createFromAsset(activity.getAssets(),
				"fonts/HomeRunScript-roman.otf");
		tv_title_payment.setTypeface(typeface);
		// tv_message_empty.setTypeface(typeface);
		// tv_total_money_payment.setTypeface(typeface);
	}

	private void AllChooseChecked(boolean choose) {
		UserInfo info = new UserInfo().getInstance();
		ArrayList<PaymentItem> arrayList = info.getPaymentList();
		if (choose == true) {
			for (int i = 0; i < arrayList.size(); i++) {
				arrayList.get(i).setSelect(false);
			}
			// paymentListAdapter.notifyDataSetChanged();
			DialogPaymentChallenge.RefeshListPayment(arrayList);
			UserInfo.getInstance().setPaymentList(arrayList);

		} else {
			for (int i = 0; i < arrayList.size(); i++) {
				// PayPalItem(String name, Integer quantity, BigDecimal price,
				// String currency, String sku)
				// new PayPalItem("old jeans with holes", 2, new BigDecimal(
				// "87.50"), "USD", "sku-12345678");

				arrayList.get(i).setSelect(true);

			}
			// paymentListAdapter.notifyDataSetChanged();
			DialogPaymentChallenge.RefeshListPayment(arrayList);
			UserInfo.getInstance().setPaymentList(arrayList);
		}
	}

	public static void SumTotalMoney() {
		PaymentItemList = UserInfo.getInstance().getPaymentList();
		MyAccountActivity.id_payment = "";
		sum = 0;
		int count = 0;
		PayPalItem Items[] = new PayPalItem[PaymentItemList.size()];
		for (int i = 0; i < PaymentItemList.size(); i++) {
			if (PaymentItemList.get(i).isSelect()) {
				count++;
				PayPalItem item = new PayPalItem(PaymentItemList.get(i)
						.getTitle(), 1, new BigDecimal(PaymentItemList.get(i)
						.getBudget()), "USD", PaymentItemList.get(i).getId());
				Items[i] = item;
				sum += Integer.parseInt(PaymentItemList.get(i).getBudget());
			}
		}
		MyAccountActivity.ArrItemPaypal = conversionArraytoArray(count, Items);
		tv_total_money_payment.setText(sum + "$");
	}

	private static PayPalItem[] conversionArraytoArray(int count,
			PayPalItem Items[]) {
		PayPalItem[] arr = new PayPalItem[count];
		int j = 0;
		for (int i = 0; i < Items.length; i++) {
			if (Items[i] != null) {
				arr[j] = Items[i];
				j++;
			}
		}
		return arr;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_ok_payment:
			// int sum =
			// Integer.parseInt(tv_total_money_payment.getText().toString().trim());
			if (sum > 0) {
				((MyAccountActivity) activity).onBuyPressed();
			} else {
				Toast.makeText(activity, "Not products selected for payment !",
						Toast.LENGTH_SHORT).show();
			}
			dismiss();
			break;
		case R.id.btn_cancel_payment:
			dismiss();
			break;
		case R.id.cb_choose_all_payment:
			if (cb_choose_all_payment.isChecked()) {
				cb_choose_all_payment.setText("UnChecked All");
				AllChooseChecked(false);
				// SumTotalMoney();

			} else {
				cb_choose_all_payment.setText("Checked All");
				AllChooseChecked(true);
				// SumTotalMoney();
			}
			break;
		}
	}
}

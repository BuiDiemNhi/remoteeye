package remoteeyes.bsp.vn.adapter;

import java.util.ArrayList;

import remateeyes.bsp.vn.dialog.DialogPaymentChallenge;
import remoteeyes.bsp.vn.R;
import remoteeyes.bsp.vn.asynctask.ImageLoaderAsyncTask;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.model.ChallengeObject;
import remoteeyes.bsp.vn.model.CommentObject;
import remoteeyes.bsp.vn.model.Friend;
import remoteeyes.bsp.vn.model.PaymentItem;
import remoteeyes.bsp.vn.model.UserInfo;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PaymentListAdapter extends ArrayAdapter<PaymentItem> {
	Activity activity;
	int layoutId;
	ArrayList<PaymentItem> paymentItemList;
	private static final int TYPE_COUNT = 2;
	private static final int TYPE_ITEM1 = 0;
	private static final int TYPE_ITEM2 = 1;

	public PaymentListAdapter(Activity activity, int layoutId,
			ArrayList<PaymentItem> paymentItemList) {
		// TODO Auto-generated constructor stub
		super(activity, layoutId);
		this.activity = activity;
		this.layoutId = layoutId;
		this.paymentItemList = paymentItemList;
	}

	@Override
	public int getViewTypeCount() {
		return TYPE_COUNT;
	}

	@Override
	public int getItemViewType(int position) {

		PaymentItem item = getItem(position);

		return (item.isSelect() == true) ? TYPE_ITEM1 : TYPE_ITEM2;
	}

	@Override
	public int getCount() {
		return paymentItemList == null ? 0 : paymentItemList.size();
	}

	@Override
	public PaymentItem getItem(int position) {
		return paymentItemList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int index, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(layoutId, null);

		final CheckBox cb_choose_payment = (CheckBox) convertView
				.findViewById(R.id.cb_choose_payment);
		TextView tv_title_challenge = (TextView) convertView
				.findViewById(R.id.tv_title_challenge);
		TextView tv_budget_challenge = (TextView) convertView
				.findViewById(R.id.tv_budget_challenge);
		try {
			if (index == paymentItemList.size()) {
				index = paymentItemList.size() - 1;
			}
			final int position = index;

			final PaymentItem item = paymentItemList.get(position);
			if (item != null) {
				try {
					switch (getItemViewType(position)) {
					case TYPE_ITEM1:
						tv_title_challenge.setText(item.getTitle());
						tv_budget_challenge.setText(item.getBudget());
						cb_choose_payment.setChecked(true);
						// item.setSelect(true);

						cb_choose_payment
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										if (item.isSelect()) {
											item.setSelect(false);
											// paymentItemList.get(position)
											// .setSelect(false);
											UserInfo.getInstance()
													.setPaymentList(
															paymentItemList);
											cb_choose_payment.setChecked(false);
											DialogPaymentChallenge
													.SumTotalMoney();

										} else {
											item.setSelect(true);
											// paymentItemList.get(position)
											// .setSelect(true);
											UserInfo.getInstance()
													.setPaymentList(
															paymentItemList);
											cb_choose_payment.setChecked(true);
											DialogPaymentChallenge
													.SumTotalMoney();
										}
									}
								});
						break;
					case TYPE_ITEM2:
						tv_title_challenge.setText(item.getTitle());
						tv_budget_challenge.setText(item.getBudget());
						cb_choose_payment.setChecked(false);
						// item.setSelect(false);

						cb_choose_payment
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										if (item.isSelect()) {
											item.setSelect(false);
											// paymentItemList.get(position)
											// .setSelect(false);
											cb_choose_payment.setChecked(false);
											UserInfo.getInstance()
													.setPaymentList(
															paymentItemList);
											DialogPaymentChallenge
													.SumTotalMoney();
										} else {
											item.setSelect(true);
											// paymentItemList.get(position)
											// .setSelect(true);
											cb_choose_payment.setChecked(true);
											UserInfo.getInstance()
													.setPaymentList(
															paymentItemList);
											DialogPaymentChallenge
													.SumTotalMoney();
										}
									}
								});
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// UserInfo.getInstance().setPaymentList(paymentItemList);
		return convertView;
	}
}

package remoteeyes.bsp.vn.adapter;

import java.util.ArrayList;

import remoteeyes.bsp.vn.R;
import remoteeyes.bsp.vn.model.HistoryPaypalItem;
import remoteeyes.bsp.vn.model.PaymentItem;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HistoryListAdapter extends ArrayAdapter<HistoryPaypalItem> {
	Activity activity;
	int layoutId;
	ArrayList<HistoryPaypalItem> historyPaypalItems;
	private static final int TYPE_COUNT = 2;
	private static final int TYPE_ITEM1 = 0;
	private static final int TYPE_ITEM2 = 1;

	public HistoryListAdapter(Activity activity, int layoutId,
			ArrayList<HistoryPaypalItem> historyPaypalItems) {
		super(activity, layoutId);
		this.activity = activity;
		this.layoutId = layoutId;
		this.historyPaypalItems = historyPaypalItems;
	}

	@Override
	public int getViewTypeCount() {
		return TYPE_COUNT;
	}

	@Override
	public int getItemViewType(int position) {

		HistoryPaypalItem item = getItem(position);

		return (item.getType() == 0) ? TYPE_ITEM1 : TYPE_ITEM2;
	}

	@Override
	public int getCount() {
		return historyPaypalItems == null ? 0 : historyPaypalItems.size();
	}

	@Override
	public HistoryPaypalItem getItem(int position) {
		return historyPaypalItems.get(position);
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

		LinearLayout ll_item_history = (LinearLayout) convertView
				.findViewById(R.id.ll_item_history);
		TextView tv_date = (TextView) convertView.findViewById(R.id.tv_date);
		TextView tv_description = (TextView) convertView
				.findViewById(R.id.tv_description);
		TextView tv_amount = (TextView) convertView
				.findViewById(R.id.tv_amount);
		TextView tv_balanceAfter = (TextView) convertView
				.findViewById(R.id.tv_balanceAfter);
		tv_description.setSelected(true);
		try {
			if (index == historyPaypalItems.size()) {
				index = historyPaypalItems.size() - 1;
			}
			final int position = index;

			final HistoryPaypalItem item = historyPaypalItems.get(position);
			if (item != null) {
				try {
					switch (getItemViewType(position)) {
					case TYPE_ITEM1:
						setItemBackround(ll_item_history, position);
						tv_date.setText(item.getDate_created());
						tv_description.setText("payment to: "
								+ item.getTitle_challenge());
						tv_amount.setText("-" + item.getTotal_amount());
						// tv_balanceAfter.setText(item.getBudget());

						tv_description.setTextColor(extracted());
						tv_amount.setTextColor(extracted());
						break;

					case TYPE_ITEM2:
						setItemBackround(ll_item_history, position);
						tv_date.setText(item.getDate_created());
						tv_description.setText("received from: "
								+ item.getTitle_challenge());
						tv_amount.setText("+" + item.getTotal_amount());
						// tv_balanceAfter.setText(item.getBudget());
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return convertView;
	}

	private void setItemBackround(LinearLayout ll_item_history, int position) {
		if (position % 2 == 1) {
			ll_item_history.setBackgroundResource(R.color.item_history_green2);
		} else {
			ll_item_history.setBackgroundResource(R.color.item_history_green1);
		}
	}

	private int extracted() {
		return activity.getResources().getColor(R.color.red);
	}
}

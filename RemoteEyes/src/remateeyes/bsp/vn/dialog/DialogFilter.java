package remateeyes.bsp.vn.dialog;

import remoteeyes.bsp.vn.MyAreaActivity;
import remoteeyes.bsp.vn.R;
import remoteeyes.bsp.vn.asynctask.FilterAcceptedChallengeAsynctask;
import remoteeyes.bsp.vn.asynctask.FilterGlobalChallengeAsynctask;
import remoteeyes.bsp.vn.asynctask.FilterPostedChallengeAsynctask;
import remoteeyes.bsp.vn.asynctask.FilterCurrentChallengeAsynctask;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.type.ShowingChallengeType;
import remoteeyes.bsp.vn.uilt.ResizeUtils;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class DialogFilter extends Dialog implements
		android.view.View.OnClickListener {

	Context context;
	Activity activity;
	Button btnCancel, btnOk;
	LinearLayout llDialogFilter, llMine, llIgnore,llUnpaid;
	CheckBox cbUpcoming, cbToday, cbMine, cbIgnore, cbHistory, cbUnpaid;
	RadioButton rbReward, rbDistance, rbRemain, rbInterval;
	TextView tvTitle, tvLineMine, tvLineIgnore;
	Typeface typeface;
	String sort;

	public DialogFilter(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_filter);
		getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		this.context = context;

		llDialogFilter = (LinearLayout) findViewById(R.id.ll_dialog_filter);
		llMine = (LinearLayout) findViewById(R.id.ll_filter_mine);
		llIgnore = (LinearLayout) findViewById(R.id.ll_filter_ignore);
		llUnpaid=(LinearLayout)findViewById(R.id.ll_filter_unpaid);
llUnpaid.setVisibility(View.GONE);
		btnCancel = (Button) findViewById(R.id.btn_cancel_confirm_filter);
		btnOk = (Button) findViewById(R.id.btn_ok_confirm_filter);

		cbUpcoming = (CheckBox) findViewById(R.id.cb_filter_upcoming);
		cbToday = (CheckBox) findViewById(R.id.cb_filter_today);
		cbIgnore = (CheckBox) findViewById(R.id.cb_filter_ignore);
		cbMine = (CheckBox) findViewById(R.id.cb_filter_mine);
		cbHistory = (CheckBox) findViewById(R.id.cb_filter_history);
		cbUnpaid= (CheckBox)findViewById(R.id.cb_filter_unpaid);
		tvTitle = (TextView) findViewById(R.id.tv_title_filter);
		tvLineMine = (TextView) findViewById(R.id.tv_line_mine);
		tvLineIgnore = (TextView) findViewById(R.id.tv_line_ignore);
		rbReward = (RadioButton) findViewById(R.id.rb_sort_reward);
		rbRemain = (RadioButton) findViewById(R.id.rb_sort_remainstart);
		rbDistance = (RadioButton) findViewById(R.id.rb_sort_distance);
		rbInterval = (RadioButton) findViewById(R.id.rb_sort_interval);

		adjustDialog();
		setFontTextView();
		updateDialog();

		btnCancel.setOnClickListener(this);
		btnOk.setOnClickListener(this);
		cbUpcoming.setOnClickListener(this);
		cbToday.setOnClickListener(this);
		cbMine.setOnClickListener(this);
		cbHistory.setOnClickListener(this);
		cbIgnore.setOnClickListener(this);
		rbReward.setOnClickListener(this);
		rbRemain.setOnClickListener(this);
		rbDistance.setOnClickListener(this);
		rbInterval.setOnClickListener(this);
		cbUnpaid.setOnClickListener(this);
	}

	private void setFontTextView() {
		typeface = Typeface.createFromAsset(context.getAssets(),
				"fonts/HomeRunScript-roman.otf");
		tvTitle.setTypeface(typeface);
	}

	public void setIsCheck() {
		cbUpcoming.setChecked(((MyAreaActivity) activity).tmp[0]);
		cbToday.setChecked(((MyAreaActivity) activity).tmp[1]);
		cbMine.setChecked(((MyAreaActivity) activity).tmp[2]);
		cbIgnore.setChecked(((MyAreaActivity) activity).tmp[3]);
		cbHistory.setChecked(((MyAreaActivity) activity).tmp[4]);
		cbUnpaid.setChecked(((MyAreaActivity)activity).tmp[5]);
		rbReward.setChecked(((MyAreaActivity) activity).tmp[6]);
		rbDistance.setChecked(((MyAreaActivity) activity).tmp[7]);
		rbRemain.setChecked(((MyAreaActivity) activity).tmp[8]);
		rbInterval.setChecked(((MyAreaActivity) activity).tmp[9]);
	}

	private void adjustDialog() {
		llDialogFilter.getLayoutParams().width = (int) (Config.screenWidth * 0.9);
		if (
				ShowingChallengeType.STATUS_SHOW_CURRENT == ShowingChallengeType.CHALLENGE_SHOW_NEARBY) {
			
			llDialogFilter.getLayoutParams().height = (int) (Config.screenHeight * 0.85);
		}else if(ShowingChallengeType.STATUS_SHOW_CURRENT == ShowingChallengeType.CHALLENGE_SHOW_POSTED||ShowingChallengeType.STATUS_SHOW_CURRENT == ShowingChallengeType.CHALLENGE_SHOW_GLOBAL){
			llDialogFilter.getLayoutParams().height = (int) (Config.screenHeight * 0.78);
		}else{
			llDialogFilter.getLayoutParams().height = (int) (Config.screenHeight * 0.7);
		}

		Drawable cancelDrawable = context.getResources().getDrawable(
				R.drawable.cancel_button);
		ResizeUtils.resizeButton(
				btnCancel,
				(int) (Config.screenWidth * 0.1),
				(int) (Config.screenWidth * 0.1
						* cancelDrawable.getIntrinsicHeight() / cancelDrawable
						.getIntrinsicWidth()), 0, 0, 0, 0);

		ResizeUtils.resizeButton(
				btnOk,
				(int) (Config.screenWidth * 0.1),
				(int) (Config.screenWidth * 0.1
						* cancelDrawable.getIntrinsicHeight() / cancelDrawable
						.getIntrinsicWidth()), 0, 0, 0, 0);
		

	}
	

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	private void updateDialog() {
		if (ShowingChallengeType.STATUS_SHOW_CURRENT == ShowingChallengeType.CHALLENGE_SHOW_ACCEPTED) {
			llMine.setVisibility(View.GONE);
			llIgnore.setVisibility(View.GONE);
			tvLineIgnore.setVisibility(View.GONE);
			tvLineMine.setVisibility(View.GONE);
			
		} else if (ShowingChallengeType.STATUS_SHOW_CURRENT == ShowingChallengeType.CHALLENGE_SHOW_POSTED) {
			llIgnore.setVisibility(View.GONE);
			llMine.setVisibility(View.GONE);
			tvLineIgnore.setVisibility(View.GONE);
			tvLineMine.setVisibility(View.GONE);
			llUnpaid.setVisibility(View.VISIBLE);
		} else if (ShowingChallengeType.STATUS_SHOW_CURRENT == ShowingChallengeType.CHALLENGE_SHOW_GLOBAL) {
			rbDistance.setVisibility(View.GONE);
		}
		
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btn_cancel_confirm_filter:
			dismiss();
			break;
		case R.id.btn_ok_confirm_filter:
			if (!cbUpcoming.isChecked() && !cbToday.isChecked()
					&& !cbMine.isChecked() && !cbIgnore.isChecked()
					&& !cbHistory.isChecked()&&!cbUnpaid.isChecked()) {
				Toast.makeText(activity,
						"You must choose one filter condition",
						Toast.LENGTH_SHORT).show();
				return;
			} else {
				((MyAreaActivity) activity).tmp[0] = cbUpcoming.isChecked();
				((MyAreaActivity) activity).tmp[1] = cbToday.isChecked();
				((MyAreaActivity) activity).tmp[2] = cbMine.isChecked();
				((MyAreaActivity) activity).tmp[3] = cbIgnore.isChecked();
				((MyAreaActivity) activity).tmp[4] = cbHistory.isChecked();
				((MyAreaActivity) activity).tmp[5] = cbUnpaid.isChecked();
				((MyAreaActivity) activity).tmp[6] = rbReward.isChecked();
				((MyAreaActivity) activity).tmp[7] = rbDistance.isChecked();
				((MyAreaActivity) activity).tmp[8] = rbRemain.isChecked();
				((MyAreaActivity) activity).tmp[9] = rbInterval.isChecked();
				if (rbReward.isChecked()) {
					sort = "budget";
				}
				if (rbDistance.isChecked()) {
					sort = "distance";
				}
				if (rbRemain.isChecked()) {
					sort = "remain_start";
				}
				if (rbInterval.isChecked()) {
					sort = "interval";
				}
				if (ShowingChallengeType.STATUS_SHOW_CURRENT == ShowingChallengeType.CHALLENGE_SHOW_ACCEPTED) {
					new FilterAcceptedChallengeAsynctask(activity).execute(
							Config.IdUser,
							String.valueOf(cbUpcoming.isChecked()),
							String.valueOf(cbHistory.isChecked()),
							String.valueOf(cbToday.isChecked()), sort,
							Config.lat + "", Config.lng + "");
				} else if (ShowingChallengeType.STATUS_SHOW_CURRENT == ShowingChallengeType.CHALLENGE_SHOW_POSTED) {
					new FilterPostedChallengeAsynctask(activity).execute(
							Config.IdUser,
							String.valueOf(cbUpcoming.isChecked()),
							String.valueOf(cbHistory.isChecked()),
							String.valueOf(cbToday.isChecked()), sort,
							Config.lat + "", Config.lng + "",String.valueOf(cbUnpaid.isChecked()));
				} else if (ShowingChallengeType.STATUS_SHOW_CURRENT == ShowingChallengeType.CHALLENGE_SHOW_NEARBY) {
					new FilterCurrentChallengeAsynctask(activity).execute(
							Config.IdUser, Config.lat + "", Config.lng + "",
							String.valueOf(cbMine.isChecked()),
							String.valueOf(cbUpcoming.isChecked()),
							String.valueOf(cbIgnore.isChecked()),
							String.valueOf(cbHistory.isChecked()),
							String.valueOf(cbToday.isChecked()), sort);
				} else if (ShowingChallengeType.STATUS_SHOW_CURRENT == ShowingChallengeType.CHALLENGE_SHOW_GLOBAL) {
					new FilterGlobalChallengeAsynctask(activity).execute(
							Config.IdUser, String.valueOf(cbMine.isChecked()),
							String.valueOf(cbUpcoming.isChecked()),
							String.valueOf(cbIgnore.isChecked()),
							String.valueOf(cbHistory.isChecked()),
							String.valueOf(cbToday.isChecked()), sort);
				}
				dismiss();
			}
			break;
		case R.id.rb_sort_reward:
			rbReward.setChecked(true);
			rbRemain.setChecked(false);
			rbDistance.setChecked(false);
			rbInterval.setChecked(false);
			sort = "budget";
			break;
		case R.id.rb_sort_distance:
			rbReward.setChecked(false);
			rbRemain.setChecked(false);
			rbDistance.setChecked(true);
			rbInterval.setChecked(false);
			sort = "distance";
			break;
		case R.id.rb_sort_remainstart:
			rbReward.setChecked(false);
			rbRemain.setChecked(true);
			rbDistance.setChecked(false);
			rbInterval.setChecked(false);
			sort = "remain_start";
			break;
		case R.id.rb_sort_interval:
			rbReward.setChecked(false);
			rbRemain.setChecked(false);
			rbDistance.setChecked(false);
			rbInterval.setChecked(true);
			sort = "interval";
			break;
		default:
			break;
		}
	}

}

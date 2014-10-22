package remateeyes.bsp.vn.dialog;

import java.util.ArrayList;

import remoteeyes.bsp.vn.R;
import remoteeyes.bsp.vn.adapter.DayArrayAdapter;
import remoteeyes.bsp.vn.asynctask.LoadCategory;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.fragments.CreateChallengeFragment.InputFilterMinMax;
import remoteeyes.bsp.vn.model.UserInfo;
import remoteeyes.bsp.vn.type.DialogType;
import remoteeyes.bsp.vn.uilt.ResizeUtils;
import android.R.integer;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

public class DialogEditReward extends Dialog implements
		android.view.View.OnClickListener {

	Context context;

	TextView tvStartDay, tvStartHour, tvStartMinute, tvBalance, tvPleaseSelect;
	EditText etDurationDay, etDurationHour, etDurationMinute, etTimeReward;
	Button btnCharge, btnTimeCancel, btnTimeOk;
	int duration_day, duration_hour, duration_minute, reward, categogy;
	String starting_day, starting_hour, starting_minute;
	LinearLayout llDialogEdit;
	Typeface typeface;
	ImageView ivC, ivF, ivR;

	public DialogEditReward(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_set_time_challenge);
		getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		this.context = context;

		llDialogEdit = (LinearLayout) findViewById(R.id.ll_edit_reward);
		btnTimeCancel = (Button) findViewById(R.id.btn_time_cancel);
		btnTimeOk = (Button) findViewById(R.id.btn_time_ok);
		tvStartDay = (TextView) findViewById(R.id.tv_start_day);
		tvStartHour = (TextView) findViewById(R.id.tv_start_hour);
		tvStartMinute = (TextView) findViewById(R.id.tv_start_minute);
		etDurationDay = (EditText) findViewById(R.id.et_time_day);
		etDurationHour = (EditText) findViewById(R.id.et_time_hour);
		etDurationHour.setFilters(new InputFilter[] { new InputFilterMinMax(
				"0", "23") });
		etDurationMinute = (EditText) findViewById(R.id.et_time_minute);
		etDurationMinute.setFilters(new InputFilter[] { new InputFilterMinMax(
				"0", "59") });
		etTimeReward = (EditText) findViewById(R.id.et_time_reward);
		tvBalance = (TextView) findViewById(R.id.tv_balance);
		tvPleaseSelect = (TextView) findViewById(R.id.tv_please_select);
		tvBalance.setText(UserInfo.getInstance().getBalance() + "");
		tvBalance.setVisibility(View.GONE);

		ivC = (ImageView) findViewById(R.id.iv_c);
		ivF = (ImageView) findViewById(R.id.iv_f);
		ivR = (ImageView) findViewById(R.id.iv_r);

		tvStartDay.setOnClickListener(this);
		tvStartHour.setOnClickListener(this);
		tvStartMinute.setOnClickListener(this);
		adjustDialog();

	}

	private void adjustDialog() {
		llDialogEdit.getLayoutParams().width = (int) (Config.screenWidth * 0.9);
		llDialogEdit.getLayoutParams().height = (int) (Config.screenHeight * 0.55);

		ResizeUtils.resizeImageView(ivC, (int) (Config.screenWidth * 0.06),
				(int) (Config.screenWidth * 0.055), 0, 0, 0,
				(int) (Config.screenWidth * 0.08), 1f, Gravity.CENTER);
		ResizeUtils.resizeImageView(ivF, (int) (Config.screenWidth * 0.06),
				(int) (Config.screenWidth * 0.055), 0, 0, 0,
				(int) (Config.screenWidth * 0.08), 1f, Gravity.CENTER);
		ResizeUtils.resizeImageView(ivR, (int) (Config.screenWidth * 0.055),
				(int) (Config.screenWidth * 0.05), 0, 0, 0,
				(int) (Config.screenWidth * 0.08), 1f, Gravity.CENTER);

		Drawable cancelDrawable = context.getResources().getDrawable(
				R.drawable.cancel_button);
		ResizeUtils.resizeButton(
				btnTimeCancel,
				(int) (Config.screenWidth * 0.1),
				(int) (Config.screenWidth * 0.1
						* cancelDrawable.getIntrinsicHeight() / cancelDrawable
						.getIntrinsicWidth()), 0, 0, 0, 0);

		ResizeUtils.resizeButton(
				btnTimeOk,
				(int) (Config.screenWidth * 0.1),
				(int) (Config.screenWidth * 0.1
						* cancelDrawable.getIntrinsicHeight() / cancelDrawable
						.getIntrinsicWidth()), 0, 0, 0, 0);

		typeface = Typeface.createFromAsset(context.getAssets(),
				"fonts/HomeRunScript-roman.otf");
		tvPleaseSelect.setTypeface(typeface);
	}

	public void setStartTimeFromWheel(String day, String hour, String minute) {
		tvStartDay.setText(day);
		tvStartHour.setText(hour);
		tvStartMinute.setText(minute);
		starting_day = day;
		starting_hour = hour;
		starting_minute = minute;
	}

	public void setText(String day, String hour, String minute,
			int DurationDay, int DurationHour, int DurationMinute, int Reward) {
		tvStartDay.setText(day);
		tvStartHour.setText(hour);
		tvStartMinute.setText(minute);
		starting_day = day;
		starting_hour = hour;
		starting_minute = minute;

		etDurationDay.setText(DurationDay + "");
		etDurationHour.setText(DurationHour + "");
		etDurationMinute.setText(DurationMinute + "");
		duration_day = DurationDay;
		duration_hour = DurationHour;
		duration_minute = DurationMinute;

		etTimeReward.setText(Reward + "");
		reward = Reward;
	}

	public String getStartTime() {
		if (tvStartDay.getText().equals("Today")) {
			String date = DayArrayAdapter.dayList.get(0) + " "
					+ tvStartHour.getText().toString() + ":"
					+ tvStartMinute.getText().toString() + ":00";
			return date;
		} else if (tvStartDay.getText().equals("Tomorrow")) {
			String date = DayArrayAdapter.dayList.get(1) + " "
					+ tvStartHour.getText().toString() + ":"
					+ tvStartMinute.getText().toString() + ":00";
			return date;
		} else
			return tvStartDay.getText().toString() + " "
					+ tvStartHour.getText().toString() + ":"
					+ tvStartMinute.getText().toString() + ":00";
	}

	public String getDurationTime() {
		if (TextUtils.isEmpty(etDurationDay.getText().toString()))
			etDurationDay.setText("0");
		if (TextUtils.isEmpty(etDurationHour.getText().toString()))
			etDurationHour.setText("0");
		if (TextUtils.isEmpty(etDurationMinute.getText().toString()))
			etDurationMinute.setText("0");
		duration_day = Integer.parseInt(etDurationDay.getText().toString());
		duration_hour = Integer.parseInt(etDurationHour.getText().toString());
		duration_minute = Integer.parseInt(etDurationMinute.getText()
				.toString());
		String a, b, c;
		if (duration_day <= 9) {
			a = "0" + etDurationDay.getText().toString();
		} else {
			a = etDurationDay.getText().toString();
		}
		if (duration_hour <= 9) {
			b = "0" + etDurationHour.getText().toString();
		} else {
			b = etDurationHour.getText().toString();
		}
		if (duration_minute <= 9) {
			c = "0" + etDurationHour.getText().toString();
		} else {
			c = etDurationHour.getText().toString();
		}
		return a + ":" + b + ":" + c;
	}

	public String getBudget() {
		return etTimeReward.getText().toString();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_start_day:
			WheelDayDialog dialog1 = (WheelDayDialog) DialogFactory.getDialog(
					context, DialogType.DIALOG_WHEEL_DAY);
			dialog1.show();
			dialog1.setDialog(this);
			break;
		case R.id.tv_start_hour:
			WheelDayDialog dialog2 = (WheelDayDialog) DialogFactory.getDialog(
					context, DialogType.DIALOG_WHEEL_DAY);
			dialog2.show();
			dialog2.setDialog(this);
		case R.id.tv_start_minute:
			WheelDayDialog dialog3 = (WheelDayDialog) DialogFactory.getDialog(
					context, DialogType.DIALOG_WHEEL_DAY);
			dialog3.show();
			dialog3.setDialog(this);
			break;
		default:
			break;
		}
	}

	public void setCancelClickListener(View.OnClickListener listener) {
		btnTimeCancel.setOnClickListener(listener);
	}

	public void setOkClickListener(View.OnClickListener listener) {
		btnTimeOk.setOnClickListener(listener);
	}

	public class InputFilterMinMax implements InputFilter {

		private int min, max;

		public InputFilterMinMax(int min, int max) {
			this.min = min;
			this.max = max;
		}

		public InputFilterMinMax(String min, String max) {
			this.min = Integer.parseInt(min);
			this.max = Integer.parseInt(max);
		}

		@Override
		public CharSequence filter(CharSequence source, int start, int end,
				Spanned dest, int dstart, int dend) {
			try {
				int input = Integer.parseInt(dest.toString()
						+ source.toString());
				if (isInRange(min, max, input))
					return null;
			} catch (NumberFormatException nfe) {
			}
			return "";
		}

		private boolean isInRange(int a, int b, int c) {
			return b > a ? c >= a && c <= b : c >= b && c <= a;
		}
	}

}

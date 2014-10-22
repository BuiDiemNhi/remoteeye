package remateeyes.bsp.vn.dialog;

import java.util.Calendar;
import java.util.Locale;

import remoteeyes.bsp.vn.CreateChallengeActivity;
import remoteeyes.bsp.vn.R;
import remoteeyes.bsp.vn.adapter.DayArrayAdapter;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.type.DialogType;
import remoteeyes.bsp.vn.uilt.ResizeUtils;
import remoteeyes.bsp.vn.wheel.widget.WheelView;
import remoteeyes.bsp.vn.wheel.widget.adapters.NumericWheelAdapter;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class WheelDayDialog extends Dialog implements
		android.view.View.OnClickListener {

	Context context;
	LinearLayout llWheelDialog;
	WheelView hours, mins, day;
	DayArrayAdapter dayArrayAdapter;
	Button btnOk, btnCancel;

	DialogEditReward dialog;
	
	public WheelDayDialog(Context context) {
		super(context);
		this.context = context;

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.wheel_day_dialog);
		this.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));

		hours = (WheelView) findViewById(R.id.hour);
		NumericWheelAdapter hourAdapter = new NumericWheelAdapter(context, 0,
				23, "%02d");
		hourAdapter.setItemResource(R.layout.wheel_text_item);
		hourAdapter.setItemTextResource(R.id.text);
		hours.setViewAdapter(hourAdapter);
		hours.setCyclic(true);

		mins = (WheelView) findViewById(R.id.mins);
		NumericWheelAdapter minAdapter = new NumericWheelAdapter(context, 0,
				59, "%02d");
		minAdapter.setItemResource(R.layout.wheel_text_item);
		minAdapter.setItemTextResource(R.id.text);
		mins.setViewAdapter(minAdapter);
		mins.setCyclic(true);

		// set current time
		Calendar calendar = Calendar.getInstance(Locale.getDefault());
		hours.setCurrentItem(calendar.get(Calendar.HOUR));
		mins.setCurrentItem(calendar.get(Calendar.MINUTE));

		day = (WheelView) findViewById(R.id.day);
		dayArrayAdapter = new DayArrayAdapter(context, calendar);
		day.setViewAdapter(dayArrayAdapter);

		llWheelDialog = (LinearLayout) findViewById(R.id.ll_wheel_dialog);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.width = (int) (Config.screenWidth * 0.8);
		params.height = (int) (Config.screenHeight * 0.5);
		llWheelDialog.setLayoutParams(params);

		btnCancel = (Button) findViewById(R.id.btn_wheel_cancel);
		btnOk = (Button) findViewById(R.id.btn_wheel_ok);
		btnCancel.setOnClickListener(this);
		btnOk.setOnClickListener(this);

		adjustInterface();
	}

	private void adjustInterface() {
		Drawable okDrawable = context.getResources().getDrawable(
				R.drawable.ok_button);
		ResizeUtils.resizeButton(
				btnOk,
				(int) (Config.screenWidth * 0.1),
				(int) (Config.screenWidth * 0.1
						* okDrawable.getIntrinsicHeight() / okDrawable
						.getIntrinsicWidth()), 0, 0, 0, 0);

		Drawable cancelDrawable = context.getResources().getDrawable(
				R.drawable.cancel_button);
		ResizeUtils.resizeButton(
				btnCancel,
				(int) (Config.screenWidth * 0.1),
				(int) (Config.screenWidth * 0.1
						* cancelDrawable.getIntrinsicHeight() / cancelDrawable
						.getIntrinsicWidth()), 0, 0, 0, 0);

		day.getLayoutParams().width = (int) (Config.screenWidth * 0.3);
		day.getLayoutParams().height = (int) (Config.screenWidth * 0.5);
		
		hours.getLayoutParams().width = (int) (Config.screenWidth * 0.12);
		hours.getLayoutParams().height = (int) (Config.screenWidth * 0.5);
		
		mins.getLayoutParams().width = (int) (Config.screenWidth * 0.12);
		mins.getLayoutParams().height = (int) (Config.screenWidth * 0.5);
	}

	public void setDialog(DialogEditReward dialog) {
		this.dialog = dialog;
	}
	
	public String getDay() {
		int pos = day.getCurrentItem();
		if(pos == 0)
			return "Today";
		else if(pos == 1)
			return "Tomorrow";
		else
			return DayArrayAdapter.dayList.get(pos);
	}

	public int getHour() {
		return hours.getCurrentItem();
	}

	public int getMinute() {
		return mins.getCurrentItem();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_wheel_cancel:
			dismiss();
			break;
		case R.id.btn_wheel_ok:
			try{
			((CreateChallengeActivity) context).fmCreateChallenge
					.setStartTimeFromWheel(getDay(), getHour() + "",
							getMinute() + "");
			}catch(Exception ex){
				dialog.setStartTimeFromWheel(getDay(), getHour() + "",
							getMinute() + "");
			}
			dismiss();
		default:
			break;
		}

	}
}

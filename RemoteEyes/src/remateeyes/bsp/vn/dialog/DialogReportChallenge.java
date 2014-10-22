package remateeyes.bsp.vn.dialog;

import remoteeyes.bsp.vn.R;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.type.DialogType;
import remoteeyes.bsp.vn.uilt.ResizeUtils;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TextView;

public class DialogReportChallenge extends Dialog implements
		android.view.View.OnClickListener, OnCheckedChangeListener {
	Context context;
	private TextView tv_title, tv_note, tv_terms_and_condition;
	private RadioButton rb_improper_for_mibors,
			rb_personal_or_intellectual_property_breach, rb_crime_cajole,
			rb_other;
	private Button btn_cancel, btn_ok;
	private LinearLayout llReportDialog;

	public DialogReportChallenge(Context context) {		
		super(context);
		this.context= context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		
		setContentView(R.layout.dialog_report_challenge);

		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_note = (TextView) findViewById(R.id.tv_note);
		tv_terms_and_condition = (TextView) findViewById(R.id.tv_terms_and_condition);
		rb_improper_for_mibors = (RadioButton) findViewById(R.id.rb_improper_for_mibors);
		rb_personal_or_intellectual_property_breach = (RadioButton) findViewById(R.id.rb_personal_or_intellectual_property_breach);
		rb_crime_cajole = (RadioButton) findViewById(R.id.rb_crime_cajole);
		rb_other = (RadioButton) findViewById(R.id.rb_other);
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		btn_ok = (Button) findViewById(R.id.btn_ok);
		llReportDialog = (LinearLayout)findViewById(R.id.ll_report_dialog);

		btn_cancel.setOnClickListener(this);
		btn_ok.setOnClickListener(this);
		rb_improper_for_mibors.setOnClickListener(this);
		rb_personal_or_intellectual_property_breach
				.setOnClickListener(this);
		rb_crime_cajole.setOnClickListener(this);
		rb_other.setOnClickListener(this);
		tv_terms_and_condition.setOnClickListener(this);

		adjustDialog();
	}
	
	private void adjustDialog() {
		llReportDialog.getLayoutParams().width = (int)(Config.screenWidth*0.9);
		llReportDialog.getLayoutParams().height = (int)(Config.screenWidth*0.85);
		
		Drawable cancelDrawable = context.getResources().getDrawable(
				R.drawable.cancel_button);
		ResizeUtils.resizeButton(
				btn_cancel,
				(int) (Config.screenWidth * 0.1),
				(int) (Config.screenWidth * 0.1
						* cancelDrawable.getIntrinsicHeight() / cancelDrawable
						.getIntrinsicWidth()), 0, 0, 0, 0);

		ResizeUtils.resizeButton(
				btn_ok,
				(int) (Config.screenWidth * 0.1),
				(int) (Config.screenWidth * 0.1
						* cancelDrawable.getIntrinsicHeight() / cancelDrawable
						.getIntrinsicWidth()), 0, 0, 0, 0);	
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.btn_ok:
			dismiss();
			break;
		case R.id.btn_cancel:
			dismiss();
			break;
		case R.id.tv_terms_and_condition:
			Dialog dialog = DialogFactory.getDialog(context,
					DialogType.DIALOG_NEW_REPORT);
			dialog.show();
			break;
		case R.id.rb_improper_for_mibors:
			selectedRadioButtton(rb_improper_for_mibors);
			break;
		case R.id.rb_personal_or_intellectual_property_breach:
			selectedRadioButtton(rb_personal_or_intellectual_property_breach);
			break;
		case R.id.rb_crime_cajole:
			selectedRadioButtton(rb_crime_cajole);
			break;
		case R.id.rb_other:
			dismiss();
			showReportOther();
			break;
		default:
			break;
		}
	}
	
	private void selectedRadioButtton(RadioButton radioButton){
		rb_improper_for_mibors.setChecked(false);
		rb_personal_or_intellectual_property_breach.setChecked(false);
		rb_crime_cajole.setChecked(false);
		rb_other.setChecked(false);
		
		radioButton.setChecked(true);
	}
	
	private void showReportOther(){
		DialogNewReportChallenge dialog = (DialogNewReportChallenge)DialogFactory.getDialog(context, DialogType.DIALOG_NEW_REPORT);
		dialog.show();
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		// TODO Auto-generated method stub

	}

}
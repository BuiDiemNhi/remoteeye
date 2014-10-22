package remateeyes.bsp.vn.dialog;

import remoteeyes.bsp.vn.R;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.uilt.ResizeUtils;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

public class DialogNewReportChallenge extends Dialog implements
		android.view.View.OnClickListener {
	Context context;
	private Button btn_cancel, btn_ok;
	private LinearLayout llNewReportDialog;

	public DialogNewReportChallenge(Context context) {
		// TODO Auto-generated constructor stub
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
		
		setContentView(R.layout.dialog_new_report_challenge);
		
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		btn_ok = (Button) findViewById(R.id.btn_ok);
		llNewReportDialog = (LinearLayout)findViewById(R.id.ll_new_report_dialog);

		btn_cancel.setOnClickListener(this);
		btn_ok.setOnClickListener(this);

		adjustDialog();
	}
	
	private void adjustDialog() {
		llNewReportDialog.getLayoutParams().width = (int)(Config.screenWidth*0.9);
		llNewReportDialog.getLayoutParams().height = (int)(Config.screenWidth*0.8);
		
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
		default:
			break;
		}
	}

}

package remateeyes.bsp.vn.dialog;

import remoteeyes.bsp.vn.R;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.model.UserInfo;
import remoteeyes.bsp.vn.uilt.ResizeUtils;
import android.R.bool;
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
import android.widget.TextView;

public class DelayPayment extends Dialog {
	Context context;
	LinearLayout llDialogDelay;
	Button btnCancel, btnOk;
	TextView tvContent, tvTitle;
	Typeface typeface;
	CheckBox dontShowAgain;
	boolean ischeck;

	

	public DelayPayment(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.delay_payment);
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		this.context = context;
		
		llDialogDelay = (LinearLayout)findViewById(R.id.ll_dialog_delay);
		btnCancel = (Button)findViewById(R.id.btn_cancel_dialog_delay);
		btnOk = (Button)findViewById(R.id.btn_ok_dialog_delay);
		tvContent = (TextView)findViewById(R.id.tvdialog_delay);
		tvTitle =(TextView)findViewById(R.id.tv_title_comfirm_delay);
		dontShowAgain= (CheckBox) findViewById(R.id.dont_show);
		adjustDialog();
		setFontTextView();
	}

	private void setFontTextView() {
		typeface = Typeface.createFromAsset(context.getAssets(),
				"fonts/HomeRunScript-roman.otf");
		tvTitle.setTypeface(typeface);
	}
	public boolean isIscheck() {
		if (
				dontShowAgain.isChecked() == true) {
			return true;
		}
		else
		
		return false;

	}

	public void setIscheck(boolean ischeck) {
		this.ischeck = ischeck;
	}
	private void adjustDialog() {
		llDialogDelay.getLayoutParams().width = (int) (Config.screenWidth * 0.8);
		llDialogDelay.getLayoutParams().height = (int) (Config.screenHeight * 0.6);

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

	public void setCancelClickListener(View.OnClickListener listener) {
		btnCancel.setOnClickListener(listener);
	}

	public void setOkClickListener(View.OnClickListener listener) {
		btnOk.setOnClickListener(listener);
	}

	public void setDontShowClickListener(View.OnClickListener listener) {
		dontShowAgain.setOnClickListener(listener);
	}

	

}

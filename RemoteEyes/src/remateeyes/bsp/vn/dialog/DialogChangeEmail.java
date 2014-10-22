package remateeyes.bsp.vn.dialog;

import java.util.ArrayList;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

public class DialogChangeEmail extends Dialog {

	Context context;
	LinearLayout llChangeDialog;
	Button btnCancel, btnOk;
	ImageView ivHome, ivPaypal;
	LinearLayout llEmail, llIconHome, llIconPaypal;
	ArrayList<LinearLayout> emailList;
	TextView tvSpaceChangeEmail, tv_unconfirmed_email_address;

	public DialogChangeEmail(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setContentView(R.layout.dialog_change_email);

		emailList = new ArrayList<LinearLayout>();

		llChangeDialog = (LinearLayout) findViewById(R.id.ll_change_dialog);
		btnCancel = (Button) findViewById(R.id.btn_cancel_change_email);
		btnOk = (Button) findViewById(R.id.btn_ok_change_email);
		ivHome = (ImageView) findViewById(R.id.iv_home_change_email);
		ivPaypal = (ImageView) findViewById(R.id.iv_paypal_change_email);
		llEmail = (LinearLayout) findViewById(R.id.ll_email_change_email);
		llIconHome = (LinearLayout) findViewById(R.id.ll_icon_home);
		llIconPaypal = (LinearLayout) findViewById(R.id.ll_icon_paypal);
		tvSpaceChangeEmail = (TextView) findViewById(R.id.tv_space_change_email);
		tv_unconfirmed_email_address = (TextView) findViewById(R.id.tv_unconfirmed_email_address);
		if (Config.flag_change_email == true) {
			Config.flag_change_email = false;
			tv_unconfirmed_email_address.setVisibility(View.GONE);
		} else {
			tv_unconfirmed_email_address.setVisibility(View.VISIBLE);
		}
		adjustDialog();
	}

	private void adjustDialog() {
		llChangeDialog.getLayoutParams().width = (int) (Config.screenWidth * 0.8);
		llChangeDialog.getLayoutParams().height = (int) (Config.screenWidth * 0.8);

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

		ResizeUtils.resizeImageView(ivHome, (int) (Config.screenWidth * 0.04),
				(int) (Config.screenWidth * 0.04),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01));

		ResizeUtils.resizeImageView(ivPaypal,
				(int) (Config.screenWidth * 0.04),
				(int) (Config.screenWidth * 0.04),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01));

		llIconHome.getLayoutParams().width = (int) (Config.screenWidth * 0.12);
		llIconHome.getLayoutParams().height = (int) (Config.screenWidth * 0.05);

		llIconPaypal.getLayoutParams().width = (int) (Config.screenWidth * 0.12);
		llIconPaypal.getLayoutParams().height = (int) (Config.screenWidth * 0.05);

		ResizeUtils.resizeTextView(tvSpaceChangeEmail,
				(int) (Config.screenWidth * 0.4),
				(int) (Config.screenWidth * 0.05), 0, 0, 0, 0);
	}

	public void setOkClickListener(View.OnClickListener listener) {
		btnOk.setOnClickListener(listener);
	}

	public void setCancelClickListener(View.OnClickListener listener) {
		btnCancel.setOnClickListener(listener);
	}

	public void makeEmailData(ArrayList<LinearLayout> emailList) {
		for (int i = 0; i < emailList.size(); i++) {
			llEmail.addView(emailList.get(i));
		}
	}
}

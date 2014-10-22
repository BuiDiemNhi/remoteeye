package remateeyes.bsp.vn.dialog;

import remoteeyes.bsp.vn.R;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.model.Friend;
import remoteeyes.bsp.vn.model.UserInfo;
import remoteeyes.bsp.vn.uilt.ResizeUtils;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DialogSendInvite extends Dialog implements OnClickListener {

	Context context;
	CheckBox cbKeepIt;
	Button btnOk, btnCancel;
	LinearLayout llSendInvite;
	TextView tvTitle, tvReceive;
	EditText et_content_send_email;
	Friend friend;
	UserInfo info;

	public DialogSendInvite(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setContentView(R.layout.dialog_send_invite);

		cbKeepIt = (CheckBox) findViewById(R.id.cb_keep_it);
		btnCancel = (Button) findViewById(R.id.btn_cancel);
		btnOk = (Button) findViewById(R.id.btn_ok);
		llSendInvite = (LinearLayout) findViewById(R.id.ll_send_invite);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvReceive = (TextView) findViewById(R.id.tv_receive);
		et_content_send_email = (EditText) findViewById(R.id.et_content_send_email);

		btnCancel.setOnClickListener(this);
		btnOk.setOnClickListener(this);

		adjustDialog();
	}

	private void adjustDialog() {
		llSendInvite.getLayoutParams().height = (int) (Config.screenWidth * 0.8);

		LinearLayout.LayoutParams paramsCBKeepIt = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		paramsCBKeepIt.width = (int) (Config.screenWidth * 0.04);
		paramsCBKeepIt.height = (int) (Config.screenWidth * 0.04);
		cbKeepIt.setLayoutParams(paramsCBKeepIt);

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

	public void setContact(Friend friend) {
		this.friend = friend;
		info = new UserInfo().getInstance();
		tvTitle.setText(String.format(
				context.getResources().getString(
						R.string.send_invitation_to_address), friend.getName()));
		String ContentSend = "Dear " + friend.getName() + ",\n"
				+ "I would like you to check app.\n" + "Thanks,\n"
				+ info.getName();
		et_content_send_email.setText(ContentSend);
		tvReceive.setText(String.format(
				context.getResources().getString(
						R.string.name_will_receive_this_message),
				friend.getName()));
	}

	private void sendEmail() {

		Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
		if (friend.getEmail() != null) {
			emailIntent.setData(Uri.parse("mailto:" + friend.getEmail()));
		} else {
			emailIntent.setData(Uri.parse("mailto:" + friend.getName()));
		}
		emailIntent.putExtra(Intent.EXTRA_SUBJECT,
				"Invites you to join Remote eye");
		emailIntent.putExtra(Intent.EXTRA_TEXT, et_content_send_email.getText()
				.toString());

		try {
			context.startActivity(Intent.createChooser(emailIntent,
					"Send email using..."));
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(context, "No email clients installed.",
					Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_cancel:
			this.dismiss();
			break;
		case R.id.btn_ok:
			sendEmail();
			this.dismiss();
			break;
		default:
			break;
		}
	}
}

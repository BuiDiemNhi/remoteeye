package remateeyes.bsp.vn.dialog;

import remoteeyes.bsp.vn.R;
import remoteeyes.bsp.vn.TermsAndConditionsActivity;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.uilt.ResizeUtils;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DialogChallengePrivacy extends Dialog {

	Context context;
	CheckBox cbPrivate;
	TextView tvPrivate , tvTitle;
	Button btnOk, btnCancel;
	Typeface typeface;
	LinearLayout llDialogPrivate;
	
	public DialogChallengePrivacy(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setContentView(R.layout.dialog_challenge_privacy);

		llDialogPrivate = (LinearLayout)findViewById(R.id.ll_dialog_private);
		cbPrivate = (CheckBox) findViewById(R.id.cb_private);
		tvPrivate = (TextView) findViewById(R.id.tv_private);
		tvTitle = (TextView) findViewById(R.id.tv_title_private);
		btnOk = (Button) findViewById(R.id.btn_private_ok);
		btnCancel = (Button) findViewById(R.id.btn_private_cancel);

		cbPrivate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (cbPrivate.isChecked())
					tvPrivate.setTextColor(context.getResources().getColor(
							R.color.black));
				else
					tvPrivate.setTextColor(context.getResources().getColor(
							R.color.red));

			}
		});
		setFontTextView();
		adjustDialog();
		setClickTerms();
	}

	private void setFontTextView() {
		typeface = Typeface.createFromAsset(context.getAssets(),
				"fonts/HomeRunScript-roman.otf");
		tvTitle.setTypeface(typeface);
	}
	
	private void adjustDialog() {
		llDialogPrivate.getLayoutParams().width = (int)(Config.screenWidth*0.7);
		llDialogPrivate.getLayoutParams().height = (int)(Config.screenHeight*0.4);
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

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
		params.width = (int) (Config.screenWidth * 0.05);
		params.height = (int) (Config.screenWidth * 0.05);
		params.setMargins(0, (int) (Config.screenHeight * 0.005),
				(int) (Config.screenWidth * 0.01), 0);
		
		cbPrivate.setLayoutParams(params);
	}

	public void setCancelClickListener(View.OnClickListener listener) {
		btnCancel.setOnClickListener(listener);
	}

	public void setOkClickListener(View.OnClickListener listener) {
		btnOk.setOnClickListener(listener);
	}

	private void setClickTerms() {
		SpannableString span = new SpannableString(context.getResources()
				.getString(R.string.confirm_accept_content));
		ClickableSpan clickable = new ClickableSpan() {

			@Override
			public void onClick(View widget) {
				context.startActivity(new Intent(context,
						TermsAndConditionsActivity.class));
			}
		};
		span.setSpan(clickable, 174, span.length(), 0);
		span.setSpan(
				new ForegroundColorSpan(context.getResources().getColor(
						R.color.text_blue_link)), 174, span.length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		tvPrivate.setText(span);
		tvPrivate.setMovementMethod(LinkMovementMethod.getInstance());
	}

	public boolean isAcceptTerm() {
		return cbPrivate.isChecked();
	}
}

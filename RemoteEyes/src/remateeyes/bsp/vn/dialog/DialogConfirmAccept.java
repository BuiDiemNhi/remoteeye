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
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DialogConfirmAccept extends Dialog {

	Context context;
	Button btnCancel, btnOk;
	LinearLayout llDialogAccept;
	CheckBox cbTerms;
	TextView tvTerms, tvTitle;
	Typeface typeface;

	public DialogConfirmAccept(final Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_confirm_accept);
		getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		this.context = context;

		btnCancel = (Button) findViewById(R.id.btn_cancel_confirm_accept);
		btnOk = (Button) findViewById(R.id.btn_ok_confirm_accept);
		llDialogAccept = (LinearLayout)findViewById(R.id.ll_dialog_accept);
		cbTerms = (CheckBox)findViewById(R.id.cb_terms_dialog_accept);
		tvTerms =(TextView)findViewById(R.id.tv_terms_dialog_accept);
		tvTitle =(TextView)findViewById(R.id.tv_title_comfirm_accept);
		cbTerms.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (cbTerms.isChecked())
					tvTerms.setTextColor(context.getResources().getColor(R.color.black));
				else
					tvTerms.setTextColor(context.getResources().getColor(R.color.red));	
				
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
		llDialogAccept.getLayoutParams().width = (int)(Config.screenWidth*0.7);
		llDialogAccept.getLayoutParams().height = (int)(Config.screenWidth*0.6);
		
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
		params.setMargins(0, (int) (Config.screenWidth * 0.005),
				(int) (Config.screenWidth * 0.01), 0);
		cbTerms.setLayoutParams(params);
		
	}

	public void setCancelClickListener(View.OnClickListener listener) {
		btnCancel.setOnClickListener(listener);
	}

	public void setOkClickListner(View.OnClickListener listener) {
		btnOk.setOnClickListener(listener);
	}
	
	private void setClickTerms(){
		SpannableString span = new SpannableString(context.getResources().getString(R.string.confirm_accept_content));
		ClickableSpan clickable = new ClickableSpan() {
			
			@Override
			public void onClick(View widget) {
				context.startActivity(new Intent(context,
						TermsAndConditionsActivity.class));
			}
		};
		span.setSpan(clickable, 174, span.length(), 0);
		span.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.text_blue_link)), 174, span.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		tvTerms.setText(span);
		tvTerms.setMovementMethod(LinkMovementMethod.getInstance());
	}
	
	public boolean isAcceptTerm(){
		return cbTerms.isChecked();
	}
}

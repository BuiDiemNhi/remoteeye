package remateeyes.bsp.vn.dialog;

import remoteeyes.bsp.vn.R;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.uilt.ResizeUtils;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DialogConfirmIgnore extends Dialog {

	Context context;
	LinearLayout llDialogIgnore;
	Button btnCancel, btnOk;
	TextView tvContent, tvTitle;
	Typeface typeface;
	
	public DialogConfirmIgnore(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_confirm_ignore);
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		this.context = context;
		
		llDialogIgnore = (LinearLayout)findViewById(R.id.ll_dialog_ignore);
		btnCancel = (Button)findViewById(R.id.btn_cancel_dialog_ignore);
		btnOk = (Button)findViewById(R.id.btn_ok_dialog_ignore);
		tvContent = (TextView)findViewById(R.id.tvdialog_ignore);
		tvTitle =(TextView)findViewById(R.id.tv_title_comfirm_ignore);
		adjustDialog();
		setFontTextView();
	}
	
	private void setFontTextView() {
		typeface = Typeface.createFromAsset(context.getAssets(),
				"fonts/HomeRunScript-roman.otf");
		tvTitle.setTypeface(typeface);
	}
	
	private void adjustDialog(){
		llDialogIgnore.getLayoutParams().width = (int)(Config.screenWidth*0.7);
		llDialogIgnore.getLayoutParams().height = (int)(Config.screenWidth*0.6);
		
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
	
	public void setContent(){
		tvContent.setText(context.getResources().getString(R.string.confirm_ignore_global_content));
	}

	public void setCancelClickListener(View.OnClickListener listener){
		btnCancel.setOnClickListener(listener);
	}
	
	public void setOkClickListener(View.OnClickListener listener){
		btnOk.setOnClickListener(listener);
	}
}

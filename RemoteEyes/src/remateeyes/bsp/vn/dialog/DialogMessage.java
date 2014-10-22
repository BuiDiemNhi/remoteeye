package remateeyes.bsp.vn.dialog;

import remoteeyes.bsp.vn.R;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.uilt.ResizeUtils;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DialogMessage extends Dialog {

	Button btnNo, btnYes;
	View.OnClickListener OkClickListener, CancelClickListener;
	TextView tvTitle, tvContent;
	LinearLayout llDialogMessage;
	Context context;
	
	public DialogMessage(Context context) {
		super(context);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_message);
		this.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		
		this.context = context;
		btnNo = (Button)findViewById(R.id.btn_no);
		btnYes = (Button)findViewById(R.id.btn_yes);
		tvTitle = (TextView)findViewById(R.id.title_message_dialog);
		tvContent = (TextView)findViewById(R.id.content_message_dialog);
		llDialogMessage = (LinearLayout)findViewById(R.id.ll_dialog_message);
				
		adjustControl();
	}
	
	public void adjustControl(){
		Drawable okDrawable = context.getResources().getDrawable(R.drawable.ok_button);
		
		ResizeUtils.resizeButton(btnNo,
				(int) (Config.screenWidth * 0.1), (int) (Config.screenWidth
						* 0.1 * okDrawable.getIntrinsicHeight() / okDrawable
						.getIntrinsicWidth()), 0, 0, 0, 0);
		
		ResizeUtils.resizeButton(btnYes,
				(int) (Config.screenWidth * 0.1), (int) (Config.screenWidth
						* 0.1 * okDrawable.getIntrinsicHeight() / okDrawable
						.getIntrinsicWidth()), 0, 0, 0, 0);
		
		llDialogMessage.getLayoutParams().width = (int) (Config.screenWidth * 0.7);
		llDialogMessage.getLayoutParams().height = (int) (Config.screenWidth * 0.5);
	}
	
	public void setTitle(String title){
		tvTitle.setText(title);
	}
	
	public void setContent(String content){
		tvContent.setText(content);
	}
	
	public void setOkClickListener(View.OnClickListener listener){
		OkClickListener = listener;
		btnYes.setOnClickListener(OkClickListener);
	}
	
	public void setCancelClickListener(View.OnClickListener listener){
		CancelClickListener = listener;
		btnNo.setOnClickListener(CancelClickListener);
	}
}
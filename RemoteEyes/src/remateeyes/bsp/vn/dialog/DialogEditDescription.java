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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class DialogEditDescription extends Dialog {

	ImageView ivQuoteOpen, ivQuoteClose;
	Context context;
	LinearLayout llDialogEdit;
	Button btnCancel, btnOk;
	EditText etDescription;
	
	public DialogEditDescription(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_edit_description);
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		this.context = context;
		
		llDialogEdit = (LinearLayout)findViewById(R.id.ll_dialog_edit_description);
		btnCancel = (Button)findViewById(R.id.btn_cancel_dialog_edit);
		btnOk = (Button)findViewById(R.id.btn_ok_dialog_edit);
		etDescription = (EditText)findViewById(R.id.et_description);
		ivQuoteOpen = (ImageView) findViewById(R.id.quote_open);
		ivQuoteClose = (ImageView) findViewById(R.id.quote_close);
		
		adjustDialog();
	}
	
	private void adjustDialog(){
		llDialogEdit.getLayoutParams().width = (int)(Config.screenWidth*0.9);
		llDialogEdit.getLayoutParams().height = (int)(Config.screenHeight*0.55);
		
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
		
		ResizeUtils.resizeImageView(ivQuoteOpen, (int) (Config.screenWidth * 0.07),
				(int) (Config.screenWidth * 0.07), (int) (Config.screenWidth * 0.02),
				(int) (Config.screenWidth * 0.02), (int) (Config.screenWidth * 0.02),
				(int) (Config.screenWidth * 0.02), RelativeLayout.ALIGN_PARENT_LEFT);

		ResizeUtils.resizeImageView(ivQuoteClose, (int) (Config.screenWidth * 0.07),
				(int) (Config.screenWidth * 0.07), (int) (Config.screenWidth * 0.02),
				(int) (Config.screenWidth * 0.02), (int) (Config.screenWidth * 0.02),
				(int) (Config.screenWidth * 1.03), RelativeLayout.ALIGN_PARENT_BOTTOM,
				RelativeLayout.ALIGN_PARENT_RIGHT);

		ResizeUtils.resizeEditText(etDescription, Config.screenWidth,
				(int) (Config.screenHeight * 0.2), 0, 0, 0, 0,
				RelativeLayout.ALIGN_PARENT_TOP);
		etDescription.setPadding((int) (Config.screenWidth * 0.1),
				(int) (Config.screenWidth * 0.02), (int) (Config.screenWidth * 0.1),
				(int) (Config.screenWidth * 0.02));
	}
	
	public void setCancelClickListener(View.OnClickListener listener){
		btnCancel.setOnClickListener(listener);
	}
	
	public void setOkClickListener(View.OnClickListener listener){
		btnOk.setOnClickListener(listener);
	}

}

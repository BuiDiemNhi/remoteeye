package remateeyes.bsp.vn.dialog;

import java.util.ArrayList;
import remoteeyes.bsp.vn.DetailChallengeActivity;
import remoteeyes.bsp.vn.DetailChallengeActivity;
import remoteeyes.bsp.vn.MyAccountActivity;
import remoteeyes.bsp.vn.MyAreaActivity;
import remoteeyes.bsp.vn.R;
import remoteeyes.bsp.vn.ZoomActivity;
import remoteeyes.bsp.vn.adapter.CommentAdapter;
import remoteeyes.bsp.vn.asynctask.AddCommentAsynctask;
import remoteeyes.bsp.vn.asynctask.ImageLoaderAsyncTask;
import remoteeyes.bsp.vn.asynctask.LoadDetailChallengeAsynctask;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.model.CommentObject;
import remoteeyes.bsp.vn.model.ImageObject;
import remoteeyes.bsp.vn.model.UserInfo;
import remoteeyes.bsp.vn.type.DialogType;
import remoteeyes.bsp.vn.uilt.ResizeUtils;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN) @SuppressLint("NewApi") public class DialogExplanatory extends Dialog implements
		android.view.View.OnClickListener {

	Context context;
	LinearLayout llDialogExplanatory, llDialogExplanatoryImage;
	TextView tvTitle;
	ImageView ivImage, ivPrev, ivNext, ivZoom;
	ListView lvComment;
	Button btnCancel, btnAddComment;
	EditText etComment;
	String id, IdChallenge, urlImage;
	ArrayList<CommentObject> commentList;
	ArrayList<ImageObject> imgList;
	int index;
	CommentAdapter commentAdapter;

	public DialogExplanatory(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_explanatory_photo);
		getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		this.context = context;

		llDialogExplanatory = (LinearLayout) findViewById(R.id.ll_dialog_explanatory);
		tvTitle = (TextView) findViewById(R.id.tv_dialog_explanatory_title);
		ivPrev = (ImageView) findViewById(R.id.iv_prev);
		ivImage = (ImageView) findViewById(R.id.iv_dialog_explanatory_image);
		ivNext = (ImageView) findViewById(R.id.iv_next);
		ivZoom = (ImageView) findViewById(R.id.iv_zoom);
		llDialogExplanatoryImage = (LinearLayout) findViewById(R.id.ll_dialog_explanatory_image);
		lvComment = (ListView) findViewById(R.id.lv_dialog_explanatory_comment);
		btnCancel = (Button) findViewById(R.id.btn_dialog_explanatory_cancel);
		btnAddComment = (Button) findViewById(R.id.btn_dialog_explanatory_add_comment);
		etComment = (EditText) findViewById(R.id.et_comment_dialog_exp);

		btnCancel.setOnClickListener(this);
		btnAddComment.setOnClickListener(this);
		ivPrev.setOnClickListener(this);
		ivNext.setOnClickListener(this);
		ivZoom.setOnClickListener(this);
		commentList = new ArrayList<CommentObject>();
		commentAdapter = new CommentAdapter(context, R.layout.item_comment,
				commentList);
		lvComment.setAdapter(commentAdapter);

		adjustDialog();
	}

	private void adjustDialog() {
		llDialogExplanatory.getLayoutParams().width = (int) (Config.screenWidth * 0.85);
		llDialogExplanatory.getLayoutParams().height = (int) (Config.screenHeight * 0.9);
		llDialogExplanatoryImage.getLayoutParams().height = (int) (Config.screenWidth * 0.45);
		lvComment.getLayoutParams().height = (int) (Config.screenWidth * 0.35);

		Drawable cancelDrawable = context.getResources().getDrawable(
				R.drawable.cancel_button);
		ResizeUtils.resizeButton(
				btnCancel,
				(int) (Config.screenWidth * 0.1),
				(int) (Config.screenWidth * 0.1
						* cancelDrawable.getIntrinsicHeight() / cancelDrawable
						.getIntrinsicWidth()), 0, 0, 0, 0);

		Drawable addCommentDrawable = context.getResources().getDrawable(
				R.drawable.comment_add);
		ResizeUtils
				.resizeButton(
						btnAddComment,
						(int) (Config.screenWidth * 0.12),
						(int) (Config.screenWidth * 0.12
								* addCommentDrawable.getIntrinsicHeight() / addCommentDrawable
								.getIntrinsicWidth()), 0, 0, 0, 0);

		ResizeUtils.resizeImageView(ivPrev, (int) (Config.screenWidth * 0.03),
				(int) (Config.screenWidth * 0.07),
				(int) (Config.screenWidth * 0.001),
				(int) (Config.screenWidth * 0.02),
				(int) (Config.screenWidth * 0.02),
				(int) (Config.screenWidth * 0.03),
				RelativeLayout.ALIGN_PARENT_LEFT,
				RelativeLayout.CENTER_VERTICAL);

		ResizeUtils.resizeImageView(ivNext, (int) (Config.screenWidth * 0.03),
				(int) (Config.screenWidth * 0.07),
				(int) (Config.screenWidth * 0.02),
				(int) (Config.screenWidth * 0.001),
				(int) (Config.screenWidth * 0.02),
				(int) (Config.screenWidth * 0.03),
				RelativeLayout.ALIGN_PARENT_RIGHT,
				RelativeLayout.CENTER_VERTICAL);

		ResizeUtils.resizeImageView(ivZoom, (int) (Config.screenWidth * 0.05),
				(int) (Config.screenWidth * 0.05),
				(int) (Config.screenWidth * 0.02),
				(int) (Config.screenWidth * 0.001),
				(int) (Config.screenWidth * 0.02),
				(int) (Config.screenWidth * 0.03),
				RelativeLayout.ALIGN_PARENT_RIGHT,
				RelativeLayout.ALIGN_PARENT_BOTTOM);

	}

	public void setTitle(String title) {
		tvTitle.setText(title);
	}

	public void setImage(Drawable image) {
		ivImage.setBackground(image);
		ivImage.getLayoutParams().width = (int) (Config.screenWidth * 0.5
				* image.getIntrinsicWidth() / image.getIntrinsicHeight());
		ivImage.getLayoutParams().height = (int) (Config.screenWidth * 0.5);
	}

	public void setCommentList(ArrayList<CommentObject> commentList) {
		this.commentList = commentList;
		lvComment.setAdapter(new CommentAdapter(context, R.layout.item_comment,
				commentList));
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setUrlImage(String url) {
		this.urlImage = url;
	}

	public void setIdChallenge(String id) {
		this.IdChallenge = id;
	}

	public void setListImage(ArrayList<ImageObject> imgList) {
		this.imgList = imgList;
	}

	@SuppressLint("NewApi") @Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_dialog_explanatory_cancel:
			new LoadDetailChallengeAsynctask(context).execute(IdChallenge);
			this.dismiss();
			break;
		case R.id.btn_dialog_explanatory_add_comment:
			if (etComment.getVisibility() == View.VISIBLE) {
				if (!etComment.getText().toString().equals("")) {

				
					try {
						new AddCommentAsynctask(context).execute(Config.IdUser,
								id, etComment.getText().toString(), "1");
						CommentObject obj = new CommentObject();
						obj.setMessage(etComment.getText().toString());
						obj.setMine(true);
						obj.setUserId(Config.IdUser);

						obj.setUrlAvatar(UserInfo.getInstance().getAvatar());

						((DetailChallengeActivity) context).detailChallengeFragment
								.addCommentExplanPhoto(obj, id);
						setCommentList(commentList);
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				etComment.setVisibility(View.INVISIBLE);
			} else {
				etComment.setVisibility(View.VISIBLE);
				etComment.setText("");
			}
			break;
		case R.id.iv_next:
			int totalNext = imgList.size();
			int indexImgNext = index + 1;
			if (totalNext > 0) {
				if (indexImgNext < totalNext) {
					ivImage.setBackground(null);
					new ImageLoaderAsyncTask(ivImage,
							(int) (Config.screenHeight * 0.5), false)
							.execute(imgList.get(indexImgNext).getUrlImage());
					setTitle(String.format(
							context.getResources().getString(
									R.string.dialog_explanatory_title),
							indexImgNext + 1, totalNext));
					setId(imgList.get(indexImgNext).getId());
					setIndex(indexImgNext);
					setCommentList(imgList.get(indexImgNext).getCommentList());
					setUrlImage(imgList.get(indexImgNext).getUrlImage());
				}
			}
			break;
		case R.id.iv_prev:
			int totalPrev = imgList.size();
			int indexImgPrev = index - 1;
			if (totalPrev > 0) {
				if (indexImgPrev > -1) {
					ivImage.setBackground(null);
					new ImageLoaderAsyncTask(ivImage,
							(int) (Config.screenHeight * 0.5), false)
							.execute(imgList.get(indexImgPrev).getUrlImage());
					setTitle(String.format(
							context.getResources().getString(
									R.string.dialog_explanatory_title),
							indexImgPrev + 1, totalPrev));
					setId(imgList.get(indexImgPrev).getId());
					setIndex(indexImgPrev);
					setCommentList(imgList.get(indexImgPrev).getCommentList());
					setUrlImage(imgList.get(indexImgPrev).getUrlImage());
				}
			}
			break;
		case R.id.iv_zoom:
			Intent i = new Intent(context, ZoomActivity.class);
			i.putExtra("url", urlImage);
			context.startActivity(i);
			break;
		default:
			break;
		}
	}
}
package remateeyes.bsp.vn.dialog;

import java.util.ArrayList;

import com.facebook.Session;

import remoteeyes.bsp.vn.DetailChallengeActivity;
import remoteeyes.bsp.vn.MyAreaActivity;
import remoteeyes.bsp.vn.R;
import remoteeyes.bsp.vn.ZoomActivity;
import remoteeyes.bsp.vn.adapter.CommentAdapter;
import remoteeyes.bsp.vn.asynctask.AddCommentAsynctask;
import remoteeyes.bsp.vn.asynctask.DownloadImageAsynctask;
import remoteeyes.bsp.vn.asynctask.ImageLoaderAsyncTask;
import remoteeyes.bsp.vn.asynctask.LoadDetailChallengeAsynctask;
import remoteeyes.bsp.vn.asynctask.RatingAsynctask;
import remoteeyes.bsp.vn.asynctask.WinChallengeAsyncstask;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.model.ChallengeObject;
import remoteeyes.bsp.vn.model.CommentObject;
import remoteeyes.bsp.vn.model.ResponsePhotoObject;
import remoteeyes.bsp.vn.model.UserInfo;
import remoteeyes.bsp.vn.social.network.FacebookUtils;
import remoteeyes.bsp.vn.type.DialogType;
import remoteeyes.bsp.vn.uilt.ResizeUtils;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

public class DialogResponsePhoto extends Dialog implements
		android.view.View.OnClickListener {

	Context context;
	Activity activity;
	LinearLayout llDialogResponsePhoto, llDialogResponsePhotoImage;
	TextView tvTitle, tvMessage;
	ImageView ivImage, ivPrev, ivNext, ivZoom, ivShare, ivDownload;
	public static ImageView iv_winner;
	public static ImageView iv_set_winner;
	ListView lvComment;
	Button btnCancel, btnAddComment;
	RatingBar rbResponsePhoto, rbResponsePhotoLink;
	EditText etComment;
	String id, type, idchal, urlImage;
	float rating = 0;
	ArrayList<CommentObject> commentList = new ArrayList<CommentObject>();
	ArrayList<ResponsePhotoObject> imgList;
	int index;
	boolean isRated;
	String titleChallenge;
	public static boolean isWined;

	public DialogResponsePhoto(final Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_response_photo);
		getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		this.context = context;

		llDialogResponsePhoto = (LinearLayout) findViewById(R.id.ll_dialog_response_photo);
		tvTitle = (TextView) findViewById(R.id.tv_dialog_response_photo_title);
		tvMessage = (TextView) findViewById(R.id.tv_dialog_response_photo_message);
		ivPrev = (ImageView) findViewById(R.id.iv_prev);
		ivImage = (ImageView) findViewById(R.id.iv_dialog_response_photo_image);
		ivNext = (ImageView) findViewById(R.id.iv_next);
		ivZoom = (ImageView) findViewById(R.id.iv_zoom);
		ivDownload = (ImageView) findViewById(R.id.iv_download);
		ivShare = (ImageView) findViewById(R.id.iv_share);
		llDialogResponsePhotoImage = (LinearLayout) findViewById(R.id.ll_dialog_response_photo_image);
		lvComment = (ListView) findViewById(R.id.lv_dialog_response_photo_comment);
		btnCancel = (Button) findViewById(R.id.btn_dialog_response_photo_cancel);
		btnAddComment = (Button) findViewById(R.id.btn_dialog_response_photo_add_comment);
		rbResponsePhoto = (RatingBar) findViewById(R.id.rb_response_photo_rating);
		rbResponsePhotoLink = (RatingBar) findViewById(R.id.rb_response_photo_rating_link);
		etComment = (EditText) findViewById(R.id.et_comment_dialog);
		iv_winner = (ImageView) findViewById(R.id.iv_winner);
		iv_set_winner = (ImageView) findViewById(R.id.iv_set_winner);
		btnCancel.setOnClickListener(this);
		btnAddComment.setOnClickListener(this);
		ivPrev.setOnClickListener(this);
		ivNext.setOnClickListener(this);
		ivZoom.setOnClickListener(this);
		ivDownload.setOnClickListener(this);
		ivShare.setOnClickListener(this);
		iv_set_winner.setOnClickListener(this);
		iv_winner.setOnClickListener(this);

		adjustDialog();
		setRatingChallengeAction();

		rbResponsePhoto
				.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

					@Override
					public void onRatingChanged(RatingBar ratingBar,
							float rating, boolean fromUser) {
						new RatingAsynctask(context).execute(id, Config.IdUser,
								rating + "", type);
						rbResponsePhoto.setIsIndicator(false);
						btnAddComment.setEnabled(true);
					}
				});
	}

	public void showIcWinner() {
		for (int i = 0; i < imgList.size(); i++) {
			if (imgList.get(i).isWin() == true) {
				iv_winner.setVisibility(View.VISIBLE);
			} else {
				iv_winner.setVisibility(View.GONE);
			}
		}

	}

	public void showIcSetWinner(boolean flag) {
		if (flag == true) {
			iv_set_winner.setVisibility(View.VISIBLE);
		} else {
			iv_set_winner.setVisibility(View.GONE);
		}
	}

	private void adjustDialog() {
		llDialogResponsePhoto.getLayoutParams().width = (int) (Config.screenWidth * 0.85);
		llDialogResponsePhoto.getLayoutParams().height = (int) (Config.screenHeight * 0.85);
		llDialogResponsePhotoImage.getLayoutParams().height = (int) (Config.screenWidth * 0.45);
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

		Drawable drawStar = context.getResources().getDrawable(
				R.drawable.response_rating_bar);
		rbResponsePhoto.getLayoutParams().height = drawStar
				.getIntrinsicHeight();
		rbResponsePhotoLink.getLayoutParams().height = drawStar
				.getIntrinsicHeight();

		ResizeUtils.resizeEditText(etComment, (int) (Config.screenWidth * 0.7),
				(int) (Config.screenWidth * 0.1),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01));

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

		ResizeUtils
				.resizeImageView(iv_winner, (int) (Config.screenWidth * 0.1),
						(int) (Config.screenWidth * 0.19),
						(int) (Config.screenWidth * 0.001),
						(int) (Config.screenWidth * 0.02),
						(int) (Config.screenWidth * 0.02),
						(int) (Config.screenWidth * 0.03),
						RelativeLayout.CENTER_IN_PARENT,
						RelativeLayout.CENTER_VERTICAL);

		ResizeUtils.resizeImageView(ivZoom, (int) (Config.screenWidth * 0.05),
				(int) (Config.screenWidth * 0.05),
				(int) (Config.screenWidth * 0), (int) (Config.screenWidth * 0),
				(int) (Config.screenWidth * 0.02),
				(int) (Config.screenWidth * 0.03),
				RelativeLayout.ALIGN_PARENT_RIGHT,
				RelativeLayout.ALIGN_PARENT_BOTTOM);
		ResizeUtils.resizeImageView(iv_set_winner,
				(int) (Config.screenWidth * 0.07),
				(int) (Config.screenWidth * 0.07),
				(int) (Config.screenWidth * 0), (int) (Config.screenWidth * 0),
				(int) (Config.screenWidth * 0.02),
				(int) (Config.screenWidth * 0.03),
				RelativeLayout.ALIGN_PARENT_LEFT,
				RelativeLayout.ALIGN_PARENT_BOTTOM);

		ResizeUtils.resizeImageView(ivDownload,
				(int) (Config.screenWidth * 0.05),
				(int) (Config.screenWidth * 0.05),
				(int) (Config.screenWidth * 0), (int) (Config.screenWidth * 0),
				(int) (Config.screenWidth * 0.02),
				(int) (Config.screenWidth * 0.03),
				RelativeLayout.ALIGN_PARENT_BOTTOM);

		RelativeLayout.LayoutParams tmp = (RelativeLayout.LayoutParams) ivDownload
				.getLayoutParams();
		tmp.addRule(RelativeLayout.LEFT_OF, ivZoom.getId());

		ResizeUtils.resizeImageView(ivShare, (int) (Config.screenWidth * 0.05),
				(int) (Config.screenWidth * 0.05),
				(int) (Config.screenWidth * 0), (int) (Config.screenWidth * 0),
				(int) (Config.screenWidth * 0.02),
				(int) (Config.screenWidth * 0.03),
				RelativeLayout.ALIGN_PARENT_BOTTOM);

		tmp = (RelativeLayout.LayoutParams) ivShare.getLayoutParams();
		tmp.addRule(RelativeLayout.LEFT_OF, ivDownload.getId());

	}

	public void setRating(float rating) {
		this.rating = rating;
		if (rating > 0) {
			rbResponsePhoto.setRating(rating);
			rbResponsePhoto.setIsIndicator(false);
		}
	}

	public void setTitle(String title) {
		tvTitle.setText(title);
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setIdChall(String id) {
		this.idchal = id;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setMessage(String message) {
		tvMessage.setText(message);
	}

	public void setImage(Drawable image) {
		ivImage.setBackground(image);
		ivImage.getLayoutParams().width = (int) (Config.screenWidth * 0.5
				* image.getIntrinsicWidth() / image.getIntrinsicHeight());
		ivImage.getLayoutParams().height = (int) (Config.screenWidth * 0.5);
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public void setTitleChallenge(String title) {
		this.titleChallenge = title;
	}

	public void setUrlImage(String url) {
		this.urlImage = url;
	}

	private void blinkingRatingBar() {
		Animation alphaAnim = new AlphaAnimation(0.0f, 1.0f);
		alphaAnim.setDuration(900);
		alphaAnim.setStartOffset(20);
		alphaAnim.setRepeatMode(Animation.REVERSE);
		alphaAnim.setRepeatCount(Animation.INFINITE);
		rbResponsePhotoLink.startAnimation(alphaAnim);
	}

	public void setRatingBar(boolean isRated) {
		if (isRated) {
			rbResponsePhotoLink.setVisibility(View.GONE);
		} else {
			rbResponsePhotoLink.setVisibility(View.VISIBLE);
			blinkingRatingBar();
		}
	}

	public void setCommentList(ArrayList<CommentObject> commentList) {
		this.commentList = commentList;
		lvComment.setAdapter(new CommentAdapter(context, R.layout.item_comment,
				commentList));
	}

	public void checkVisibleButton(boolean isRated) {
		if (isRated) {
			btnAddComment.setEnabled(true);
		} else {
			btnAddComment.setEnabled(false);
		}
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void setRate(Boolean isRated) {
		this.isRated = isRated;
	}

	public void setListImage(ArrayList<ResponsePhotoObject> imgList) {
		this.imgList = imgList;
	}

	private void setRatingChallengeAction() {
		rbResponsePhoto
				.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

					@Override
					public void onRatingChanged(RatingBar ratingBar,
							float rating, boolean fromUser) {
						rbResponsePhotoLink.clearAnimation();
						rbResponsePhotoLink.setVisibility(View.GONE);

					}
				});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_dialog_response_photo_cancel:
			new LoadDetailChallengeAsynctask(context).execute(idchal);
			this.dismiss();
			break;
		case R.id.btn_dialog_response_photo_add_comment:
			if (etComment.getVisibility() == View.VISIBLE) {
				if (!etComment.getText().toString().equals("")) {
					new AddCommentAsynctask(context).execute(Config.IdUser, id,
							etComment.getText().toString(), type);
					CommentObject co = new CommentObject();
					co.setMessage(etComment.getText().toString());
					co.setMine(true);
					co.setUrlAvatar(UserInfo.getInstance().getAvatar());
					co.setUserId(Config.IdUser);
					try {
						((MyAreaActivity) context).detailChallengeFragment
								.addCommentReponsePhoto(co, id);
					} catch (Exception ex) {
						((DetailChallengeActivity) context).detailChallengeFragment
								.addCommentReponsePhoto(co, id);
					}
					setCommentList(commentList);
				}
				etComment.setVisibility(View.INVISIBLE);
			} else {
				etComment.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.iv_next:
			int totalNext = imgList.size();
			int indexImgNext = index + 1;
			if (totalNext > 0) {
				if (indexImgNext < totalNext) {
					ivImage.setBackground(null);
					rbResponsePhoto.setRating((float) 0.0);
					new ImageLoaderAsyncTask(ivImage,
							(int) (Config.screenHeight * 0.5), false)
							.execute(imgList.get(indexImgNext).getUrlImage());
					setTitle(String.format(
							context.getResources().getString(
									R.string.dialog_response_title),
							indexImgNext + 1, totalNext));
					setId(imgList.get(indexImgNext).getId());
					setIndex(indexImgNext);
					setType("1");
					if (imgList.get(indexImgNext).isWin()) {
						iv_winner.setVisibility(View.VISIBLE);
						showIcSetWinner(false);
						// setWined(true);
					} else {
						iv_winner.setVisibility(View.GONE);
						showIcSetWinner(false);
						// setWined(true);
					}
					checkVisibleButton(isRated);
					setRating(imgList.get(indexImgNext).getRate());
					if (isRated) {
						setMessage(context.getResources().getString(
								R.string.dialog_response_message_rated));
					} else {
						setMessage(context.getResources().getString(
								R.string.dialog_response_message_not_rate_yet));
						setRatingBar(isRated);
					}
					setCommentList(imgList.get(indexImgNext).getCommentList());
					setUrlImage(imgList.get(indexImgNext).getUrlImage());
				}
			}
			break;
		case R.id.iv_set_winner:
			if (isWined == true) {
				showIcWinner();
			} else {

				new WinChallengeAsyncstask(activity, idchal, imgList.get(index)
						.getUserID(), imgList.get(index).getId(), "1")
						.execute();
			}

			break;
		case R.id.iv_winner:
			if (iv_winner.getVisibility() == View.VISIBLE) {
				iv_winner.setVisibility(View.GONE);
			}
		case R.id.iv_prev:
			int totalPrev = imgList.size();
			int indexImgPrev = index - 1;
			if (totalPrev > 0) {
				if (indexImgPrev > -1) {
					ivImage.setBackground(null);
					rbResponsePhoto.setRating((float) 0.0);
					new ImageLoaderAsyncTask(ivImage,
							(int) (Config.screenHeight * 0.5), false)
							.execute(imgList.get(indexImgPrev).getUrlImage());
					setTitle(String.format(
							context.getResources().getString(
									R.string.dialog_response_title),
							indexImgPrev + 1, totalPrev));
					setId(imgList.get(indexImgPrev).getId());
					setIndex(indexImgPrev);
					setType("1");
					if (imgList.get(indexImgPrev).isWin()) {
						iv_winner.setVisibility(View.VISIBLE);
						showIcSetWinner(false);
						// setWined(true);
					} else {
						iv_winner.setVisibility(View.GONE);
						showIcSetWinner(false);
						// setWined(true);
					}
					checkVisibleButton(isRated);
					setRating(imgList.get(indexImgPrev).getRate());
					if (isRated) {
						setMessage(context.getResources().getString(
								R.string.dialog_response_message_rated));
					} else {
						setMessage(context.getResources().getString(
								R.string.dialog_response_message_not_rate_yet));
						setRatingBar(isRated);
					}
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
		case R.id.iv_download:
			new DownloadImageAsynctask(context).execute(imgList.get(index)
					.getUrlImage());
			break;

		case R.id.iv_share:
			if (FacebookUtils.currentSession == null)
				FacebookUtils.currentSession = Session
						.openActiveSessionFromCache(activity);
			if (FacebookUtils.currentSession != null
					&& FacebookUtils.currentSession.isOpened()) {
				FacebookUtils.publishImageDialog(activity, imgList.get(index)
						.getUrlImage(), titleChallenge);
			} else {
				FacebookUtils.connectToFB(activity);
				FacebookUtils.shareLinkRunnable = new Runnable() {

					@Override
					public void run() {
						FacebookUtils.publishImageDialog(activity,
								imgList.get(index).getUrlImage(),
								titleChallenge);
					}
				};
			}
			break;
		default:
			break;
		}
	}

	public void setWined(boolean isWined) {
		this.isWined = isWined;
	}
}

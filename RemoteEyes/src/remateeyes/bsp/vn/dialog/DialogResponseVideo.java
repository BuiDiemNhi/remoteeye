package remateeyes.bsp.vn.dialog;

import java.util.ArrayList;

import com.facebook.Session;

import remoteeyes.bsp.vn.DetailChallengeActivity;
import remoteeyes.bsp.vn.MyAreaActivity;
import remoteeyes.bsp.vn.R;
import remoteeyes.bsp.vn.VideoActivity;
import remoteeyes.bsp.vn.ZoomActivity;
import remoteeyes.bsp.vn.adapter.CommentAdapter;
import remoteeyes.bsp.vn.asynctask.AddCommentAsynctask;
import remoteeyes.bsp.vn.asynctask.DownloadImageAsynctask;
import remoteeyes.bsp.vn.asynctask.ImageLoaderAsyncTask;
import remoteeyes.bsp.vn.asynctask.LoadDetailChallengeAsynctask;
import remoteeyes.bsp.vn.asynctask.RatingAsynctask;
import remoteeyes.bsp.vn.asynctask.WinChallengeAsyncstask;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.model.CommentObject;
import remoteeyes.bsp.vn.model.ResponsePhotoObject;
import remoteeyes.bsp.vn.model.ResponseVideoObject;
import remoteeyes.bsp.vn.model.UserInfo;
import remoteeyes.bsp.vn.social.network.FacebookUtils;
import remoteeyes.bsp.vn.uilt.ResizeUtils;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.RatingBar.OnRatingBarChangeListener;

public class DialogResponseVideo extends Dialog implements
		android.view.View.OnClickListener {

	Context context;
	Activity activity;
	LinearLayout llDialogResponseVideo, llDialogResponseVideoImage;
	TextView tvTitle, tvMessage;
	ImageView ivImage, ivPrev, ivNext, ivPlay;
	public static ImageView iv_winner, ivSetWinner;
	ListView lvComment;
	Button btnCancel, btnAddComment;
	RatingBar rbResponseVideo, rbResponseVideoLink;
	EditText etComment;
	String id, type, idchal, urlThumb, urlVideo;
	float rating = 0;
	ArrayList<CommentObject> commentList = new ArrayList<CommentObject>();
	ArrayList<ResponseVideoObject> videoList;
	int index;
	boolean isRated;
	String titleChallenge;
	public static boolean isWined;

	public DialogResponseVideo(final Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_response_video);
		getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		this.context = context;

		llDialogResponseVideo = (LinearLayout) findViewById(R.id.ll_dialog_response_video);
		tvTitle = (TextView) findViewById(R.id.tv_dialog_response_video_title);
		tvMessage = (TextView) findViewById(R.id.tv_dialog_response_video_message);
		ivPrev = (ImageView) findViewById(R.id.iv_prev);
		ivImage = (ImageView) findViewById(R.id.iv_dialog_response_video_image);
		ivNext = (ImageView) findViewById(R.id.iv_next);
		ivPlay = (ImageView) findViewById(R.id.iv_play);
		ivSetWinner = (ImageView) findViewById(R.id.iv_set_winner_video);
		llDialogResponseVideoImage = (LinearLayout) findViewById(R.id.ll_dialog_response_video_image);
		lvComment = (ListView) findViewById(R.id.lv_dialog_response_video_comment);
		btnCancel = (Button) findViewById(R.id.btn_dialog_response_video_cancel);
		btnAddComment = (Button) findViewById(R.id.btn_dialog_response_video_add_comment);
		rbResponseVideo = (RatingBar) findViewById(R.id.rb_response_video_rating);
		rbResponseVideoLink = (RatingBar) findViewById(R.id.rb_response_video_rating_link);
		etComment = (EditText) findViewById(R.id.et_comment_dialog);
		iv_winner = (ImageView) findViewById(R.id.iv_winner_video);
		btnCancel.setOnClickListener(this);
		btnAddComment.setOnClickListener(this);
		ivPrev.setOnClickListener(this);
		ivNext.setOnClickListener(this);
		ivPlay.setOnClickListener(this);
		ivSetWinner.setOnClickListener(this);
		iv_winner.setOnClickListener(this);
		adjustDialog();
		setRatingChallengeAction();

		rbResponseVideo
				.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

					@Override
					public void onRatingChanged(RatingBar ratingBar,
							float rating, boolean fromUser) {
						new RatingAsynctask(context).execute(id, Config.IdUser,
								rating + "", type);
						rbResponseVideo.setIsIndicator(false);
						btnAddComment.setEnabled(true);
					}
				});
	}

	private void adjustDialog() {
		llDialogResponseVideo.getLayoutParams().width = (int) (Config.screenWidth * 0.85);
		llDialogResponseVideo.getLayoutParams().height = (int) (Config.screenHeight * 0.85);
		llDialogResponseVideoImage.getLayoutParams().height = (int) (Config.screenWidth * 0.45);
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
		rbResponseVideo.getLayoutParams().height = drawStar
				.getIntrinsicHeight();
		rbResponseVideoLink.getLayoutParams().height = drawStar
				.getIntrinsicHeight();
		ResizeUtils.resizeImageView(ivSetWinner,
				(int) (Config.screenWidth * 0.07),
				(int) (Config.screenWidth * 0.07),
				(int) (Config.screenWidth * 0), (int) (Config.screenWidth * 0),
				(int) (Config.screenWidth * 0.02),
				(int) (Config.screenWidth * 0.03),
				RelativeLayout.ALIGN_PARENT_LEFT,
				RelativeLayout.CENTER_VERTICAL);

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
	}

	public void setRating(float rating) {
		this.rating = rating;
		if (rating > 0) {
			rbResponseVideo.setRating(rating);
			rbResponseVideo.setIsIndicator(false);
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

	public void setUrlThumb(String url) {
		this.urlThumb = url;
	}

	public void setUrlVideo(String url) {
		this.urlVideo = url;
	}

	private void blinkingRatingBar() {
		Animation alphaAnim = new AlphaAnimation(0.0f, 1.0f);
		alphaAnim.setDuration(900);
		alphaAnim.setStartOffset(20);
		alphaAnim.setRepeatMode(Animation.REVERSE);
		alphaAnim.setRepeatCount(Animation.INFINITE);
		rbResponseVideoLink.startAnimation(alphaAnim);
	}

	public void setRatingBar(boolean isRated) {
		if (isRated) {
			rbResponseVideoLink.setVisibility(View.GONE);
		} else {
			rbResponseVideoLink.setVisibility(View.VISIBLE);
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

	public void setListVideo(ArrayList<ResponseVideoObject> videoList) {
		this.videoList = videoList;
	}

	private void setRatingChallengeAction() {
		rbResponseVideo
				.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

					@Override
					public void onRatingChanged(RatingBar ratingBar,
							float rating, boolean fromUser) {
						rbResponseVideoLink.clearAnimation();
						rbResponseVideoLink.setVisibility(View.GONE);

					}
				});
	}

	public void showIcWinner() {
		for (int i = 0; i < videoList.size(); i++) {
			if (videoList.get(i).isWin() == true) {
				iv_winner.setVisibility(View.VISIBLE);
			} else {
				iv_winner.setVisibility(View.GONE);
			}
		}

	}

	public void setWined(boolean isWined) {
		this.isWined = isWined;
	}

	public void showIcSetWinner(boolean flag) {
		if (flag == true) {
			ivSetWinner.setVisibility(View.VISIBLE);
		} else {
			ivSetWinner.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_dialog_response_video_cancel:
			new LoadDetailChallengeAsynctask(context).execute(idchal);
			this.dismiss();
			break;
		case R.id.btn_dialog_response_video_add_comment:
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
						((DetailChallengeActivity) context).detailChallengeFragment
								.addCommentReponseVideo(co, id);
					} catch (Exception ex) {
						((DetailChallengeActivity) context).detailChallengeFragment
								.addCommentReponseVideo(co, id);
					}
					setCommentList(commentList);
				}
				etComment.setVisibility(View.INVISIBLE);
			} else {
				etComment.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.iv_next:
			int totalNext = videoList.size();
			int indexImgNext = index + 1;
			if (totalNext > 0) {
				if (indexImgNext < totalNext) {
					ivImage.setBackground(null);
					rbResponseVideo.setRating((float) 0.0);
					new ImageLoaderAsyncTask(ivImage,
							(int) (Config.screenHeight * 0.5), false)
							.execute(videoList.get(indexImgNext).getUrlThumb());
					setTitle(String.format(
							context.getResources().getString(
									R.string.dialog_response_title),
							indexImgNext + 1, totalNext));
					setId(videoList.get(indexImgNext).getId());
					setIndex(indexImgNext);
					setType("1");
					if (videoList.get(indexImgNext).isWin()) {
						iv_winner.setVisibility(View.VISIBLE);
						showIcSetWinner(false);
						// setWined(true);
					} else {
						iv_winner.setVisibility(View.GONE);
						showIcSetWinner(false);
						// setWined(true);
					}
					checkVisibleButton(isRated);
					setRating(videoList.get(indexImgNext).getRate());
					if (isRated) {
						setMessage(context.getResources().getString(
								R.string.dialog_response_message_rated));
					} else {
						setMessage(context.getResources().getString(
								R.string.dialog_response_message_not_rate_yet));
						setRatingBar(isRated);
					}
					setCommentList(videoList.get(indexImgNext).getCommentList());
					setUrlThumb(videoList.get(indexImgNext).getUrlThumb());
					setUrlVideo(videoList.get(indexImgNext).getUrlVideo());
				}
			}
			break;
		case R.id.iv_prev:
			int totalPrev = videoList.size();
			int indexImgPrev = index - 1;
			if (totalPrev > 0) {
				if (indexImgPrev > -1) {
					ivImage.setBackground(null);
					rbResponseVideo.setRating((float) 0.0);
					new ImageLoaderAsyncTask(ivImage,
							(int) (Config.screenHeight * 0.5), false)
							.execute(videoList.get(indexImgPrev).getUrlThumb());
					setTitle(String.format(
							context.getResources().getString(
									R.string.dialog_response_title),
							indexImgPrev + 1, totalPrev));
					setId(videoList.get(indexImgPrev).getId());
					setIndex(indexImgPrev);
					setType("1");

					if (videoList.get(indexImgPrev).isWin()) {
						iv_winner.setVisibility(View.VISIBLE);
						showIcSetWinner(false);
						// setWined(true);
					} else {
						iv_winner.setVisibility(View.GONE);
						showIcSetWinner(false);
						// setWined(true);
					}

					checkVisibleButton(isRated);
					setRating(videoList.get(indexImgPrev).getRate());
					if (isRated) {
						setMessage(context.getResources().getString(
								R.string.dialog_response_message_rated));
					} else {
						setMessage(context.getResources().getString(
								R.string.dialog_response_message_not_rate_yet));
						setRatingBar(isRated);
					}
					setCommentList(videoList.get(indexImgPrev).getCommentList());
					setUrlThumb(videoList.get(indexImgPrev).getUrlThumb());
					setUrlVideo(videoList.get(indexImgPrev).getUrlVideo());
				}
			}
			break;
		case R.id.iv_play:
			Intent i = new Intent(context, VideoActivity.class);
			i.putExtra("url", urlVideo);
			context.startActivity(i);
			break;
		case R.id.iv_set_winner_video:
			if (isWined == true) {
				showIcWinner();
			} else {

				new WinChallengeAsyncstask(activity, idchal, videoList.get(
						index).getUserID(), videoList.get(index).getId(), "2")
						.execute();
			}
			// if (iv_winner.getVisibility() == View.GONE) {
			// ivPlay.setVisibility(View.GONE);
			// iv_winner.setVisibility(View.VISIBLE);
			// } else {
			// ivPlay.setVisibility(View.VISIBLE);
			// iv_winner.setVisibility(View.GONE);
			// }
			break;

		case R.id.iv_winner_video:
			if (iv_winner.getVisibility() == View.VISIBLE) {
				ivPlay.setVisibility(View.VISIBLE);
				iv_winner.setVisibility(View.GONE);
			}
		default:
			break;
		}
	}

}

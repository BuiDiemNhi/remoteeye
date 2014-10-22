package remoteeyes.bsp.vn.asynctask;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import remoteeyes.bsp.vn.DetailChallengeActivity;
import remoteeyes.bsp.vn.MyAreaActivity;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.common.JSONParser;
import remoteeyes.bsp.vn.fragments.DetailChallengeFragment;
import remoteeyes.bsp.vn.model.ChallengeObject;
import remoteeyes.bsp.vn.model.CommentObject;
import remoteeyes.bsp.vn.model.ImageObject;
import remoteeyes.bsp.vn.model.LocationObject;
import remoteeyes.bsp.vn.model.ResponsePhotoObject;
import remoteeyes.bsp.vn.model.ResponseVideoObject;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class LoadDetailChallengeAsynctask extends
		AsyncTask<String, Void, String> {
	Context context;
	ProgressDialog dialog;
	ChallengeObject challengeObject;
	int winner_id;
	int type_win;
	int media_id;

	public LoadDetailChallengeAsynctask(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		dialog = new ProgressDialog(context);
		challengeObject = new ChallengeObject();
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		dialog.setMessage("Loading....");
		dialog.show();
	}

	// run UI alter
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		try {
			if (dialog.isShowing())
				dialog.dismiss();
		} catch (Exception ex) {
		}
		challengeObject = new ChallengeObject();
		try {
			JSONObject jsonObj = new JSONObject(result);
			JSONObject jsonChallenge = jsonObj.getJSONObject("result");
			challengeObject.setAccept(jsonChallenge.getInt("accept"));
			challengeObject.setAvatar(jsonChallenge.getString("author_avatar"));
			challengeObject.setIsShare(jsonChallenge.getInt("is_shared"));
			challengeObject.setCategory(jsonChallenge.getInt("type_challenge"));
			challengeObject.setIspaid(jsonChallenge.getInt("is_paid"));

			winner_id = jsonChallenge.getInt("winner_id");
			type_win = jsonChallenge.getInt("type_win");
			media_id = jsonChallenge.getInt("media_id");

			challengeObject.setWinner_id(winner_id);
			challengeObject.setType_win(type_win);
			challengeObject.setMedia_id(media_id);

			jsonChallenge.getString("dateEdited");
			challengeObject.setStarting_on_year(Integer.parseInt(jsonChallenge
					.getString("dateStart").substring(0, 4)));
			challengeObject.setStarting_on_month(Integer.parseInt(jsonChallenge
					.getString("dateStart").substring(5, 7)));
			challengeObject.setStarting_on_day(Integer.parseInt(jsonChallenge
					.getString("dateStart").substring(8, 10)));
			challengeObject.setStarting_on_hour(Integer.parseInt(jsonChallenge
					.getString("dateStart").substring(11, 13)));
			challengeObject.setStarting_on_minute(Integer
					.parseInt(jsonChallenge.getString("dateStart").substring(
							14, 16)));
			challengeObject.setStarting_on_second(0);
			challengeObject.setDescription(jsonChallenge
					.getString("description"));

			String[] duration = jsonChallenge.getString("duration").split(":");
			challengeObject.setDuration_day(Integer.parseInt(duration[0]));
			challengeObject.setDuration_hour(Integer.parseInt(duration[1]));
			challengeObject.setDuration_minute(Integer.parseInt(duration[2]));

			challengeObject.setiPublic(Integer.parseInt(jsonChallenge
					.getString("public")));
			challengeObject.setReward(Integer.parseInt(jsonChallenge
					.getString("reward")));
			challengeObject.setTitle(jsonChallenge.getString("title"));
			challengeObject.setTotalImage(jsonChallenge.getInt("totalImage"));
			challengeObject
					.setCountAccept(jsonChallenge.getInt("count_accept"));

			JSONArray jarrLocation = jsonChallenge.getJSONArray("locations");
			ArrayList<LocationObject> locationList = new ArrayList<LocationObject>();
			for (int i = 0; i < jarrLocation.length(); i++) {
				LocationObject location = new LocationObject();
				JSONObject jsonLocation = jarrLocation.getJSONObject(i);
				location.setId(jsonLocation.getString("ID"));
				location.setAddress(jsonLocation.getString("address"));
				location.setChallengeId(jsonLocation.getString("challenge_ID"));
				location.setArea(jsonLocation.getString("area"));
				location.setLat(Double.parseDouble(jsonLocation
						.getString("lat")));
				location.setLng(Double.parseDouble(jsonLocation
						.getString("lng")));
				location.updateDistance();
				locationList.add(location);
			}
			challengeObject.setId(locationList.get(0).getChallengeId());
			challengeObject.setLocationsList(locationList);

			JSONArray jarrExpImage = jsonChallenge.getJSONArray("image");
			ArrayList<ImageObject> imagesList = new ArrayList<ImageObject>();
			for (int i = 0; i < jarrExpImage.length(); i++) {
				JSONObject jsonImage = jarrExpImage.getJSONObject(i);
				ImageObject img = new ImageObject();
				img.setUrlImage(jsonImage.getString("image"));
				img.setId(jsonImage.getString("ID"));

				// Comment List
				ArrayList<CommentObject> commentList = new ArrayList<CommentObject>();
				JSONArray jarrComment = jsonImage.getJSONArray("comment");
				for (int j = 0; j < jarrComment.length(); j++) {
					JSONObject jsonComment = jarrComment.getJSONObject(j);
					CommentObject commentObj = new CommentObject();
					commentObj.setId(jsonComment.getString("ID"));
					commentObj.setUrlAvatar(jsonComment.getString("avatar"));
					commentObj.setMessage(jsonComment.getString("content"));
					commentObj.setUserId(jsonComment.getString("userID"));
					commentList.add(commentObj);
					if (commentObj.getUserId().equals(Config.IdUser)) {
						commentObj.setMine(true);
					} else {
						commentObj.setMine(false);
					}
				}
				img.setCommentList(commentList);
				imagesList.add(img);
			}
			challengeObject.setImagesObjectList(imagesList);

			JSONArray jarrResponsePhoto = jsonChallenge.getJSONArray("photo");
			ArrayList<ResponsePhotoObject> photoList = new ArrayList<ResponsePhotoObject>();
			if (winner_id == 0 || type_win == 2) {
				for (int i = 0; i < jarrResponsePhoto.length(); i++) {
					ResponsePhotoObject obj = new ResponsePhotoObject();
					JSONObject jsonPhoto = jarrResponsePhoto.getJSONObject(i);

					obj.setId(jsonPhoto.getString("ID"));
					obj.setRate(Integer.parseInt(jsonPhoto.getString("rating")));
					obj.setRated(jsonPhoto.getString("rated").equals("0") ? false
							: true);
					obj.setUrlImage(jsonPhoto.getString("image"));
					obj.setUserID(jsonPhoto.getString("userID"));
					obj.setWin(false);

					// Comment List
					ArrayList<CommentObject> commentList = new ArrayList<CommentObject>();
					JSONArray jarrComment = jsonPhoto.getJSONArray("comment");
					for (int j = 0; j < jarrComment.length(); j++) {
						JSONObject jsonComment = jarrComment.getJSONObject(j);
						CommentObject commentObj = new CommentObject();
						commentObj.setId(jsonComment.getString("ID"));
						commentObj
								.setUrlAvatar(jsonComment.getString("avatar"));
						commentObj.setMessage(jsonComment.getString("content"));
						commentObj.setUserId(jsonComment.getString("userID"));
						commentList.add(commentObj);
						if (commentObj.getUserId().equals(Config.IdUser)) {
							commentObj.setMine(true);
						} else {
							commentObj.setMine(false);
						}
					}
					obj.setCommentList(commentList);

					photoList.add(obj);
				}
			} else if (type_win == 1) {
				for (int i = 0; i < jarrResponsePhoto.length(); i++) {
					ResponsePhotoObject obj = new ResponsePhotoObject();
					JSONObject jsonPhoto = jarrResponsePhoto.getJSONObject(i);
					if (media_id != Integer.parseInt(jsonPhoto.getString("ID"))) {
						obj.setId(jsonPhoto.getString("ID"));
						obj.setRate(Integer.parseInt(jsonPhoto
								.getString("rating")));
						obj.setRated(jsonPhoto.getString("rated").equals("0") ? false
								: true);
						obj.setUrlImage(jsonPhoto.getString("image"));
						obj.setUserID(jsonPhoto.getString("userID"));
						obj.setWin(false);
					} else {
						obj.setId(jsonPhoto.getString("ID"));
						obj.setRate(Integer.parseInt(jsonPhoto
								.getString("rating")));
						obj.setRated(jsonPhoto.getString("rated").equals("0") ? false
								: true);
						obj.setUrlImage(jsonPhoto.getString("image"));
						obj.setUserID(jsonPhoto.getString("userID"));
						obj.setWin(true);
					}

					// Comment List
					ArrayList<CommentObject> commentList = new ArrayList<CommentObject>();
					JSONArray jarrComment = jsonPhoto.getJSONArray("comment");
					for (int j = 0; j < jarrComment.length(); j++) {
						JSONObject jsonComment = jarrComment.getJSONObject(j);
						CommentObject commentObj = new CommentObject();
						commentObj.setId(jsonComment.getString("ID"));
						commentObj
								.setUrlAvatar(jsonComment.getString("avatar"));
						commentObj.setMessage(jsonComment.getString("content"));
						commentObj.setUserId(jsonComment.getString("userID"));
						commentList.add(commentObj);
						if (commentObj.getUserId().equals(Config.IdUser)) {
							commentObj.setMine(true);
						} else {
							commentObj.setMine(false);
						}
					}
					obj.setCommentList(commentList);

					photoList.add(obj);
				}
			}
			challengeObject.setResponsePhotoList(photoList);

			JSONArray jarrResponseVideo = jsonChallenge.getJSONArray("video");
			ArrayList<ResponseVideoObject> videoList = new ArrayList<ResponseVideoObject>();
			if (winner_id == 0 || type_win == 1) {
				for (int i = 0; i < jarrResponseVideo.length(); i++) {
					ResponseVideoObject obj = new ResponseVideoObject();
					JSONObject jsonVideo = jarrResponseVideo.getJSONObject(i);
					obj.setId(jsonVideo.getString("ID"));
					obj.setRate(Integer.parseInt(jsonVideo.getString("rating")));
					obj.setRated(jsonVideo.getString("rated").equals("0") ? false
							: true);
					obj.setUrlVideo(jsonVideo.getString("video"));
					obj.setDuration(jsonVideo.getString("duration"));
					obj.setUrlThumb(jsonVideo.getString("thumb"));
					obj.setUserID(jsonVideo.getString("userID"));
					obj.setWin(false);

					// Comment List
					ArrayList<CommentObject> commentList = new ArrayList<CommentObject>();
					JSONArray jarrComment = jsonVideo.getJSONArray("comment");
					for (int j = 0; j < jarrComment.length(); j++) {
						JSONObject jsonComment = jarrComment.getJSONObject(j);
						CommentObject commentObj = new CommentObject();
						commentObj.setId(jsonComment.getString("ID"));
						commentObj
								.setUrlAvatar(jsonComment.getString("avatar"));
						commentObj.setMessage(jsonComment.getString("content"));
						commentObj.setUserId(jsonComment.getString("user_id"));
						commentList.add(commentObj);
						if (commentObj.getUserId().equals(Config.IdUser)) {
							commentObj.setMine(true);
						} else {
							commentObj.setMine(false);
						}
					}
					obj.setCommentList(commentList);
					videoList.add(obj);
				}
			} else if (type_win == 2) {
				for (int i = 0; i < jarrResponseVideo.length(); i++) {

					ResponseVideoObject obj = new ResponseVideoObject();
					JSONObject jsonVideo = jarrResponseVideo.getJSONObject(i);
					if (media_id != Integer.parseInt(jsonVideo.getString("ID"))) {
						obj.setId(jsonVideo.getString("ID"));
						obj.setRate(Integer.parseInt(jsonVideo
								.getString("rating")));
						obj.setRated(jsonVideo.getString("rated").equals("0") ? false
								: true);
						obj.setUrlVideo(jsonVideo.getString("video"));
						obj.setDuration(jsonVideo.getString("duration"));
						obj.setUrlThumb(jsonVideo.getString("thumb"));
						obj.setUserID(jsonVideo.getString("userID"));
						obj.setWin(false);
					} else {
						obj.setId(jsonVideo.getString("ID"));
						obj.setRate(Integer.parseInt(jsonVideo
								.getString("rating")));
						obj.setRated(jsonVideo.getString("rated").equals("0") ? false
								: true);
						obj.setUrlVideo(jsonVideo.getString("video"));
						obj.setDuration(jsonVideo.getString("duration"));
						obj.setUrlThumb(jsonVideo.getString("thumb"));
						obj.setUserID(jsonVideo.getString("userID"));
						obj.setWin(true);
					}

					// Comment List
					ArrayList<CommentObject> commentList = new ArrayList<CommentObject>();
					JSONArray jarrComment = jsonVideo.getJSONArray("comment");
					for (int j = 0; j < jarrComment.length(); j++) {
						JSONObject jsonComment = jarrComment.getJSONObject(j);
						CommentObject commentObj = new CommentObject();
						commentObj.setId(jsonComment.getString("ID"));
						commentObj
								.setUrlAvatar(jsonComment.getString("avatar"));
						commentObj.setMessage(jsonComment.getString("content"));
						commentObj.setUserId(jsonComment.getString("user_id"));
						commentList.add(commentObj);
						if (commentObj.getUserId().equals(Config.IdUser)) {
							commentObj.setMine(true);
						} else {
							commentObj.setMine(false);
						}
					}
					obj.setCommentList(commentList);
					videoList.add(obj);
				}
			}

			challengeObject.setResponseVideoList(videoList);

			challengeObject.setInterval(jsonChallenge.getString("interval"));
			challengeObject.setIsExpired(jsonChallenge.getInt("is_expired"));
			int countAvatar = challengeObject.getImagesObjectList().size();
			int countPhoto = challengeObject.getResponsePhotoList().size();
			int countVideo = challengeObject.getResponseVideoList().size();
			challengeObject
					.setCountImage(countAvatar + countPhoto + countVideo);
			challengeObject.setRemainStart(jsonChallenge
					.getString("remain_start"));
			if (Config.EMAILUSER
					.equals(jsonChallenge.getString("author_email"))) {
				challengeObject.setMine(true);
			} else {
				challengeObject.setMine(false);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (challengeObject != null) {
			try {
				((MyAreaActivity) context).detailChallengeFragment
						.loadChallenge(challengeObject);
			} catch (Exception ex) {
				// ex.printStackTrace();
			}
			try {
				((DetailChallengeActivity) context).detailChallengeFragment
						.loadChallenge(challengeObject);
				// ((DetailChallengeActivity) context).loadCountChallenge();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		JSONParser jsonParser = new JSONParser();

		String url = String.format(Config.GET_DETAIL_CHALLENGE, params[0],
				Config.IdUser);
		JSONObject jsonObject = jsonParser.getJSONFromUrl(url);

		return jsonObject.toString();
	}
}

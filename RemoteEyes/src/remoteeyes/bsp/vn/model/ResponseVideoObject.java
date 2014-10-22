package remoteeyes.bsp.vn.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ResponseVideoObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private boolean isRated;
	private float rate;
	private ArrayList<CommentObject> commentList;
	private String urlVideo;
	private String duration;
	private String urlThumb;
	private boolean isWin;
	private String userID;

	public boolean isWin() {
		return isWin;
	}

	public void setWin(boolean isWin) {
		this.isWin = isWin;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getUrlThumb() {
		return urlThumb;
	}

	public void setUrlThumb(String urlThumb) {
		this.urlThumb = urlThumb;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isRated() {
		return isRated;
	}

	public void setRated(boolean isRated) {
		this.isRated = isRated;
	}

	public float getRate() {
		return rate;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}

	public ArrayList<CommentObject> getCommentList() {
		return commentList;
	}

	public void setCommentList(ArrayList<CommentObject> commentList) {
		this.commentList = commentList;
	}

	public String getUrlVideo() {
		return urlVideo;
	}

	public void setUrlVideo(String urlVideo) {
		this.urlVideo = urlVideo;
	}
}

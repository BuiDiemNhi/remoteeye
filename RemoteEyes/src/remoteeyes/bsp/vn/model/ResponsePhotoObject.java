package remoteeyes.bsp.vn.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ResponsePhotoObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private boolean isRated;
	private float rate;
	private boolean isWin;
	private String userID;

	private ArrayList<CommentObject> commentList;
	private String urlImage;

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public boolean isWin() {
		return isWin;
	}

	public void setWin(boolean isWin) {
		this.isWin = isWin;
	}

	public String getUrlImage() {
		return urlImage;
	}

	public void setUrlImage(String urlImage) {
		this.urlImage = urlImage;
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

}

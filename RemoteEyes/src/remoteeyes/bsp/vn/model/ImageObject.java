package remoteeyes.bsp.vn.model;

import java.util.ArrayList;

public class ImageObject {

	private String id;
	private ArrayList<CommentObject> commentList;
	private String urlImage;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public ArrayList<CommentObject> getCommentList() {
		return commentList;
	}
	public void setCommentList(ArrayList<CommentObject> commentList) {
		this.commentList = commentList;
	}
	public String getUrlImage() {
		return urlImage;
	}
	public void setUrlImage(String urlImage) {
		this.urlImage = urlImage;
	}
}

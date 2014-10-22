package remoteeyes.bsp.vn.model;

import java.util.ArrayList;

public class Comment {

	public String idUserCommet;
	public String content;
	public String urlAvatar;
	public ArrayList<CommentObject> commentList;
	
	public String getIdUserCommet() {
		return idUserCommet;
	}
	public void setIdUserCommet(String idUserCommet) {
		this.idUserCommet = idUserCommet;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getUrlAvatar() {
		return urlAvatar;
	}
	public void setUrlAvatar(String urlAvatar) {
		this.urlAvatar = urlAvatar;
	}
	public ArrayList<CommentObject> getCommentList() {
		return commentList;
	}
	public void setCommentList(ArrayList<CommentObject> commentList) {
		this.commentList = commentList;
	}
	
	
}

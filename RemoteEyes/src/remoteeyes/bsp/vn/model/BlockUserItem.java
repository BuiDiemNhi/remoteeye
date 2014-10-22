package remoteeyes.bsp.vn.model;

import java.util.ArrayList;

public class BlockUserItem {
	private String Id;
	private String name;
	private String email;
	private String userID;
	private String avatar;
	private String status;
	private ArrayList<LinkedProfileItem> linkedProfileList = new ArrayList<LinkedProfileItem>();

	public BlockUserItem() {
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ArrayList<LinkedProfileItem> getLinkedProfileList() {
		return linkedProfileList;
	}

	public void setLinkedProfileList(
			ArrayList<LinkedProfileItem> linkedProfileList) {
		this.linkedProfileList = linkedProfileList;
	}

}

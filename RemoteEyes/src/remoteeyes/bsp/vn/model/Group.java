package remoteeyes.bsp.vn.model;

import java.util.ArrayList;

public class Group {

	private String id;
	private String name;
	private ArrayList<Friend> friendOfGroupList = new ArrayList<Friend>();
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<Friend> getFriendOfGroupList() {
		return friendOfGroupList;
	}
	public void setFriendOfGroupList(ArrayList<Friend> friendOfGroupList) {
		this.friendOfGroupList = friendOfGroupList;
	}
}

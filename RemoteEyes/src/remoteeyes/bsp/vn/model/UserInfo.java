package remoteeyes.bsp.vn.model;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class UserInfo {

	private String id;
	private String email;
	private String email_paypal;
	private String gender;
	private int birth_year;
	private int birth_month;
	private int birth_day;
	private String avatar;
	private String name;
	private float balance;
	private boolean iSocial;
	private int id_email_other;
	private boolean isdontShow;
	private String LocInfo;

	
	public boolean isIsdontShow() {
		return isdontShow;
	}

	public void setIsdontShow(boolean isdontShow) {
		this.isdontShow = isdontShow;
	}

	private ArrayList<BlockUserItem> block_list = new ArrayList<BlockUserItem>();
	private ArrayList<OtherEmailItem> othersEmail = new ArrayList<OtherEmailItem>();
	private ArrayList<LinkedProfileItem> linkedProfileItem = new ArrayList<LinkedProfileItem>();
	private ArrayList<PaymentItem> paymentList = new ArrayList<PaymentItem>();

	private int total_posted;
	private int total_responded;
	private int total_images;
	private int total_rating_image;
	private int total_video;
	private int total_rating_video;

	public ArrayList<LinkedProfileItem> getLinkedProfileItem() {
		return linkedProfileItem;
	}

	public void setLinkedProfileItem(
			ArrayList<LinkedProfileItem> linkedProfileItem) {
		this.linkedProfileItem = linkedProfileItem;
	}

	public static void setInstance(UserInfo instance) {
		UserInfo.instance = instance;
	}

	public ArrayList<BlockUserItem> getBlock_list() {
		return block_list;
	}

	public void setBlock_list(ArrayList<BlockUserItem> block_list) {
		this.block_list = block_list;
	}

	public void setOthersEmail(ArrayList<OtherEmailItem> othersEmail) {
		this.othersEmail = othersEmail;
	}

	public ArrayList<OtherEmailItem> getOthersEmail() {
		return othersEmail;
	}

	public ArrayList<PaymentItem> getPaymentList() {
		return paymentList;
	}

	public void setPaymentList(ArrayList<PaymentItem> paymentList) {
		this.paymentList = paymentList;
	}

	public int getId_email_other() {
		return id_email_other;
	}

	public void setId_email_other(int id_email_other) {
		this.id_email_other = id_email_other;
	}

	public boolean isiSocial() {
		return iSocial;
	}

	public void setiSocial(boolean iSocial) {
		this.iSocial = iSocial;
	}

	private static UserInfo instance = new UserInfo();

	public static UserInfo getInstance() {
		return instance;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail_paypal() {
		return email_paypal;
	}

	public void setEmail_paypal(String email_paypal) {
		this.email_paypal = email_paypal;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(int gender) {
		if (gender == 0)
			this.gender = "female";
		else if (gender == 1)
			this.gender = "male";
		else if (gender == 2)
			this.gender = "Null";
	}

	public int getBirth_year() {
		return birth_year;
	}

	public void setBirth_year(int birth_year) {
		this.birth_year = birth_year;
	}

	public int getBirth_month() {
		return birth_month;
	}

	public void setBirth_month(int birth_month) {
		this.birth_month = birth_month;
	}

	public int getBirth_day() {
		return birth_day;
	}

	public void setBirth_day(int birth_day) {
		this.birth_day = birth_day;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getBalance() {
		return balance;
	}

	public void setBalance(float balance) {
		this.balance = balance;
	}

	public String getBirthday() {
		if (birth_year == 0)
			return "Null";
		return birth_year + "-" + String.format("%02d", birth_month) + "-"
				+ String.format("%02d", birth_day);
	}

	public int getTotal_posted() {
		return total_posted;
	}

	public void setTotal_posted(int total_posted) {
		this.total_posted = total_posted;
	}

	public int getTotal_responded() {
		return total_responded;
	}

	public void setTotal_responded(int total_responded) {
		this.total_responded = total_responded;
	}

	public int getTotal_images() {
		return total_images;
	}

	public void setTotal_images(int total_images) {
		this.total_images = total_images;
	}

	public int getTotal_rating_image() {
		return total_rating_image;
	}

	public void setTotal_rating_image(int total_rating_image) {
		this.total_rating_image = total_rating_image;
	}

	public int gettotal_video() {
		return total_video;
	}

	public void setTotal_video(int total_video) {
		this.total_video = total_video;
	}

	public int gettotal_rating_video() {
		return total_rating_video;
	}

	public void settotal_rating_video(int total_rating_video) {
		this.total_rating_video = total_rating_video;
	}
}

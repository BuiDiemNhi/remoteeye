package remoteeyes.bsp.vn.model;

import java.io.Serializable;
import java.util.ArrayList;

import android.R.bool;
import android.provider.ContactsContract.CommonDataKinds.Im;

public class ChallengeObject implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private String title;
	private int starting_on_year;
	private int starting_on_month;
	private int starting_on_day;
	private int starting_on_hour;
	private int starting_on_minute;
	private int starting_on_second;
	private int duration_day;
	private int duration_hour;
	private int duration_minute;
	private int reward;
	private int category;
	private String description;
	private int iPublic = 1;
	private int accept;
	private String avatar;
	private int totalImage;
	public ArrayList<String> imagesList;
	public ArrayList<ImageObject> imagesObjectList;
	public ArrayList<LocationObject> locationsList = new ArrayList<LocationObject>();
	private ArrayList<ResponsePhotoObject> responsePhotoList = new ArrayList<ResponsePhotoObject>();
	private ArrayList<ResponseVideoObject> responseVideoList = new ArrayList<ResponseVideoObject>();
	private boolean isMine;
	private String email;
	private String name;
	private int countAccept;
	private int isShare;
	private String interval;
	private int isExpired;
	private int countImage;
	private String remainStart;
	private int ispaid;
	private int media_id;
	private int type_win;
	private int winner_id;

	public int getWinner_id() {
		return winner_id;
	}

	public void setWinner_id(int winner_id) {
		this.winner_id = winner_id;
	}

	public int getMedia_id() {
		return media_id;
	}

	public void setMedia_id(int media_id) {
		this.media_id = media_id;
	}

	public int getType_win() {
		return type_win;
	}

	public void setType_win(int type_win) {
		this.type_win = type_win;
	}

	public int getIspaid() {
		return ispaid;
	}

	public void setIspaid(int ispaid) {
		this.ispaid = ispaid;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getCountImage() {
		return countImage;
	}

	public void setCountImage(int countImage) {
		this.countImage = countImage;
	}

	public String getRemainStart() {
		if (!remainStart.equals("")) {
			String result = "";
			String[] tmp = remainStart.split(" ");
			if (!tmp[0].equals("")) {
				result = tmp[0] + " " + tmp[1];
			} else {
				result = remainStart;
			}
			return result;
		} else
			return remainStart;
	}

	public void setRemainStart(String remainStart) {
		this.remainStart = remainStart;
	}

	public String getInterval() {
		String result = "";
		String[] tmp = interval.split(" ");
		if (!tmp[0].equals("")) {
			result = tmp[0] + " " + tmp[1];
		} else {
			result = interval;
		}
		return result;
	}

	public void setInterval(String interval) {
		this.interval = interval;
	}

	public int getIsExpired() {
		return isExpired;
	}

	public void setIsExpired(int isExpired) {
		this.isExpired = isExpired;
	}

	public boolean isGlobal() {
		if (locationsList.get(0).getLat() == 0
				&& locationsList.get(0).getLng() == 0)
			return true;
		else
			return false;
	}

	public boolean isArea() {
		if (!locationsList.get(0).getArea().equals(""))
			return true;
		else
			return false;
	}

	public int getCountAccept() {
		return countAccept;
	}

	public void setCountAccept(int countAccept) {
		this.countAccept = countAccept;
	}

	public String getEmail() {
		return email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isMine() {
		return isMine;
	}

	public void setMine(boolean isMine) {
		this.isMine = isMine;
	}

	public ArrayList<ResponsePhotoObject> getResponsePhotoList() {
		return responsePhotoList;
	}

	public void setResponsePhotoList(
			ArrayList<ResponsePhotoObject> responsePhotoList) {
		this.responsePhotoList = responsePhotoList;
	}

	public ArrayList<ResponseVideoObject> getResponseVideoList() {
		return responseVideoList;
	}

	public void setResponseVideoList(
			ArrayList<ResponseVideoObject> responseVideoList) {
		this.responseVideoList = responseVideoList;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public int getTotalImage() {
		return totalImage;
	}

	public void setTotalImage(int totalImage) {
		this.totalImage = totalImage;
	}

	public int getAccept() {
		return accept;
	}

	public void setAccept(int accept) {
		this.accept = accept;
	}

	public ArrayList<String> getImagesList() {
		return imagesList;
	}

	public void setImagesList(ArrayList<String> imagesList) {
		this.imagesList = imagesList;
	}

	public ArrayList<ImageObject> getImagesObjectList() {
		return imagesObjectList;
	}

	public void setImagesObjectList(ArrayList<ImageObject> imagesObjectList) {
		this.imagesObjectList = imagesObjectList;
	}

	public ArrayList<LocationObject> getLocationsList() {
		return locationsList;
	}

	public void setLocationsList(ArrayList<LocationObject> locationsList) {
		this.locationsList = locationsList;
	}

	public int getiPublic() {
		return iPublic;
	}

	public void setiPublic(int iPublic) {
		this.iPublic = iPublic;
	}

	public ChallengeObject() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getStarting_on_year() {
		return starting_on_year;
	}

	public void setStarting_on_year(int starting_on_year) {
		this.starting_on_year = starting_on_year;
	}

	public int getStarting_on_month() {
		return starting_on_month;
	}

	public void setStarting_on_month(int starting_on_month) {
		this.starting_on_month = starting_on_month;
	}

	public int getStarting_on_day() {
		return starting_on_day;
	}

	public void setStarting_on_day(int starting_on_day) {
		this.starting_on_day = starting_on_day;
	}

	public int getStarting_on_hour() {
		return starting_on_hour;
	}

	public void setStarting_on_hour(int starting_on_hour) {
		this.starting_on_hour = starting_on_hour;
	}

	public int getStarting_on_minute() {
		return starting_on_minute;
	}

	public void setStarting_on_minute(int starting_on_minute) {
		this.starting_on_minute = starting_on_minute;
	}

	public int getStarting_on_second() {
		return starting_on_second;
	}

	public void setStarting_on_second(int starting_on_second) {
		this.starting_on_second = starting_on_second;
	}

	public int getDuration_day() {
		return duration_day;
	}

	public void setDuration_day(int duration_day) {
		this.duration_day = duration_day;
	}

	public int getDuration_hour() {
		return duration_hour;
	}

	public void setDuration_hour(int duration_hour) {
		this.duration_hour = duration_hour;
	}

	public int getDuration_minute() {
		return duration_minute;
	}

	public void setDuration_minute(int duration_minute) {
		this.duration_minute = duration_minute;
	}

	public int getReward() {
		return reward;
	}

	public void setReward(int reward) {
		this.reward = reward;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDuration() {
		String result = "";
		if (duration_day > 0)
			result += String.format("%2d", duration_day) + "d";
		if (duration_hour > 0)
			result += String.format("%2d", duration_hour) + "h";
		if (duration_minute > 0)
			result += String.format("%2d", duration_minute) + "m";
		return result;
	}

	public String getStartTime() {
		return starting_on_year + "-"
				+ String.format("%02d", starting_on_month) + "-"
				+ String.format("%02d", starting_on_day) + " "
				+ String.format("%02d", starting_on_hour) + ":"
				+ String.format("%02d", starting_on_minute);
	}

	public String getLastestLocation() {
		LocationObject lo = locationsList.get(0);
		String result = "";
		if (lo.getAddress() != null && !lo.getAddress().equals(""))
			result += lo.getAddress();
		else
			result += lo.getArea();
		return result;
	}

	public int getIsShare() {
		return isShare;
	}

	public void setIsShare(int isShare) {
		this.isShare = isShare;
	}
}

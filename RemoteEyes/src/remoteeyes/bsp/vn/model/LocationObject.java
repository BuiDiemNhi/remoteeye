package remoteeyes.bsp.vn.model;

import java.io.Serializable;

import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.uilt.GPSTracker;

public class LocationObject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String address;
	private String area;
	private double lat;
	private double lng;
	private String challenge_id;
	private int distance;
	
	public int getDistance() {
		return distance;
	}
	public void updateDistance() {
		distance =  GPSTracker.distance(Config.lat, Config.lng, lat, lng);
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getChallengeId() {
		return challenge_id;
	}
	public void setChallengeId(String challenge_id) {
		this.challenge_id = challenge_id;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
}

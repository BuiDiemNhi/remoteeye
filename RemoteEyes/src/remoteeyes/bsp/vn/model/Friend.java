package remoteeyes.bsp.vn.model;

import android.graphics.Bitmap;

public class Friend {

	private String id;
	private String avatarUrl;
	private Bitmap contactPicture;
	private String name;
	private String email;
	private boolean stateBlock;
	private boolean isFacebook;
	private boolean isTwitter;
	private boolean isGoogle;
	private boolean isInstagram;
	private boolean isLinkedIn;
	private boolean isYoutube;
	private boolean isSelect;
	private String idSocial;

	// private boolean idTwitter;
	// private boolean idGoogle;
	// private boolean idInstagram;
	// private boolean idLinkedIn;
	// private boolean idYoutube;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public Bitmap getContactPicture() {
		return contactPicture;
	}

	public void setContactPicture(Bitmap contactPicture) {
		this.contactPicture = contactPicture;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean getStateBlock() {
		return stateBlock;
	}

	public void setStateBlock(boolean stateBlock) {
		this.stateBlock = stateBlock;
	}

	public boolean isFacebook() {
		return isFacebook;
	}

	public void setFacebook(boolean isFacebook) {
		this.isFacebook = isFacebook;
	}

	public boolean isTwitter() {
		return isTwitter;
	}

	public void setTwitter(boolean isTwitter) {
		this.isTwitter = isTwitter;
	}

	public boolean isGoogle() {
		return isGoogle;
	}

	public void setGoogle(boolean isGoogle) {
		this.isGoogle = isGoogle;
	}

	public boolean isInstagram() {
		return isInstagram;
	}

	public void setInstagram(boolean isInstagram) {
		this.isInstagram = isInstagram;
	}

	public boolean isLinkedIn() {
		return isLinkedIn;
	}

	public void setLinkedIn(boolean isLinkedIn) {
		this.isLinkedIn = isLinkedIn;
	}

	public boolean isYoutube() {
		return isYoutube;
	}

	public void setYoutube(boolean isYoutube) {
		this.isYoutube = isYoutube;
	}

	public boolean getSelect() {
		return isSelect;
	}

	public void setSelect(Boolean isSelect) {
		this.isSelect = isSelect;
	}

	public String getIdSocial() {
		return idSocial;
	}

	public void setIdSocial(String idSocial) {
		this.idSocial = idSocial;
	}

}

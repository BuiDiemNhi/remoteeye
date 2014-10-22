package remoteeyes.bsp.vn.model;

public class HistoryPaypalItem {
	private String id;
	private String paypal_id;
	private String id_user;
	private String date_created;
	private String title_challenge;
	private String currency_code;
	private int type;
	private int quantity;
	private float total_amount;

	public HistoryPaypalItem() {
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPaypal_id() {
		return paypal_id;
	}

	public void setPaypal_id(String paypal_id) {
		this.paypal_id = paypal_id;
	}

	public String getDate_created() {
		return date_created;
	}

	public void setDate_created(String date_created) {
		this.date_created = date_created;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getTitle_challenge() {
		return title_challenge;
	}

	public void setTitle_challenge(String title_challenge) {
		this.title_challenge = title_challenge;
	}

	public String getId_user() {
		return id_user;
	}

	public void setId_user(String id_user) {
		this.id_user = id_user;
	}

	public String getCurrency_code() {
		return currency_code;
	}

	public void setCurrency_code(String currency_code) {
		this.currency_code = currency_code;
	}

	public float getTotal_amount() {
		return total_amount;
	}

	public void setTotal_amount(float total_amount) {
		this.total_amount = total_amount;
	}

}

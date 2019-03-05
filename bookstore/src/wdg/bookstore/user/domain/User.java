package wdg.bookstore.user.domain;

public class User {
	private String uid;//Ö÷¼ü
	private String username;
	private String password;
	private String email;
	private String code;//¼¤»îÂë
	private boolean state;//×´Ì¬
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public boolean isState() {
		return state;
	}
	public void setState(boolean state) {
		this.state = state;
	}
	@Override
	public String toString() {
		return "User [code=" + code + ", email=" + email + ", password=" + password
				+ ", state=" + state + ", uid=" + uid + ", username=" + username
				+ "]";
	}

}

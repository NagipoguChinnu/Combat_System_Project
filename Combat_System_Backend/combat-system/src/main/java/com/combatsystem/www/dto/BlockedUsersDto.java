package com.combatsystem.www.dto;

public class BlockedUsersDto {

	private Long id;
	private String userName;
	private String email;
	private String deviceType;
	private double latiude;
	private double longitude;
	private String role;
	public BlockedUsersDto(Long id, String userName, String email, String deviceType, double latiude, double longitude,
			String role) {
		super();
		this.id = id;
		this.userName = userName;
		this.email = email;
		this.deviceType = deviceType;
		this.latiude = latiude;
		this.longitude = longitude;
		this.role = role;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public double getLatiude() {
		return latiude;
	}
	public void setLatiude(double latiude) {
		this.latiude = latiude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
}

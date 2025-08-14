package com.combatsystem.www.dto;


public class SoldierDto {
	private Long id;
	private String userName;
	private String email;
	private String deviceType;
	private double latitude;
	private double longitude;
	private String password;

 // Constructors
 public SoldierDto() {}
 
 public SoldierDto(Long id, String userName, String email, String deviceType, double latitude, double longitude) {
     this.id = id;
     this.userName = userName;
     this.email = email;
     this.deviceType = deviceType;
     this.latitude = latitude;
     this.longitude = longitude;
 }
 public SoldierDto( String userName, String email, String deviceType, double latitude, double longitude,String password) {
    
     this.userName = userName;
     this.email = email;
     this.deviceType = deviceType;
     this.latitude = latitude;
     this.longitude = longitude;
     this.password=password;
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

 public double getLatitude() {
	return latitude;
 }

 public void setLatitude(double latitude) {
	this.latitude = latitude;
 }

 public double getLongitude() {
	return longitude;
 }

 public void setLongitude(double longitude) {
	this.longitude = longitude;
 }

 public String getPassword() {
	return password;
 }

 public void setPassword(String password) {
	this.password = password;
 }

 // Getters and Setters
 // (Generate using IDE or manually)
}


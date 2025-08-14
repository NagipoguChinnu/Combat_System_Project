package com.combatsystem.www.dto;

public class UserResponseDto {

    private Long id;
    private String name;
    private String email;
    private String role;
    private String status;
    private double latitude;
    private double longitude;
    private String password;

    public UserResponseDto(){
    	
    }
    public UserResponseDto(Long id, String name, String email, String role, String status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.status = status;
    }

	public UserResponseDto(String name, String email, String role, double latitude, double longitude,String password) 
	{
		   this.name=name;
		   this.email=email;
		   this.role=role;
		   this.latitude=latitude;
		   this.longitude=longitude;
		   this.password=password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
}

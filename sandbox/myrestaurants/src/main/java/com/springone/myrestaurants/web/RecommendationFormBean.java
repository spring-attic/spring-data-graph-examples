package com.springone.myrestaurants.web;

public class RecommendationFormBean {

	
	private long restaurantId;
	
	// Restaurant name
	private String name;
	
	private int rating;
	
	private String comments;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public long getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(long restaurantId) {
		this.restaurantId = restaurantId;
	}

	public int getRating() {
		return rating;
	}
	
	

	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
}

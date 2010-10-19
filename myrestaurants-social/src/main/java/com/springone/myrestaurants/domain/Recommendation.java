package com.springone.myrestaurants.domain;

import org.springframework.datastore.graph.annotation.EndNode;
import org.springframework.datastore.graph.annotation.RelationshipEntity;
import org.springframework.datastore.graph.annotation.StartNode;

@RelationshipEntity
public class Recommendation {
    @StartNode
    private UserAccount user;
    @EndNode
    private Restaurant restaurant;

    private int stars;
    private String comment;


    public Recommendation() {
    }

    public void rate(int stars, String comment) {
        this.stars = stars;
        this.comment = comment;
    }

	public int getStars() {
		return stars;
	}

	public String getComment() {
		return comment;
	}

	public UserAccount getUser() {
		return user;
	}

	public Restaurant getRestaurant() {
		return restaurant;
	}



    
    
}

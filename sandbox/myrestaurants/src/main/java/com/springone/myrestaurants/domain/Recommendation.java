package com.springone.myrestaurants.domain;

import org.springframework.datastore.graph.api.GraphRelationship;
import org.springframework.datastore.graph.api.GraphRelationshipEndNode;
import org.springframework.datastore.graph.api.GraphRelationshipStartNode;

@GraphRelationship
public class Recommendation {
    @GraphRelationshipStartNode
    private UserAccount user;
    @GraphRelationshipEndNode
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

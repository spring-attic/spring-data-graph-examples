package com.springone.myrestaurants.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.springone.myrestaurants.domain.Recommendation;
import com.springone.myrestaurants.domain.Restaurant;
import com.springone.myrestaurants.domain.UserAccount;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@DirtiesContext
public class UserAccountRelationshipsTest extends AbstractTestWithUserAccount {

    @Autowired
    UserAccountRepository userAccountRepo;
    
    @Autowired
    RestaurantRepository restaurantRepository;

    @Transactional
    @Test
    public void testAddFriend() {
    	UserAccount user = userAccountRepo.findUserAccount(userId);
    	UserAccount newUser = new UserAccount();
    	newUser.setFirstName("John");
    	newUser.setLastName("Doe");
    	newUser.setBirthDate(new Date());
    	newUser.setNickname("Bubba");
    	newUser.setUserName("jdoe");
    	userAccountRepo.persist(newUser);
    	em.flush();
    	user.getFriends().add(newUser);
    	em.flush();
    	UserAccount updatedUser = userAccountRepo.findUserAccount(userId);
    	Assert.assertNotNull("should have found something" ,updatedUser);
    	Assert.assertEquals("user should now have correct number of friends", 1, updatedUser.getFriends().size());
    }

    @Transactional
    @Test
    public void testAddRecommendation() {
    	UserAccount user = userAccountRepo.findUserAccount(userId);
    	Restaurant rest = restaurantRepository.findRestaurant(22L);
    	user.rate(rest, 3, "Pretty Good");
    	em.flush();
    	UserAccount updatedUser = userAccountRepo.findUserAccount(userId);
    	Assert.assertNotNull("should have found something" ,updatedUser);
    	List<Recommendation> recommendations = new ArrayList<Recommendation>();
    	for (Recommendation r : updatedUser.getRecommendations()) {
    		recommendations.add(r);
    	}
    	Assert.assertEquals("user should now have correct number of recommendations", 1, recommendations.size());
    	Assert.assertEquals("recommendation should have correct rating", 3, recommendations.get(0).getStars());
    	Assert.assertEquals("recommendation should have correct comment", "Pretty Good", recommendations.get(0).getComment());
    }

}

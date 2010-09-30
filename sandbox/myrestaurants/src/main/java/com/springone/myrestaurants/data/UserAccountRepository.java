package com.springone.myrestaurants.data;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.springone.myrestaurants.domain.UserAccount;

@Repository
public class UserAccountRepository {

	@PersistenceContext
    private EntityManager entityManager;

	@Transactional
	public UserAccount findUserAccount(Long id) {
        if (id == null) return null;
        final UserAccount userAccount = entityManager.find(UserAccount.class, id);
        userAccount.getId();
        return userAccount;
    }
    @Transactional
	public UserAccount findByName(String name) {
		if (name == null) return null;		
		Query q = entityManager.createQuery("SELECT u FROM UserAccount u WHERE u.userName = :username");
		q.setParameter("username", name);
		
		java.util.List resultList = q.getResultList();
		if (resultList.size() > 0)
		{
            final UserAccount userAccount = (UserAccount) resultList.get(0);
            userAccount.getId();
            return userAccount;
		} 
		return null;
	}

	@Transactional
    public void persist(UserAccount userAccount) {
        this.entityManager.persist(userAccount);
        userAccount.getId();
    }

	@Transactional
    public UserAccount merge(UserAccount userAccount) {
        userAccount.getId();
        UserAccount merged = this.entityManager.merge(userAccount);
        this.entityManager.flush();
        return merged;
    }
}

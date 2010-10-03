package com.springone.myrestaurants.data;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.datastore.graph.neo4j.spi.node.Neo4jHelper;
import org.springframework.datastore.graph.neo4j.support.GraphDatabaseContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import com.springone.myrestaurants.domain.UserAccount;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class UserAccountRepositoryTests {

    protected final Log log = LogFactory.getLog(getClass());
    
    private Long userId;

    @Autowired
    PlatformTransactionManager transactionManager;

    @Autowired
    private GraphDatabaseContext graphDatabaseContext;

    @PersistenceContext
    EntityManager em;
    
    @PersistenceUnit
    EntityManagerFactory emf;
    
    @Autowired
    UserAccountRepository repo;

    @BeforeTransaction
    public void setUp() {
    	EntityManager setUpEm = emf.createEntityManager();
    	EntityTransaction setUpTx = setUpEm.getTransaction();
    	setUpTx.begin();
    	UserAccount u = new UserAccount();
    	u.setFirstName("Bubba");
    	u.setLastName("Jones");
    	u.setBirthDate(new Date());
    	u.setUserName("user");
    	setUpEm.persist(u);
    	setUpEm.flush();
    	this.userId = u.getId();
    	setUpTx.commit();
    }
    
    @Transactional
    @Rollback(false)
    @Before
    public void cleanDb() {
        Neo4jHelper.cleanDb(graphDatabaseContext);
    }

    @Transactional
    @Test
    public void testFindUser() {
    	UserAccount o = repo.findUserAccount(userId);
    	Assert.assertNotNull("should have found something" ,o);
    	Assert.assertEquals("should have found the right one", "user", o.getUserName());
    }

    @Transactional
    @Test
    public void testFindByName() {
    	UserAccount o = repo.findByName("user");
    	Assert.assertNotNull("should have found something" ,o);
    	Assert.assertEquals("should have found the right one", "user", o.getUserName());
    }

    @Transactional
    @Test
    public void testPersist() {
    	UserAccount u = new UserAccount();
    	u.setFirstName("John");
    	u.setLastName("Doe");
    	u.setBirthDate(new Date());
    	u.setUserName("jdoe");
    	repo.persist(u);
    	em.flush();
		List results = em.createNativeQuery("select id, user_name, first_name from user_account where user_name = ?")
    			.setParameter(1, u.getUserName()).getResultList();
    	Assert.assertEquals("should have found the entry", 1, results.size());
    	Assert.assertEquals("should have found the correct entry", "John", ((Object[])results.get(0))[2]);
    }
    
    @Transactional
    @Test
    public void testMerge() {
    	EntityManager separateTxEm = emf.createEntityManager();
    	EntityTransaction separateTx = separateTxEm.getTransaction();
    	separateTx.begin();
    	UserAccount user = separateTxEm.find(UserAccount.class, userId);
    	separateTxEm.flush();
    	Assert.assertTrue("entity is part of separate em", separateTxEm.contains(user));
    	separateTx.commit();
    	separateTxEm.detach(user);
    	Assert.assertFalse("entity is no longer part of separate em", separateTxEm.contains(user));
    	Assert.assertFalse("entity is not part of main em", em.contains(user));
    	user.setLastName("Hendrix");
    	UserAccount mergedUser = repo.merge(user);
    	em.flush();
    	Assert.assertTrue("entity is now part of main em", em.contains(mergedUser));
		List results = em.createNativeQuery("select id, user_name, last_name from user_account where id = ?")
				.setParameter(1, userId).getResultList();
		Assert.assertEquals("should have found the entry", 1, results.size());
		Assert.assertEquals("should have found the updated entry", "Hendrix", ((Object[])results.get(0))[2]);
    }

    @AfterTransaction
    public void tearDown() {
    	EntityManager tearDownEm = emf.createEntityManager();
    	EntityTransaction tearDownTx = tearDownEm.getTransaction();
    	tearDownTx.begin();
    	UserAccount u = tearDownEm.find(UserAccount.class, this.userId);
    	tearDownEm.remove(u);
    	tearDownEm.flush();
    	tearDownTx.commit();    	
    }

}

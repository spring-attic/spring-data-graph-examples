package com.springone.myrestaurants.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Version;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Configurable
@Entity
public class Restaurant {

    private String name;

    private String city;
    
    private String state;

    private String zipCode;

	public String getName() {
        return this.name;
    }

	public void setName(String name) {
        this.name = name;
    }

	public String getCity() {
        return this.city;
    }

	public void setCity(String city) {
        this.city = city;
    }

	public String getState() {
        return this.state;
    }

	public void setState(String state) {
        this.state = state;
    }

	public String getZipCode() {
        return this.zipCode;
    }

	public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

	@PersistenceContext
    transient EntityManager entityManager;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

	@Version
    @Column(name = "version")
    private Integer version;

	public Long getId() {
        return this.id;
    }

	public void setId(Long id) {
        this.id = id;
    }

	public Integer getVersion() {
        return this.version;
    }

	public void setVersion(Integer version) {
        this.version = version;
    }

	@Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }

	@Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Restaurant attached = this.entityManager.find(this.getClass(), this.id);
            this.entityManager.remove(attached);
        }
    }

	@Transactional
    public void flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }

	@Transactional
    public Restaurant merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Restaurant merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new Restaurant().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countRestaurants() {
        return ((Number) entityManager().createQuery("select count(o) from Restaurant o").getSingleResult()).longValue();
    }

	@SuppressWarnings("unchecked")
    public static List<Restaurant> findAllRestaurants() {
        return entityManager().createQuery("select o from Restaurant o").getResultList();
    }

	public static Restaurant findRestaurant(Long id) {
        if (id == null) return null;
        return entityManager().find(Restaurant.class, id);
    }

	@SuppressWarnings("unchecked")
    public static List<Restaurant> findRestaurantEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("select o from Restaurant o").setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public String toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }

	public static Restaurant fromJsonToRestaurant(String json) {
        return new JSONDeserializer<Restaurant>().use(null, Restaurant.class).deserialize(json);
    }

	public static String toJsonArray(Collection<Restaurant> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }

	public static Collection<Restaurant> fromJsonArrayToRestaurants(String json) {
        return new JSONDeserializer<List<Restaurant>>().use(null, ArrayList.class).use("values", Restaurant.class).deserialize(json);
    }

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Version: ").append(getVersion()).append(", ");
        sb.append("Name: ").append(getName()).append(", ");
        sb.append("City: ").append(getCity()).append(", ");
        sb.append("State: ").append(getState()).append(", ");
        sb.append("ZipCode: ").append(getZipCode());
        return sb.toString();
    }
}

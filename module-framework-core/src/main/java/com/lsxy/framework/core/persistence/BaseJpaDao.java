package com.lsxy.framework.core.persistence;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


public abstract class BaseJpaDao {
	
	@PersistenceContext
	private EntityManager em;

	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}
	
	
}

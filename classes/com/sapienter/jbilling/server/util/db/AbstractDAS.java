/*
    jbilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2007 Sapienter Billing Software Corp. and Emiliano Conde

    This file is part of jbilling.

    jbilling is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    jbilling is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with jbilling.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.sapienter.jbilling.server.util.db;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.ejb.HibernateEntityManager;
import org.hibernate.ejb.HibernateEntityManagerFactory;

import com.sapienter.jbilling.common.SessionInternalError;


public abstract class AbstractDAS<T> {

    private static final Logger LOG = Logger.getLogger(AbstractDAS.class);
    private static SessionFactory sessionFactory;
    private EntityManager em = null;
    private Class<T> persistentClass;
    //  use "jbilling-jta" for JTA or "jbilling-jdbc" for jdbc
    private static final EntityManagerFactory emf =
        Persistence.createEntityManagerFactory("jbilling-jta"); 
    // if querys will be run cached or not
    private boolean queriesCached = false;

    
    // Create the initial SessionFactory from the default configuration files
    static {
        try {
            LOG.debug("Initializing Hibernate");

            sessionFactory = ((HibernateEntityManagerFactory) emf).getSessionFactory();

            LOG.debug("Hibernate initialized");
        } catch (Throwable ex) {
            // We have to catch Throwable, otherwise we will miss
            // NoClassDefFoundError and other subclasses of Error
            LOG.error("Building SessionFactory failed.", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
    
    public AbstractDAS() {
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
                                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    
    /**
     * Merges the entity, creating or updating as necessary
     * @param newEntity
     * @return
     */
    public T save(T newEntity) {
        //T retValue = em.merge(newEntity);
        T retValue = (T) getSession().merge(newEntity);
        return retValue;
    }
    
    public void delete(T entity) {
        //em.remove(entity);
        getSession().delete(entity);
    }

    protected Session getSession() {
        Session mySession = null;
        if (em != null) {
            mySession = ((HibernateEntityManager) em).getSession();
        } else {
            mySession = sessionFactory.getCurrentSession();
        }
        // just make sure queries only return objects, never single columns
        // OR, modifications in the persitance context are not relevant to the result
        mySession.setFlushMode(FlushMode.COMMIT);
        return mySession;
    }
    
    public Class<T> getPersistentClass() {
        return persistentClass;
    }

    /**
     * This will load a proxy. If the row does not exist, it still returns an
     * object (not null) and  it will NOT throw an
     * exception (until the other fields are accessed).
     * Use this by default, if the row is missing, it is an error.
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    public T find(Serializable id) {
    	if (id == null) return null;
        T entity = (T) getSession().load(getPersistentClass(), id);

        return entity;
    }
    
    /**
     * This will hit the DB. If the row does not exist, it will NOT throw an
     * exception but it WILL return NULL
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    public T findNow(Serializable id) {
    	if (id == null) return null;
        T entity = (T) getSession().get(getPersistentClass(), id);

        return entity;
    }    

    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        return findByCriteria();
    }

    @SuppressWarnings("unchecked")
    public List<T> findByExample(T exampleInstance, String... excludeProperty) {
        Criteria crit = getSession().createCriteria(getPersistentClass());
        Example example =  Example.create(exampleInstance);
        for (String exclude : excludeProperty) {
            example.excludeProperty(exclude);
        }
        crit.add(example);
        crit.setCacheable(queriesCached);
        return crit.list();
    }

    @SuppressWarnings("unchecked")
    public T findByExampleSingle(T exampleInstance, String... excludeProperty) {
        Criteria crit = getSession().createCriteria(getPersistentClass());
        Example example =  Example.create(exampleInstance);
        for (String exclude : excludeProperty) {
            example.excludeProperty(exclude);
        }
        crit.add(example);
        crit.setCacheable(queriesCached);
        return (T) crit.uniqueResult();
    }
    
    @SuppressWarnings("unchecked")
    public T makePersistent(T entity) {
        getSession().saveOrUpdate(entity);
        return entity;
    }

    public void makeTransient(T entity) {
        getSession().delete(entity);
    }

    public void flush() {
        getSession().flush();
    }

    public void clear() {
        getSession().clear();
    }

    /**
     * Use this inside subclasses as a convenience method.
     */
    @SuppressWarnings("unchecked")
    protected List<T> findByCriteria(Criterion... criterion) {
        Criteria crit = getSession().createCriteria(getPersistentClass());
        for (Criterion c : criterion) {
            crit.add(c);
        }
        crit.setCacheable(queriesCached);
        return crit.list();
   }

    @SuppressWarnings("unchecked")
    protected T findByCriteriaSingle(Criterion... criterion) {
        Criteria crit = getSession().createCriteria(getPersistentClass());
        for (Criterion c : criterion) {
            crit.add(c);
        }
        crit.setCacheable(queriesCached);
        return (T) crit.uniqueResult();
   }

    protected void useCache() {
        queriesCached = true;
    }
    
    /**
     * Makes this DTO now attached to the session and part of the persistent context.
     * This WILL trigger an update, which is usually fine since the reason to reattach
     * is to modify the object.
     * @param dto
     */
    public void reattach(T dto) {
    	getSession().update(dto);
    }
    
    protected void touch(List<T> list, String methodName) {
    	
//    	// find any getter, but not the id or we'll get stuck with the proxy
//    	for (Method myMethod: persistentClass.getDeclaredMethods()) {
//    		if (myMethod.getName().startsWith("get") && !myMethod.getName().equals("getId")) {
//    			toCall = myMethod;
//    			break;
//    		}
//    	}
    	
    	try {
        	Method toCall = persistentClass.getMethod(methodName, null);
			for(int f=0; list.size() < f; f++) {
				toCall.invoke(list.get(f), null);
			}
		} catch (Exception e) {
			throw new SessionInternalError("Error invoking method when touching proxy object", 
					AbstractDAS.class, e);
			
		} 
	}
}

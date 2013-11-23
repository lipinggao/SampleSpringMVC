package com.test.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.ScrollableResults;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.test.dao.GenericDao;


/**
 * This class serves as the Base class for all other DAOs - namely to hold common CRUD methods that they might
 * all use. You should only need to extend this class when your require custom CRUD logic.
 * 
 * <p>
 * To register this class in your Spring context file, use the following XML.
 * 
 * <pre>
 *      &lt;bean id=&quot;fooDao&quot; class=&quot;com.darcytech.gaia.dao.hibernate.GenericDaoHibernate&quot;&gt;
 *          &lt;constructor-arg value=&quot;com.darcytech.gaia.model.Foo&quot;/&gt;
 *          &lt;property name=&quot;sessionFactory&quot; ref=&quot;sessionFactory&quot;/&gt;
 *      &lt;/bean&gt;
 * </pre>
 * 
 * @author <a href="mailto:bwnoll@gmail.com">Bryan Noll</a>
 * @param <T>
 *            a type variable
 * @param <PK>
 *            the primary key for that type
 */
public class GenericDaoHibernate<T, PK extends Serializable> extends HibernateDaoSupport implements
        GenericDao<T, PK> {
    /**
     * Log variable for all child classes. Uses LogFactory.getLog(getClass()) from Commons Logging
     */
    protected final Logger logger = Logger.getLogger(getClass());

    private Class<T> persistentClass;

    /**
     * Constructor that takes in a class to see which type of entity to persist
     * 
     * @param persistentClass
     *            the class type you'd like to persist
     */
    public GenericDaoHibernate(final Class<T> persistentClass) {
        this.persistentClass = persistentClass;
    }

    /**
     * {@inheritDoc}
     */
    public List<T> getAll() {
        return super.getHibernateTemplate().loadAll(this.persistentClass);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<T> getAllDistinct() {
        Collection result = new LinkedHashSet(getAll());
        return new ArrayList(result);
    }

    /**
     * {@inheritDoc}
     */
    public T get(PK id) {
       return super.getHibernateTemplate().get(this.persistentClass, id);
    }

    /**
     * {@inheritDoc}
     */
    public boolean exists(PK id) {
        T entity = (T) super.getHibernateTemplate().get(this.persistentClass, id);
        return entity != null;
    }

    /**
     * {@inheritDoc}
     */
    public T save(T object) {
        return (T) super.getHibernateTemplate().merge(object);
    }

    /**
     * {@inheritDoc}
     */
    public List<T> saveAll(List<T> objects) {
        HibernateTemplate template = getHibernateTemplate();
        List<T> result = new ArrayList<T>(objects.size());
        for (T object : objects) {
            result.add((T) template.merge(object));
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public void remove(PK id) {
        super.getHibernateTemplate().delete(this.get(id));
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<T> findByNamedQuery(String queryName, Map<String, Object> queryParams) {
        String[] params = new String[queryParams.size()];
        Object[] values = new Object[queryParams.size()];
        int index = 0;
        Iterator<String> i = queryParams.keySet().iterator();
        while (i.hasNext()) {
            String key = i.next();
            params[index] = key;
            values[index++] = queryParams.get(key);
        }
        return getHibernateTemplate().findByNamedQueryAndNamedParam(queryName, params, values);
    }

    /**
     * Set the Hibernate SessionFactory to be used by this DAO.
     * 
     * This method is added because {@link HibernateDaoSupport} does not support autowire, and we do not want
     * to set spring context's default autowire property to "byName" (i.e.
     * <code>default-autowire="byName"</code>).
     * 
     * @see #setSessionFactory(SessionFactory)
     */
    @Autowired
    public void setHibernateSessionFactory(SessionFactory sessionFactory) {
        setSessionFactory(sessionFactory);
    }

    public void closeScrollableResults(ScrollableResults sr) {
        try {
            if (sr != null) {
                sr.close();
            }
        } catch (HibernateException ignore) {
            logger.error("could not close scrollable result!", ignore);
        } catch (Throwable ignore) {
            logger.error("Unexpected exception!", ignore);
        }
    }

    public void closeSession(StatelessSession session) {
        try {
            if (session != null) {
                session.close();
            }
        } catch (HibernateException ignore) {
            logger.error("could not close scrollable result!", ignore);
        } catch (Throwable ignore) {
            logger.error("Unexpected exception!", ignore);
        }
    }

}

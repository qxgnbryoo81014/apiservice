package com.myapi.apiservice.dao.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import com.myapi.apiservice.dao.IGenericService;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Transactional("transactionManagerB")
public class GenericServiceB<T, ID extends Serializable> implements IGenericService<T, ID>{// implements IGenericService {
	
	protected Class<T> entityClass;
	private SessionFactory sessionFactory;
	
	public GenericServiceB(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/**
	 * 取得 Session (手動控制事務提交/回滾)
	 * @return
	 */
	public Session getOpenSession(){
		return getSessionFactory().openSession();
	}

	@Override
	public T get(ID id) throws DataAccessException {
		T load = (T) sessionFactory.getCurrentSession().get(getEntityClass(), id);
		return load;
	}
	
	@Override
	public Serializable save(T t) throws Exception {
		return sessionFactory.getCurrentSession().save(t);
	}
	
	@Override
	public void update(T t) throws Exception {
		sessionFactory.getCurrentSession().update(t);
	}
	
	@Override
	public void delete(T t) throws Exception {
		sessionFactory.getCurrentSession().delete(t);
	}
	
	@Override
	public void saveOrUpdate(T t) throws DataAccessException {
		sessionFactory.getCurrentSession().saveOrUpdate(t);
	}
	
	@Override
	public List<T> find(String queryString, Object[] values) {
		Query query = sessionFactory.getCurrentSession().createQuery(queryString);
		if(values!=null && values.length>0){
			for(int i=0; i<values.length; i++){
				query.setParameter(i, values[i]);
			}
		}
		return query.list();
	}
	
	@Override
	public void executeHQL(String hql, Object[] values) throws Exception {
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		for(int i = 0;i < values.length;i++){
			query.setParameter(i, values[i]);
		}
		query.executeUpdate();		
	}
	
	@Override
	public List<T> findNativeAlies(String sql, String alies, Object[] values) throws Exception {
		NativeQuery<T> sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(sql);
		sqlQuery.addEntity(getEntityClass());
		if(values != null && values.length > 0){
			for (int i = 0; i < values.length; i++) {
				sqlQuery.setParameter(i, values[i]);
			}
		}
		return sqlQuery.list();
	}

	@Override
	public List<Map<String,Object>> findNativeMap(String sql, Object... values) throws Exception {
		NativeQuery<Map<String,Object>> sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(sql);
//		sqlQuery.addEntity(new ArrayList<Map<String,Object>>().getClass());
		if(values != null && values.length > 0){
			for (int i = 0; i < values.length; i++) {
				sqlQuery.setParameter(i, values[i]);
			}
		}
		//--轉換為 MAP 物件
		return sqlQuery.list();
	}
}

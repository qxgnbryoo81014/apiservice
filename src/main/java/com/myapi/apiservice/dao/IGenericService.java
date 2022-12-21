package com.myapi.apiservice.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface IGenericService<T, ID extends Serializable> {

	// 以PK取得資料(查不到return null)
	public T get(ID id)throws Exception;
	
	// 單筆新增
	public Serializable save(T t) throws Exception;
	
	// 單筆修改
	public void update(T t) throws Exception;

	// 單筆刪除
	public void delete(T t) throws Exception;
	
	// 新增或儲存
	public void saveOrUpdate(T t) throws Exception;
	
	// HQL查詢
	public List<T> find(String hql, Object[] values) throws Exception;
	
	public void executeHQL(String hql, Object[] values) throws Exception;
	
	// TODO
	// Native SQL - mapping to object(alies) 待測試
	public List<T> findNativeAlies(String sql, String alies, Object[] values)throws Exception;
	
	// Native SQL
	public List<Map<String,Object>> findNativeMap(String sql, Object...values) throws Exception;
}

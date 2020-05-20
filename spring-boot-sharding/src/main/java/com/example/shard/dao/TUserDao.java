package com.example.shard.dao;

import java.util.List;

import com.example.shard.domain.CommonQueryBean;
import com.example.shard.domain.TUser;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 
 * TUser数据库操作接口类
 * 
 **/

@Repository
public interface TUserDao{


	/**
	 * 
	 * 查询（根据主键ID查询）
	 * 
	 **/
	TUser  selectByPrimaryKey(@Param("id") Long id);

	/**
	 * 
	 * 删除（根据主键ID删除）
	 * 
	 **/
	int deleteByPrimaryKey(@Param("id") Long id);

	/**
	 * 
	 * 添加
	 * 
	 **/
	int insert(TUser record);

	/**
	 * 
	 * 修改 （匹配有值的字段）
	 * 
	 **/
	int updateByPrimaryKeySelective(TUser record);

	/**
	 * 
	 * list分页查询
	 * 
	 **/
	List<TUser> list4Page(@Param("record") TUser record, @Param("commonQueryParam") CommonQueryBean query);

	/**
	 * 
	 * count查询
	 * 
	 **/
	long count(@Param("record") TUser record);

	/**
	 * 
	 * list查询
	 * 
	 **/
	List<TUser> list(@Param("record") TUser record);

}
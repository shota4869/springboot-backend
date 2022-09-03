package com.springboot.rest.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.springboot.rest.Entity.MCategoryEntity;

/**
 * MCategory reposiroty.
 * 
 * @author takaseshota
 */
@Mapper
public interface MCategoryRepository {

	@Select("SELECT * "
			+ "FROM m_category_code ")
	List<MCategoryEntity> findAll();
}

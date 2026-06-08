package com.artshop.repository;

import com.artshop.model.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * MyBatis data-access contract for the {@code categories} table.
 * All SQL lives in {@code resources/mapper/CategoryMapper.xml} and binds
 * parameters exclusively via {@code #{...}}.
 */
@Mapper
public interface CategoryMapper {

    List<Category> findAll();

    Optional<Category> findById(@Param("id") String id);

    int insert(Category category);

    int update(Category category);

    int deleteById(@Param("id") String id);
}

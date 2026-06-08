package com.artshop.repository;

import com.artshop.model.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * MyBatis data-access contract for the {@code products} table.
 * All SQL lives in {@code resources/mapper/ProductMapper.xml} and binds
 * parameters exclusively via {@code #{...}}.
 */

@Mapper
public interface ProductMapper {

    List<Product> findAll();

    /**
     * Page of products ordered by name, bounded by {@code limit}/{@code offset}.
     */
    List<Product> findPage(@Param("limit") int limit, @Param("offset") int offset);

    /**
     * Total number of products - used to build the pagination envelope.
     */
    long countAll();

    List<Product> findByCategoryId(@Param("categoryId") String categoryId);

    /**
     * Page of products in a category ordered by name, bounded by {@code limit}/{@code offset}.
     */
    List<Product> findPageByCategoryId(@Param("categoryId") String categoryId,
                                       @Param("limit") int limit,
                                       @Param("offset") int offset);

    /**
     * Total number of products in a category - used to build the pagination envelope.
     */
    long countByCategoryId(@Param("categoryId") String categoryId);

    Optional<Product> findById(@Param("id") String id);

    /**
     * Bulk fetch by id - used by cart/order validation to avoid N+1 queries.
     */
    List<Product> findByIds(@Param("ids") List<String> ids);

    int insert(Product product);

    int update(Product product);

    int deleteById(@Param("id") String id);
}

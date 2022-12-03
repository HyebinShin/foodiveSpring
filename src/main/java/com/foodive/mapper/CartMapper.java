package com.foodive.mapper;

import com.foodive.domain.CartDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {

    public int insert(CartDTO cart);
    public int delete(
            @Param("id") String id,
            @Param("pno") Long pno
    );
    public List<CartDTO> findById(String id);
    public int update(CartDTO cart);

    public int countCart(
            @Param("id") String id,
            @Param("pno") Long pno
    );

}

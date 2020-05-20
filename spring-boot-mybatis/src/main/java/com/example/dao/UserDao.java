package com.example.dao;

import com.example.domain.UserVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao {

    UserVo findById(@Param(value = "id") Long id);
}

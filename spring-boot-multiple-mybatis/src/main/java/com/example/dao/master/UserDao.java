package com.example.dao.master;

import com.example.domain.master.UserVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao {

    UserVo findById(@Param(value = "id") Long id);
}

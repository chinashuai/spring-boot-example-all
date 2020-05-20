package com.example.dao.second;

import com.example.domain.second.SchoolVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolDao {

    SchoolVo findById(@Param(value = "id") Long id);
}

package com.example.controller;

import com.example.dao.master.UserDao;
import com.example.dao.second.SchoolDao;
import com.example.domain.master.UserVo;
import com.example.domain.second.SchoolVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class MultipleDataSourceController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private SchoolDao schoolDao;

    @RequestMapping(value = "/findData")
    public HashMap<String, Object> findMultipleData() {
        UserVo userVo = userDao.findById(1l);
        SchoolVo schoolVo = schoolDao.findById(1l);
        if (userVo != null) {
            System.out.println(userVo.toString());
        }
        if (schoolVo != null) {
            System.out.println(schoolVo.toString());
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userVo", userVo);
        hashMap.put("schoolVo", schoolVo);
        return hashMap;
    }



}

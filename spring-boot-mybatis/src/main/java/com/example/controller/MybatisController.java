package com.example.controller;

import com.example.dao.UserDao;
import com.example.domain.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MybatisController {

    @Autowired
    private UserDao userDao;

    @RequestMapping(value = "/findUserById")
    public UserVo demoReturnSuccess() {
        UserVo userVo = userDao.findById(1l);
        if (userVo != null) {
            System.out.println(userVo.toString());
        }
        return userVo;
    }

}

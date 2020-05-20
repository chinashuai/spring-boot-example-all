package com.example.shard.controller;

import com.example.shard.dao.TUserDao;
import com.example.shard.domain.TUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class TestController {


    @Autowired
    private TUserDao tUserDao;

    @RequestMapping("/list")
    @ResponseBody
    public List<TUser> list(){
        TUser tUser = new TUser();
        tUser.setUserId(12l);
        List<TUser> list = tUserDao.list(tUser);
        return list;
    }


}

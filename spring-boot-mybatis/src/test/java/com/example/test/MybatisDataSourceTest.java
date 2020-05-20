package com.example.test;

import com.example.dao.UserDao;
import com.example.domain.UserVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MybatisDataSourceTest {

    @Autowired
    private UserDao userDao;

    @Test
    public void ddd() {
        UserVo userVo = userDao.findById(1l);
        if (userVo != null) {
            System.out.println(userVo.toString());
        }
    }

}

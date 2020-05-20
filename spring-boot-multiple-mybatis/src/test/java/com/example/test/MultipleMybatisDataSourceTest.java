package com.example.test;

import com.example.dao.master.UserDao;
import com.example.dao.second.SchoolDao;
import com.example.domain.master.UserVo;
import com.example.domain.second.SchoolVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MultipleMybatisDataSourceTest {

    @Autowired
    private UserDao userDao;

    @Autowired
    private SchoolDao schoolDao;

    @Test
    public void test() {
        UserVo userVo = userDao.findById(1l);
        SchoolVo schoolVo = schoolDao.findById(1l);
        if (userVo != null) {
            System.out.println(userVo.toString());
        }
        if (schoolVo != null) {
            System.out.println(schoolVo.toString());
        }
    }

}

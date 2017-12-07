package com.ktanx.jdbc.test.persist;

import com.ktanx.jdbc.persist.JdbcDao;
import com.ktanx.jdbc.test.model.UidUser;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by liyd on 17/6/7.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-uuid.xml"})
public class JdbcTemplateDaoImplUUIDTest {

    @Autowired
    private JdbcDao jdbcDao;

    @Test
    public void uuidInsert() {

        UidUser uidUser = new UidUser();
        uidUser.setLoginName("liyd");
        uidUser.setPassword("123456");

        String uuid = (String) jdbcDao.insert(uidUser);

        System.out.println(uuid);

        UidUser uidUser2 = jdbcDao.get(UidUser.class, uuid);

        System.out.println(uidUser2.getLoginName());

        Assert.assertTrue(uidUser.getLoginName().equals(uidUser2.getLoginName()));
    }


}

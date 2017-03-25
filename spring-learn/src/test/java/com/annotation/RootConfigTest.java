package com.annotation;

import learn.RootConfig;
import learn.bean.UserFactoryBean;
import learn.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by wanghb on 2017/3/22.
 * 基于JavaConfig的配置
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RootConfig.class)
@ActiveProfiles("dev")
public class RootConfigTest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserFactoryBean userFactoryBean;

//    @Test
    public void testSayHello() {
        userService.sayHello();
        userFactoryBean.build();
    }

    @Test
    public void testInitContext() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(RootConfig.class);

        UserService userService = (UserService)applicationContext.getBean("userService");
        userService.sayHello();
    }
}

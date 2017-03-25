package learn.bean;

import learn.service.UserService;

/**
 * Created by wanghb on 2017/3/22.
 */
public class UserFactoryBean {

    private UserService userService;

    public UserFactoryBean(UserService userService) {
        this.userService = userService;
    }

    public void build() {
        System.out.println("创建一个对象");
        userService.sayHello();
    }
}

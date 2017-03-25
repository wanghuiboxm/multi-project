package learn;

import learn.bean.UserFactoryBean;
import learn.service.UserService;
import org.springframework.context.annotation.*;

/**
 * Created by wanghb on 2017/3/22.
 * 根配置
 */
//指定该对象是一个JavaConfig对象
@Configuration
//指定扫描的包路径
@ComponentScan("learn")
//启动自动代理
//@EnableAspectJAutoProxy
public class RootConfig {

    @Bean
    //当profile为dev时，该bean才会激活
    @Profile("dev")
    public UserFactoryBean userFactoryBean(UserService userService) {
        return new UserFactoryBean(userService);
    }
}

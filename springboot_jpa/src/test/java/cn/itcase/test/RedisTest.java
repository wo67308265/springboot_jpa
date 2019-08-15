package cn.itcase.test;

import cn.itcase.SpringbootJpaApplication;
import cn.itcase.dao.UserDao;
import cn.itcase.domain.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @Author: Mr.Wu
 * @Date: 2019/8/15
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringbootJpaApplication.class)
public class RedisTest {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Test
    public void test() throws JsonProcessingException {
        //1.从redis中获取指定的数据,数据形式json字符串
        String userListJson = redisTemplate.boundValueOps("user.findAll").get();

        //2.判断redis中是否存在数据
        if (null==userListJson){
            //3.1如果没有数据，从数据库中获取
            List<User> users = userDao.findAll();
            //3.2将数据转成json字符串
            ObjectMapper objectMapper = new ObjectMapper();
            userListJson = objectMapper.writeValueAsString(users);
            //3.3将数据存到redis缓存中
            redisTemplate.boundValueOps("user.findAll").set(userListJson);

            System.out.println("--------从数据库中获取user数据--------");
        }else {
            System.out.println("--------从redis中获取user数据--------");
        }

        //4.打印数据
        System.out.println(userListJson);



    }
}

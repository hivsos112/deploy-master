package com.bsoft.deploy.service;

import com.bsoft.deploy.dao.entity.User;
import com.bsoft.deploy.dao.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * desc
 * Created on 2018/8/7.
 *
 * @author yangl
 */
@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

    public User findUser(String loginName) {
        return userMapper.findUser(loginName);
    }

}

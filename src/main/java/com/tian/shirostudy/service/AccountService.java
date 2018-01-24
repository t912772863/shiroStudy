package com.tian.shirostudy.service;

import com.tian.shirostudy.dao.entity.Account;
import com.tian.shirostudy.dao.mapper.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2018/1/24 0024.
 */
@Service
public class AccountService {
    @Autowired
    private AccountMapper accountMapper;

    /**
     * 根据用户名查询帐户信息
     * @param username
     * @return
     */
    public Account queryByUsername(String username) {
        return accountMapper.queryByUsername(username);
    }

}

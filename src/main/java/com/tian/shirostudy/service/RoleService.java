package com.tian.shirostudy.service;

import com.tian.shirostudy.dao.entity.Role;
import com.tian.shirostudy.dao.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2018/4/15 0015.
 */
@Service
public class RoleService {
    @Autowired
    private RoleMapper roleMapper;

    public List<Role> queryByAccountId(Long accountId){
        return roleMapper.queryByAccountId(accountId);
    }
}

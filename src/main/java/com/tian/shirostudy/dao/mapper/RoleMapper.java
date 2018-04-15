package com.tian.shirostudy.dao.mapper;

import com.tian.shirostudy.dao.entity.Role;

import java.util.List;

public interface RoleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Role record);

    int insertSelective(Role record);

    Role selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);

    /**
     * 查询某个用户的所有角色
     * @param accountId
     * @return
     */
    List<Role> queryByAccountId(Long accountId);
}
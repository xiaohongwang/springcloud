package com.springboot.mapper;

import com.springboot.model.RolesPermissions;

public interface RolesPermissionsMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table roles_permissions
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table roles_permissions
     *
     * @mbggenerated
     */
    int insert(RolesPermissions record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table roles_permissions
     *
     * @mbggenerated
     */
    int insertSelective(RolesPermissions record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table roles_permissions
     *
     * @mbggenerated
     */
    RolesPermissions selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table roles_permissions
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(RolesPermissions record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table roles_permissions
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(RolesPermissions record);
}
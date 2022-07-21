package com.itheima.riggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.riggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}

package com.itheima.riggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.riggie.common.R;
import com.itheima.riggie.entity.Employee;
import com.itheima.riggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request,@RequestBody Employee employee){

        //* 1。将页面提交的密码password进行md5加密处理

        String password=employee.getPassword();
        password= DigestUtils.md5DigestAsHex(password.getBytes());

        //* 2.根据也买你提交的用户名username查询数据库

        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());

        Employee one = employeeService.getOne(queryWrapper);

         //* 3.如果没有查询到则返回登录失败结果
        if (one == null){
            return R.error("登陆失败");
        }

        //* 4.密码比对，如果不一致则返回登陆失败结
        if (!one.getPassword().equals(password)){
            return R.error("密码错误");
        }

        //* 5.查看员工状态，如果为已禁用状态，则返回员工以禁用结果
        if (one.getStatus() == 0 ) {
            return R.error("账号已禁用");
        }

        //* 6.登录成功，将员工id存入Session并返回登录成功结果
        request.getSession().setAttribute("employee",one.getId());
        return R.success(one);
    }

    /***
     * 员工退出方法
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //清理session中的员工id
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
    log.info("新增员工,员工信息:{}",employee.toString());
//    设置初始密码,初始密码为123456,需要进行md5加密
    employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

//    employee.setCreateTime(LocalDateTime.now());
//    employee.setUpdateTime(LocalDateTime.now());
//
          // 获取当前用户的id
//        Long emid = (Long)request.getSession().getAttribute("employee");
//        employee.setCreateUser(emid);
//        employee.setUpdateUser(emid);

        log.info("新增员工,员工信息:{}",employee.toString());

        employeeService.save(employee);
    return R.success("新增用户成功");
    }

    /***
     * 员工分页方法
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        log.info("page = {},pageSize = {},name = {}",page,pageSize,name);
        
        //构建一个page构造器,包含分页条件
        Page pageInfo = new Page(page,pageSize);

        //准备模糊查询,添加查询条件构造器
        LambdaQueryWrapper<Employee> QueryWrapper = new LambdaQueryWrapper();
        //添加查询条件 根据Employee::getName查询，查询条件name
        QueryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        //添加排序查询条件  orderby:以....排序 ,desc: 降序
        QueryWrapper.orderByDesc(Employee::getUpdateTime);

        //执行查询 会把查询结果放到pageInfo中
        employeeService.page(pageInfo,QueryWrapper);

        return R.success(pageInfo);
    }

    /****
     * 修改员工的方法
     * @param request
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> Update(HttpServletRequest request,@RequestBody Employee employee){
        //日志文件查看是否成功接受参数
        log.info("更改方法接收参数是{}",employee.toString());
        //获取当前登录id
//        Long empId = (Long)request.getSession().getAttribute("employee");
        //加入更改用户的时间
//        employee.setUpdateTime(LocalDateTime.now());
        //加入更改的用户id
//        employee.setUpdateUser(empId);
        //执行更改方法
        employeeService.updateById(employee);
        return R.success("更改成功了");
    }

    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable long id){
        log.info("id是{}",id);
        Employee employee = employeeService.getById(id);
        if (employee != null) {
            return R.success(employee);
        }
        return R.error("没有查询到员工信息");
    }

}

package com.itheima.riggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.riggie.dto.DishDto;
import com.itheima.riggie.entity.Dish;

/**
 * @author 赵自昌
 */
public interface DishService extends IService<Dish> {
    /****
     * 新增菜品，同时插入菜品对应的口味数据，需要同时操作两张表： Dish， DishFlavor
     * @param dishDto
     */
    public void saveWithFlavor(DishDto dishDto);

    /****
     * 菜品分页 同时处理查询菜品的对应分类
     * @param page
     * @param pageSize
     * @param name
     */
    public Page page(int page , int pageSize , String name);
}

package com.itheima.riggie.service;

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
}

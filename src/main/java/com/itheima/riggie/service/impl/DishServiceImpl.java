package com.itheima.riggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.riggie.dto.DishDto;
import com.itheima.riggie.entity.Dish;
import com.itheima.riggie.entity.DishFlavor;
import com.itheima.riggie.mapper.DishMapper;
import com.itheima.riggie.service.DishFlavorService;
import com.itheima.riggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 赵自昌
 */
@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    /****
     * 新增菜品，同时保存对应的口味数据
     * @param dishDto
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品基本信息到Dish表
        this.save(dishDto);

        //菜品id
        Long dishId = dishDto.getId();

        //菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item)->{
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        //保存对应的口味数据到DishFlavor表
        dishFlavorService.saveBatch(flavors);
    }
}

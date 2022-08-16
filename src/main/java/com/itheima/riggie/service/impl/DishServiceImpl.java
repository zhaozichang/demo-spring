package com.itheima.riggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.riggie.dto.DishDto;
import com.itheima.riggie.entity.Category;
import com.itheima.riggie.entity.Dish;
import com.itheima.riggie.entity.DishFlavor;
import com.itheima.riggie.mapper.DishMapper;
import com.itheima.riggie.service.CategoryService;
import com.itheima.riggie.service.DishFlavorService;
import com.itheima.riggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;

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

    /****
     * 菜品分页，同时查询菜品分类
     * @param page
     * @param pageSize
     * @param name
     */
    @Override
    public Page page(int page, int pageSize, String name) {
        //构造分页对象
        Page<Dish> pageInfo = new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(name!=null,Dish::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        //执行分页查询
        dishService.page(pageInfo,queryWrapper);

        //对象拷贝
        //BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");
        //将菜品的分类查询出来，并把它传入到dto当中
        List<Dish> records = pageInfo.getRecords();

        List<DishDto> dishDtos = new ArrayList<>();
        for (Dish dish:records) {

            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(dish,dishDto);
            //根据id查询菜品分类对象
            Long categoryId = dish.getCategoryId();
            //查询出菜品
            Category category = categoryService.getById(categoryId);
            if (category != null){
            //取出菜品的名称
            String categoryName = category.getName();
            //存入Dto
            dishDto.setCategoryName(categoryName);
            }
            dishDtos.add(dishDto);
        }
        //将分页对象存入分页构造器
        dishDtoPage.setRecords(dishDtos);
        return dishDtoPage;
    }
}

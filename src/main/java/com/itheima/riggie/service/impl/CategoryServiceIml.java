package com.itheima.riggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.riggie.common.CustomException;
import com.itheima.riggie.entity.Category;
import com.itheima.riggie.entity.Dish;
import com.itheima.riggie.entity.Setmeal;
import com.itheima.riggie.mapper.CategoryMapper;
import com.itheima.riggie.service.CategoryService;
import com.itheima.riggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceIml extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    SetmealService setmealService;
    @Autowired
    DishServiceImpl dishService;

    /****
     * 判断订单中是否关联其他套餐
     * @param id
     */
    @Override
    public void remove(long id) {
        //创建sql查询条件构造器
        LambdaQueryWrapper<Dish> dishQueryWrapper = new LambdaQueryWrapper<>();

        //添加查询条件，完整sql语句,根据id查询
        dishQueryWrapper.eq(Dish::getCategoryId,id);
        int dishCount = dishService.count(dishQueryWrapper);

        //查询当前分类中是否关联了菜品，如果关联了菜品，则抛出异常
        if (dishCount > 0) {
            //关联了菜品，抛出异常
            //new一个CustomException，用构造方法存入信息，然后throm抛出
            throw new CustomException("当前分类关联了菜品，不能删除");
        }

        //创建sql查询条件构造器
        LambdaQueryWrapper<Setmeal> setmealQueryWrapper = new LambdaQueryWrapper<>();

        //添加查询条件，完整sql语句,根据id查询
        setmealQueryWrapper.eq(Setmeal::getCategoryId,id);
        int setmealCount = setmealService.count(setmealQueryWrapper);

        //查询当前分类是否关联了套餐，如果关联了套餐，则抛出异常
        if (setmealCount > 0) {
            //关联了套餐，抛出异常
            //new一个CustomException，用构造方法存入信息，然后throm抛出
            throw new CustomException("当前分类关联了套餐，不能删除");
        }

        //正常删除分类
        super.removeById(id);
    }
}

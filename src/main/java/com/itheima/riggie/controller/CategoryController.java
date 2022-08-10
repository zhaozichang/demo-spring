package com.itheima.riggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.riggie.common.R;
import com.itheima.riggie.entity.Category;
import com.itheima.riggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;



    @PostMapping
    public R<String> save(@RequestBody Category category){
        log.info("category:{}",category);
        categoryService.save(category);
        return R.success("新增分类成功");
    }

    /***
     * 订单分页方法
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page , int pageSize){
        //新建一个page对象
        Page<Category> pageInfo = new Page<>(page, pageSize);
        //新建一个lambdoqueryWrapper对象，创建条件查询构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加排序条件，根据sort进行进行排序
        queryWrapper.orderByAsc(Category::getSort);

        //分页查询,返回数据会自动放在pageInfo中
        categoryService.page(pageInfo,queryWrapper);

        return R.success(pageInfo);
    }

    /***
     * 分类删除方法
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(Long ids){

        log.info("id是{}",ids);

        categoryService.remove(ids);

        return R.success("分类信息删除成功");
    }

    @PutMapping
    public R<String> update(@RequestBody Category category){
        log.info("修改分类信息：{}",category);

        categoryService.updateById(category);
        return R.success("修改分类信息成功");
    }

    /***
     * 根据条件查询分类数据
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> List(Category category){
        //条件构造器
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper();
        //添加查询条件
        lambdaQueryWrapper.eq(category.getType()!=null,Category::getType,category.getType());
        //添加排序条件
        lambdaQueryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list = categoryService.list(lambdaQueryWrapper);

        return R.success(list);
    }
}

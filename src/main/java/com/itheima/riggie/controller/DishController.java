package com.itheima.riggie.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.riggie.common.R;
import com.itheima.riggie.dto.DishDto;
import com.itheima.riggie.service.DishFlavorService;
import com.itheima.riggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    DishService dishService;

    @Autowired
    DishFlavorService dishFlavorService;


    /***
     * 菜品新增方法
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());

        //菜品新增方法
        dishService.saveWithFlavor(dishDto);

        return R.success("新增成功");
    }

    /***
     * 菜品分页方法
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page ,int pageSize ,String name){
        Page pageInfo = dishService.page(page, pageSize, name);
        return R.success(pageInfo);
    }
}

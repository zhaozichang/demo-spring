package com.itheima.riggie.controller;


import com.itheima.riggie.common.R;
import com.itheima.riggie.dto.DishDto;
import com.itheima.riggie.service.DishFlavorService;
import com.itheima.riggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

package com.itheima.riggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.riggie.entity.Category;

public interface CategoryService extends IService<Category> {
    public void remove(long Id);
}

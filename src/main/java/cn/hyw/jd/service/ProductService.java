package cn.hyw.jd.service;

import cn.hyw.jd.pojo.Item;
import cn.hyw.jd.pojo.product;

import java.util.List;

/**
 * @Author: 洪迎伟
 * @DateTime: 2019/12/19 9:15
 * @Description: TODO
 */
public interface ProductService {
    public void save(product item);

    public List<product> findAll(product item);
}

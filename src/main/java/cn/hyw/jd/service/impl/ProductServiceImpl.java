package cn.hyw.jd.service.impl;

import cn.hyw.jd.Dao.productDao;
import cn.hyw.jd.pojo.Item;
import cn.hyw.jd.pojo.product;
import cn.hyw.jd.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: 洪迎伟
 * @DateTime: 2019/12/19 9:16
 * @Description: TODO
 */
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private productDao productDao;

    @Transactional
    public void save(product item) {
        this.productDao.save(item);
    }

    public List<product> findAll(product item) {
        //声明查询条件
        Example<product> example = Example.of(item);
        List<product> list = this.productDao.findAll(example);
        return list;
    }
}

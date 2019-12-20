package cn.hyw.jd.service.impl;

import cn.hyw.jd.Dao.ItemDao;
import cn.hyw.jd.Dao.productDao;
import cn.hyw.jd.pojo.Item;
import cn.hyw.jd.pojo.product;
import cn.hyw.jd.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService{
    @Autowired
    private ItemDao itemDao;

    @Transactional
    public void save(Item item) {
        this.itemDao.save(item);
    }

    public List<Item> findAll(Item item) {
        //声明查询条件
        Example<Item> example = Example.of(item);
        List<Item> list = this.itemDao.findAll(example);
        return list;
    }
}

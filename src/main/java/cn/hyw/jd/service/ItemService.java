package cn.hyw.jd.service;


import cn.hyw.jd.pojo.Item;

import java.util.List;

public interface ItemService{

    public void save(Item item);
    public List<Item> findAll(Item item);
}

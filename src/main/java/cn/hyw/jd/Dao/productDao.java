package cn.hyw.jd.Dao;

import cn.hyw.jd.pojo.product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * @Author: 洪迎伟
 * @DateTime: 2019/12/19 8:34
 * @Description: TODO
 */
@Component
public interface productDao extends JpaRepository<product, String> {
}

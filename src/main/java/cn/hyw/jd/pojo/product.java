package cn.hyw.jd.pojo;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * @Author: 洪迎伟
 * @DateTime: 2019/12/19 8:23
 * @Description: TODO
 */
@Data
@Entity(name="product")
public class product {
    /**
     * 商品id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String pid;
    /**
     * 商品名称
     */
    private String pname;
    /**
     * 商品市场价格
     */
    private Double market_price;
    /**
     * 商品原价
     */
    private Double shop_price;
    /**
     * 商品的图片地址
     */
    private String pimage;
    /**
     * 商品上货时间
     */
    private Date pdate;
    /**
     * 商品的是否是热门
     */
    private Integer is_hot;
    /**
     * 商品详情
     */
    private String pdesc;
    /**
     * 商品是否上架
     */
    private Integer pflag;

}

package cn.hyw.jd.task;

import cn.hyw.jd.pojo.Item;
import cn.hyw.jd.pojo.product;
import cn.hyw.jd.service.ItemService;
import cn.hyw.jd.service.ProductService;
import cn.hyw.jd.util.HttpUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class ItemTask {

    @Autowired
    private HttpUtils httpUtils;

    @Autowired
    private ItemService itemService;

    @Autowired
    private ProductService productService;


    private static final ObjectMapper MAPPER = new ObjectMapper();

    //当下载任务完成后，间隔多长时间进行下一次的任务
    @Scheduled(fixedDelay = 10*1000)
    public void itemtask() throws Exception{
        String url = "https://search.jd.com/Search?keyword=%E6%89%8B%E6%9C%BA&enc=utf-8&qrst=1&rt=1&stop=1&vt=2" +
                "&wq=%E6%89%8B%E6%9C%BA&cid2=653&cid3=655&s=228&click=0&page=";
        for(int i = 1; i<=8; i=i+2){
            String html = httpUtils.doGetHtml(url+i);
            this.parseProduct(html);
        }
     }
    //解析HTML页面, 获取商品数据，并进行存储
    public void parseItem(String html) throws Exception{
        Document doc = Jsoup.parse(html);
        System.out.println(doc.toString());
        //获取spu
        Elements spuElements = doc.select("div#J_goodsList > ul >li");
        for (Element element : spuElements) {
            long spu = Long.parseLong(element.attr("data-spu"));

            //获取sku
            Elements skuElements = element.select("li.ps-item");
            for (Element skuElement : skuElements) {
                //获取sku
                long sku = Long.parseLong(skuElement.select("[data-sku]").attr("data-sku"));


                //根据商品sku查询商品数据是否已被存储
                Item item = new Item();
                //随机id

                item.setSku(sku);
                //查询该商品是否已被存储
                List<Item> list = itemService.findAll(item);
                if(list.size() > 0){
                    continue;
                }

                //设置商品的spu
                item.setSpu(spu);
                //设置商品的详情url
                String itemUrl = "https://item.jd.com/"+sku+".html";
                item.setUrl(itemUrl);

                //设置商品的图片
                String picUrl = "https:" + skuElement.select("img[data-sku]").first().attr("data-lazy-img");
                picUrl = picUrl.replace("/n9/", "/n1/");
                String imageName = this.httpUtils.doGetImage(picUrl);
                item.setPic(imageName);


                //获取商品的价格
                String priceJson = this.httpUtils.doGetHtml("https://p.3.cn/prices/mgets?skuIds=J_" + sku);
                long price = MAPPER.readTree(priceJson).get(0).get("p").asLong();
                item.setPrice(price);

                //获取商品的标题
                String itemInformation = this.httpUtils.doGetHtml(item.getUrl());
                String title = Jsoup.parse(itemInformation).select("div.sku-name").text();
                item.setTitle(title);
                item.setCreated(new Date());
                item.setUpdated(item.getCreated());

                itemService.save(item);
            }
        }
    }


    //解析HTML页面, 获取商品数据，并进行存储
    public void parseProduct(String html) throws Exception{
        Document doc = Jsoup.parse(html);
        //获取spu
        Elements spuElements = doc.select("div#J_goodsList > ul >li");
        for(Element element: spuElements){
            //获取spu的id
            product item = null;
            try {
                long spu = Long.parseLong(element.attr("data-spu"));

                item = new product();
                //设置item的id
                item.setPid(UUID.randomUUID().toString().substring(0,5)+spu);

                //获取sku
                Element skuElement = element.select("li.ps-item").first(); //获得第一个sku

                //查询商品是否被存储
                List<product> list = productService.findAll(item);
                if(list.size() > 0){
                    continue;
                }

                //设置商品的详情url
                //获取sku
                long sku = Long.parseLong(skuElement.select("[data-sku]").attr("data-sku"));

                String itemUrl = "https://item.jd.com/"+sku+".html";

                //设置商品的图片
                String picUrl = "https:" + skuElement.select("img[data-sku]").first().attr("data-lazy-img");
                picUrl = picUrl.replace("/n9/", "/n1/");
                String imageName = this.httpUtils.doGetImage(picUrl);
                if(imageName==null||imageName.equals("")){
                    continue;
                }
                //设置商品的图片位置
                item.setPimage("products/6/"+imageName);

                //获取商品的价格
                String priceJson = this.httpUtils.doGetHtml("https://p.3.cn/prices/mgets?skuIds=J_" + sku);

                long price = MAPPER.readTree(priceJson).get(0).get("p").asLong();

                //shop_price
                item.setShop_price((double) price);

                //market_price
                item.setMarket_price((double)price-200);


                //获取商品的标题
                String itemInformation = this.httpUtils.doGetHtml(itemUrl);
                String title = Jsoup.parse(itemInformation).select("div.sku-name").text();
                item.setPdesc(title);

                //设置名字
                String[] s = title.split(" ");
                item.setPname(s[0]+" "+s[1]);

                //设置商品上架时间
                item.setPdate(new Date());
                item.setIs_hot(1);
                item.setPflag(0);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                continue;
            }

            productService.save(item);
        }
    }
}

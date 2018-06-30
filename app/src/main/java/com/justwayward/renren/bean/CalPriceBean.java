package com.justwayward.renren.bean;

import java.util.List;

/**
 * Created by gaoyuan on 2018/2/11.
 */

public class CalPriceBean {

    /**
     * novel_id : 1
     * chapter_num : 20
     * chapter_free_num : 4
     * chapter_vip_num : 16
     * discount : 8.50
     * pay_ids : [{"id":1,"coin":16},{"id":2,"coin":16},{"id":3,"coin":16},{"id":8,"coin":50},{"id":9,"coin":49},{"id":10,"coin":49},{"id":11,"coin":51},{"id":12,"coin":49},{"id":13,"coin":50},{"id":14,"coin":50},{"id":15,"coin":49},{"id":16,"coin":51},{"id":17,"coin":50},{"id":18,"coin":52},{"id":19,"coin":52},{"id":20,"coin":50}]
     * vip_price : 590
     */

    private int novel_id;
    private String chapter_num;
    private int chapter_free_num;
    private int chapter_vip_num;
    private String discount;
    private int vip_price;
    private List<PayIdsBean> pay_ids;

    public int getNovel_id() {
        return novel_id;
    }

    public void setNovel_id(int novel_id) {
        this.novel_id = novel_id;
    }

    public String getChapter_num() {
        return chapter_num;
    }

    public void setChapter_num(String chapter_num) {
        this.chapter_num = chapter_num;
    }

    public int getChapter_free_num() {
        return chapter_free_num;
    }

    public void setChapter_free_num(int chapter_free_num) {
        this.chapter_free_num = chapter_free_num;
    }

    public int getChapter_vip_num() {
        return chapter_vip_num;
    }

    public void setChapter_vip_num(int chapter_vip_num) {
        this.chapter_vip_num = chapter_vip_num;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public int getVip_price() {
        return vip_price;
    }

    public void setVip_price(int vip_price) {
        this.vip_price = vip_price;
    }

    public List<PayIdsBean> getPay_ids() {
        return pay_ids;
    }

    public void setPay_ids(List<PayIdsBean> pay_ids) {
        this.pay_ids = pay_ids;
    }

    public static class PayIdsBean {
        /**
         * id : 1
         * coin : 16
         */

        private int id;
        private int coin;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCoin() {
            return coin;
        }

        public void setCoin(int coin) {
            this.coin = coin;
        }
    }
}

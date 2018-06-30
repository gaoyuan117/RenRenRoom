package com.justwayward.renren.bean;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by gaoyuan on 2018/1/22.
 */

public class BuyHistoryBean {

    /**
     * total : 1
     * per_page : 15
     * current_page : 1
     * last_page : 1
     * data : [{"id":80,"uid":10,"type":"pay","money":"0.00","coin":-50,"add_time":1516631731,"note":"支付万象天机,1个章节"}]
     */

    private int total;
    private int per_page;
    private int current_page;
    private int last_page;
    private List<DataBean> data;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPer_page() {
        return per_page;
    }

    public void setPer_page(int per_page) {
        this.per_page = per_page;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public int getLast_page() {
        return last_page;
    }

    public void setLast_page(int last_page) {
        this.last_page = last_page;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean  implements Comparable<DataBean> {
        /**
         * id : 80
         * uid : 10
         * type : pay
         * money : 0.00
         * coin : -50
         * add_time : 1516631731
         * note : 支付万象天机,1个章节
         */

        private int id;
        private int uid;
        private String type;
        private String money;
        private int coin;
        private long add_time;
        private String note;
        private int status;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public int getCoin() {
            return coin;
        }

        public void setCoin(int coin) {
            this.coin = coin;
        }

        public long getAdd_time() {
            return add_time;
        }

        public void setAdd_time(long add_time) {
            this.add_time = add_time;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        @Override
        public int compareTo(@NonNull DataBean another) {
            int i = (int) (this.getAdd_time() - another.getAdd_time());//先按照年龄排序
            return i;
        }
    }
}

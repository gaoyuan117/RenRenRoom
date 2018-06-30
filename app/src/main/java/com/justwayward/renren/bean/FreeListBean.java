package com.justwayward.renren.bean;

import java.util.List;

/**
 * Created by gaoyuan on 2018/3/3.
 */

public class FreeListBean {

    /**
     * category_id : 3
     * data : [{"id":35,"uid":0,"title":"复仇女王双面人","author":"双面人","category_id":46,"view_num":7,"collect_num":4,"word_num":13668,"click_num":801,"day_num":100,"desc":"<p><span style=\"font-family: 微软雅黑, \" microsoft=\"\" font-size:=\"\">云朵唯疑惑，她的这个二妹是有什么好东西可以给她看的。<br/>云朵唯上前一步，啊！<br/>一个声音传入这个安静的小屋，而显得不安静了。<br/>云朵唯看着这些都是她和利益上有往来的男人，在照片上甚是亲密，如果这些照片发到网上，她云朵唯这辈子算是毁掉<\/span><\/p>","pic":"http://oz4nnu8m9.bkt.clouddn.com/admin/20180131/0be5c6703fc5bbc912a002ebab8e8e71.jpg!watermark","status":1,"labels":"女频,其他,书城","add_time":1517385490,"update_time":1518493374,"is_recommend":1,"main_recommend":1,"index_recommend":0,"type":"huiyuan","vip_read":1,"copyright":"","free_read_start":1520006400,"free_read_end":1521734399,"category_name":"其他"}]
     */

    private String category_id;
    private List<DataBean> data;

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 35
         * uid : 0
         * title : 复仇女王双面人
         * author : 双面人
         * category_id : 46
         * view_num : 7
         * collect_num : 4
         * word_num : 13668
         * click_num : 801
         * day_num : 100
         * desc : <p><span style="font-family: 微软雅黑, " microsoft="" font-size:="">云朵唯疑惑，她的这个二妹是有什么好东西可以给她看的。<br/>云朵唯上前一步，啊！<br/>一个声音传入这个安静的小屋，而显得不安静了。<br/>云朵唯看着这些都是她和利益上有往来的男人，在照片上甚是亲密，如果这些照片发到网上，她云朵唯这辈子算是毁掉</span></p>
         * pic : http://oz4nnu8m9.bkt.clouddn.com/admin/20180131/0be5c6703fc5bbc912a002ebab8e8e71.jpg!watermark
         * status : 1
         * labels : 女频,其他,书城
         * add_time : 1517385490
         * update_time : 1518493374
         * is_recommend : 1
         * main_recommend : 1
         * index_recommend : 0
         * type : huiyuan
         * vip_read : 1
         * copyright :
         * free_read_start : 1520006400
         * free_read_end : 1521734399
         * category_name : 其他
         */

        private int id;
        private int uid;
        private String title;
        private String author;
        private int category_id;
        private int view_num;
        private int collect_num;
        private int word_num;
        private int click_num;
        private int day_num;
        private String desc;
        private String pic;
        private int status;
        private String labels;
        private int add_time;
        private int update_time;
        private int is_recommend;
        private int main_recommend;
        private int index_recommend;
        private String type;
        private int vip_read;
        private String copyright;
        private int free_read_start;
        private int free_read_end;
        private String category_name;

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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public int getCategory_id() {
            return category_id;
        }

        public void setCategory_id(int category_id) {
            this.category_id = category_id;
        }

        public int getView_num() {
            return view_num;
        }

        public void setView_num(int view_num) {
            this.view_num = view_num;
        }

        public int getCollect_num() {
            return collect_num;
        }

        public void setCollect_num(int collect_num) {
            this.collect_num = collect_num;
        }

        public int getWord_num() {
            return word_num;
        }

        public void setWord_num(int word_num) {
            this.word_num = word_num;
        }

        public int getClick_num() {
            return click_num;
        }

        public void setClick_num(int click_num) {
            this.click_num = click_num;
        }

        public int getDay_num() {
            return day_num;
        }

        public void setDay_num(int day_num) {
            this.day_num = day_num;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getLabels() {
            return labels;
        }

        public void setLabels(String labels) {
            this.labels = labels;
        }

        public int getAdd_time() {
            return add_time;
        }

        public void setAdd_time(int add_time) {
            this.add_time = add_time;
        }

        public int getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(int update_time) {
            this.update_time = update_time;
        }

        public int getIs_recommend() {
            return is_recommend;
        }

        public void setIs_recommend(int is_recommend) {
            this.is_recommend = is_recommend;
        }

        public int getMain_recommend() {
            return main_recommend;
        }

        public void setMain_recommend(int main_recommend) {
            this.main_recommend = main_recommend;
        }

        public int getIndex_recommend() {
            return index_recommend;
        }

        public void setIndex_recommend(int index_recommend) {
            this.index_recommend = index_recommend;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getVip_read() {
            return vip_read;
        }

        public void setVip_read(int vip_read) {
            this.vip_read = vip_read;
        }

        public String getCopyright() {
            return copyright;
        }

        public void setCopyright(String copyright) {
            this.copyright = copyright;
        }

        public int getFree_read_start() {
            return free_read_start;
        }

        public void setFree_read_start(int free_read_start) {
            this.free_read_start = free_read_start;
        }

        public int getFree_read_end() {
            return free_read_end;
        }

        public void setFree_read_end(int free_read_end) {
            this.free_read_end = free_read_end;
        }

        public String getCategory_name() {
            return category_name;
        }

        public void setCategory_name(String category_name) {
            this.category_name = category_name;
        }
    }
}

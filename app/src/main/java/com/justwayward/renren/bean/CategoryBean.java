package com.justwayward.renren.bean;

import java.util.List;

/**
 * Created by gaoyuan on 2017/11/23.
 */

public class CategoryBean {

    /**
     * id : 1
     * category : 分类1
     * pid : 0
     * list_order : 0
     * sub_category : [{"id":2,"category":"测试分类","pid":1,"list_order":0,"novel_num":0}]
     */

    private int id;
    private String category;
    private String type;
    private int pid;
    private int list_order;
    private List<SubCategoryBean> sub_category;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getList_order() {
        return list_order;
    }

    public void setList_order(int list_order) {
        this.list_order = list_order;
    }

    public List<SubCategoryBean> getSub_category() {
        return sub_category;
    }

    public void setSub_category(List<SubCategoryBean> sub_category) {
        this.sub_category = sub_category;
    }

    public static class SubCategoryBean {
        /**
         * id : 2
         * category : 测试分类
         * pid : 1
         * list_order : 0
         * novel_num : 0
         */

        private int id;
        private String category;
        private String name;
        private String min;
        private String max;
        private int pid;
        private int list_order;
        private int novel_num;

        public String getMax() {
            return max;
        }

        public void setMax(String max) {
            this.max = max;
        }

        public String getMin() {
            return min;
        }

        public void setMin(String min) {
            this.min = min;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public int getPid() {
            return pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        public int getList_order() {
            return list_order;
        }

        public void setList_order(int list_order) {
            this.list_order = list_order;
        }

        public int getNovel_num() {
            return novel_num;
        }

        public void setNovel_num(int novel_num) {
            this.novel_num = novel_num;
        }
    }
}

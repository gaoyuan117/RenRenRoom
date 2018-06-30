package com.justwayward.renren.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gaoyuan on 2018/1/24.
 */

public class SubZoneBean implements Serializable {

    /**
     * id : 9
     * pid : 1
     * zone_name : 国外电影
     * zone_link :
     * icon :
     * list_order : 0
     * status : 1
     * add_time : 1516777418
     * sub : [{"id":13,"pid":9,"zone_name":"变形金刚","zone_link":"","icon":"","list_order":0,"status":1,"add_time":1516777462,"sub":[{"id":19,"pid":13,"zone_name":"变形金刚1","zone_link":"http://novel.jzbwlkj.com/admin/index/index.html","icon":"","list_order":0,"status":1,"add_time":1516777787,"sub":[]}]}]
     */

    private int id;
    private int pid;
    private String zone_name;
    private String zone_link;
    private String icon;
    private int list_order;
    private int status;
    private int add_time;
    private List<SubBeanX> sub;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getZone_name() {
        return zone_name;
    }

    public void setZone_name(String zone_name) {
        this.zone_name = zone_name;
    }

    public String getZone_link() {
        return zone_link;
    }

    public void setZone_link(String zone_link) {
        this.zone_link = zone_link;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getList_order() {
        return list_order;
    }

    public void setList_order(int list_order) {
        this.list_order = list_order;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getAdd_time() {
        return add_time;
    }

    public void setAdd_time(int add_time) {
        this.add_time = add_time;
    }

    public List<SubBeanX> getSub() {
        return sub;
    }

    public void setSub(List<SubBeanX> sub) {
        this.sub = sub;
    }

    public static class SubBeanX implements Serializable {
        /**
         * id : 13
         * pid : 9
         * zone_name : 变形金刚
         * zone_link :
         * icon :
         * list_order : 0
         * status : 1
         * add_time : 1516777462
         * sub : [{"id":19,"pid":13,"zone_name":"变形金刚1","zone_link":"http://novel.jzbwlkj.com/admin/index/index.html","icon":"","list_order":0,"status":1,"add_time":1516777787,"sub":[]}]
         */

        private int id;
        private int pid;
        private String zone_name;
        private String zone_link;
        private String icon;
        private String desc;
        private int list_order;
        private int status;
        private int add_time;
        private List<SubBean> sub;

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPid() {
            return pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        public String getZone_name() {
            return zone_name;
        }

        public void setZone_name(String zone_name) {
            this.zone_name = zone_name;
        }

        public String getZone_link() {
            return zone_link;
        }

        public void setZone_link(String zone_link) {
            this.zone_link = zone_link;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public int getList_order() {
            return list_order;
        }

        public void setList_order(int list_order) {
            this.list_order = list_order;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getAdd_time() {
            return add_time;
        }

        public void setAdd_time(int add_time) {
            this.add_time = add_time;
        }

        public List<SubBean> getSub() {
            return sub;
        }

        public void setSub(List<SubBean> sub) {
            this.sub = sub;
        }

        public static class SubBean implements Serializable {
            /**
             * id : 19
             * pid : 13
             * zone_name : 变形金刚1
             * zone_link : http://novel.jzbwlkj.com/admin/index/index.html
             * icon :
             * list_order : 0
             * status : 1
             * add_time : 1516777787
             * sub : []
             */

            private int id;
            private int pid;
            private String zone_name;
            private String zone_link;
            private String icon;
            private int list_order;
            private int status;
            private int add_time;
            private String desc;
            private List<?> sub;

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getPid() {
                return pid;
            }

            public void setPid(int pid) {
                this.pid = pid;
            }

            public String getZone_name() {
                return zone_name;
            }

            public void setZone_name(String zone_name) {
                this.zone_name = zone_name;
            }

            public String getZone_link() {
                return zone_link;
            }

            public void setZone_link(String zone_link) {
                this.zone_link = zone_link;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public int getList_order() {
                return list_order;
            }

            public void setList_order(int list_order) {
                this.list_order = list_order;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getAdd_time() {
                return add_time;
            }

            public void setAdd_time(int add_time) {
                this.add_time = add_time;
            }

            public List<?> getSub() {
                return sub;
            }

            public void setSub(List<?> sub) {
                this.sub = sub;
            }
        }
    }
}

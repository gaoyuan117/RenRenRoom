package com.justwayward.renren.bean;

import java.io.Serializable;

/**
 * Created by gaoyuan on 2017/11/16.
 */

public class BookBean implements Serializable {


    private int id;
    private int novel_id;
    private int source_id;
    private String source_url;
    private String chapter;
    private String content;
    private int word_num;
    private int is_vip;
    private int list_order;
    private int add_time;
    private String novel_type;
    private boolean user_member;
    private boolean user_pay;
    private int prev_id;
    private int next_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNovel_id() {
        return novel_id;
    }

    public void setNovel_id(int novel_id) {
        this.novel_id = novel_id;
    }

    public int getSource_id() {
        return source_id;
    }

    public void setSource_id(int source_id) {
        this.source_id = source_id;
    }

    public String getSource_url() {
        return source_url;
    }

    public void setSource_url(String source_url) {
        this.source_url = source_url;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getWord_num() {
        return word_num;
    }

    public void setWord_num(int word_num) {
        this.word_num = word_num;
    }

    public int getIs_vip() {
        return is_vip;
    }

    public void setIs_vip(int is_vip) {
        this.is_vip = is_vip;
    }

    public int getList_order() {
        return list_order;
    }

    public void setList_order(int list_order) {
        this.list_order = list_order;
    }

    public int getAdd_time() {
        return add_time;
    }

    public void setAdd_time(int add_time) {
        this.add_time = add_time;
    }

    public String getNovel_type() {
        return novel_type;
    }

    public void setNovel_type(String novel_type) {
        this.novel_type = novel_type;
    }

    public boolean isUser_member() {
        return user_member;
    }

    public void setUser_member(boolean user_member) {
        this.user_member = user_member;
    }

    public boolean isUser_pay() {
        return user_pay;
    }

    public void setUser_pay(boolean user_pay) {
        this.user_pay = user_pay;
    }

    public int getPrev_id() {
        return prev_id;
    }

    public void setPrev_id(int prev_id) {
        this.prev_id = prev_id;
    }

    public int getNext_id() {
        return next_id;
    }

    public void setNext_id(int next_id) {
        this.next_id = next_id;
    }
}

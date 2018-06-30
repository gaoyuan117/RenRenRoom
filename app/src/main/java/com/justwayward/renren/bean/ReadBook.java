package com.justwayward.renren.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by gaoyuan on 2017/12/29.
 */

@Entity
public class ReadBook {
    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "bookId")
    @NotNull
    private String bookId;

    @Property(nameInDb = "chapterId")
    @NotNull
    private String chapterId;

    @Property(nameInDb = "isbuy")
    private boolean isbuy;
    @Property(nameInDb = "type")
    private String type;
    @Generated(hash = 695049632)
    public ReadBook(Long id, @NotNull String bookId, @NotNull String chapterId,
            boolean isbuy, String type) {
        this.id = id;
        this.bookId = bookId;
        this.chapterId = chapterId;
        this.isbuy = isbuy;
        this.type = type;
    }
    @Generated(hash = 919017683)
    public ReadBook() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getBookId() {
        return this.bookId;
    }
    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
    public String getChapterId() {
        return this.chapterId;
    }
    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }
    public boolean getIsbuy() {
        return this.isbuy;
    }
    public void setIsbuy(boolean isbuy) {
        this.isbuy = isbuy;
    }
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }
    


}

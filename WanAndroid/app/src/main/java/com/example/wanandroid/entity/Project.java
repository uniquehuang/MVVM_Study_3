package com.example.wanandroid.entity;

/**
 * @author dengfeng
 * @data 2023/4/21
 * @description
 */
public class Project {

    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Project(int id, String desc, String envelopePic, String title, String author, String niceDate) {
        this.id = id;
        this.desc = desc;
        this.envelopePic = envelopePic;
        this.title = title;
        this.author = author;
        this.niceDate = niceDate;
    }

    String desc;

    String envelopePic;

    public String getEnvelopePic() {
        return envelopePic;
    }

    public void setEnvelopePic(String envelopePic) {
        this.envelopePic = envelopePic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    String title;

    String author;

    String niceDate;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getNiceDate() {
        return niceDate;
    }

    public void setNiceDate(String niceDate) {
        this.niceDate = niceDate;
    }

    public Project(String desc, String title, String author, String niceDate) {
        this.desc = desc;
        this.title = title;
        this.author = author;
        this.niceDate = niceDate;
    }
}

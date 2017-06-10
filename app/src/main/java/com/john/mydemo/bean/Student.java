package com.john.mydemo.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by John on 2017/6/10.
 */
@Entity
public class Student {
    @Id(autoincrement = true)
    private Long id;
    @NotNull
    private String sname;
    @NotNull
    private int age;
    private String remark;
    private String ext;
@Generated(hash = 824390988)
public Student(Long id, @NotNull String sname, int age, String remark,
        String ext) {
    this.id = id;
    this.sname = sname;
    this.age = age;
    this.remark = remark;
    this.ext = ext;
}
    @Generated(hash = 1556870573)
    public Student() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getSname() {
        return this.sname;
    }
    public void setSname(String sname) {
        this.sname = sname;
    }
    public int getAge() {
        return this.age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public String getRemark() {
        return this.remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getExt() {
        return this.ext;
    }
    public void setExt(String ext) {
        this.ext = ext;
    }
}

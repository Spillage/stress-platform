package com.xielu.arch.platform.server.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Log4j2
@ToString
@Table(name = "testPlan")
public class TestPlan implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "host")
    private String host;

    @Column(name = "creator")
    private String creator;

    @Column(name = "report")
    private String report;

    @Column(name = "WrkScript")
    private String wrkScript;

    //连接数
    @Column(name = "connNum")
    private int connNum;

    //持续时长
    @Column(name = "elaspe")
    private int elaspe;

    //状态
    @Column(name = "status")
    private String status;

    //线程数
    @Column(name = "ThreadNum")
    private int threadNum;

    @Column(name = "createtime",nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private String createTime;

    @Column(name = "updatetime",columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable=false, updatable=false)
    private String updateTime;
}

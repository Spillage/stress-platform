package com.xielu.arch.platform.server.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "agentSurvived")
public class AgentSurvived implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "agentIP")
    private String agentIP;

    @Column(name = "createtime")
    private String createTime;

    @Column(name = "updatetime",columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable=false, updatable=false)
    private String updateTime;
}

package edu.bupt.wangfu.info.device;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户类，存储用户信息
 */
@Data
public class User implements Serializable{
    private static final long serialVersionUID = 1L;

    //用户id，区分不同用户
    private String id;

    private String name;

    //主机地址
    private String address;
}

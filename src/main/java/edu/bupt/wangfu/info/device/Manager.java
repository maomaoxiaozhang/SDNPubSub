package edu.bupt.wangfu.info.device;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
public class Manager {

    @Value("${adminName:G1}")
    private String adminName;

    @Value("${adminV6Addr:FF0E:0000:0000:0000:0001:2345:6789:ABCD}")
    private String adminV6Addr;

    @Value("${sysV6Addr:FF0E:0000:0000:0000:0001:2345:6790:ABCD}")
    private String sysV6Addr;

    @Value("30001")
    private int adminPort;

    @Value("30002")
    private int sysPort;

    //集群内控制器，key是groupName
    @Value("${groups:#{null}}")
    private Map<String, Controller> groups;


}

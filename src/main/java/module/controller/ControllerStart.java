package module.controller;

import config.ControllerConfig;
import info.device.Controller;
import module.controller.ospf.TopoBuild;
import module.controller.ospf.TopoMgr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Timer;

/**
 * 控制器入口程序
 */
@Component
public class ControllerStart {
    @Autowired
    private Controller controller;

    @Autowired
    private TopoBuild topoBuild;

    @Autowired
    private TopoMgr topoMgr;

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ControllerConfig.class);
        ControllerStart start = (ControllerStart) context.getBean("controllerStart");
        System.out.println(start.controller);
        start.topoBuild.build();
        new Timer().schedule(start.topoMgr, start.controller.getFirstHelloDelay(), start.controller.getHelloPeriod());
    }
}

package info.device;

import lombok.Data;

/**
 * 主机信息
 *
 * @author caoming
 */
@Data
public class Host extends DevInfo{
    private String ip;
    private String swId;
    private String port;
}

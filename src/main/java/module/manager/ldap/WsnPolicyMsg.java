package module.manager.ldap;

import lombok.Data;

import java.io.Serializable;

@Data
public class WsnPolicyMsg implements Serializable{
    private static final long serialVersionUID = 1L;

    private static String topicName;
}

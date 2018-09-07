import module.manager.ldap.LdapUtil;
import module.manager.ldap.TopicEntry;

import javax.naming.NamingException;

public class Start {
    public static void main(String[] args) {
        LdapUtil util = new LdapUtil();
        try {
            util.connectLdap();
            util.getWithAllChildrens(
                    new TopicEntry("all", "1",
                            "ou=all_test,dc=wsn,dc=com", null));
        } catch (NamingException e) {
            e.printStackTrace();
        }

    }
}

package core.web;

import org.springframework.stereotype.Component;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.Query;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.util.Set;

/**
 * Created by alexandremasanes on 08/04/2017.
 */

@Component
public final class Server {

    public final String HOST;

    public final Short PORT;

    public Server() throws Exception {
        String host = null;
        Short  port = null;

        MBeanServer beanServer = ManagementFactory.getPlatformMBeanServer();
        Set<ObjectName> objectNames = beanServer.queryNames(new ObjectName("*:type=Connector,*"),
                Query.match(Query.attr("protocol"), Query.value("HTTP/1.1")));
        host = InetAddress.getLocalHost().getHostAddress();
        port = Short.parseShort(objectNames.iterator().next().getKeyProperty("port"));
        String ipadd = "http" + "://" + host + ":" + port;

        HOST = host;
        PORT = port;
    }
}

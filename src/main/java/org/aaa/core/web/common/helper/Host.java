package org.aaa.core.web.common.helper;

import static java.net.InetAddress.getLocalHost;

import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.Map;

/**
 * Created by alexandremasanes on 22/08/2017.
 */

@Component
public class Host {

    @SuppressWarnings("all")
    @Value("#{'${ipPrinterURIs}'.split(';')}")
    private String[] ipPrinterURIs;

    @Value("#{@systemEnvironment['AAA_SERVERNAME']}")
    private String domainName;

    @Value("#{@systemEnvironment['AAA_API_SUBDOMAIN']}")
    private String apiSubdomain;

    private String publicIpAddress;

    private String privateIpAdress;

    public String getDomainName() {
        return domainName;
    }

    public String getApiSubdomain() {
        return apiSubdomain;
    }

    public String getPublicIpAddress() {
        return publicIpAddress;
    }

    public String getPrivateIpAdress() {
        return privateIpAdress;
    }

    @PostConstruct
    protected void init() throws UnknownHostException, URISyntaxException {

        ResponseEntity<String> response;
        HttpStatus httpStatus;

        for(String ipPrinterURI : ipPrinterURIs) {
            response = new RestTemplate().exchange(
                    new RequestEntity<String>(
                            HttpMethod.GET,
                            new URI("http://" + ipPrinterURI)
                    ),
                    String.class
            );
            httpStatus = response.getStatusCode();
            if(httpStatus.is2xxSuccessful() && response.hasBody()) {
                publicIpAddress = response.getBody();
                privateIpAdress = getLocalHost().getHostAddress();
                return;
            }
        }

        throw new BeanInitializationException("host.publicIpAdress could not be fetched.");
    }
}
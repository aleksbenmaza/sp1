package org.aaa.core.web.common.helper;

import static java.net.InetAddress.getLocalHost;

import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

/**
 * Created by alexandremasanes on 22/08/2017.
 */

@Component
public class Host {

    @Value("${internalRequest.readTimeout}")
    private int readTimeout;

    @Value("${internalRequest.connectTimeout}")
    private int connectTimeout;

    @SuppressWarnings("all")
    @Value("#{'${ipPrinterURIs}'.split(';')}")
    private String[] ipPrinterURIs;

    @Value("#{@systemEnvironment['AAA_SERVERNAME']}")
    private String domainName;

    @Value("#{@systemEnvironment['AAA_CUSTOMER_API_SUBDOMAIN']}")
    private String customerApiSubdomain;

    @Value("#{@systemEnvironment['AAA_PUBLIC_API_SUBDOMAIN']}")
    private String publicApiSubdomain;

    @Value("#{@systemEnvironment['AAA_ADMIN_API_SUBDOMAIN']}")
    private String adminApiSubdomain;

    private String publicIpAddress;

    private String privateIpAdress;

    public String getDomainName() {
        return domainName;
    }

    public String getCustomerApiSubdomain() {
        return customerApiSubdomain;
    }

    public String getPublicIpAddress() {
        return publicIpAddress;
    }

    public String getPrivateIpAdress() {
        return privateIpAdress;
    }

    public String getPublicApiSubdomain() {
        return publicApiSubdomain;
    }

    public String getAdminApiSubdomain() {
        return adminApiSubdomain;
    }

    @PostConstruct
    protected void init() throws UnknownHostException, URISyntaxException {
        ResponseEntity<String> response;
        HttpStatus httpStatus;
        RestTemplate restTemplate;

        restTemplate = new RestTemplate(
                new HttpComponentsClientHttpRequestFactory() {{
                    setReadTimeout(readTimeout);
                    setConnectTimeout(connectTimeout);
        }});

        for(String ipPrinterURI : ipPrinterURIs) {
            try {
                response = restTemplate.exchange(
                        new RequestEntity<String>(
                                HttpMethod.GET,
                                new URI("http://" + ipPrinterURI)
                        ),
                        String.class
                );
            } catch(ResourceAccessException e) {
                continue;
            }
            httpStatus = response.getStatusCode();
            if(httpStatus.is2xxSuccessful()
            && response.hasBody()) {
                publicIpAddress = response.getBody();
                privateIpAdress = getLocalHost().getHostAddress();
                return;
            }
        }

        throw new BeanInitializationException("host.publicIpAddress could not be fetched.");
    }
}
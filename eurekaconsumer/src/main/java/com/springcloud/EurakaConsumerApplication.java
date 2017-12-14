package com.springcloud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by xiaohong on 2017/12/12.
 */
@SpringBootApplication
@EnableEurekaClient
@RestController
public class EurakaConsumerApplication {
    public static void main(String[] args){
        SpringApplication.run(EurakaConsumerApplication.class, args);
    }


    /**服务发现客户端*/

    @Autowired
    DiscoveryClient client;

    @RequestMapping("/services")
    public String getServices(){
        //所有服务 ID 列表
      return   client.getServices().toString();
    }


    @RequestMapping("/sentence")
    public  String getSentence() {
        return getWord("eureka-provider")+" "
                + getWord("eureka-provider2") + " "
                + getWord("eureka-provider3");
    }

    public String getWord(String service) {
        List<ServiceInstance> list = client.getInstances(service);
        if (list != null && list.size() > 0 ) {
            URI uri = list.get(0).getUri();
            if (uri != null ) {
                try {
                    String path = uri.toString();
                    uri = new URI( path + "/hi");
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                return (new RestTemplate()).getForObject(uri,String.class);
            }
        }
        return null;
    }

}

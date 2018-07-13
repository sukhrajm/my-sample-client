package sample.client;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.interceptor.RetryInterceptorBuilder;
import org.springframework.retry.interceptor.StatefulRetryOperationsInterceptor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


/**
 * Copied from tutorial https://cloud.spring.io/spring-cloud-consul/
 */
@Configuration
@EnableAutoConfiguration
@EnableDiscoveryClient
@EnableConfigServer
@RestController
@SpringBootApplication
@RibbonClient(name = "my-sample-server", configuration = CloudConfig.class)
@EnableFeignClients
public class ServiceDiscoveryApplication {

  @Autowired
  private DiscoveryClient discoveryClient;
 
  
  @Autowired
  private MyFeignClient myFeignClient;
    
  @LoadBalanced
  @Bean
  public RestTemplate loadbalancedRestTemplate() {
    return new RestTemplate();
  }
  
  @RequestMapping("/callendpoint")  
  public String callEndpoint() {
    return loadbalancedRestTemplate().getForObject("http://my-sample-server/testhome", String.class);
  }
  
  @RequestMapping("/badrequest")  
  public String badrequest() {
    return loadbalancedRestTemplate().getForObject("http://my-sample-server/badrequest", String.class);
  }
  
  @RequestMapping("/callendpoint2")  
  public String callEndpoint2() {
    return myFeignClient.callOtherEndpoint();
  }
  
  @RequestMapping("/callendpoint3")  
  public ResponseEntity callEndpoint3() {
    return myFeignClient.callBadRequest();
  }
  
  
  @RequestMapping("/")
  public String home() {
    return "Hello World from sample-app-client";
  }
  
  @RequestMapping("/discoveryClient")
  public List<ServiceInstance> serviceUrl() {
     return discoveryClient.getInstances("my-sample-server").stream().collect(Collectors.toList());
  }
  
  @Bean(name = "consulRetryInterceptor")
  public StatefulRetryOperationsInterceptor consulRetryInterceptor() {
    //return new RetryOperationsInterceptor();
    
     StatefulRetryOperationsInterceptor interceptor =
                        RetryInterceptorBuilder.stateful()                                
                                .maxAttempts(5)
                                .backOffOptions(1, 2, 10) // initialInterval, multiplier, maxInterval
                                .build();
     
    return interceptor;
  }
  
  public static void main(String[] args) {
    SpringApplication.run(ServiceDiscoveryApplication.class, args);
  }
  
  
  
  
 }

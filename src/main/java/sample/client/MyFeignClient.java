package sample.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 */
@FeignClient(value = "my-sample-server", /*configuration=MyFeignConfig.class,*/ decode404=true)
public interface MyFeignClient {

  @RequestMapping(method = RequestMethod.GET, value = "/testhome")
  public String callOtherEndpoint();
  
  @RequestMapping(method = RequestMethod.GET, value = "/badrequest")
  public ResponseEntity callBadRequest();
  
  /*@Configuration
  public class MyFeignConfig {

      @Bean
      public Retryer retryer() {
        //return new Retryer.Default();
        return Retryer.NEVER_RETRY;
      }

      @Bean
      public Logger.Level feignLoggerLevel() {
        return Logger.Level.BASIC;
      }
  } */
  
}

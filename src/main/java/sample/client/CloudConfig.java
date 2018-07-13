package sample.client;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;

/**
 *
 * @author sukrat01
 */
public class CloudConfig {

  
  /*@Bean
  public IPing ribbonPing(IClientConfig config) {
      return new PingUrl();
  } */
  
  @Bean
  public IRule ribbonRule(IClientConfig config) {
    return new RandomRule();
  }
  
}

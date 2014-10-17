package wallet

import com.mongodb.Mongo
import org.springframework.context.annotation.{Bean, Configuration, ComponentScan}
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.data.mongodb.core.{MongoOperations, MongoTemplate}

/**
 * This config class will trigger Spring @annotation scanning and auto configure Spring context.
 *
 * @author saung
 * @since 1.0
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
class HelloConfig {
  @Bean
  def mongo(): Mongo = {
    var mo: MongoOperations = new MongoTemplate(new Mongo(), "database");
    new Mongo("localhost");
  }

  @Bean
  def mongoTemplate(): MongoTemplate ={
    new MongoTemplate(mongo(), "test");
  }
 }
package wallet

import java.util.Date

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.web.bind.annotation.{RequestBody, RequestMethod, RequestMapping, RestController}

/**
 * This object bootstraps Spring Boot web application.
 * Via Gradle: gradle bootRun
 *
 * @author saung
 * @since 1.0
 */
object HelloWebApplication {

	def main(args: Array[String]) {
	   SpringApplication.run(classOf[HelloConfig]);
	}
}
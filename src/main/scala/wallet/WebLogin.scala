package wallet

import java.util.Date

import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.validator.constraints.NotEmpty
import org.springframework.context.annotation.Bean

import scala.beans.BeanProperty

/**
 * Created by vaibhavb on 9/19/14.
 */

class WebLogin(@BeanProperty @JsonProperty("login_id") login_id: String,
               @BeanProperty @JsonProperty("url") url: String,
               @BeanProperty @JsonProperty("login") login: String,
               @BeanProperty @JsonProperty("password") password: String,
               @BeanProperty @JsonProperty("u_id") u_id : String) {
  @BeanProperty var login_id_i: String= login_id
  @BeanProperty var url_i: String= url
  @BeanProperty var login_i: String= login
  @BeanProperty var password_i: String= password
  @BeanProperty var user_id_i: String=u_id

  @NotEmpty
  def getWebUrl() = url

  @NotEmpty
  def getWebLogin() = login
}

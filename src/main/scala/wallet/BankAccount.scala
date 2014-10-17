package wallet

import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.validator.constraints.NotEmpty
import org.springframework.context.annotation.Bean

import scala.beans.BeanProperty

/**
 * Created by vaibhavb on 9/19/14.
 */

class BankAccount(@BeanProperty @JsonProperty("ba_id") ba_id: String,
                  @BeanProperty @JsonProperty("account_name") account_name: String,
                  @BeanProperty @JsonProperty("routing_number") routing_number: String,
                  @BeanProperty @JsonProperty("account_number") account_number: String,
                  @BeanProperty @JsonProperty("u_id") u_id : String) {
  @BeanProperty var ba_id_i: String=ba_id
  @BeanProperty var account_name_i: String= account_name
  @BeanProperty var routing_number_i: String= routing_number
  @BeanProperty var account_number_i: String= account_number
  @BeanProperty var user_id_i: String= u_id

  @NotEmpty
  def getRoutingNumber() = routing_number

  @NotEmpty
  def getAccountNumber() = account_number
}

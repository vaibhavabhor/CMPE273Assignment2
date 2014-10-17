package wallet

import java.util.Date

import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.validator.constraints.NotEmpty
import org.springframework.context.annotation.Bean

import scala.beans.BeanProperty

/**
 * Created by vaibhavb on 9/19/14.
 */

class IDCard (@JsonProperty("card_id") card_id: String,
              @JsonProperty("card_name") card_name: String,
              @JsonProperty("card_number") card_number: String,
              @JsonProperty("expiration_date") expiration_date: String,
              @JsonProperty("user_id") u_id : String) {
  @BeanProperty var card_id_i: String= card_id
  @BeanProperty var card_name_i: String= card_name
  @BeanProperty var card_number_i: String= card_number
  @BeanProperty var expiration_date_i: String= expiration_date
  @BeanProperty var user_id_i:String=u_id

  @NotEmpty
  def getCardName() = card_name

  @NotEmpty
  def getCardNumber() = card_number

}

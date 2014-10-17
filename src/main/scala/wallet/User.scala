package wallet
import java.util.Date
import javax.validation.constraints.NotNull

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.BeanProperty
import com.fasterxml.jackson.databind.BeanProperty
import org.hibernate.validator.constraints.NotEmpty

import scala.beans.BeanProperty


/**
 * Created by vaibhavb on 9/19/14.
 */
class User (@JsonProperty("u_id") u_id : String,
            @JsonProperty("email") email: String,
            @JsonProperty("password") password: String,
            @JsonProperty("name") name: String,
            @JsonProperty("created_at") created_at: Date ,
            @JsonProperty("updates_at") updates_at: Date
             )
{
  var user_id: String= u_id
  var email_i: String= email
  var password_i: String= password
  var name_i: String= name
  var created_at_i: Date= created_at
  var updates_at_i: Date= updates_at

  @NotEmpty
  def getEmail() = email

  @NotEmpty
  def getPassword() = password

  //val idCardList = collection.mutable.ArrayBuffer[IDCard]()
  //val UserIdCardMap = collection.mutable.Map[String, collection.mutable.ArrayBuffer[IDCard]]()
  //val webLoginList = collection.mutable.ArrayBuffer[WebLogin]()
  //val UserWebLoginMap = collection.mutable.Map[String, collection.mutable.ArrayBuffer[WebLogin]]()
  //val bankAccountList = collection.mutable.ArrayBuffer[BankAccount]()
  //val UserBankAccountMap = collection.mutable.Map[String, collection.mutable.ArrayBuffer[BankAccount]]()
}

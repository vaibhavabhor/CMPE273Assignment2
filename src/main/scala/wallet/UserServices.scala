package wallet

import java.text.{SimpleDateFormat, DateFormat}
import java.util.{TimeZone, Calendar}
import org.springframework.data.mongodb.core.query.{Query, Criteria}
import org.springframework.stereotype.Component
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.{MongoOperations, MongoTemplate, SimpleMongoDbFactory}
;
import com.mongodb.{Mongo, MongoClient}
;

/**
 * Created by vaibhavb on 9/19/14.
 */
@Component
class UserServices() {
  //var util.ArrayList[User]
  //val arrayBuffer = mutable.ArrayBuffer(1, 2, 3)
  //val hashMap = mutable.Map().empty
  val userMap = collection.mutable.Map[String, User]()

  val idCardList = collection.mutable.ArrayBuffer[IDCard]()
  val UserIdCardMap = collection.mutable.Map[String, collection.mutable.ArrayBuffer[IDCard]]()

  val webLoginList = collection.mutable.ArrayBuffer[WebLogin]()
  val UserWebLoginMap = collection.mutable.Map[String, collection.mutable.ArrayBuffer[WebLogin]]()

  val bankAccountList = collection.mutable.ArrayBuffer[BankAccount]()
  val UserBankAccountMap = collection.mutable.Map[String, collection.mutable.ArrayBuffer[BankAccount]]()
  val start = 1000
  val end   = 2000
  val rnd = new scala.util.Random
  var mongoTemplate = new MongoTemplate(new Mongo("localhost"), "test");

  def getRandomNumber(): Int={
    return start + rnd.nextInt( (end - start) + 1 )
  }

  def addUser(us:User): String ={
    var key: Int = getRandomNumber()
    val key_string: String = "u-" + key.toString
    us.created_at_i = Calendar.getInstance().getTime
    us.user_id = key_string
    userMap.put(key_string, us)
    mongoTemplate.insert(us)
    key_string
  }

  def getUser(user_id:String): Option[User]={
    val results = mongoTemplate.find(new Query(Criteria where("user_id") is(user_id)), classOf[User], "user")
    //results.foreach(println)
    (for(result <- results) println(result))
    return userMap.get(user_id)
  }

  def updateUser(user_id: String, us:User): Option[User]={
    val org_user : Option[User] = userMap.get(user_id)
    if(org_user.isEmpty)
    {
      "User Not Found!"
    }
    else{
      val user: User = org_user.get
      us.created_at_i = user.created_at_i
      us.user_id = user_id
      us.updates_at_i = Calendar.getInstance().getTime
    }
    val usr = userMap.put(user_id, us)
    return userMap.get(user_id)
  }

  def createIdCard(user_id:String, id_card:IDCard): String={
    var key: Int = getRandomNumber()
    var key_string: String = "c-" + key.toString
    id_card.card_id_i = key_string
    id_card.user_id_i= user_id
    idCardList+=id_card
    UserIdCardMap.put(user_id, idCardList)
    mongoTemplate.insert(id_card)
    key_string
  }

  def getAllIdCards(user_id:String): Option[collection.mutable.ArrayBuffer[IDCard]]={
    //return idCardMap.get(user_id)
    return UserIdCardMap.get(user_id)
  }

  def deleteIdCard(user_id: String, id_card: String): Unit={
    val idList = UserIdCardMap.get(user_id).get
    idList.foreach { card: IDCard =>
      if(card.card_id_i == id_card) {
        idList.-(card)
      }
    }
  }

  def createWebLogin(user_id:String, web_login:WebLogin): String={
    var key: Int = getRandomNumber()
    var key_string: String = "i-" + key.toString
    web_login.login_id_i = key_string
    web_login.user_id_i= user_id
    webLoginList+=web_login
    UserWebLoginMap.put(user_id, webLoginList)
    mongoTemplate.insert(web_login)
    key_string
  }

  def getAllWebLogins(user_id:String): Option[collection.mutable.ArrayBuffer[WebLogin]]={
    return UserWebLoginMap.get(user_id)
  }

  def deleteWebLogin(user_id:String, login_id:String): Unit={
    val wLoginList = UserWebLoginMap.get(user_id).get
    wLoginList.foreach { web_login: WebLogin =>
      if(web_login.login_id_i == login_id) {
        wLoginList.-(web_login)
      }
    }
  }

  def createBankAccount(user_id:String, bank_account: BankAccount): String={
    var key: Int = getRandomNumber()
    var key_string: String = "b-" + key.toString
    bank_account.ba_id_i  = key_string
    bank_account.user_id_i= user_id
    bankAccountList+=bank_account
    UserBankAccountMap.put(user_id, bankAccountList)
    mongoTemplate.insert(bank_account)
    key_string
  }

  def getAllBankAccounts(user_id:String): Option[collection.mutable.ArrayBuffer[BankAccount]]={
    return UserBankAccountMap.get(user_id)
  }

  def deleteBankAccount(user_id:String, ba_id:String): Unit={
    val bAccountList = UserBankAccountMap.get(user_id).get
    bAccountList.foreach { bank_Account: BankAccount =>
      if(bank_Account.ba_id_i == ba_id) {
        bAccountList.-(bank_Account)
      }
    }
  }
}

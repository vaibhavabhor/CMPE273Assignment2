package wallet

import java.text.{SimpleDateFormat, DateFormat}
import java.util
import java.util._
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.{HttpHeaders, HttpStatus, ResponseEntity}
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation._
import org.springframework.web.context.request.WebRequest
import wallet.UserServices
import collection.JavaConverters._
import scala.beans.BeanProperty
import scala.collection.immutable.HashMap

/**
 * Created by vaibhavb on 9/19/14.
 */

@RestController
@RequestMapping(value = Array("/api/v1"))
class HelloWorld @Autowired() (us:UserServices){

  @RequestMapping(Array{"/"})
  def sayHello(): String =
  {
    "Hello World!!!"
  }

  // Validation, error response
  // Getter and setter to domain classes

  // User Operations
  @RequestMapping(value = Array("/users"), method = Array(RequestMethod.POST))
  def addUser(@Valid @RequestBody request: User, bindingResult: BindingResult, httpResponse : HttpServletResponse) = {
    if( bindingResult.hasErrors())
    {
      httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST)
      "Please provide all valid parameters in the request!"
    }
    else{
      val httpHeader: HttpHeaders = new HttpHeaders()
      httpResponse.setStatus(HttpServletResponse.SC_CREATED)
      val key: String = us.addUser(request)
      print(key)
      val df: DateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
      df.setTimeZone(TimeZone.getTimeZone("GMT"));
      //Map("user_id" -> key , "email" -> request.getEmail(), "password" -> request.getPassword(), "created_at" -> df.format(request.created_at_i)).asJava
      val userResponse: UserResponse = UserResponse(request.user_id, request.email_i, request.password_i, df.format(request.created_at_i))
      new ResponseEntity[UserResponse](userResponse, httpHeader, HttpStatus.OK)
    }
  }

  @RequestMapping(value = Array("/users/{userId}"), method = Array(RequestMethod.GET))
  @ResponseBody
  def getUser(@PathVariable userId: String, webRequest: WebRequest) = {
    val usr: Option[User] = us.getUser(userId)
    if(usr.isEmpty)
    {
      "User Not Found!"
    }
    else{
      val user: User = usr.get
      val hashCode = user.hashCode()
      if(!webRequest.checkNotModified(webRequest.getHeader("If-None-Match")))
      {
        val df: DateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        val httpHeader: HttpHeaders = new HttpHeaders()
        httpHeader.put("ETag", Arrays.asList[String](hashCode.toString))
        httpHeader.put("Cache-Control",Arrays.asList("max-age=1000"))
        val df1: DateFormat = new SimpleDateFormat("E,d MMM Y HH:mm:ss z")
        httpHeader.put("Last-Modified",  Arrays.asList[String](df1.format(user.created_at_i)))
        val hashMap: HashMap[String, String] = new HashMap()
        val userResponse: UserResponse = UserResponse(user.user_id, user.email_i, user.password_i, df.format(user.created_at_i))
        new ResponseEntity[UserResponse](userResponse, httpHeader, HttpStatus.OK)
      }
      else
      {
        new ResponseEntity[String](HttpStatus.NOT_MODIFIED)
      }
    }
  }

  @RequestMapping(value = Array("/users/{userId}"), method = Array(RequestMethod.PUT))
  def updateUser(@RequestBody request: User, @PathVariable userId: String, bindingResult: BindingResult, httpResponse : HttpServletResponse) = {
    if( bindingResult.hasErrors())
    {
      httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST)
      //new ErrorMessage("Error")
      "Please provide all valid parameters in the request!"
    }
    else {
      httpResponse.setStatus(HttpServletResponse.SC_CREATED)
      val usr: Option[User] = us.updateUser(userId, request)
      if (usr.isEmpty) {
        "User Not Found!"
      }
      else {
        val httpHeader: HttpHeaders = new HttpHeaders()
        val user: User = usr.get
        val df: DateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        //Map("user_id" -> user.user_id, "email" -> user.email_i, "password" -> user.password_i, "created_at" -> df.format(user.created_at_i)).asJava
        val userResponse: UserResponse = UserResponse(request.user_id, request.email_i, request.password_i, df.format(request.created_at_i))
        new ResponseEntity[UserResponse](userResponse, httpHeader, HttpStatus.OK)
      }
    }
  }

  // ID card operations
  @RequestMapping(value = Array("/users/{userId}/idcards"), method = Array(RequestMethod.POST))
  def addIdCard(@Valid @RequestBody request: IDCard, @PathVariable userId: String, bindingResult: BindingResult, httpResponse : HttpServletResponse) = {
    if( bindingResult.hasErrors())
    {
      httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST)
      //new ErrorMessage("Error")
      "Please provide all valid parameters in the request!"
    }
    else{
      val httpHeader: HttpHeaders = new HttpHeaders()
      httpResponse.setStatus(HttpServletResponse.SC_CREATED)
      val key: String = us.createIdCard(userId, request)
      val idCardResponse: IDCardResponse = IDCardResponse(key, request.card_name_i, request.card_number_i, request.expiration_date_i)
      new ResponseEntity[IDCardResponse](idCardResponse, httpHeader, HttpStatus.OK)
      //Map("card_id" -> key, "card_name" -> request.card_name_i, "card_number" -> request.card_number_i, "expiration_date" -> request.expiration_date_i).asJava
    }
  }

  @RequestMapping(value = Array("/users/{userId}/idcards"), method = Array(RequestMethod.GET))
  @ResponseBody
  def getIdCards(@PathVariable userId: String) = {
    val idc: Option[collection.mutable.ArrayBuffer[IDCard]] = us.getAllIdCards(userId)
    if(idc.isEmpty)
    {
      "ID Card Not Found!"
    }
    else{
      val idcards: collection.mutable.ArrayBuffer[IDCard] = idc.get
      (for(idcard <- idcards) yield idcard).asJava
    }
  }

  @RequestMapping(value = Array("/users/{userId}/idcards/{cardId}"), method = Array(RequestMethod.DELETE))
  def deleteIdCard(@PathVariable userId: String, @PathVariable cardId: String)={
    us.deleteIdCard(userId, cardId)
    "IDCard"+ cardId + "deleted successfully!"
  }

  // Web Login operations
  @RequestMapping(value = Array("/users/{userId}/weblogins"), method = Array(RequestMethod.POST))
  def addWebLogin(@Valid @RequestBody request: WebLogin, @PathVariable userId: String, bindingResult: BindingResult, httpResponse : HttpServletResponse) = {
    if (bindingResult.hasErrors()) {
      httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST)
      //new ErrorMessage("Error")
      "Please provide all valid parameters in the request!"
    }
    else {
      httpResponse.setStatus(HttpServletResponse.SC_CREATED)
      val key: String = us.createWebLogin(userId, request)
      //key WebLoginResponse
      val httpHeader: HttpHeaders = new HttpHeaders()
      val webLoginResponse: WebLoginResponse = WebLoginResponse(key, request.url_i, request.login_i, request.password_i)
      new ResponseEntity[WebLoginResponse](webLoginResponse, httpHeader, HttpStatus.OK)
      //Map("login_id" -> key, "url" -> request.url_i, "login" -> request.login_i, "password" -> request.password_i).asJava
    }
  }

  @RequestMapping(value = Array("/users/{userId}/weblogins"), method = Array(RequestMethod.GET))
  @ResponseBody
  def getWebLogins(@PathVariable userId: String) ={
    val wl: Option[collection.mutable.ArrayBuffer[WebLogin]] = us.getAllWebLogins(userId)
    if(wl.isEmpty){
      "Web Login Not Found!"
    }
    else
    {
      val weblogins: collection.mutable.ArrayBuffer[WebLogin] = wl.get
      (for(weblogin <- weblogins) yield weblogin).asJava
    }
  }

  @RequestMapping(value = Array("/users/{userId}/weblogins/{loginId}"), method = Array(RequestMethod.DELETE))
  def deleteWebLogin(@PathVariable userId: String, @PathVariable loginId: String)={
    us.deleteWebLogin(userId, loginId)
    "Web Login"+ loginId + "deleted successfully!"
  }

  // Bank Account operations
  @RequestMapping(value = Array("/users/{userId}/bankaccounts"), method = Array(RequestMethod.POST))
  def addBankAccount(@Valid @RequestBody request: BankAccount, @PathVariable userId: String, bindingResult: BindingResult, httpResponse : HttpServletResponse) = {
    if (bindingResult.hasErrors()) {
      httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST)
      //new ErrorMessage("Error")
      "Please provide all valid parameters in the request!"
    }
    else {
      httpResponse.setStatus(HttpServletResponse.SC_CREATED)
      val key: String = us.createBankAccount(userId, request)
      val httpHeader: HttpHeaders = new HttpHeaders()

      val bankAccountResponse: BankAccountResponse = BankAccountResponse(key, request.account_name_i, request.routing_number_i, request.getAccount_name_i)
      new ResponseEntity[BankAccountResponse](bankAccountResponse, httpHeader, HttpStatus.OK)
      //Map("ba_id" -> key, "account_name" -> request.account_name_i, "routing_number" -> request.routing_number_i, "account_number" -> request.getAccount_name_i).asJava
    }
  }

  @RequestMapping(value = Array("/users/{userId}/bankaccounts"), method = Array(RequestMethod.GET))
  @ResponseBody
  def getBankAccounts(@PathVariable userId: String) ={
    val ba: Option[collection.mutable.ArrayBuffer[BankAccount]] = us.getAllBankAccounts(userId)
    if(ba.isEmpty){
      "Bank Account Not Found!"
    }
    else
    {
      val bankaccounts: collection.mutable.ArrayBuffer[BankAccount] = ba.get
      (for(bankaccount <- bankaccounts) yield bankaccount).asJava
    }
  }

  @RequestMapping(value = Array("/users/{userId}/bankaccounts/{baId}"), method = Array(RequestMethod.DELETE))
  def deleteBankAccount(@PathVariable userId: String, @PathVariable baId: String)={
    us.deleteBankAccount(userId, baId)
    "Bank Account"+ baId + "deleted successfully!"
  }
}

case class UserResponse(@BeanProperty user_id : String,@BeanProperty email : String,@BeanProperty password : String,@BeanProperty created_at: String)

case class IDCardResponse(@BeanProperty card_id : String,@BeanProperty card_name : String,@BeanProperty card_number : String,@BeanProperty expiration_date: String)

case class WebLoginResponse(@BeanProperty login_id : String,@BeanProperty url : String,@BeanProperty login : String,@BeanProperty password: String)

case class BankAccountResponse(@BeanProperty ba_id : String,@BeanProperty account_name : String,@BeanProperty routing_number : String,@BeanProperty account_number: String)
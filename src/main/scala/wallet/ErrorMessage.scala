package wallet

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by vaibhavb on 10/1/14.
 */
class ErrorMessage(@JsonProperty("message") message: String)
{

}

package org.leanpoker.player

import com.google.gson.{JsonPrimitive, JsonObject}
import org.scalatest.FunSuite

/**
 * Created by fditrani on 31/10/15.
 */
class Player$Test extends FunSuite {

  test("print json"){

    val request: JsonObject = new JsonObject()
    val nestedJson = new JsonObject()
    nestedJson.add("nested", new JsonPrimitive("hello"))
    request.add("key", nestedJson)

    val jsonObject = request.getAsJsonObject()
    val element = jsonObject.get("key").getAsJsonObject().get("nested").getAsString()
    println(element)


    Player.betRequest(request)

  }


}

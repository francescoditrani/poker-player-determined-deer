package org.leanpoker.player

import com.google.gson.{JsonPrimitive, JsonObject}
import org.scalatest.FunSuite

/**
 * Created by fditrani on 31/10/15.
 */
class Player$Test extends FunSuite {

  test("tris in test"){


    assert(!Player.isTrisIn(Seq("1","2","1","3")))
    assert(Player.isTrisIn(Seq("1","2","1","3","1")))

//    assert(!Player.isGoodDoubleIn(Seq("8","7","8")))
//    assert(Player.isGoodDoubleIn(Seq("8","10","10")))

    assert(Player.twoCouple(Seq("8","10","10","8")))
    assert(!Player.twoCouple(Seq("8","10","10")))

//    val request: JsonObject = new JsonObject()
//    val nestedJson = new JsonObject()
//    nestedJson.add("nested", new JsonPrimitive("hello"))
//    request.add("key", nestedJson)
//
//    val jsonObject = request.getAsJsonObject()
//    val element = jsonObject.get("key").getAsJsonObject().get("nested").getAsString()
//    println(element)
//
//
//    Player.betRequest(request)

  }


}

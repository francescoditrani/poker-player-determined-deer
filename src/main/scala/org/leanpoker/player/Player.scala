package org.leanpoker.player

import com.google.gson.{JsonObject, JsonElement}

object Player {

  val VERSION = "Default Scala folding player"

//
//  def getMyCards(meAsAPlayer: JsonObject): Seq[String] = {
//    val holeCards = meAsAPlayer.get("hole_cards").getAsJsonArray()
//    holeCards.iterator().
//  }

  def betRequest(request: JsonElement) = {

    try {


      val jsonObject = request.getAsJsonObject()

      val jsonRequest: JsonObject = jsonObject.getAsJsonObject()

      val myIndex: Int = jsonRequest.get("in_action").getAsInt()
      val arrayOfPlayers = jsonRequest.get("players").getAsJsonArray()


      val meAsAPlayer = arrayOfPlayers.get(myIndex).getAsJsonObject()


      val currentStatus = meAsAPlayer.get("status").getAsString()

      currentStatus match {
        case "active" => calculateBetForPlayer(jsonObject, meAsAPlayer)
        case _ => 0
      }


    }
    catch {
      case e: Exception => {
        println(e.getMessage)
        0
      }
    }


  def calculateBetForPlayer(jsonObject: JsonObject, meAsAPlayer: JsonObject) = {
    val largest_current = jsonObject.get("current_buy_in").getAsInt()

    val stack = meAsAPlayer.get("stack").getAsInt()
    val bet = meAsAPlayer.get("bet").getAsInt()

//    val myCards: Seq[String] = getMyCards(meAsAPlayer)

    val playable = stack-bet

    val call = largest_current - bet

    call
  }


  }

  def showdown(game: JsonElement) {

  }
}

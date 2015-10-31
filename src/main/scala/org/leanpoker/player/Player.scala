package org.leanpoker.player

import com.google.gson.{JsonObject, JsonElement}
import scala.collection.JavaConversions._

object Player {

  val VERSION = "Default Scala folding player"


  def getMyCards(meAsAPlayer: JsonObject): Seq[String] = {
    val holeCards = meAsAPlayer.get("hole_cards").getAsJsonArray()
    val holeCardsString = holeCards.iterator().toSeq.map( (cardJsonElement) => {
      cardJsonElement.getAsJsonObject().get("rank").getAsString()
    })
    println(holeCardsString)
    holeCardsString
  }


  def goodCards() = {
    true
  }



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
  }


  def calculateBetForPlayer(jsonObject: JsonObject, meAsAPlayer: JsonObject) = {
    val largest_current = jsonObject.get("current_buy_in").getAsInt()

    val stack = meAsAPlayer.get("stack").getAsInt()
    val bet = meAsAPlayer.get("bet").getAsInt()

    val myCards: Seq[String] = getMyCards(meAsAPlayer)

    val playable = stack-bet

    val call = largest_current - bet

    println(s"$largest_current $stack, $bet,$myCards, $playable, $call"  )

    myCards match {
      case _ if goodCards() => call
      case _ => 0
    }


  }



  def showdown(game: JsonElement) {

  }
}

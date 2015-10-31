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


  def goodSingleCard(singleCard: String): Boolean = {
    Seq("J","Q","K","A", "10").contains(singleCard)

  }



  def aGoodCardIn(myCards: Seq[String]): Boolean = {
    myCards.exists(goodSingleCard)
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

  def areCoupleOfCardsIn(myCards: Seq[String]): Boolean = {
    myCards.toSet.size < myCards.size
  }


  def getCommunityCards(jsonObject: JsonObject): Seq[String] = {
    jsonObject.get("community_cards").getAsJsonArray()
      .iterator().toSeq.map( (cardJsonElement) => {
      cardJsonElement.getAsJsonObject().get("rank").getAsString()
    })
  }

//  def isTrisIn(cards: Seq[String]) = {
//
//  }

  def calculateBetForPlayer(jsonObject: JsonObject, meAsAPlayer: JsonObject) = {
    val largest_current = jsonObject.get("current_buy_in").getAsInt()

    val stack = meAsAPlayer.get("stack").getAsInt()
    val bet = meAsAPlayer.get("bet").getAsInt()

    val myCards: Seq[String] = getMyCards(meAsAPlayer)
    val communityCards: Seq[String] = getCommunityCards(jsonObject)

    val playable = stack-bet

    val call = largest_current - bet
    val minimum_raise = jsonObject.get("minimum_raise").getAsInt()
    val raise = call + minimum_raise

    println(s"$largest_current $stack, $bet,$myCards, $playable, $call"  )




    myCards match {
//      case _ if isTrisIn(myCards ++ communityCards) => raise + minimum_raise
      case _ if areCoupleOfCardsIn(myCards ++ communityCards) => raise
      case _ if aGoodCardIn(myCards) => call
      case _ => 0
    }


  }



  def showdown(game: JsonElement) {

  }
}

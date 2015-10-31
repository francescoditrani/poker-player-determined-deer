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
    Seq("Q","K","A").contains(singleCard)

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

  def isGoodDoubleIn(cards: Seq[String]): Boolean = {
    val sequenceOfTris: Seq[Boolean] = cards.groupBy(identity).map { case (item, duplicates) => {
      duplicates.size >= 2 && Seq("J","Q","K","A","10","9").contains(item)
    }
    }.toSeq
    sequenceOfTris.contains(true)
  }


  def getCommunityCards(jsonObject: JsonObject): Seq[String] = {
    jsonObject.get("community_cards").getAsJsonArray()
      .iterator().toSeq.map( (cardJsonElement) => {
      cardJsonElement.getAsJsonObject().get("rank").getAsString()
    })
  }


  def isTrisIn(cards: Seq[String]):Boolean = {

    val sequenceOfTris: Seq[Boolean] = cards.groupBy(identity).map { case (item, duplicates) => {
      duplicates.size >= 3
    }
    }.toSeq
    sequenceOfTris.contains(true)

  }


  def currentPotkLow(pot: Int, small_blind: Int): Boolean = {
    val onlyBlinds = 8*small_blind
    pot <= onlyBlinds
  }

//  def goodCommonNotForMyCards(communityCards: Seq[String], myCards: Seq[String]) = {
//    communityCards.groupBy(identity)
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
    val doubleraise = call + minimum_raise*2
    val small_blind = jsonObject.get("small_blind").getAsInt()

    val pot = jsonObject.get("pot").getAsInt()
    val bet_index = jsonObject.get("bet_index").getAsInt()



    myCards match {
//      case _ if goodCommonNotForMyCards(communityCards, myCards) => 0
      case _ if isTrisIn(myCards ++ communityCards) => doubleraise
      case _ if isGoodDoubleIn(myCards ++ communityCards) => call
      case _ if small_blind == 160 => 0
      case _ if aGoodCardIn(myCards) && currentPotkLow(pot, small_blind) => call
//      case _ if bet_index == 6 && currentPotkLow(pot, small_blind) => raise
      case _ => 0
    }


  }



  def showdown(game: JsonElement) {

  }
}

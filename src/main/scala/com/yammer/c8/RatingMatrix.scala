package com.yammer.c8

import scala.collection.mutable.Set
import scala.collection.mutable.Seq
import scala.collection.mutable.HashMap

object RatingMatrix {
  // userId:items => 1,2,3,...
  // userId:<rating> => 1,3,...
  val userIndex = new HashMap[String, Set[Int]]
  
  // itemId:users => 1,2,3,...
  // itemId:<rating> => 1,3,...
  val itemIndex = new HashMap[String, Set[Int]]
  
  // userId => itemId,rating,itemId,rating
  val userRatings = new HashMap[String, Seq[Int]]
  
  // itemId => userId,rating,userId,rating
  val itemRatings = new HashMap[String, Seq[Int]]
  
  def insert(userId:Int, itemId:Int, rating:Int) {
    insertUserIndex(userId, itemId, rating)
    insertItemIndex(userId, itemId, rating)
    insertUserRating(userId, itemId, rating)
    insertItemRating(userId, itemId, rating)
  }
  
  def insertUserIndex(userId:Int, itemId:Int, rating:Int) {
    val itemKey = userId.toString() + ":items"
    val ratingKey = userId.toString() + ":" + rating.toString()
    
    List(itemKey, ratingKey).foreach(
      key =>
        userIndex.get(key) match {
          case Some(userIdx) =>
            userIdx + itemId
          case None => 
            userIndex.put(key, Set(itemId))
        }
    )
  }
  
  def insertItemIndex(userId:Int, itemId:Int, rating:Int) {
    val userKey = itemId.toString() + ":users"
    val ratingKey = itemId.toString() + ":" + rating.toString()
    
    List(userKey, ratingKey).foreach(
      key =>
        itemIndex.get(key) match {
          case Some(itemIdx) =>
            itemIdx + userId
          case None => 
            itemIndex.put(key, Set(userId))
        }
    )
  }
  
  def insertUserRating(userId:Int, itemId:Int, rating:Int) {
    val itemKey = itemId.toString()
    
    userRatings.get(itemKey) match {
      case Some(userRatings) =>
        userRatings ++ Seq(itemId, rating)
      case None =>
        userRatings.put(itemKey, Seq(itemId, rating))
    }
  }
  
  def insertItemRating(userId:Int, itemId:Int, rating:Int) {
    val userKey = userId.toString()
    
    itemRatings.get(userKey) match {
      case Some(itemRatings) =>
        itemRatings ++ Seq(userId, rating)
      case None =>
        itemRatings.put(userKey, Seq(userId, rating))
    }
  }
    
  /*
   * Ways to access information
   */
  
  // Returns every user's rating for an item
  def findRatingsForItem(itemId:Int):HashMap[Int, Int] = {
    var ratings = new HashMap[Int, Int]
    
    // findUsersWhoRated(itemId) match {
    //   case None =>
    //     return ratings
    //   case Some(users) =>
    //     val user = users.iterator()
    //     var userId = user.nextDoc()
    //     while(userId != DocIdSetIterator.NO_MORE_DOCS) {
    //       findRating(userId, itemId) match {
    //         case None =>
    //           // do nothing?
    //         case Some(rating) =>
    //           ratings.put(userId, rating)
    //       }
    // 
    //       userId = user.nextDoc()
    //     }
    // }
      
    return ratings
  }
  
  // Returs every item's rating from a user
  def findRatingsForUser(userId:Int):HashMap[Int, Int] = {
    var ratings = new HashMap[Int, Int]
    
    // findItemsRatedBy(userId) match {
    //   case None =>
    //     return ratings
    //   case Some(items) =>
    //     val item = items.iterator()
    //     var itemId = item.nextDoc()
    //     while(itemId != DocIdSetIterator.NO_MORE_DOCS) {
    //       findRating(userId, itemId) match {
    //         case None =>
    //           // do nothing?
    //         case Some(rating) =>
    //           ratings.put(itemId, rating)
    //       }
    // 
    //       itemId = item.nextDoc()
    //     }
    // }
      
    return ratings
  }
  
  // Returns the set of users who rated the item
  def findUsersWhoRated(itemId:Int):Option[Set[Int]] = {
    itemIndex.get(itemId.toString() + ":users")
  }
  
  // Returns the set of users who rated the item 'rating'
  def findUsersWithRatings(itemId:Int, rating:Int):Option[Set[Int]] = {
    itemIndex.get(itemId.toString() + ":" + rating.toString())
  }
  
  // Returns the set of items rated by a user
  def findItemsRatedBy(userId:Int):Option[Set[Int]] = {
    userIndex.get(userId.toString() + ":items")
  }
  
  // Returns the set of items rated 'rating' by the user
  def findItemsWithRatings(userId:Int, rating:Int):Option[Set[Int]] = {
    userIndex.get(userId.toString() + ":" + rating.toString())
  }
}
package com.yammer.c8

import java.util._
import com.kamikaze.docidset.api._
import com.kamikaze.docidset.impl._
import com.kamikaze.docidset.utils._
import scala.collection.mutable.HashMap
import org.apache.lucene.search.DocIdSetIterator

object RatingMatrix {
  val data = new HashMap[String, Int]
  val indices = new HashMap[String, DocSet]
  
  // We need to look for existing rating in the matrix 
  //  and swap the indices to the new value if they exist
  
  def insert(user_id:Int, item_id:Int, rating:Int) {
    // Class Information
    insert("classes", rating)
    
    // Data
    data.put(item_id.toString() + user_id.toString(), rating)
    
    // Item Indices
    insert(item_id.toString() + ":users", user_id)
    insert(item_id.toString() + ":" + rating.toString(), user_id)
    
    // User Indices
    insert(user_id.toString() + ":items", item_id)
    insert(user_id.toString() + ":" + rating.toString(), item_id)
  }
  
  def findClasses():Option[DocSet] = {
    indices.get("classes")
  }
  
  def findRating(user_id:Int, item_id:Int):Option[Int] = {
    data.get(item_id.toString() + user_id.toString())
  }
  
  // Returns every user's rating for an item
  def findRatings(item_id:Int):HashMap[Int, Int] = {
    var ratings = new HashMap[Int, Int]
    
    findUsersWhoRated(item_id) match {
      case None =>
        return ratings
      case Some(raters) =>
        val rater = raters.iterator()
        var user_id = rater.nextDoc()
        while(user_id != DocIdSetIterator.NO_MORE_DOCS) {
          findRating(user_id, item_id) match {
            case None =>
              // do nothing?
            case Some(rating) =>
              ratings.put(user_id, rating)
          }

          user_id = rater.nextDoc()
        }
    }
      
    return ratings
  }
  
  // Returns the set of users who rated the item
  def findUsersWhoRated(item_id:Int):Option[DocSet] = {
    indices.get(item_id.toString() + ":users")
  }
  
  // Returns the set of users who rated the item 'rating'
  def findUsersWithRatings(item_id:Int, rating:Int):Option[DocSet] = {
    indices.get(item_id.toString() + ":" + rating.toString())
  }
  
  // Returns the set of items rated by a user
  def findItemsRatedBy(user_id:Int):Option[DocSet] = {
    indices.get(user_id.toString() + ":items")
  }
  
  // Returns the set of items rated 'rating' by the user
  def findItemsWithRatings(user_id:Int, rating:Int):Option[DocSet] = {
    indices.get(user_id.toString() + ":" + rating.toString())
  }
  
  protected def insert(key:String, value:Int) {
    indices.get(key) match {
      case None => 
        val docset = PForDeltaDocSetFactory.getPForDeltaDocSetInstance()
        docset.addDoc(value)
        indices.put(key, docset)
      case Some(docset) => 
        docset.find(value) match {
          case false => 
            docset.addDoc(value)
          case true =>
            // do nothing
        }
    }
  }
}
package com.yammer.c8

import com.kamikaze.docidset.api._
import com.kamikaze.docidset.impl._
import com.kamikaze.docidset.utils._
import scala.collection.mutable.HashMap

object UserRatingMatrix extends RatingMatrix {
  val ratings = new HashMap[String, DocSet]
  
  def insert(user_id:Int, item_id:Int, rating:Int):Boolean = {
    // look for existing rating in the matrix and swap
    //  the indices to the new value if they exist
    updateIndices(user_id, item_id, rating)
    return true
  }
  
  def updateIndices(user_id:Int, item_id:Int, rating:Int) {
    val item_raters_key = item_id.toString() + ":raters"
    val item_ratings_key = item_id.toString() + ":ratings"
    val user_all_key = user_id.toString() + ":all"
    val user_rating_key = user_id.toString() + ":" + rating.toString()
    
    // ub_ratings["#{item_id}"] << "#{user_id}:#{rating}"
    // ub_ratings["#{user_id}:all"] << item_id
    // ub_ratings["#{user_id}:#{rating}"] << item_id
    
    // docset.addDoc(user_id)
    // ratings.put(item_raters_key, docset)
    
    val result = ratings.get(item_id.toString())
    
    result match {
      case None => 
        val docset = PForDeltaDocSetFactory.getPForDeltaDocSetInstance()
        docset.addDoc(user_id)
        ratings.put(item_raters_key, docset)
      case Some(docset) => 
        docset.addDoc(user_id)
    }
  }  
}
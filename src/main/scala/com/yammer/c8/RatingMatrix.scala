package com.yammer.c8

import com.kamikaze.docidset.api._
import com.kamikaze.docidset.impl._
import com.kamikaze.docidset.utils._
import scala.collection.mutable.HashMap

// {api, impl, utils}

object RatingMatrix {
  val ratings = new HashMap[String, DocSet]
    
  def add(user_id:Int, item_id:Int, rating:Int) {
    addUserVectors(user_id, item_id, rating)
    addItemVectors(user_id, item_id, rating)
  }
  
  def addUserVectors(user_id:Int, item_id:Int, rating:Int) {
    val item_raters_key = item_id.toString() + ":raters"
    val item_ratings_key = item_id.toString() + ":ratings"
    val user_all_key = user_id.toString() + ":all"
    val user_rating_key = user_id.toString() + ":" + rating.toString()
    
    // ub_ratings["#{item_id}"] << "#{user_id}:#{rating}"
    // ub_ratings["#{user_id}:all"] << item_id
    // ub_ratings["#{user_id}:#{rating}"] << item_id
    
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
  
  def addItemVectors(user_id:Int, item_id:Int, rating:Int) {
    
  }
  
  
}
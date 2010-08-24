package com.yammer.c8

import com.kamikaze.docidset.api._
import com.kamikaze.docidset.impl._
import com.kamikaze.docidset.utils._
import scala.collection.mutable.HashMap

object UserRatingMatrix extends RatingMatrix {
  val data = new HashMap[String, Int]
  val indices = new HashMap[String, DocSet]
  
  // We need to look for existing rating in the matrix 
  //  and swap the indices to the new value if they exist
  
  def insert(user_id:Int, item_id:Int, rating:Int) {
    // Data
    data.put(item_id.toString() + user_id.toString(), rating)
    
    // Indices
    insert(item_id.toString() + ":rater", user_id)
    insert(item_id.toString() + ":rating", rating)
    insert(user_id.toString() + ":rated", item_id)
    insert(user_id.toString() + ":" + rating.toString(), item_id)
  }
  
  def find(user_id:Int, item_id:Int):Option[Int] = {
    data.get(item_id.toString() + user_id.toString())
  }
  
  protected def insert(key:String, value:Int) {
    indices.get(key) match {
      case None => 
        val docset = PForDeltaDocSetFactory.getPForDeltaDocSetInstance()
        docset.addDoc(value)
        indices.put(key, docset)
      case Some(docset) => 
        docset.addDoc(value)
    }
  }
}
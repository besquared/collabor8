package com.yammer.c8

import com.kamikaze.docidset.api._
import com.kamikaze.docidset.impl._
import com.kamikaze.docidset.utils._

// {api, impl, utils}

object RatingMatrix {
  val docset = PForDeltaDocSetFactory.getPForDeltaDocSetInstance()
  
  def add(user_id:Int, item_id:Int, rating:Int) {
    docset.addDoc(user_id)
    return true
  }
  
  def updateUserVectors(user_id:Int, item_id:Int, rating:Int) {
    
  }
  
  def updateItemVectors(user_id:Int, item_id:Int, rating:Int) {
    
  }
  
  
}
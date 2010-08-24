package com.yammer.c8

trait RatingMatrix {
  def insert(user_id:Int, item_id:Int, rating:Int): Boolean
}
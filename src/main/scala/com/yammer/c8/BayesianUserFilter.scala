package com.yammer.c8

import java.util._

object BayesianUserFilter {
  def classify(user_id:Int, items:ArrayList[Int], neighborhood:ArrayList[Int] = new ArrayList[Int], candidates:ArrayList[Int] = new ArrayList[Int]) {
    val scores = new HashMap[Int, Double]
    
    // find all users 
  }
}
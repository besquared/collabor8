package com.yammer.c8

import scala.collection.mutable.Set
import scala.collection.mutable.Seq
import scala.collection.mutable.HashMap

class BayesianUserFilter(ratings:RatingMatrix) {
  def classify(userId:Int, itemIds:Set[Int], neighborhood:Set[Int]) {
    // classify all items
  }
  
  def classify(userId:Int, itemId:Int, neighborhood:Set[Int]) {
    val scores = new HashMap[Int, Double]
    val userRatings = ratings.findItemsRatedBy(userId)
    
    List(-1, 1).foreach(
      classJ => {
        scores(classJ) = 1
        var allRatingsCount = 0.0
        var classRatingsCount = 0.0
        val userClassRatings = ratings.findItemsWithRatings(userId, classJ)
        
        for((raterId, rating) <- ratings.findRatingsForItem(itemId)) {
          if(userId != raterId && neighborhood.contains(raterId)) {
            val raterRatings = ratings.findItemsRatedBy(raterId)
            val raterClassRatings = ratings.findItemsWithRatings(raterId, classJ)
            
            allRatingsCount += raterRatings.size
            classRatingsCount += raterClassRatings.size
            
            val similarRatings = raterRatings.getOrElse(Set.empty) & userRatings.getOrElse(Set.empty)
            val matchingRatings = raterClassRatings.getOrElse(Set.empty) & userClassRatings.getOrElse(Set.empty)
            
            // With Laplacian Smoothness Correction
            scores(classJ) *= (matchingRatings.size + 1) / (similarRatings.size + 2)
          }
        }
        
        scores(classJ) *= classRatingsCount / allRatingsCount
      }
    )
  }
}
package com.yammer.c8.tests

import com.codahale.simplespec.Spec
import com.yammer.c8.RatingMatrix

object RatingMatrixSpec extends Spec {
  class `A ratings matrix` {
    val matrix = RatingMatrix
    matrix.insert(1, 1, -1)
    matrix.insert(2, 1, 1)
    matrix.insert(1, 2, 1)

    def `should find entries` {
      matrix.findRating(1, 1) must beSome(-1)
      matrix.findRating(1, 2) must beSome(1)
    }
    
    def `should find all classes` {
      matrix.findClasses match {
        case None =>
          return false
        case Some(classes) =>
          classes.size must be(2)
      }
    }
    
    def `should find all ratings for an item` {
      val ratings = matrix.findRatingsForItem(1)
      ratings must havePair((1, -1))
      ratings must havePair((2, 1))
    }
    
    def `should find all users who rated an item` {
      matrix.findUsersWhoRated(1) match {
        case None =>
          return false
        case Some(users) =>
          users.size must be(2)
      }
    }
  }
}
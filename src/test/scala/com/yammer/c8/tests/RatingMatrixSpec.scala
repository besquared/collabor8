package com.yammer.c8.tests

import com.codahale.simplespec.Spec
import com.yammer.c8.RatingMatrix
import org.apache.lucene.search.DocIdSetIterator

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
      val classes = matrix.findClasses
      
      classes must beSomething
    }
    
    def `should find all ratings` {
      val ratings = matrix.findRatings(1)
      ratings must havePair((1, -1))
      ratings must havePair((2, 1))
    }
    
    def `should find all users who rated an item` {
      matrix.findUsersWhoRated(1) match {
        case None =>
          return false
        case Some(users) =>
          val iter = users.iterator()
          var user_id = iter.nextDoc()
          while(user_id != DocIdSetIterator.NO_MORE_DOCS) {
            println(user_id)
            user_id = iter.nextDoc()
          }
          users.size must be(2)
      }
    }
  }
}
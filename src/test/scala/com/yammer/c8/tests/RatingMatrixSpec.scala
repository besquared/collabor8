package com.yammer.c8.tests

import com.codahale.simplespec.Spec
import com.yammer.c8.UserRatingMatrix

object RatingMatrixSpec extends Spec {
  class `A normal ratings matrix` {
    val matrix = RatingMatrix

    def `should insert new entries` {
      matrix.insert(1, 1, -1)
      matrix.find(1, 1) must beSome(-1)
    }
  }
}
package com.yammer.c8.tests

import com.yammer.c8.RatingMatrix
import com.codahale.simplespec.Spec

object RatingMatrixSpec extends Spec {
  class `A normal ratings matrix` {
    val ratings = RatingMatrix

    def `should have the proper number of entries` {
      true
    }
  }
}
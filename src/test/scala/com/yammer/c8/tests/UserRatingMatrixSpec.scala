package com.yammer.c8.tests

import com.codahale.simplespec.Spec
import com.yammer.c8.UserRatingMatrix

object UserRatingMatrixSpec extends Spec {
  class `A normal ratings matrix` {
    val matrix = UserRatingMatrix

    def `should insert new entries` {
      matrix.insert(1, 1, -1)
      matrix.find(1, 1) must beSome(-1)
    }
  }
}
package com.yammer.c8

import com.kamikaze.docidset.api._
import com.kamikaze.docidset.impl._
import com.kamikaze.docidset.utils._

// {api, impl, utils}

object RatingMatrix {
  def test() {
    var pForDeltaDocSet = PForDeltaDocSetFactory.getPForDeltaDocSetInstance()
    pForDeltaDocSet.addDoc(100)
    return true
  }
}
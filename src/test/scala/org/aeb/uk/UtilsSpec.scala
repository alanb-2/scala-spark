package org.aeb.uk

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import org.uk.aeb.Utils

class UtilsSpec extends AnyFlatSpec with should.Matchers {

  "Utils" should "add two integers together" in {
    Utils.add(1, 2) shouldEqual 3
  }

}

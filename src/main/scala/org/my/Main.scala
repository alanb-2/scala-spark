package org.my

object Main {

  def main(args: Array[String]): Unit =
    println(s"Hello, world with ${args.mkString("[", ", ", "]")}")

}

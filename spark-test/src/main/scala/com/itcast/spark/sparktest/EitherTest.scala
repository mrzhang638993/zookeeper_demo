package com.itcast.spark.sparktest


object EitherTest {

  /**
   * 测试spark的either测试方法执行操作实现
   * */
  def testEither(b: Double): Double = {
    val a = 10.0
    a / b
  }

  // Either 对应的是LEFT以及Right执行相关的语句操作实现的.LEFT和Right并不指定具体的类别的
  def save(f: Double => Double, b: Double): Either[Double, (Double, Exception)] = {
    try {
      var result=testEither(b)
      Left(result)
    } catch {
      case e:Exception=>Right(b,e)
    }
  }

  def main(args: Array[String]): Unit = {
    val result: Either[Double, (Double, Exception)] = save(testEither, 0.0)
    result match {
      case Left(a) =>println(a)
      case Right(b) =>println(b._1+"----"+b._2)
    }
  }
}

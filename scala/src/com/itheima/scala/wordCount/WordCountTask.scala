package com.itheima.scala.wordCount

case class WordCountTask(fileName:String){
  def apply(fileName: String): WordCountTask = new WordCountTask(fileName)
}
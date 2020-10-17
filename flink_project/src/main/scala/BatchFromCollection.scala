import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment, _}

object BatchFromCollection {

  def main(args: Array[String]): Unit = {
    // 创建批处理运行环境
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    // 加载本地数据
    val localElements: DataSet[String] = env.fromElements("spark", "flink", "hadoop")
    localElements.print()
    //下面测试读取元组数据
    val turpleElements: DataSet[(String, Int)] = env.fromElements(("spark", 1), ("flink", 2))
    turpleElements.print()
    // 创建dataset数据集
    val dataset: DataSet[String] = env.fromCollection(List[String]("flink", "spark", "hadoop"))
    dataset.print()
    // 加载生成序列
    val seq: DataSet[Long] = env.generateSequence(1, 9)
    seq.print()
  }
}

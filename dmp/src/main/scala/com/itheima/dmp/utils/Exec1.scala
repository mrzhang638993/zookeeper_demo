package com.itheima.dmp.utils

import org.apache.spark.sql.{DataFrame, SparkSession}

object Exec1 {

  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession.builder()
      .appName("myjob")
      .master("local[6]")
      .getOrCreate()
    // spark 读取数据库的数据
    val dept: DataFrame = spark.read
      .format("jdbc")
      .option("url", "jdbc:mysql://192.168.1.205:3306/spark_sql")
      .option("driver", "com.mysql.jdbc.Driver")
      .option("dbtable", "dept")
      .option("user", "root")
      .option("password", "123456")
      .load()
    val emp: DataFrame = spark.read
      .format("jdbc")
      .option("url", "jdbc:mysql://192.168.1.205:3306/spark_sql")
      .option("driver", "com.mysql.jdbc.Driver")
      .option("dbtable", "emp")
      .option("user", "root")
      .option("password", "123456")
      .load()
    emp.createOrReplaceTempView("emp")
    dept.createOrReplaceTempView("dept")
    //获取每个部门下面存在多少员工
    spark.sql("select  count(1) as num,dept.deptno as dept from  dept ,emp  where dept.deptno=emp.deptno  group by dept.deptno")
      .show()
    // 列出最低薪金大于1500的各种工作
    spark.sql("select  job,min(sal) as sal  from emp group by job  having (sal)>1500").show()
    // 列出薪金高于公司平均薪金的所有员工
    spark.sql("select t.ename from (select ename,sal,(select avg(p.sal)  from emp p)  as av  from emp )t where t.sal>t.av").show()
  }
}

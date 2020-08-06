----使用自连接的方式即可实现的（实现连续3个月的历史记录）
select a.name ,a.month,b.month,c.month,a.degree,b.degree,c.degree  from  student a
 join student b on a.name=b.name
 join student c  on a.name=c.name
 where     cast(substr(a.month,6,1) as int)=cast(substr(b.month,6,1) as int)-1
and cast(substr(b.month,6,1) as int)=cast(substr(c.month,6,1) as int)-1
and a.degree='A'and b.degree='A' and c.degree='A';
-----下面是老师的答案，不便于理解
----疑问信息：怎么可以扩展为多个字段的操作逻辑
create table if not exists  student(name string,month string,degree string) row format delimited fields terminated by ' ';
load data local inpath "/opt/test/student.txt" into table student;
select
    a1.name,
    a1.month,
    a1.degree
from
(
    select
        name,
        month,
        degree,
        sum(if(degree='A', 1, 0)) OVER(PARTITION BY name ORDER BY month ROWS BETWEEN 2 PRECEDING AND CURRENT ROW) AS score1,
        sum(if(degree ='A', 1, 0)) OVER(PARTITION BY name ORDER BY month ROWS BETWEEN 1 PRECEDING AND 1 following) AS score2,
        sum(if(degree = 'A', 1, 0)) OVER(PARTITION BY name ORDER BY month ROWS BETWEEN  CURRENT ROW AND 2 following) AS score3
    from student
)  a1
where
    a1.score1 = 3 or
    a1.score2 = 3 or
    a1.score3 = 3

------备注：
/*
OVER(PARTITION BY name ORDER BY month ROWS BETWEEN 2 PRECEDING AND CURRENT ROW) AS score1,
关键点：使用窗口函数，OVER对应的不要求原始数据表需要进行分区操作的。对应的在分区内进行操作的
*/
/*
 包括如下的3中情况：
 1.以当前行为基准，向前2行即可。
     ROWS BETWEEN 对应的是窗口函数的，
     2 PRECEDING 对应的是往前2行，
     CURRENT ROW 对应的是当前行，总共统计的是3行数据的。
     如果3行都是A的话，score1的结果是3的
 2.以当前行位基准，上下各一行即可。
    ROWS BETWEEN  窗口函数
    1 PRECEDING  下面一行
    1 following   上面一行
    加上自身，对应的包括3行。
 3.当前行和下面的2行数据
    CURRENT ROW: 当前行
    2 following： 下面2行数据
    合计总共3行数据
*/
-------测试方法三,使用LEAD的方式实现操作管理实现逻辑
select a.month ,a.name  from
(select
name ,month,degree,
LEAD(degree,1,null) OVER(PARTITION BY name ORDER BY month) AS next_1_time,
LEAD(degree,2,null) OVER(PARTITION BY name ORDER BY month) AS next_2_time
from  student  )a  where a.degree='A'  and a.next_1_time='A' and a.next_2_time='A';
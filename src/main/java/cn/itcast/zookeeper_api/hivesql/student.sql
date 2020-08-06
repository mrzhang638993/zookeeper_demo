----使用自连接的方式即可实现的
select a.name ,a.month,b.month,c.month,a.degree,b.degree,c.degree  from  student a
 join student b on a.name=b.name
 join student c  on a.name=c.name
 where     cast(substr(a.month,6,1) as int)=cast(substr(b.month,6,1) as int)-1
and cast(substr(b.month,6,1) as int)=cast(substr(c.month,6,1) as int)-1
and a.degree='A'and b.degree='A' and c.degree='A';




# 需要采用join的方式实现相关的逻辑操作，对应的这个题目采用在map端实现join操作的逻辑
进行优化操作。

采用map端的join操作实现

使用统计的是最多的10个url的，使用map端的join不是很适合的。map端的join操作对应的是小表和大表的
两个大表对应的是reduce端的join操作的。

计算的是每一个url中独立的用户数目最多的。
假设登陆日志中上下线信息完整，且同一上下线时间段内使用的ip唯一
同一个时间范围之内，一个ip对应的是一个用户的。

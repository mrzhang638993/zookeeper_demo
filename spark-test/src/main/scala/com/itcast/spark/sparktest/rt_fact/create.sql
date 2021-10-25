CREATE TABLE `topic_par_group_offset` (
  `topic` varchar(255) NOT NULL,
  `partition` int(11) NOT NULL,
  `groupid` varchar(255) NOT NULL,
  `offset` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`topic`,`partition`,`groupid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;
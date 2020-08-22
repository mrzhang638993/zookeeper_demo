kafka在zookeeper默认使用/为根目录，
试将/更换为/kafka.并建立topic，
使用kafka-console-producer.sh 生产消息，
bin/kafka-console-consumer.sh消费信息
（最好截图验证消息是否发送成功，验证kafka在zk上的目录是否正确）


# 下面是验证的脚本文件
bin/kafka-topics.sh  --create --partitions 3  --replication-factor 2  --zookeeper node01:2181,node02:2181,node03:2181/kafka    --topic test-kafka1
#  消费消息
bin/kafka-console-producer.sh --broker-list node01:9092,node02:9092,node03:9092   --topic test-kafka1
#  生成消息
bin/kafka-console-consumer.sh  --zookeeper node01:2181,node02:2181,node03:2181/kafka --topic test-kafka1

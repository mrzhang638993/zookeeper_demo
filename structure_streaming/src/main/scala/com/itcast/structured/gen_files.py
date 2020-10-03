import os

for index in range(100):
    content = """
    {"name":"Michael"}
    {"name":"Andy", "age":30}
    {"name":"Justin", "age":19}
    """

    file_name = "/export/dataset/text{0}.json".format(index)

    with open(file_name, "w") as file:
        file.write(content)

    # 执行hdfs的命令执行操作
    os.system("/export/servers/hadoop/bin/hdfs dfs -mkdir -p /dataset/dataset/")
    os.system("/export/servers/hadoop/bin/hdfs dfs -put {0} /dataset/dataset/".format(file_name))
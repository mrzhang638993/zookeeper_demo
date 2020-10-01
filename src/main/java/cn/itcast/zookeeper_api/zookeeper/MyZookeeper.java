package cn.itcast.zookeeper_api.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.ACL;

import java.util.List;

public class MyZookeeper {

    /**
     * 创建zk客户端进行操作
     */
    public static CuratorFramework getClient() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        String connectStr = "192.168.100.201:2181,192.168.100.202:2181,192.168.100.203:2181";
        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(connectStr, 10000, 10000, retryPolicy);
        curatorFramework.start();
        return curatorFramework;
    }

    /**
     * 创建节点
     */
    public static void createZNode(String path, byte[] data, CreateMode createMode, List<ACL> list) throws Exception {
        // 休眠1秒钟,尝试3次。
        CuratorFramework client = getClient();
        client.create().creatingParentContainersIfNeeded().withMode(createMode).withACL(list).forPath(path, data);
        client.close();
    }

    /**
     * 修改znode的数据进行管理操作
     */
    public static void updateData(String path, byte[] data) throws Exception {
        CuratorFramework client = getClient();
        //  没有节点的话，创建新的节点数据。
        client.setData().forPath(path, data);
        client.close();
    }

    /**
     * 获取Znode的data数据
     */
    public static String getData(String path) throws Exception {
        CuratorFramework client = getClient();
        byte[] bytes = client.getData().forPath(path);
        return new String(bytes);
    }

    /**
     * 编程清空一个节点的所有的子节点
     */
    public static void deletePathChildNodes(String path) throws Exception {
        // 监控路径下面的所有的子节点信息
        CuratorFramework client = getClient();
        List<String> paths = client.getChildren().forPath(path);
        int length = paths.size();
        for (int i = 0; i < length; i++) {
            String childPath = paths.get(i);
            client.delete().deletingChildrenIfNeeded().forPath(path + "/" + childPath);
        }
        System.out.println("节点删除完成");
    }

    /**
     * zk的watch机制
     */
    public static void zkNodeWatch(String path) throws Exception {
        // zk的watch机制
        CuratorFramework client = getClient();
        TreeCache treeCache = new TreeCache(client, path);
        //  获取监听器容器treeCache.getListenable()
        treeCache.getListenable().addListener(new TreeCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework,
                                   TreeCacheEvent treeCacheEvent) throws Exception {
                ChildData data = treeCacheEvent.getData();
                if (data != null) {
                    TreeCacheEvent.Type type = treeCacheEvent.getType();
                    switch (type) {
                        case NODE_ADDED:
                            System.out.println("新增节点");
                            break;
                        case NODE_UPDATED:
                            System.out.println("监控到节点被更新");
                            break;
                        case NODE_REMOVED:
                            System.out.println("有节点被移除操作");
                            break;
                        default:
                            break;
                    }
                }
            }
        });
        //  开启监听操作
        treeCache.start();
        // 程序需要挂起，避免线程执行到这个地方结束了,可以持续监听的。后续需要进行处理
        Thread.sleep(1000000000);
    }


    public static void main(String[] args) throws Exception {
        //createZNode("/hello/world","good".getBytes(),CreateMode.PERSISTENT,null);
        //  创建临时节点数据,创建成功的话,对应的会话结束的话,对应的内容是消失了。
        //createZNode("/hello/temp","good".getBytes(),CreateMode.EPHEMERAL,null);
        //updateData("/hello/world","many people".getBytes());
        //String data = getData("/hello/world");
        //System.out.println(data);
        //zkNodeWatch("/hello/world");
        deletePathChildNodes("/hello");
    }
}

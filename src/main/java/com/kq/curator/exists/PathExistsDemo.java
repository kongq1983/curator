package com.kq.curator.exists;

import com.kq.Config;
import com.kq.curator.util.ZKUtil;
import org.apache.curator.framework.CuratorFramework;

/** 判断路径是否存在
 * @author kq
 * @date 2020-08-26 17:19
 * @since 2020-0630
 *
 * example:
 * /dev/zhejiang/hz
 */
public class PathExistsDemo {

    public static void main(String[] args) throws Exception{

        CuratorFramework client = Config.getNameSpaceClient(Config.NAMESPACE);

        String path = "/zhejiang/hz";
        boolean exists = ZKUtil.isExists(client,path);
        System.out.println(path+", exists="+exists);
        if(exists){
            ZKUtil.deletePath(client,path);
            exists = ZKUtil.isExists(client,path);
            System.out.println(path+", exists="+exists);
        }

        client.create().creatingParentsIfNeeded().forPath(path,path.getBytes());

        exists = ZKUtil.isExists(client,path);
        System.out.println(path+", exists="+exists);


    }

}

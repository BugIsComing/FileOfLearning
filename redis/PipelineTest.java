package client;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

/**
 * 引用Jedis库
 */
public class PipelineTest {
    public static void main(String[] args) {
        int count = 1000;
        long start = System.currentTimeMillis();
        withoutPipeline(count);
        long end = System.currentTimeMillis();
        System.out.println("withoutPipeline: " + (end - start));

        start = System.currentTimeMillis();
        usePipeline(count);
        end = System.currentTimeMillis();
        System.out.println("usePipeline: " + (end - start));
    }

    private static void withoutPipeline(int count) {
        Jedis jr = null;
        try {
			//这个ip是阿里云的一个服务器ip，上面部署了一个redis
            jr = new Jedis("112.74.36.223", 6379);
            for (int i = 0; i < count; i++) {
                jr.incr("testKey1");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jr != null) {
                jr.disconnect();
            }
        }
    }

    private static void usePipeline(int count) {
        Jedis jr = null;
        try {
            jr = new Jedis("112.74.36.223", 6379);
            Pipeline pl = jr.pipelined();
            for (int i = 0; i < count; i++) {
                pl.incr("testKey2");
            }
            pl.sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jr != null) {
                jr.disconnect();
            }
        }
    }
}

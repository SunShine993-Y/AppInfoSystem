package com.bdqn.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Tuple;

import java.util.Set;

public class JedisUtils {

	// 连接池
	private static JedisPool pool;
	// 在ServletContextListener 的 销毁方法中，可以销毁pool pool.close()

	static {
		// 初始化
		// Jedis连接池配置
		JedisPoolConfig config = new JedisPoolConfig();

		// 以下配置是非必须的，不设置就会走默认的
		// 空闲时的最大连接数
		config.setMaxIdle(50);
		// 空闲时的最小连接数
		config.setMinIdle(10);
		// 连接池的最大容量 5000
		// config.setMaxTotal(5000);
		pool = new JedisPool(config, "192.168.65.128", 6379);

	}

	/**
	 * 获取Jedis链接对象
	 * 
	 */
	public static Jedis getJedis() {
		return pool.getResource();
	}

	/**
	 * 获取连接池
	 */
	public static JedisPool getJedisPool() {
		return pool;
	}







		/*第一次运行如果报错在连接池中找不到资源 那就是没有开启防火墙中端口号*/
		/*  /sbin/iptables -I INPUT -p tcp --dport 6379 -j ACCEPT  */

	
}

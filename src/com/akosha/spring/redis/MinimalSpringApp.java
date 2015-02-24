package com.akosha.spring.redis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class MinimalSpringApp {

	public final static String CONFIG_PATH = "classpath*:com/akosha/spring/redis/application-config.xml";

	@Autowired
	Foo myBean;

	@Autowired
	public RedisTemplate<String,HashMap<Integer,HashMap<Integer,List<String>>>> redisTemplate;


	public static void main(final String[] args) {
		final ApplicationContext context = new ClassPathXmlApplicationContext(CONFIG_PATH);
		final MinimalSpringApp minimalSpringApp = context.getBean(MinimalSpringApp.class);
		
		minimalSpringApp.startCaching();
		
	}

	private void startCaching() {
		// TODO Auto-generated method stub
		final ApplicationContext context = new ClassPathXmlApplicationContext(CONFIG_PATH);
		final MinimalSpringApp minimalSpringApp = context.getBean(MinimalSpringApp.class);

		HashMap<String, ArrayList<String>> innerMap = new HashMap<>();

		ArrayList<String> list = new ArrayList<String>();
		list.add("abc");
		list.add("pqr");

		innerMap.put("map1", list);

		HashMap<String, HashMap<String,ArrayList<String>>> outerMap = new HashMap<>();

		outerMap.put("key", innerMap);

		redisTemplate.opsForHash().putAll("spring_redis_key", outerMap);

		System.out.println("-----------writing done---------");

		Map<Object, Object> outputOuterMap = redisTemplate.opsForHash().entries("spring_redis_key");
		HashMap<String, ArrayList<String>> outputInnerMap = (HashMap<String, ArrayList<String>>) outputOuterMap.get("key");

		for(Entry<String, ArrayList<String>> mapEntry : outputInnerMap.entrySet()) {
			System.out.println(mapEntry.getKey()+"::"+mapEntry.getValue());
		}
		System.out.println("-----------reading done---------");
	}
}

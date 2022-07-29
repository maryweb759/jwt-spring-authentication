package com.example.demo.service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
@Service
@Primary
public class GuavaCacheLoginAttemptService implements LoginAttempService{

	private LoadingCache<String, Integer> loadingAttempCache = 
			CacheBuilder.newBuilder().expireAfterWrite(15, TimeUnit.MINUTES)
			.maximumSize(100) 
			// we use cache loader for calculate a value in the event of it
			// not being found in a Guava LoadingCache.
			// تحتاج ذاكرة التخزين المؤقت إلى التحديث ، فسيتم استخدام CacheLoader لحساب القيم.
			.build(new CacheLoader<>() {

				@Override
				public Integer load(String key) throws Exception {
					// TODO Auto-generated method stub
					return 0;
				}
			});
	
	@Override
	public void loginFailed(String userName) {
		int attempts = getAttempts(userName);
		// add key to cache and replace old value with new number value
		loadingAttempCache.put(userName, attempts + ATTEMPT_INCREMENT);
		
	}

	@Override
	public void loginSuccessed(String userName) {
		// if logged in success delete cache for that key 
		evictCache(userName);
		
	}

	@Override
	public boolean hasExceededMaxAttempts(String username) {
		// return true if trying value more then 5
		return getAttempts(username) >= MAX_ATTEMPTS;
	}

	private int getAttempts(String userName) {
		Integer attempts = loadingAttempCache.getIfPresent(userName);
		// get the value count else return null
		return Objects.requireNonNullElse(attempts, 0);
	}

	@Override
	public void evictCache(String userName) {
		loadingAttempCache.invalidate(userName);

		
	}
	
}

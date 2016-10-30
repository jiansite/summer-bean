package cn.cerc.jbean.core;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

import cn.cerc.jdb.cache.IMemcache;

public class MemcachedTest {
	// private static final Logger log = Logger.getLogger(MemcachedTest.class);

	@Test
	@Ignore
	public void test() {
		String buffKey = "test";
		String value = "OK!";
		IMemcache client = Application.getMemcache();
		client.set(buffKey, value, 2);

		Object buffData;
		for (int i = 1; i < 5; i++) {
			buffData = client.get(buffKey);
			String msg = String.format("第 %d 次测试", i);
			assertEquals(msg, i <= 2 ? value : null, buffData);
			sleep();
		}
	}

	private void sleep() {
		try {
			Thread.sleep(1005);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

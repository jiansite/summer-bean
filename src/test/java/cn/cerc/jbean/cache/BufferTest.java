package cn.cerc.jbean.cache;

import org.junit.Ignore;
import org.junit.Test;

public class BufferTest {

	@Test
	@Ignore
	public void test() {
		// 注意：进行此测试，必须先准备好本地测试目录下有application.xml文件
		Buffer buff = new Buffer("key");
		if (buff.isNull()) {
			System.out.println("key not exists.");
			buff.setField("num", 1);
		} else {
			System.out.println("key exists.");
			buff.setField("num", buff.getInt("num") + 1);
		}
		buff.post();
		System.out.println(buff);
	}

}

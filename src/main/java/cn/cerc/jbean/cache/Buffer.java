package cn.cerc.jbean.cache;

import cn.cerc.jbean.core.Application;
import cn.cerc.jdb.cache.CacheQuery;

public class Buffer extends CacheQuery {

	public Buffer() {
		super(Application.getMemcache());
	}

	public Buffer(String key) {
		super(Application.getMemcache());
		this.setKey(key);
	}

	public Buffer(Object... keys) {
		super(Application.getMemcache());
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < keys.length; i++) {
			if (i > 0)
				str.append(".");
			str.append(keys[i]);
		}
		setKey(str.toString());
	}

	@Deprecated // 请改使用post函数
	public final void update() {
		post();
	}

}

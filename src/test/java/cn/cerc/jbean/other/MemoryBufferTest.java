package cn.cerc.jbean.other;

import static org.junit.Assert.assertEquals;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

public class MemoryBufferTest {
    private static final Logger log = Logger.getLogger(MemoryBufferTest.class);

    @Test
    @Ignore
    public void test_connect() {
        try (MemoryBuffer rec = new MemoryBuffer(BufferType.test, "test");) {
            if (rec.Connected()) {
                if (rec.isNull()) {
                    rec.setField("Code_", "1000");
                    rec.setField("Name_", "Jason");
                    rec.setField("num", 0);
                    log.info("Init memcached.");
                } else {
                    log.info("read memcached.");
                }
                rec.setField("num", rec.getInt("num") + 1);
            } else {
                assertEquals("联系不上 Memcahced 服务器！", "ok", "erro");
            }
        }
    }

    @Test
    @Ignore
    public void test_read_write() {
        String data = "AAA";
        try (MemoryBuffer buff = new MemoryBuffer(BufferType.test, "test");) {
            buff.setField("A", data);
        }
        try (MemoryBuffer buff = new MemoryBuffer(BufferType.test, "test");) {
            assertEquals(data, buff.getString("A"));
        }
        MemoryBuffer.delete(BufferType.test, "test");
        ;
        try (MemoryBuffer buff = new MemoryBuffer(BufferType.test, "test");) {
            assertEquals(null, buff.getRecord().getField("A"));
        }
    }
}

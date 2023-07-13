package com.github.julyss2019.bukkit.voidframework.test.log;

import com.github.julyss2019.bukkit.voidframework.logging.logger.appender.internal.RollingFileNamePatternConverter;
import org.junit.Assert;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RollingFileNamePatternConverterTest {
    @Test
    public void test() {
/*        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Assert.assertEquals("demo_" + sdf.format(new Date()) + ".log", RollingFileNamePatternConverter.convert("demo_%d{yyyy-MM-dd}.log"));
        Assert.assertEquals("demo_%d", RollingFileNamePatternConverter.convert("demo_%%d"));

        try {
            RollingFileNamePatternConverter.convert("demo_%d{yyyy-MM-dd");
            Assert.assertTrue(false);
        } catch (Exception e) {}

        try {
            RollingFileNamePatternConverter.convert("demo_%dyyyy-MM-dd}");
            Assert.assertTrue(false);
        } catch (Exception e) {}*/
    }
}

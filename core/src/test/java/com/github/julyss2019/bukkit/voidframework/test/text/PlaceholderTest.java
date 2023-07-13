package com.github.julyss2019.bukkit.voidframework.test.text;

import com.github.julyss2019.bukkit.voidframework.text.PlaceholderContainer;
import com.github.julyss2019.bukkit.voidframework.text.internal.PlaceholderConverter;
import org.junit.Assert;
import org.junit.Test;

public class PlaceholderTest {
    @Test
    public void testConvert() {
        PlaceholderContainer placeholderContainer = new PlaceholderContainer();

        placeholderContainer.put("key1", "world");
        Assert.assertEquals("hello world", PlaceholderConverter.convert("hello ${key1}", placeholderContainer));
        Assert.assertEquals("hello world ${key1}", PlaceholderConverter.convert("hello world $${key1}", placeholderContainer));

        try {
            PlaceholderConverter.convert("hello ${world", placeholderContainer);
            Assert.assertTrue(false);
        } catch (Exception e) {
        }

        try {
            PlaceholderConverter.convert("hello $world}", placeholderContainer);
            Assert.assertTrue(false);
        } catch (Exception e) {
        }
    }
}

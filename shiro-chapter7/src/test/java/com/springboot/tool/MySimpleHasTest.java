package com.springboot.tool;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static org.junit.Assert.*;

public class MySimpleHasTest {
    public static final Logger log =
            LoggerFactory.getLogger(MySimpleHasTest.class);

    @Test
    public void testEncry(){
        Map<String, String> result =
                    MySimpleHas.getEncryPass("xiaohong", "xiaohong");

        //971f141c2c36e0cc810df866fd078a1b
        log.info("salt: " + result.get("salt"));
        //502db4e25057cfb196b7520dc58d32e2
        log.info("encryPass: " + result.get("encryPassword"));
    }

}
/**
 * File Created at 2011-12-05
 * $Id$
 * <p>
 * Copyright 2008 Alibaba.com Croporation Limited.
 * All rights reserved.
 * <p>
 * This software is the confidential and proprietary information of
 * Alibaba Company. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Alibaba.com.
 */
package com.netease.protocol.thrift;

import com.netease.dubbo.common.extension.ExtensionLoader;
import com.netease.gen.dubbo.$__DemoStub;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author <a href="mailto:gang.lvg@alibaba-inc.com">gang.lvg</a>
 */
public class ThriftUtilsTest {

    @Test
    public void testGenerateMethodArgsClassName() {

        Assert.assertEquals(
                $__DemoStub.echoString_args.class.getName(),
                ThriftUtils.generateMethodArgsClassName(
                        com.netease.gen.dubbo.Demo.class.getName(),
                        "echoString"));

        Assert.assertEquals(
                $__DemoStub.echoString_args.class.getName(),
                ExtensionLoader.getExtensionLoader(ClassNameGenerator.class)
                        .getExtension(DubboClassNameGenerator.NAME).generateArgsClassName(
                        com.netease.gen.dubbo.Demo.class.getName(), "echoString"));

    }

    @Test
    public void testGenerateMethodResultClassName() {

        Assert.assertEquals($__DemoStub.echoString_result.class.getName(),
                ThriftUtils.generateMethodResultClassName(
                        com.netease.gen.dubbo.Demo.class.getName(),
                        "echoString"));

        Assert.assertEquals($__DemoStub.echoString_result.class.getName(),
                ExtensionLoader.getExtensionLoader(ClassNameGenerator.class)
                        .getExtension(DubboClassNameGenerator.NAME).generateResultClassName(
                        com.netease.gen.dubbo.Demo.class.getName(), "echoString"));

    }

    @Test
    public void testGenerateMethodArgsClassNameThrift() {
        Assert.assertEquals(com.netease.gen.thrift.Demo.echoString_args.class.getName(),
                ThriftUtils.generateMethodArgsClassNameThrift(
                        com.netease.gen.thrift.Demo.Iface.class.getName(),
                        "echoString"));

        Assert.assertEquals(com.netease.gen.thrift.Demo.echoString_args.class.getName(),
                ExtensionLoader.getExtensionLoader(ClassNameGenerator.class)
                        .getExtension(ThriftClassNameGenerator.NAME).generateArgsClassName(
                        com.netease.gen.thrift.Demo.Iface.class.getName(),
                        "echoString"));

    }

    @Test
    public void testGenerateMethodResultClassNameThrift() {
        Assert.assertEquals(com.netease.gen.thrift.Demo.echoString_result.class.getName(),
                ThriftUtils.generateMethodResultClassNameThrift(
                        com.netease.gen.thrift.Demo.Iface.class.getName(),
                        "echoString"));

        Assert.assertEquals(com.netease.gen.thrift.Demo.echoString_result.class.getName(),
                ExtensionLoader.getExtensionLoader(ClassNameGenerator.class)
                        .getExtension(ThriftClassNameGenerator.NAME).generateResultClassName(
                        com.netease.gen.thrift.Demo.Iface.class.getName(),
                        "echoString"));

    }

}

/*
 * Copyright 1999-2011 Alibaba Group.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.netease.protocol.dubbo;

import com.netease.dubbo.common.URL;
import com.netease.dubbo.common.extension.ExtensionLoader;
import com.netease.Protocol;
import com.netease.ProxyFactory;
import com.netease.protocol.dubbo.support.DemoService;
import com.netease.protocol.dubbo.support.DemoServiceImpl;
import com.netease.service.EchoService;

import junit.framework.TestCase;

public class RpcFilterTest extends TestCase {
    private Protocol protocol = ExtensionLoader.getExtensionLoader(Protocol.class).getAdaptiveExtension();
    private ProxyFactory proxy = ExtensionLoader.getExtensionLoader(ProxyFactory.class).getAdaptiveExtension();

    public void testRpcFilter() throws Exception {
        DemoService service = new DemoServiceImpl();
        URL url = URL.valueOf("dubbo://127.0.0.1:9010/com.netease.DemoService?service.filter=echo");
        protocol.export(proxy.getInvoker(service, DemoService.class, url));
        service = proxy.getProxy(protocol.refer(DemoService.class, url));
        assertEquals("123", service.echo("123"));
        // cast to EchoService
        EchoService echo = proxy.getProxy(protocol.refer(EchoService.class, url));
        assertEquals(echo.$echo("test"), "test");
        assertEquals(echo.$echo("abcdefg"), "abcdefg");
        assertEquals(echo.$echo(1234), 1234);
    }

}
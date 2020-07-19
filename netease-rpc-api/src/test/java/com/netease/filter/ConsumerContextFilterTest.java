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
package com.netease.filter;

import com.netease.dubbo.common.URL;
import com.netease.dubbo.common.utils.NetUtils;
import com.netease.Filter;
import com.netease.Invocation;
import com.netease.Invoker;
import com.netease.RpcContext;
import com.netease.support.DemoService;
import com.netease.support.MockInvocation;
import com.netease.support.MyInvoker;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * ConsumerContextFilterTest.java
 *
 * @author tony.chenl
 */
public class ConsumerContextFilterTest {
    Filter consumerContextFilter = new ConsumerContextFilter();

    @Test
    public void testSetContext() {
        URL url = URL.valueOf("test://test:11/test?group=dubbo&version=1.1");
        Invoker<DemoService> invoker = new MyInvoker<DemoService>(url);
        Invocation invocation = new MockInvocation();
        consumerContextFilter.invoke(invoker, invocation);
        assertEquals(invoker, RpcContext.getContext().getInvoker());
        assertEquals(invocation, RpcContext.getContext().getInvocation());
        assertEquals(NetUtils.getLocalHost() + ":0", RpcContext.getContext().getLocalAddressString());
        assertEquals("test:11", RpcContext.getContext().getRemoteAddressString());

    }
}
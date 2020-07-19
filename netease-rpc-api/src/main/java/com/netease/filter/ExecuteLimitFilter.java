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

import com.netease.dubbo.common.Constants;
import com.netease.dubbo.common.URL;
import com.netease.dubbo.common.extension.Activate;
import com.netease.Filter;
import com.netease.Invocation;
import com.netease.Invoker;
import com.netease.Result;
import com.netease.RpcException;
import com.netease.RpcStatus;

import java.util.concurrent.Semaphore;

/**
 * ThreadLimitInvokerFilter
 *
 * @author william.liangf
 */
@Activate(group = Constants.PROVIDER, value = Constants.EXECUTES_KEY)
public class ExecuteLimitFilter implements Filter {

    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        URL url = invoker.getUrl();
        String methodName = invocation.getMethodName();
        Semaphore executesLimit = null;
        boolean acquireResult = false;
        int max = url.getMethodParameter(methodName, Constants.EXECUTES_KEY, 0);
        if (max > 0) {
            RpcStatus count = RpcStatus.getStatus(url, invocation.getMethodName());
//            if (count.getActive() >= max) {
            /**
             * http://manzhizhen.iteye.com/blog/2386408
             * 通过信号量来做并发控制（即限制能使用的线程数量）
             * 2017-08-21 yizhenqiang
             */
            executesLimit = count.getSemaphore(max);
            if(executesLimit != null && !(acquireResult = executesLimit.tryAcquire())) {
                throw new RpcException("Failed to invoke method " + invocation.getMethodName() + " in provider " + url + ", cause: The service using threads greater than <dubbo:service executes=\"" + max + "\" /> limited.");
            }
        }
        long begin = System.currentTimeMillis();
        boolean isException = false;
        RpcStatus.beginCount(url, methodName);
        try {
            Result result = invoker.invoke(invocation);
            return result;
        } catch (Throwable t) {
            isException = true;
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else {
                throw new RpcException("unexpected exception when ExecuteLimitFilter", t);
            }
        } finally {
            RpcStatus.endCount(url, methodName, System.currentTimeMillis() - begin, isException);
            if(acquireResult) {
                executesLimit.release();
            }
        }
    }

}
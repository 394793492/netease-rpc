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
package com.netease.protocol.dubbo.telnet;

import com.netease.dubbo.common.extension.Activate;
import com.netease.dubbo.common.utils.ReflectUtils;
import com.netease.dubbo.remoting.Channel;
import com.netease.dubbo.remoting.telnet.TelnetHandler;
import com.netease.dubbo.remoting.telnet.support.Help;
import com.netease.Exporter;
import com.netease.Invoker;
import com.netease.protocol.dubbo.DubboProtocol;

import java.lang.reflect.Method;

/**
 * ListTelnetHandler
 *
 * @author william.liangf
 */
@Activate
@Help(parameter = "[-l] [service]", summary = "List services and methods.", detail = "List services and methods.")
public class ListTelnetHandler implements TelnetHandler {

    public String telnet(Channel channel, String message) {
        StringBuilder buf = new StringBuilder();
        String service = null;
        boolean detail = false;
        if (message.length() > 0) {
            String[] parts = message.split("\\s+");
            for (String part : parts) {
                if ("-l".equals(part)) {
                    detail = true;
                } else {
                    if (service != null && service.length() > 0) {
                        return "Invaild parameter " + part;
                    }
                    service = part;
                }
            }
        } else {
            service = (String) channel.getAttribute(ChangeTelnetHandler.SERVICE_KEY);
            if (service != null && service.length() > 0) {
                buf.append("Use default service " + service + ".\r\n");
            }
        }
        if (service == null || service.length() == 0) {
            for (Exporter<?> exporter : DubboProtocol.getDubboProtocol().getExporters()) {
                if (buf.length() > 0) {
                    buf.append("\r\n");
                }
                buf.append(exporter.getInvoker().getInterface().getName());
                if (detail) {
                    buf.append(" -> ");
                    buf.append(exporter.getInvoker().getUrl());
                }
            }
        } else {
            Invoker<?> invoker = null;
            for (Exporter<?> exporter : DubboProtocol.getDubboProtocol().getExporters()) {
                if (service.equals(exporter.getInvoker().getInterface().getSimpleName())
                        || service.equals(exporter.getInvoker().getInterface().getName())
                        || service.equals(exporter.getInvoker().getUrl().getPath())) {
                    invoker = exporter.getInvoker();
                    break;
                }
            }
            if (invoker != null) {
                Method[] methods = invoker.getInterface().getMethods();
                for (Method method : methods) {
                    if (buf.length() > 0) {
                        buf.append("\r\n");
                    }
                    if (detail) {
                        buf.append(ReflectUtils.getName(method));
                    } else {
                        buf.append(method.getName());
                    }
                }
            } else {
                buf.append("No such service " + service);
            }
        }
        return buf.toString();
    }

}
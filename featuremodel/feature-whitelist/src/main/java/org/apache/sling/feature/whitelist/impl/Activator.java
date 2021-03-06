/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.sling.feature.whitelist.impl;

import org.apache.sling.feature.service.Features;
import org.apache.sling.feature.whitelist.WhitelistServiceFactory;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;

public class Activator implements BundleActivator {
    private ServiceTracker<Features, Features> tracker;
    private ServiceRegistration<?> resolverHookServiceRegistration;

    @Override
    public synchronized void start(BundleContext context) throws Exception {
        tracker = new ServiceTracker<>(context, Features.class, null);
        tracker.open();

        WhitelistServiceFactory wsf = new WhitelistServiceFactoryImpl(context, tracker);
        context.registerService(WhitelistServiceFactory.class, wsf, null);
        /*
        WhitelistEnforcer enforcer = new WhitelistEnforcer(context, tracker);
        Dictionary<String, Object> resHookProps = new Hashtable<>();
        resHookProps.put(Constants.SERVICE_PID, WhitelistEnforcer.class.getName());
        resolverHookServiceRegistration = context.registerService(
                new String[] {ManagedService.class.getName(), ResolverHookFactory.class.getName()},
                enforcer, resHookProps);
                */
    }

    @Override
    public synchronized void stop(BundleContext context) throws Exception {
        resolverHookServiceRegistration.unregister();
        tracker.close();
    }
}

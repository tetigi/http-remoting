/*
 * Copyright 2017 Palantir Technologies, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.palantir.remoting3.jaxrs;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.SharedMetricRegistries;
import com.google.common.reflect.Reflection;
import com.palantir.remoting3.clients.ClientConfiguration;
import com.palantir.remoting3.ext.refresh.Refreshable;
import com.palantir.remoting3.ext.refresh.RefreshableProxyInvocationHandler;

/**
 * Variant of {@link JaxRsClient} with additional scala serialization support.
 */
public final class JaxRsScalaClient {

    private JaxRsScalaClient() {}

    /** See {@link JaxRsClient}. */
    public static <T> T create(Class<T> serviceClass, String userAgent, ClientConfiguration config) {
        return new FeignJaxRsScalaClientBuilder(config, SharedMetricRegistries.getOrCreate("remoting"))
                .build(serviceClass, userAgent);
    }

    public static <T> T create(Class<T> serviceClass, String userAgent, ClientConfiguration config,
            MetricRegistry registry) {
        return new FeignJaxRsScalaClientBuilder(config, registry).build(serviceClass, userAgent);
    }

    /** See {@link JaxRsClient}. */
    public static <T> T create(
            Class<T> serviceClass,
            String userAgent,
            Refreshable<ClientConfiguration> config) {
        return Reflection.newProxy(serviceClass, RefreshableProxyInvocationHandler.create(
                config,
                serviceConfiguration -> create(serviceClass, userAgent, serviceConfiguration)));
    }
}

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.test.e2e.agent.metrics;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.test.e2e.agent.common.BasePluginE2EIT;
import org.apache.shardingsphere.test.e2e.agent.common.env.E2ETestEnvironment;
import org.apache.shardingsphere.test.e2e.agent.common.util.OkHttpUtils;
import org.apache.shardingsphere.test.e2e.agent.metrics.result.MetricsMetaDataResult;
import org.apache.shardingsphere.test.e2e.agent.metrics.result.MetricsQueryResult;
import org.junit.Test;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

@Slf4j
public final class MetricsPluginE2EIT extends BasePluginE2EIT {
    
    @Test
    public void assertProxyWithAgent() throws IOException {
        super.assertProxyWithAgent();
        Properties props = E2ETestEnvironment.getInstance().getProps();
        String metaDataURL = props.getProperty("prometheus.metadata.url");
        String queryURL = props.getProperty("prometheus.query.url");
        Collection<String> metricsNames = buildMetricsNames();
        for (String each : metricsNames) {
            String metaDataURLWithParam = buildURLWithParameter(metaDataURL, each);
            String queryURLWithParam = buildURLWithParameter(queryURL, each);
            try {
                assertMetadata(each, OkHttpUtils.getInstance().get(metaDataURLWithParam, MetricsMetaDataResult.class));
                assertQuery(each, OkHttpUtils.getInstance().get(queryURLWithParam, MetricsQueryResult.class));
            } catch (final IOException ex) {
                log.info("Access prometheus HTTP RESTful API error: ", ex);
            }
        }
    }
    
    private Collection<String> buildMetricsNames() {
        Collection<String> result = new LinkedHashSet<>();
        result.add("proxy_requests_total");
        result.add("proxy_current_connections");
        result.add("proxy_execute_latency_millis_bucket");
        result.add("routed_sql_total");
        result.add("routed_result_total");
        result.add("proxy_commit_transactions_total");
        result.add("proxy_rollback_transactions_total");
        result.add("proxy_execute_errors_total");
        return result;
    }
    
    private String buildURLWithParameter(final String url, final String metricsName) {
        return String.join("", url, metricsName);
    }
    
    // TODO remove if metadata result is not detailed.
    private void assertMetadata(final String metricName, final MetricsMetaDataResult metricsMetaDataResult) {
        assertThat(String.format("Metric name `%s` is not success.", metricName), metricsMetaDataResult.getStatus(), is("success"));
        assertNotNull(String.format("Metric name `%s` is null.", metricName), metricsMetaDataResult.getData());
    }
    
    // TODO add more detailed assert
    private static void assertQuery(final String metricName, final MetricsQueryResult metricsQueryResult) {
        assertThat(String.format("Metric name `%s` is not success.", metricName), metricsQueryResult.getStatus(), is("success"));
        assertFalse(String.format("Metric name `%s` is empty.", metricName), metricsQueryResult.getData().getResult().isEmpty());
    }
}

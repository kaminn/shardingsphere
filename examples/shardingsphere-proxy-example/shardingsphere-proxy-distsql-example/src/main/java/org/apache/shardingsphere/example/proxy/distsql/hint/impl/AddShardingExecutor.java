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

package org.apache.shardingsphere.example.proxy.distsql.hint.impl;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.example.proxy.distsql.hint.AbstractHintExecutor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
public final class AddShardingExecutor extends AbstractHintExecutor {
    
    private static final String ADD_SHARDING_HINT_DATABASE = "add sharding hint database_value t_order=1";
    
    private static final String ADD_SHARDING_HINT_TABLE = "add sharding hint table_value t_order=1";
    
    private static final String SHOW_STATUS = "show sharding hint status";
    
    @Override
    public void init(Statement statement) {
        this.statement = statement;
    }
    
    @Override
    public void execute() throws SQLException, InterruptedException {
        executeShow();
        executeAdd();
        executeShow();
    }
    
    private void executeShow() throws SQLException {
        log.info("show ...");
        ResultSet resultSet = statement.executeQuery(SHOW_STATUS);
        log.info(new Gson().toJson(getResultData(resultSet)));
    }
    
    private void executeAdd() throws SQLException {
        log.info("add sharding hint ...");
        statement.execute(ADD_SHARDING_HINT_DATABASE);
        statement.execute(ADD_SHARDING_HINT_TABLE);
    }
}

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

package org.apache.shardingsphere.infra.metadata.auth.model.privilege.database;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.shardingsphere.infra.metadata.auth.model.privilege.PrivilegeType;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Database privilege.
 */
@Getter
@EqualsAndHashCode
public final class DatabasePrivilege {
    
    private final Collection<PrivilegeType> globalPrivileges = new LinkedHashSet<>();
    
    private final Map<String, SchemaPrivilege> specificPrivileges = new LinkedHashMap<>();
    
    /**
     * Has privileges.
     *
     * @param schema schema
     * @param privileges privileges
     * @return has privileges or not
     */
    public boolean hasPrivileges(final String schema, final Collection<PrivilegeType> privileges) {
        return hasGlobalPrivileges(privileges) || hasSpecificPrivileges(schema, privileges);
    }
    
    /**
     * Has privileges.
     *
     * @param schema schema
     * @param table table
     * @param privileges privileges
     * @return has privileges or not
     */
    public boolean hasPrivileges(final String schema, final String table, final Collection<PrivilegeType> privileges) {
        return hasGlobalPrivileges(privileges) || hasSpecificPrivileges(schema, table, privileges);
    }
    
    private boolean hasGlobalPrivileges(final Collection<PrivilegeType> privileges) {
        return globalPrivileges.containsAll(privileges);
    }
    
    private boolean hasSpecificPrivileges(final String schema, final Collection<PrivilegeType> privileges) {
        return specificPrivileges.containsKey(schema) && specificPrivileges.get(schema).hasPrivileges(getSpecificPrivileges(privileges));
    }
    
    private boolean hasSpecificPrivileges(final String schema, final String table, final Collection<PrivilegeType> privileges) {
        return specificPrivileges.containsKey(schema) && specificPrivileges.get(schema).hasPrivileges(table, getSpecificPrivileges(privileges));
    }
    
    private Collection<PrivilegeType> getSpecificPrivileges(final Collection<PrivilegeType> privileges) {
        return privileges.stream().filter(each -> !globalPrivileges.contains(each)).collect(Collectors.toList());
    }
}

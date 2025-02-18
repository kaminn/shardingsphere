+++
title = "SHOW SHARDING TABLE RULE"
weight = 2
+++

### 描述

`SHOW SHARDING TABLE RULE` 语法用于查询指定逻辑库中的分片规则。

### 语法

{{< tabs >}}
{{% tab name="语法" %}}
```sql
ShowShardingTableRule ::=
  'SHOW' 'SHARDING' 'TABLE' ('RULE' tableName | 'RULES') ('FROM' databaseName)?

tableName ::=
  identifier

databaseName ::=
  identifier
```
{{% /tab %}}
{{% tab name="铁路图" %}}
<iframe frameborder="0" name="diagram" id="diagram" width="100%" height="100%"></iframe>
{{% /tab %}}
{{< /tabs >}}

### 补充说明

- 未指定 `databaseName` 时，默认是当前使用的 `DATABASE`。 如果也未使用 `DATABASE` 则会提示 `No database selected`。

### 返回值说明

| 列                                | 说明                                |
| --------------------------------- | ---------------------------------- |
| table                             | 逻辑表名                            |
| actual_data_nodes                 | 实际的数据节点                       |
| actual_data_sources               | 实际的数据源（通过 RDL 创建的规则时显示）|
| database_strategy_type            | 数据库分片策略类型                    |
| database_sharding_column          | 数据库分片键                         |
| database_sharding_algorithm_type  | 数据库分片算法类型                    |
| database_sharding_algorithm_props | 数据库分片算法参数                    |
| table_strategy_type               | 表分片策略类型                       |
| table_sharding_column             | 表分片键                            |
| table_sharding_algorithm_type     | 表分片算法类型                       |
| table_sharding_algorithm_props    | 表分片算法参数                       |
| key_generate_column               | 分布式主键生成列                     |
| key_generator_type                | 分布式主键生成器类型                  |
| key_generator_props               | 分布式主键生成器参数                  |

 ### 示例
- 查询指定逻辑库的分片规则
```sql
SHOW SHARDING TABLE RULES FROM sharding_db;
```
```sql
+--------------+-------------------+---------------------+------------------------+--------------------------+----------------------------------+-----------------------------------+---------------------+-----------------------+-------------------------------+--------------------------------+---------------------+--------------------+---------------------+
| table        | actual_data_nodes | actual_data_sources | database_strategy_type | database_sharding_column | database_sharding_algorithm_type | database_sharding_algorithm_props | table_strategy_type | table_sharding_column | table_sharding_algorithm_type | table_sharding_algorithm_props | key_generate_column | key_generator_type | key_generator_props |
+--------------+-------------------+---------------------+------------------------+--------------------------+----------------------------------+-----------------------------------+---------------------+-----------------------+-------------------------------+--------------------------------+---------------------+--------------------+---------------------+
| t_order      |                   | ds_0,ds_1           |                        |                          |                                  |                                   | mod                 | order_id              | mod                           | sharding-count=4               |                     |                    |                     |
| t_order_item |                   | ds_0,ds_1           |                        |                          |                                  |                                   | mod                 | order_id              | mod                           | sharding-count=4               |                     |                    |                     |
+--------------+-------------------+---------------------+------------------------+--------------------------+----------------------------------+-----------------------------------+---------------------+-----------------------+-------------------------------+--------------------------------+---------------------+--------------------+---------------------+
2 rows in set (0.12 sec)
```

- 查询当前逻辑库的分片规则
```sql
SHOW SHARDING TABLE RULES;
```
```sql
+--------------+-------------------+---------------------+------------------------+--------------------------+----------------------------------+-----------------------------------+---------------------+-----------------------+-------------------------------+--------------------------------+---------------------+--------------------+---------------------+
| table        | actual_data_nodes | actual_data_sources | database_strategy_type | database_sharding_column | database_sharding_algorithm_type | database_sharding_algorithm_props | table_strategy_type | table_sharding_column | table_sharding_algorithm_type | table_sharding_algorithm_props | key_generate_column | key_generator_type | key_generator_props |
+--------------+-------------------+---------------------+------------------------+--------------------------+----------------------------------+-----------------------------------+---------------------+-----------------------+-------------------------------+--------------------------------+---------------------+--------------------+---------------------+
| t_order      |                   | ds_0,ds_1           |                        |                          |                                  |                                   | mod                 | order_id              | mod                           | sharding-count=4               |                     |                    |                     |
| t_order_item |                   | ds_0,ds_1           |                        |                          |                                  |                                   | mod                 | order_id              | mod                           | sharding-count=4               |                     |                    |                     |
+--------------+-------------------+---------------------+------------------------+--------------------------+----------------------------------+-----------------------------------+---------------------+-----------------------+-------------------------------+--------------------------------+---------------------+--------------------+---------------------+
2 rows in set (0.12 sec)
```
- 查询指定逻辑表的分片规则

```sql
SHOW SHARDING TABLE RULE t_order;
```

```sql
+--------------+-------------------+---------------------+------------------------+--------------------------+----------------------------------+-----------------------------------+---------------------+-----------------------+-------------------------------+--------------------------------+---------------------+--------------------+---------------------+
| table        | actual_data_nodes | actual_data_sources | database_strategy_type | database_sharding_column | database_sharding_algorithm_type | database_sharding_algorithm_props | table_strategy_type | table_sharding_column | table_sharding_algorithm_type | table_sharding_algorithm_props | key_generate_column | key_generator_type | key_generator_props |
+--------------+-------------------+---------------------+------------------------+--------------------------+----------------------------------+-----------------------------------+---------------------+-----------------------+-------------------------------+--------------------------------+---------------------+--------------------+---------------------+
| t_order      |                   | ds_0,ds_1           |                        |                          |                                  |                                   | mod                 | order_id              | mod                           | sharding-count=4               |                     |                    |                     |
+--------------+-------------------+---------------------+------------------------+--------------------------+----------------------------------+-----------------------------------+---------------------+-----------------------+-------------------------------+--------------------------------+---------------------+--------------------+---------------------+
1 rows in set (0.12 sec)
```

### 保留字

`CREATE`、`SHARDING`、`TABLE`、`RULE`、`FROM`

### 相关链接

- [保留字](/cn/user-manual/shardingsphere-proxy/distsql/syntax/reserved-word/)

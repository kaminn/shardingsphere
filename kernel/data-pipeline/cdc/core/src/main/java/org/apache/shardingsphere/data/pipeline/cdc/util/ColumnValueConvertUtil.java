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

package org.apache.shardingsphere.data.pipeline.cdc.util;

import com.google.protobuf.BoolValue;
import com.google.protobuf.ByteString;
import com.google.protobuf.BytesValue;
import com.google.protobuf.DoubleValue;
import com.google.protobuf.FloatValue;
import com.google.protobuf.Int32Value;
import com.google.protobuf.Int64Value;
import com.google.protobuf.Message;
import com.google.protobuf.StringValue;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.data.pipeline.cdc.protocol.response.BigDecimalValue;
import org.apache.shardingsphere.data.pipeline.cdc.protocol.response.BigIntegerValue;
import org.apache.shardingsphere.data.pipeline.cdc.protocol.response.BlobValue;
import org.apache.shardingsphere.data.pipeline.cdc.protocol.response.ClobValue;
import org.apache.shardingsphere.data.pipeline.cdc.protocol.response.LocalTimeValue;
import org.apache.shardingsphere.data.pipeline.cdc.protocol.response.NullValue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * Column value convert util.
 */
@Slf4j
public final class ColumnValueConvertUtil {
    
    /**
     * Convert java object to protobuf message.
     *
     * @param object object
     * @return protobuf message
     */
    public static Message convertToProtobufMessage(final Object object) {
        if (null == object) {
            return NullValue.newBuilder().build();
        }
        if (object instanceof Integer) {
            return Int32Value.of((int) object);
        }
        if (object instanceof Short) {
            return Int32Value.of(((Short) object).intValue());
        }
        if (object instanceof Byte) {
            return Int32Value.of(((Byte) object).intValue());
        }
        if (object instanceof Long) {
            return Int64Value.of((long) object);
        }
        if (object instanceof BigInteger) {
            return BigIntegerValue.newBuilder().setValue(ByteString.copyFrom(((BigInteger) object).toByteArray())).build();
        }
        if (object instanceof Float) {
            return FloatValue.of((float) object);
        }
        if (object instanceof Double) {
            return DoubleValue.of((double) object);
        }
        if (object instanceof BigDecimal) {
            return BigDecimalValue.newBuilder().setValue(object.toString()).build();
        }
        if (object instanceof String) {
            return StringValue.of(object.toString());
        }
        if (object instanceof Boolean) {
            return BoolValue.of((boolean) object);
        }
        if (object instanceof byte[]) {
            return BytesValue.of(ByteString.copyFrom((byte[]) object));
        }
        if (object instanceof Date) {
            return converToProtobufTimestamp((Date) object);
        }
        if (object instanceof LocalDateTime) {
            return converToProtobufTimestamp(Timestamp.valueOf((LocalDateTime) object));
        }
        if (object instanceof LocalDate) {
            return converToProtobufTimestamp(Timestamp.valueOf(((LocalDate) object).atStartOfDay()));
        }
        if (object instanceof LocalTime) {
            LocalTime localTime = (LocalTime) object;
            return LocalTimeValue.newBuilder().setValue(localTime.toString()).build();
        }
        if (object instanceof ZonedDateTime) {
            return converToProtobufTimestamp(Timestamp.valueOf(((ZonedDateTime) object).toLocalDateTime()));
        }
        if (object instanceof Instant) {
            Instant instant = (Instant) object;
            return com.google.protobuf.Timestamp.newBuilder().setSeconds(instant.getEpochSecond()).setNanos(instant.getNano()).build();
        }
        if (object instanceof Clob) {
            Clob clob = (Clob) object;
            try {
                return ClobValue.newBuilder().setValue(clob.getSubString(1, (int) clob.length())).build();
            } catch (final SQLException ex) {
                log.error("get clob length failed", ex);
                throw new RuntimeException(ex);
            }
        }
        if (object instanceof Blob) {
            Blob blob = (Blob) object;
            try {
                return BlobValue.newBuilder().setValue(ByteString.copyFrom(blob.getBytes(1, (int) blob.length()))).build();
            } catch (final SQLException ex) {
                log.error("get blob bytes failed", ex);
                throw new RuntimeException(ex);
            }
        }
        return StringValue.newBuilder().setValue(object.toString()).build();
    }
    
    private static com.google.protobuf.Timestamp converToProtobufTimestamp(final Date timestamp) {
        long millis = timestamp.getTime();
        return com.google.protobuf.Timestamp.newBuilder().setSeconds(millis / 1000).setNanos((int) ((millis % 1000) * 1000000)).build();
    }
}

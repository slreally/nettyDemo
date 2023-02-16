package com.example.protobuf;

import com.google.protobuf.InvalidProtocolBufferException;

import java.util.Map;
import java.util.TreeMap;

public class Test {
    public static void main(String[] args) throws InvalidProtocolBufferException {
        DbsCassandraProto.Value value = DbsCassandraProto.Value.newBuilder()
                .setDType(0)
                .setDValue(2.9)
                .build();

        DbsCassandraProto.ResultOfQuery result = DbsCassandraProto.ResultOfQuery.newBuilder()
                .putResult(1L, value)
                .putResult(2L, value)
                .build();

        byte[] bytes = result.toByteArray();
        DbsCassandraProto.ResultOfQuery resultOfQuery = DbsCassandraProto.ResultOfQuery.parseFrom(bytes);
        Map<Long, DbsCassandraProto.Value> resultMap = resultOfQuery.getResultMap();
        for (Map.Entry<Long, DbsCassandraProto.Value> entry : resultMap.entrySet()) {
            System.out.println(entry.getKey() + "  " + entry.getValue().getDValue());
        }
    }
}

//package com.tbfp.teamplannerbe.domain.chat.entity;
//
//import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
//
//import java.time.LocalDateTime;
//import java.time.ZoneOffset;
//
//public class LocalDateTimeConverter implements DynamoDBTypeConverter<String, LocalDateTime> {
//
//    @Override
//    public String convert(final LocalDateTime time) {
//        return time.toInstant(ZoneOffset.UTC).toString();
//    }
//
//    @Override
//    public LocalDateTime unconvert(final String stringValue) {
//        return LocalDateTime.parse(stringValue);
//    }
//}
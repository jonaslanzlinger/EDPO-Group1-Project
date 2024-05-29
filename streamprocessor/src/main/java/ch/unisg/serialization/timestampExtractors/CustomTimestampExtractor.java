package ch.unisg.serialization.timestampExtractors;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.streams.processor.TimestampExtractor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Custom timestamp extractor that extracts the timestamp from the value of the record.
 * Done by utilizing a regular expression to extract the timestamp from the value.
 */
public class CustomTimestampExtractor implements TimestampExtractor {

    @Override
    public long extract(ConsumerRecord<Object, Object> record, long previousTimestamp) {

        Pattern pattern = Pattern.compile("timestamp=(.*?),");
        Matcher matcher = pattern.matcher(record.value().toString());

        if (matcher.find()) {
            String timestampStr = matcher.group(1);
            if (!timestampStr.isEmpty()) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SS");
                LocalDateTime dateTime = LocalDateTime.parse(timestampStr, formatter);
                Instant timestamp = dateTime.toInstant(ZoneOffset.UTC);
                return timestamp.toEpochMilli();
            }
        }
        return System.currentTimeMillis();
    }
}
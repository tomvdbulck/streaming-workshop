package be.ordina.workshop.streaming.opendatatraffic;

import be.ordina.workshop.streaming.opendatatraffic.cloud.Channels;
import be.ordina.workshop.streaming.opendatatraffic.domain.TrafficEvent;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.binder.kstream.annotations.KStreamProcessor;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.handler.annotation.SendTo;

import java.util.Arrays;


@SpringBootApplication
public class OpenDataTrafficApplication {


	public static void main(String[] args) {
		SpringApplication.run(OpenDataTrafficApplication.class, args);
	}


	@EnableBinding(Channels.class)
	@EnableAutoConfiguration
	public static class StreamProcessor {
		@StreamListener("trafficEventsKStream")
		@SendTo("ouputKStreams")
		public KStream<?, String> process(KStream<?, TrafficEvent> input) {
			return input
					.flatMapValues(value -> Arrays.asList(value.getSensorId()))
					.map((key, sensorId) -> new KeyValue<>(sensorId, sensorId))
					.groupByKey(Serdes.String(), Serdes.String())
					.count(TimeWindows.of(50000), "store-name")
					.toStream()
					.map((w, c) -> new KeyValue<>(null, "Count for " + w.key() + ": " + c));
		}

		@StreamListener("binding1")
		public void sink(String input) {
			System.out.println("FOOBAR -- " + input);
		}
	}

}

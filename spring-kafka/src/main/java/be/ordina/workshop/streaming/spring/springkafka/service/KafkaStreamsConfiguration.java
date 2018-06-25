package be.ordina.workshop.streaming.spring.springkafka.service;

import be.ordina.workshop.streaming.spring.springkafka.domain.SensorData;
import be.ordina.workshop.streaming.spring.springkafka.domain.SensorDataSerde;
import be.ordina.workshop.streaming.spring.springkafka.domain.TrafficEvent;
import be.ordina.workshop.streaming.spring.springkafka.domain.TrafficEventSerde;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.streams.Consumed;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.kstream.internals.WindowedDeserializer;
import org.apache.kafka.streams.kstream.internals.WindowedSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.kafka.support.serializer.JsonSerializer;

import javax.annotation.PostConstruct;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableKafka
@EnableKafkaStreams
@Slf4j
public class KafkaStreamsConfiguration {

    private List<String> sensorIdsToProcess;

    private final HashMap<String, Integer> totalVehicleCountPerSensor;
    private final HashMap<String, Integer> totalVehicleCountPerSensorPerType;
    private final HashMap<String, Integer> highestSpeedPerSensor;
    private final HashMap<String, Integer> lowestSpeedPerSensor;

    public KafkaStreamsConfiguration(){
        this.totalVehicleCountPerSensor = new HashMap<>();
        this.totalVehicleCountPerSensorPerType = new HashMap<>();
        this.highestSpeedPerSensor = new HashMap<>();
        this.lowestSpeedPerSensor = new HashMap<>();
    }

    public void printOutStats() {
        System.out.println("==========    TOTAL PER SENSOR ===========");
        for (String key : totalVehicleCountPerSensor.keySet()) {
            System.out.println(key + ": " + totalVehicleCountPerSensor.get(key));
        }

        System.out.println("==========    TOTAL PER SENSOR PER VEHICLE TYPE ===========");
        for (String key : totalVehicleCountPerSensorPerType.keySet()) {
            System.out.println(key + ": " + totalVehicleCountPerSensorPerType.get(key));
        }

        System.out.println("==========    FASTERS PER SENSOR ===========");
        for (String key : highestSpeedPerSensor.keySet()) {
            System.out.println(key + ": " + highestSpeedPerSensor.get(key));
        }

        System.out.println("==========    SLOWEST PER SENSOR ===========");
        for (String key : lowestSpeedPerSensor.keySet()) {
            System.out.println(key + ": " + lowestSpeedPerSensor.get(key));
        }
    }

    private void updateStats(TrafficEvent trafficEvent) {

        //System.out.println("Processing data for trafficEvent" + trafficEvent);

        Integer totalCountSensor = (totalVehicleCountPerSensor.get(trafficEvent.getSensorId()) != null ? totalVehicleCountPerSensor.get(trafficEvent.getSensorId()) : 0);
        totalVehicleCountPerSensor.put(trafficEvent.getSensorId(), totalCountSensor + trafficEvent.getTrafficIntensity());

        Integer totalCountPerSensorPerType = (totalVehicleCountPerSensorPerType.get(trafficEvent.getSensorId() + " " +trafficEvent.getVehicleClass().name()) != null
                ? totalVehicleCountPerSensorPerType.get(trafficEvent.getSensorId() + " " +trafficEvent.getVehicleClass().name()) : 0);
        totalVehicleCountPerSensorPerType.put(trafficEvent.getSensorId() + " " +trafficEvent.getVehicleClass().name(), totalCountPerSensorPerType + trafficEvent.getTrafficIntensity());

        Integer fastestPerSensor = (highestSpeedPerSensor.get(trafficEvent.getSensorId()) != null ? highestSpeedPerSensor.get(trafficEvent.getSensorId()) : 0);
        if (fastestPerSensor < trafficEvent.getVehicleSpeedCalculated()) {
            highestSpeedPerSensor.put(trafficEvent.getSensorId(), trafficEvent.getVehicleSpeedCalculated());
        }

        Integer lowestSpeedPserSensor = (lowestSpeedPerSensor.get(trafficEvent.getSensorId()) != null ? lowestSpeedPerSensor.get(trafficEvent.getSensorId()) : 0);
        if (lowestSpeedPserSensor > trafficEvent.getVehicleSpeedCalculated()) {
            lowestSpeedPerSensor.put(trafficEvent.getSensorId(), trafficEvent.getVehicleSpeedCalculated());
        }

    }


    @Autowired
    private KafkaProperties kafkaProperties;

    @PostConstruct
    public void setupSensorsToFilter() throws Exception {
        this.sensorIdsToProcess =  Files.lines(Paths.get(getClass().getResource("/sensors_mechelen_n_z.txt").toURI() ))
                .map(String::toUpperCase)
                .collect(Collectors.toList());
    }

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public StreamsConfig kStreamsConfigs() {
        Map<String, Object> props = new HashMap<>();
        /**
         * Required properties are: APPLICATION_ID_CONFIG and BOOTSTRAP_SERVERS_CONFIG
         *
         * SERDE: Serializer/Deserializer
         * https://kafka.apache.org/10/documentation/streams/developer-guide/datatypes.html
         * so that data can be materialized when needed
         *
         */
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "spring-kafka-traffic");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, new JsonSerde<TrafficEvent>(TrafficEvent.class).getClass());

        return new StreamsConfig(props);
    }

    @Bean
    public KStream<String, TrafficEvent> kStreamStart (StreamsBuilder streamsBuilder) {

        System.out.println(">>>>>>>>>>>>>> enter kstreamStart ");


        /**
         * Read in the data of the sensor topic.
         * Choose the correct key
         *
         * Store these into a KTable
         *
         */
        KStream<String, SensorData> sensorDescriptionsStream = streamsBuilder.stream("sensorDataOutput", Consumed.with(Serdes.String(), new SensorDataSerde()));

        KStream<String, SensorData> sensorDescriptionsWithKey = sensorDescriptionsStream.selectKey((key, value) -> value.getUniekeId());
        sensorDescriptionsWithKey.to("dummy-topic");

        KTable<String, SensorData> sensorDataKTable = streamsBuilder.table("dummy-topic", Consumed.with(Serdes.String(), new SensorDataSerde()));
        //sensorDataKTable.print();


        /**
         *
         * Retrieve the data from the traffic events
         *
         * Enrich these with the sensor data.
         *
         */

        KStream<String, TrafficEvent> stream = streamsBuilder.stream("trafficEventsOutput", Consumed.with(Serdes.String(), new TrafficEventSerde()));
        stream.selectKey((key,value) -> value.getSensorId())
                .join(sensorDataKTable,((TrafficEvent trafficEvent, SensorData sensorData) -> {
                    trafficEvent.setSensorData(sensorData);
                    return trafficEvent;
                }), Joined.with(Serdes.String(), new TrafficEventSerde(), null))
                .to("enriched-trafficEventsOutput");


        /**
         *
         * Filter out data which we do not want to process
         *
         */

        KStream<String, TrafficEvent> streamToProcessData = streamsBuilder.stream("enriched-trafficEventsOutput", Consumed.with(Serdes.String(), new TrafficEventSerde()));
        streamToProcessData.selectKey((key,value) -> value.getSensorId())
                .filter((key, value) -> canProcessSensor(key));

        streamToProcessData.print();


        this.createWindowStream(streamToProcessData);

        /**
         * Process data for each record of the stream.
         */

        streamToProcessData.foreach((key, value) -> updateStats(value));
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>> return ");

        return stream;
    }


    private KStream createWindowStream(KStream<String, TrafficEvent> streamToProcessData) {



        streamToProcessData.groupByKey()
                .windowedBy(TimeWindows.of(300000).advanceBy(60000));


        Initializer initializer = () -> new SensorCount();

        KTable<byte[], Long> aggregatedStream =
                streamToProcessData.groupByKey().windowedBy(TimeWindows.of(300000).advanceBy(60000))
                .aggregate(initializer, (key, value, aggregate) -> aggregate.addValue(value.getTrafficIntensity()),
                        Materialized.with(Serdes.String(), new JsonSerde<>(SensorCount.class)))
                .toStream()
                .print()


    }


    static class SensorCount {

        int count;
        Integer sum;

        public SensorCount() {
        }

        public SensorCount addValue(Integer value) {
            this.sum += value;
            count++;
            return this;
        }

        public double average() {
            return sum / count;
        }

        public void getSum() {

        }
    }

    static class WindowedSerde<T> implements Serde<Windowed<T>> {

        private final Serde<Windowed<T>> inner;

        public WindowedSerde(Serde<T> serde) {
            inner = Serdes.serdeFrom(
                    new WindowedSerializer<>(serde.serializer()),
                    new WindowedDeserializer<>(serde.deserializer()));
        }

        @Override
        public Serializer<Windowed<T>> serializer() {
            return inner.serializer();
        }

        @Override
        public Deserializer<Windowed<T>> deserializer() {
            return inner.deserializer();
        }

        @Override
        public void configure(Map<String, ?> configs, boolean isKey) {
            inner.serializer().configure(configs, isKey);
            inner.deserializer().configure(configs, isKey);
        }

        @Override
        public void close() {
            inner.serializer().close();
            inner.deserializer().close();
        }

    }





    private boolean canProcessSensor(String key) {
        return this.sensorIdsToProcess.contains(key);
    }


    @Bean
    public KStream<String, SensorData> kStreamSensorIdentification(StreamsBuilder streamsBuilder){
        //KStream<String, SensorData> sensorDescriptionsStream = streamsBuilder.stream("sensorDataOutput", Consumed.with(Serdes.String(), new SensorDataSerde()));

        //KStream<String, SensorData> sensorDescriptionsWithKey = sensorDescriptionsStream.selectKey((key, value) -> value.getUniekeId());
        //sensorDescriptionsWithKey.to("sensorDataOutputWithKey");
        //sensorDescriptionsWithKey.print();

        //return sensorDescriptionsStream;

        return null;
    }



/**
 //@Bean
    public KStream<Windowed<String>, Integer> kStream(StreamsBuilder streamsBuilder) {

        System.out.println(">>>>>>>>>>>>>> enter kstream");

        JsonSerde<TrafficEvent> trafficEventJsonSerde = new JsonSerde<>(TrafficEvent.class);

        KStream<String, String> stream = streamsBuilder.stream("trafficEventsOutput", Consumed.with(Serdes.String(), Serdes.String()));

        KStream<Windowed<String>, Integer> countedSensorStream = stream.map(new SensorKeyValueMapper())
                .groupByKey()
                .reduce((Integer value1, Integer value2) -> value1 + value2, TimeWindows.of(1000), "windowStore")
                .toStream();

        KStream<String, TrafficEvent> stream = streamsBuilder.stream("trafficEventsOutput", Consumed.with(Serdes.String(), new TrafficEventSerde()));

        KStream<Windowed<String>, Integer> countedSensorStream = stream.map(new SensorKeyValueMapper())
                .groupByKey()
                .reduce((Integer value1, Integer value2) -> value1 + value2, TimeWindows.of(1000), "windowStore")
                .toStream();

        countedSensorStream.to("streams-output", Produced.valueSerde(Serdes.Integer()));

        System.out.println("print ");
        countedSensorStream.print();


        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>> return ");

        return countedSensorStream;

    }
 **/

    public class SensorKeyValueMapper implements KeyValueMapper<String, String, KeyValue<String, Integer>> {


        @Override
        public KeyValue<String, Integer> apply(String key, String value) {
            return new KeyValue<>(value, 1);
        }

        //@Override
        public KeyValue<String, Integer> apply(String key, TrafficEvent value) {
            return new KeyValue<>(value.getSensorId(), 1);
        }

    }

    public class CountReducer implements Reducer<Integer> {

        @Override
        public Integer apply(Integer val1, Integer val2) {
            return val1 + val2;
        }
    }
}

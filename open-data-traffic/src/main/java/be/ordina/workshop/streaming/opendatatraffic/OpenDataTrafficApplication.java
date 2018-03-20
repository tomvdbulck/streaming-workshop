package be.ordina.workshop.streaming.opendatatraffic;

import be.ordina.workshop.streaming.opendatatraffic.cloud.InputChannels;
import be.ordina.workshop.streaming.opendatatraffic.cloud.OutputChannels;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;


@SpringBootApplication
@EnableBinding({InputChannels.class, OutputChannels.class})
public class OpenDataTrafficApplication {


	public static void main(String[] args) {
		SpringApplication.run(OpenDataTrafficApplication.class, args);
	}
}

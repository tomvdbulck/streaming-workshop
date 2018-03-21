package be.ordina.workshop.streaming.opendatatraffic;

import be.ordina.workshop.streaming.opendatatraffic.cloud.Channels;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;


@SpringBootApplication
@EnableBinding({Channels.class})
public class OpenDataTrafficApplication {


	public static void main(String[] args) {
		SpringApplication.run(OpenDataTrafficApplication.class, args);
	}
}

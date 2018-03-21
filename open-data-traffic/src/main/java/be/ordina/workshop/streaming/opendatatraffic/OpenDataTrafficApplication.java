package be.ordina.workshop.streaming.opendatatraffic;

import be.ordina.workshop.streaming.opendatatraffic.cloud.Channels;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;


@SpringBootApplication
@EnableBinding({Channels.class})
@Slf4j
public class OpenDataTrafficApplication {


	public static void main(String[] args) {
		SpringApplication.run(OpenDataTrafficApplication.class, args);
	}


}

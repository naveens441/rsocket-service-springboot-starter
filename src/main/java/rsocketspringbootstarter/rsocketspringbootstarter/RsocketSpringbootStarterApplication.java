package rsocketspringbootstarter.rsocketspringbootstarter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.Stream;

@SpringBootApplication
public class RsocketSpringbootStarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(RsocketSpringbootStarterApplication.class, args);
    }

}

@Controller
@RequiredArgsConstructor
class GreetingController {
	private final GreetingService greetingService;

    @MessageMapping("greetings.{timeInSeconds}")
	Flux<GreetingResponse> greet(@DestinationVariable("timeInSeconds") int timeInSeconds,
								 GreetingRequest request){
		return this.greetingService.greet(request,timeInSeconds);
	}
}

@Service
class GreetingService {
    Flux<GreetingResponse> greet(GreetingRequest greetingRequest,int delay) {
        return Flux.fromStream(Stream.generate(() -> new GreetingResponse(
                "Hello" + greetingRequest.getName() + "@" + Instant.now())
        )).delayElements(Duration.ofSeconds(delay));
    }
}


@Data
@NoArgsConstructor
@AllArgsConstructor
class GreetingRequest {
    private String name;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class GreetingResponse {
    private String message;
}

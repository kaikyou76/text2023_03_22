import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}

@RestController
class StatusCheckController {

    private final RestTemplate restTemplate;

    public StatusCheckController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/checkStatus")
    public String checkStatus() {
        String sourceUrl = "YBMトップメニューのURL";
        String targetUrl = "ルート集配認可リクエストAPIのURL";

        try {
            restTemplate.getForObject(sourceUrl, String.class);
            restTemplate.getForObject(targetUrl, String.class);
            return "ステータスは正常です";
        } catch (Exception e) {
            return "ステータスが正常ではありません";
        }
    }
}

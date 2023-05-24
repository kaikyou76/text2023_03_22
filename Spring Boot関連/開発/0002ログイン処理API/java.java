import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class ApiAccessChecker {
    
    public static void main(String[] args) {
        String apiUrl = "http://example.com/api"; // アクセス先のAPIのURL
        
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);
        
        HttpStatus statusCode = response.getStatusCode();
        if (statusCode.is2xxSuccessful()) {
            System.out.println("ステータス: 正常 (" + statusCode + ")");
        } else {
            System.out.println("ステータス: 異常 (" + statusCode + ")");
        }
    }
}

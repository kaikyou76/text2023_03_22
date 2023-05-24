import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CheckEndpointStatusService {

    public boolean isEndpointStatusOk() {
        String rootApiUrl = "http://ルート集配ログイン処理APIのURL";
        String ybmTokenApiUrl = "http://YBMトークンエンドポイントAPIのURL";

        RestTemplate restTemplate = new RestTemplate();

        // ルート集配ログイン処理APIへのリクエスト
        ResponseEntity<Void> rootApiResponse = restTemplate.getForEntity(rootApiUrl, Void.class);

        if (rootApiResponse.getStatusCode() == HttpStatus.OK) {
            // ルート集配ログイン処理APIへのリクエストが正常終了した場合は、YBMトークンエンドポイントAPIにアクセスしてステータスを確認
            ResponseEntity<Void> ybmTokenApiResponse = restTemplate.getForEntity(ybmTokenApiUrl, Void.class);

            return ybmTokenApiResponse.getStatusCode() == HttpStatus.OK;
        }

        return false;
    }
}

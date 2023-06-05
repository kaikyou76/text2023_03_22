import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class MyApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);

        String fileName = "example.csv"; // ファイル名を指定

        // ファイルの存在を確認
        File file = new File(fileName);
        if (!file.exists()) {
            String errorMessage = "存在しない CSV/EOFファイル名: " + fileName;
            throw new BatRuntimeException("BT000E002", errorMessage);
        }

        // ファイルが存在する場合、その他の処理を実行する
        // ...
    }
}

class BatRuntimeException extends RuntimeException {
    private final String messageId;
    private final String message;

    public BatRuntimeException(String messageId, String message) {
        super(message);
        this.messageId = messageId;
        this.message = message;
    }

    public String getMessageId() {
        return messageId;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class MyApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);

        String fileName = "example.csv"; // �t�@�C�������w��

        // �t�@�C���̑��݂��m�F
        File file = new File(fileName);
        if (!file.exists()) {
            String errorMessage = "���݂��Ȃ� CSV/EOF�t�@�C����: " + fileName;
            throw new BatRuntimeException("BT000E002", errorMessage);
        }

        // �t�@�C�������݂���ꍇ�A���̑��̏��������s����
        // ...
    }
}

class BatRuntimeException extends RuntimeException {
    private final String messageId;

    public BatRuntimeException(String messageId, String message) {
        super(message);
        this.messageId = messageId;
    }

    public String getMessageId() {
        return messageId;
    }
}

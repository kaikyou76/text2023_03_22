import org.springframework.batch.core.step.skip.SkipRuntimeException;

public class BatRuntimeException extends SkipRuntimeException {

    public BatRuntimeException(String message) {
        super(message);
    }
}

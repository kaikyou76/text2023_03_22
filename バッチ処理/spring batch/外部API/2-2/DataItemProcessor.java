import org.springframework.batch.item.ItemProcessor;

public class DataItemProcessor implements ItemProcessor<DataDTO, DataDTO> {

    @Override
    public DataDTO process(DataDTO data) throws Exception {
        // データの変換や加工処理を行う場合はここに実装する
        return data;
    }
}

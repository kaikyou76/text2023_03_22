import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class OrganizationController {

    @GetMapping("/upload")
    public String showUploadForm(Model model) {
        model.addAttribute("organizationForm", new OrganizationForm());
        return "upload-form";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String processUpload(@Valid OrganizationForm organizationForm, BindingResult result) {
        if (result.hasErrors()) {
            return "upload-form";
        }

        MultipartFile file = organizationForm.getFile();
        List<FieldError> validationErrors = validateCSV(file);

        if (validationErrors.isEmpty()) {
            // バリデーションエラーがない場合の処理
            return "success";
        } else {
            // バリデーションエラーがある場合の処理
            for (FieldError error : validationErrors) {
                result.rejectValue(error.getField(), error.getCode(), error.getDefaultMessage());
            }
            return "upload-form";
        }
    }

    private List<FieldError> validateCSV(MultipartFile file) {
        List<FieldError> validationErrors = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            int lineNumber = 1;
            reader.readLine(); // ヘッダー行を読み飛ばす

            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");

                if (values.length != 9) {
                    FieldError error = new FieldError("organizationForm", "line " + lineNumber, "列数が一致しません。");
                    validationErrors.add(error);
                }

                String organizationCd = values[0];
                if (organizationCd == null) {
                    FieldError error = new FieldError("organizationForm", "line " + lineNumber, "組織コードが空になっています！");
                    validationErrors.add(error);
                } else if (organizationCd.length() != 19 || !organizationCd.matches("\\d+")) {
                    FieldError error = new FieldError("organizationForm", "line " + lineNumber, "組織コードは19桁数字に直してください。");
                    validationErrors.add(error);
                }

                // 以下省略...

                String updateDate = values[8];
                if (updateDate == null) {
                    FieldError error = new FieldError("organizationForm", "line " + lineNumber, "更新日が空になっています！");
                    validationErrors.add(error);
                } else {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                    try {
                        LocalDateTime.parse(updateDate, formatter);
                    } catch (Exception e) {
                        FieldError error = new FieldError("organizationForm", "line " + lineNumber, "更新日はYYYYMMDDHH24MISS形式にしてください。");
                        validationErrors.add(error);
                    }
                }

                lineNumber++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return validationErrors;
    }
}

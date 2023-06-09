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

import org.springframework.beans.factory.annotation.Autowired;
import jp.co.netmarks.persistence.OrganizationService;

@Controller
public class OrganizationController {
	
	private final OrganizationService organizationService;

	@Autowired
	public OrganizationController(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}

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
			List<String> csvLines = readCSV(file);
            organizationService.insertOrganization(csvLines);
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

                if (values.length != 8) {
                    FieldError error = new FieldError("organizationForm", "line " + lineNumber, "列数が一致しません。");
                    validationErrors.add(error);
                }

                String organizationCd = values[0];
                if (organizationCd == null) {
                    FieldError error = new FieldError("organizationForm", "line " + lineNumber, "組織コードが空になっています！");
                    validationErrors.add(error);
                } else if (organizationCd.length() != 19) {
                    FieldError error = new FieldError("organizationForm", "line " + lineNumber, "組織コードが19桁に直して下しい。");
                    validationErrors.add(error);
                } else if (!organizationCd.matches("\\d+")) {
                    FieldError error = new FieldError("organizationForm", "line " + lineNumber, "組織コードは数字に直してください。");
                    validationErrors.add(error);
                }

                String organizationNm = values[1];
                if (organizationNm == null) {
                    FieldError error = new FieldError("organizationForm", "line " + lineNumber, "組織名が空になっています！");
                    validationErrors.add(error);
                } else if (organizationNm.length() > 40) {
                    FieldError error = new FieldError("organizationForm", "line " + lineNumber, "組織名が最大桁数40を超えています！");
                    validationErrors.add(error);
                }

                String organizationNo = values[2];
                if (organizationNo == null) {
                    FieldError error = new FieldError("organizationForm", "line " + lineNumber, "組織番号が空になっています！");
                    validationErrors.add(error);
                } else if (organizationNo.length() != 7) {
                    FieldError error = new FieldError("organizationForm", "line " + lineNumber, "組織番号を７桁に直してください！");
                    validationErrors.add(error);
                } else if (!organizationNo.matches("\\d+")) {
                    FieldError error = new FieldError("organizationForm", "line " + lineNumber, "組織番号は数字に直してください。");
                    validationErrors.add(error);
                }

                String organizationAbbreviatedNm = values[3];
                if (organizationAbbreviatedNm == null) {
                    FieldError error = new FieldError("organizationForm", "line " + lineNumber, "組織名略称が空になっています！");
                    validationErrors.add(error);
                } else if (organizationAbbreviatedNm.length() > 10) {
                    FieldError error = new FieldError("organizationForm", "line " + lineNumber, "組織名略称が最大桁数10を超えています！");
                    validationErrors.add(error);
                }

                String printOrder = values[4];
                if (printOrder == null) {
                    FieldError error = new FieldError("organizationForm", "line " + lineNumber, "プリント順が空になっています！");
                    validationErrors.add(error);
                } else if (printOrder.length() != 2) {
                    FieldError error = new FieldError("organizationForm", "line " + lineNumber, "プリント順の桁数2に直してください！");
                    validationErrors.add(error);
                } else if (!printOrder.matches("\\d+")) {
                    FieldError error = new FieldError("organizationForm", "line " + lineNumber, "プリント順は数字に直してください。");
                    validationErrors.add(error);
                }

                String classSales = values[5];
                if (classSales == null) {
                    FieldError error = new FieldError("organizationForm", "line " + lineNumber, "営業区分が空になっています！");
                    validationErrors.add(error);
                } else if (classSales.length() != 2) {
                    FieldError error = new FieldError("organizationForm", "line " + lineNumber, "営業区分を2桁に直してください！");
                    validationErrors.add(error);
                } else if (!classSales.matches("\\d+")) {
                    FieldError error = new FieldError("organizationForm", "line " + lineNumber, "営業区分は数字に直してください。");
                    validationErrors.add(error);
                }

                String classDataInput = values[6];
                if (classDataInput == null) {
                    FieldError error = new FieldError("organizationForm", "line " + lineNumber, "データ入力区分が空になっています！");
                    validationErrors.add(error);
                } else if (classDataInput.length() != 2) {
                    FieldError error = new FieldError("organizationForm", "line " + lineNumber, "データ入力区分を2桁に直してください！");
                    validationErrors.add(error);
                } else if (!classDataInput.matches("\\d+")) {
                    FieldError error = new FieldError("organizationForm", "line " + lineNumber, "データ入力区分は数字に直してください。");
                    validationErrors.add(error);
                }

                String updateDate = values[7];
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
    
    private List<String> readCSV(MultipartFile file) {
        List<String> csvLines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            reader.readLine(); // ヘッダー行を読み飛ばす

            while ((line = reader.readLine()) != null) {
                csvLines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return csvLines;
    }	
}

このコードは、Spring Frameworkを使用して、組織データのCSVファイルのアップロードとバリデーションを行うためのコントローラークラスです。以下に、コードの概要を説明します。

1. `@Controller` アノテーションは、このクラスがコントローラーであることを示しています。
2. `OrganizationController` クラスは、`OrganizationService` を注入するためのコンストラクタを持っています。
3. `showUploadForm` メソッドは、アップロードフォームを表示するためのGETリクエストを処理します。`Model` オブジェクトを使用して、ビューにデータを渡します。
4. `processUpload` メソッドは、アップロードされたファイルの処理を行うためのPOSTリクエストを処理します。バリデーションエラーがある場合は、エラーメッセージを返してアップロードフォームを再表示します。バリデーションエラーがない場合は、CSVファイルを読み取り、組織データをデータベースに挿入します。
5. `validateCSV` メソッドは、アップロードされたCSVファイルのバリデーションを行います。各フィールドのバリデーションルールに従って、エラーメッセージを生成します。
6. `readCSV` メソッドは、アップロードされたCSVファイルを読み取り、行ごとにリストに格納します。

以上が修正後のコードです。このコードを使用すると、アップロードされたCSVファイルのバリデーションが実行され、エラーメッセージが表示されます。バリデーションエラーがない場合は、組織データがデータベースに挿入されます。

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import jp.co.netmarks.persistence.OrganizationService;
import jp.co.netmarks.persistence.BatRuntimeException;

@Controller
public class OrganizationController {

    private MessageSource messageSource;
    private final OrganizationService organizationService;

    @Autowired
    public OrganizationController(OrganizationService organizationService, MessageSource messageSource) {
        this.organizationService = organizationService;
        this.messageSource = messageSource;
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        // ファイル移動のためのディレクトリ設定
        String inputDir = "InputDir"; // 変数A
        String receiveDir = "ReceiveDin"; // 変数L--連携ファイルディレクトリ (AD及び統合IDシステムから連携されたファイルの配置フォルダ)

        // 連携ファイルの移動
        moveFiles(receiveDir, inputDir);

        String errorMessage;
		// 処理1: organization.csvを受け取る
        // fileを使用して処理を行います
        String fileName = file.getOriginalFilename();

        try {
            // 必須CSVファイルとEOFファイルの存在チェック
            String csvDir = inputDir; // CSVファイルの格納ディレクトリは変数A
            String eofAdFile = csvDir + File.separator + "EOFAD"; // EOFADファイル名は変数D
            String eofAmFile = csvDir + File.separator + "EOFAM"; // EOFAMファイル名は変数E
            String csvAdFile = csvDir + File.separator + "organization.csv"; // 組織情報ファイル名は変数F
            String csvDepartmentFile = csvDir + File.separator + "department.csv"; // 店部課情報ファイル名は変数G
            String csvEmployeeFile = csvDir + File.separator + "employee.csv"; // 社員情報ファイル名は変数H

            if (!checkFileExists(eofAdFile) || !checkFileExists(eofAmFile) ||
                    !checkFileExists(csvAdFile) || !checkFileExists(csvDepartmentFile) || !checkFileExists(csvEmployeeFile)) {
                errorMessage = "CSV/EOFファイル[" + fileName + "]が存在しません。";
                System.out.println(errorMessage);
                throw new BatRuntimeException(errorMessage);
            }
            errorMessage = messageSource.getMessage("BT_000_E002", new Object[]{fileName}, LocaleContextHolder.getLocale());
        } catch (NoSuchMessageException e) {
            errorMessage = "CSV/EOFファイル[" + fileName + "]が存在しません。";
        }

        // 処理2: バリデーションチェックを行う
        List<FieldError> validationErrors = validateCSV(file);
        if (validationErrors.isEmpty()) {
			List<String> csvLines = readCSV(file);
            organizationService.insertOrganization(csvLines);
            return "success"; // 成功した場合の遷移先を指定してください
        } else {
            // バリデーションエラーがある場合の処理
            for (FieldError error : validationErrors) {
                result.rejectValue(error.getField(), error.getCode(), error.getDefaultMessage());
            }		
            return "upload-form"; // エラー時の遷移先
        }
    }

    private static void moveFiles(String sourceDir, String destinationDir) {
        File sourceDirectory = new File(sourceDir);
        File[] files = sourceDirectory.listFiles();
        if (files != null) {
            for (File file : files) {
                try {
                    File destination = new File(destinationDir + File.separator + file.getName());
                    Files.move(file.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static boolean checkFileExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
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

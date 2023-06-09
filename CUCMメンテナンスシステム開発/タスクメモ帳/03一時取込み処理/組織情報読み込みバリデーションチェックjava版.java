import org.springframework.validation.FieldError;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String fileName = "organization.csv";

        List<FieldError> validationErrors = validateCSV(fileName);

        if (validationErrors.isEmpty()) {
            System.out.println("CSVデータのバリデーションが成功しました。");
        } else {
            System.out.println("CSVデータのバリデーションエラーがあります:");
            for (FieldError error : validationErrors) {
                System.out.println("Field: " + error.getField() + ", Message: " + error.getDefaultMessage());
            }
        }
    }

    private static List<FieldError> validateCSV(String fileName) {
        List<FieldError> validationErrors = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            int lineNumber = 1;
            reader.readLine(); // ヘッダー行を読み飛ばす

            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");

                if (values.length != 8) {
                    FieldError error = new FieldError("organization", "line " + lineNumber, "列数が一致しません。");
                    validationErrors.add(error);
                }

                String organizationCd = values[0];
                if (organizationCd == null) {
                    FieldError error = new FieldError("organization", "line " + lineNumber, "組織コードが空になっています！");
                    validationErrors.add(error);
                } else if (organizationCd.length() != 19) {
                    FieldError error = new FieldError("organization", "line " + lineNumber, "組織コードが19桁に直して下しい。");
                    validationErrors.add(error);
                } else if (!organizationCd.matches("\\d+")) {
                    FieldError error = new FieldError("organization", "line " + lineNumber, "組織コードは数字に直してください。");
                    validationErrors.add(error);
                }

                String organizationNm = values[1];
                if (organizationNm == null) {
                    FieldError error = new FieldError("organization", "line " + lineNumber, "組織名が空になっています！");
                    validationErrors.add(error);
                } else if (organizationNm.length() > 40) {
                    FieldError error = new FieldError("organization", "line " + lineNumber, "組織名が最大桁数40を超えています！");
                    validationErrors.add(error);
                }

                String organizationNo = values[2];
                if (organizationNo == null) {
                    FieldError error = new FieldError("organization", "line " + lineNumber, "組織番号が空になっています！");
                    validationErrors.add(error);
                } else if (organizationNo.length() != 7) {
                    FieldError error = new FieldError("organization", "line " + lineNumber, "組織番号を７桁に直してください！");
                    validationErrors.add(error);
                } else if (!organizationNo.matches("\\d+")) {
                    FieldError error = new FieldError("organization", "line " + lineNumber, "組織番号は数字に直してください。");
                    validationErrors.add(error);
                }

                String organizationAbbreviatedNm = values[3];
                if (organizationAbbreviatedNm == null) {
                    FieldError error = new FieldError("organization", "line " + lineNumber, "組織名略称が空になっています！");
                    validationErrors.add(error);
                } else if (organizationAbbreviatedNm.length() > 10) {
                    FieldError error = new FieldError("organization", "line " + lineNumber, "組織名略称が最大桁数10を超えています！");
                    validationErrors.add(error);
                }

                String printOrder = values[4];
                if (printOrder == null) {
                    FieldError error = new FieldError("organization", "line " + lineNumber, "プリント順が空になっています！");
                    validationErrors.add(error);
                } else if (printOrder.length() != 2) {
                    FieldError error = new FieldError("organization", "line " + lineNumber, "プリント順の桁数2に直してください！");
                    validationErrors.add(error);
                } else if (!printOrder.matches("\\d+")) {
                    FieldError error = new FieldError("organization", "line " + lineNumber, "プリント順は数字に直してください。");
                    validationErrors.add(error);
                }

                String classSales = values[5];
                if (classSales == null) {
                    FieldError error = new FieldError("organization", "line " + lineNumber, "営業区分が空になっています！");
                    validationErrors.add(error);
                } else if (classSales.length() != 2) {
                    FieldError error = new FieldError("organization", "line " + lineNumber, "営業区分を2桁に直してください！");
                    validationErrors.add(error);
                } else if (!classSales.matches("\\d+")) {
                    FieldError error = new FieldError("organization", "line " + lineNumber, "営業区分は数字に直してください。");
                    validationErrors.add(error);
                }

                String classDataInput = values[6];
                if (classDataInput == null) {
                    FieldError error = new FieldError("organization", "line " + lineNumber, "データ入力区分が空になっています！");
                    validationErrors.add(error);
                } else if (classDataInput.length() != 2) {
                    FieldError error = new FieldError("organization", "line " + lineNumber, "データ入力区分を2桁に直してください！");
                    validationErrors.add(error);
                } else if (!classDataInput.matches("\\d+")) {
                    FieldError error = new FieldError("organization", "line " + lineNumber, "データ入力区分は数字に直してください。");
                    validationErrors.add(error);
                }

                lineNumber++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return validationErrors;
    }
}

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/organization")
public class OrganizationController {

    private final OrganizationService organizationService;

    @Autowired
    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @PostMapping("/import")
    public String importOrganizationData() {
        int importedCount = organizationService.importOrganizationData();
        String message = "一時情報(組織情報)を取込みました。(データ件数: " + importedCount + ")";
        System.out.println("メッセージID: BT_000_1006");
        System.out.println("パラメーター: " + message);
        return "import_result";
    }
}
下記仕様をSPINGMVCでコード実装してください

■一時取处理
1. 組織情報
ステップ１[tmp_integratedid_organization」テーブルのレコードをすべて削除(TRUNCATE)する
controller、servｉｃｅ、mybatisの連携で適当にcontrollerに関連コード書いてください。

ステップ2組織情報(organization.csv)在読込み,データのバリデーションチェックをおこなう。
テーブル「tmp_integratedid_organization」詳細は以下の通りです。
項目名　　　入力必須　　　形式　　　文字種類　　　　　桁数　　　　　　最大桁数　　　　　　カラム名 
組織コード　　　O　　　　　-　　　　　数字　　　　　　19　　　　　　-　　　　　　　　organization_cd
組織名　　　O　　　　　-　　　　　-　　　　　　-　　　　　　40　　　　　　　　organization_nm　　　　　　　　　　　　　　
組織番号　　　O　　　　　-　　　　　数字　　　　　　　7　　　　　　-　　　　　　　　organization_no　　　　　　
組織名略称　　　O　　　　　-　　　　　-　　　　　　　-　　　　　　10　　　　　　　　organization_abbreviated_nm
プリント順　　　O　　　　　-　　　　　数字　　　　　　　-　　　　　　4　　　　　　　　print_order
営業区分　　　O　　　　　-　　　　　数字　　　　　　2　　　　　　-　　　　　　　　class_sales
データ入力区分　　　O　　　　　-　　　　　数字　　　　　　2　　　　　　-　　　　　　　　class_data_input
更新日付　　　O　　　　　YYYYMMDDHH24MISS　　　　　-　　　　　　-　　　　　　-　　　　　　　　update_date

ステップ3読み込んだデータを「tmp_integratedid_organization」テーブルに格納する

ステップ4処理終了ログ出力
以下のメッセージIDでログを出力する
メッセージID: BT_000_1006
パラメーター:一時情報(組織情報)を取込みました。(データ件数:$1)
$1:データ件数



		
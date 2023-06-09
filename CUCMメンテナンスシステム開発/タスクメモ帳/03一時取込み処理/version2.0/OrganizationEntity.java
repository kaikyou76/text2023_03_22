import java.time.LocalDateTime;

public class OrganizationEntity {
    private int organizationCd;
    private String organizationNm;
    private int organizationNo;
    private String organizationAbbreviatedNm;
    private int printOrder;
    private int classSales;
    private int classDataInput;
    private LocalDateTime updateDate;

    public OrganizationEntity() {
        // デフォルトコンストラクタ
    }

    public OrganizationEntity(int organizationCd, String organizationNm, int organizationNo, String organizationAbbreviatedNm, int printOrder, int classSales, int classDataInput, LocalDateTime updateDate) {
        this.organizationCd = organizationCd;
        this.organizationNm = organizationNm;
        this.organizationNo = organizationNo;
        this.organizationAbbreviatedNm = organizationAbbreviatedNm;
        this.printOrder = printOrder;
        this.classSales = classSales;
        this.classDataInput = classDataInput;
        this.updateDate = updateDate;
    }

    // ゲッターとセッターの省略

    public int getOrganizationCd() {
        return organizationCd;
    }

    public void setOrganizationCd(int organizationCd) {
        this.organizationCd = organizationCd;
    }

    public String getOrganizationNm() {
        return organizationNm;
    }

    public void setOrganizationNm(String organizationNm) {
        this.organizationNm = organizationNm;
    }

    public int getOrganizationNo() {
        return organizationNo;
    }

    public void setOrganizationNo(int organizationNo) {
        this.organizationNo = organizationNo;
    }

    public String getOrganizationAbbreviatedNm() {
        return organizationAbbreviatedNm;
    }

    public void setOrganizationAbbreviatedNm(String organizationAbbreviatedNm) {
        this.organizationAbbreviatedNm = organizationAbbreviatedNm;
    }

    public int getPrintOrder() {
        return printOrder;
    }

    public void setPrintOrder(int printOrder) {
        this.printOrder = printOrder;
    }

    public int getClassSales() {
        return classSales;
    }

    public void setClassSales(int classSales) {
        this.classSales = classSales;
    }

    public int getClassDataInput() {
        return classDataInput;
    }

    public void setClassDataInput(int classDataInput) {
        this.classDataInput = classDataInput;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }
}

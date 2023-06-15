import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.netmarks.persistence.OrganizationMapper;
import jp.co.netmarks.persistence.OrganizationEntity

@Service
public class OrganizationService {

    private final OrganizationMapper organizationMapper;

    @Autowired
    public OrganizationService(OrganizationMapper organizationMapper) {
        this.organizationMapper = organizationMapper;
    }
    
    public void insertOrganization(List<String> csvLines) {
        for (String line : csvLines) {
            OrganizationEntity organization = parseOrganization(line);
            organizationMapper.insertOrganization(organization);
        }
    }
    private OrganizationEntity parseOrganization(String line) {
        String[] values = line.split(",");

        OrganizationEntity organization = new OrganizationEntity();
        organization.setOrganizationCd(values[0]);
        organization.setOrganizationNm(values[1]);
        organization.setOrganizationNo(values[2]);
        organization.setOrganizationAbbreviatedNm(values[3]);
        organization.setPrintOrder(values[4]);
        organization.setClassSales(values[5]);
        organization.setClassDataInput(values[6]);
        organization.setUpdateDate(values[7]);

        return organization;
    }	
    
    public void updateOrganization(String organizationCd) {
        organizationMapper.updateOrganization(organizationCd);
    }

    public void deleteOrganization(String organizationCd) {
        organizationMapper.deleteOrganization(organizationCd);
    }	
}

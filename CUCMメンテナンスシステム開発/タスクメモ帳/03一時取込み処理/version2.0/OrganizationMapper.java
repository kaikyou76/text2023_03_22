package jp.co.netmarks.persistence;

import jp.co.netmarks.model.OrganizationEntity;

public interface OrganizationMapper {

    void insertOrganization(OrganizationEntity organization);

    void updateOrganization(OrganizationEntity organization);

    void deleteOrganization(String organizationCd);
}

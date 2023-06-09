import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrganizationRepository {

    void truncateTable();

    void insertOrganizations(@Param("organizations") List<Organization> organizations);
}

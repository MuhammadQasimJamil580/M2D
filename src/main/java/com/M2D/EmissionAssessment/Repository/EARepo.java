package com.M2D.EmissionAssessment.Repository;

import com.M2D.EmissionAssessment.Model.EAModel;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EARepo extends JpaRepository<EAModel,Long> {

 EAModel findByIdAndStatusNot(Long id,String status);

 Optional<List<EAModel>> findByStatusIsNot(String status);
 Optional<List<EAModel>> findByStatusIn(List<String> status);
 @Query(value = "SELECT  " +
         " IFNULL(b.name,''), " +
         " IFNULL(b.description,'N/A'), " +
         " IFNULL(b.consumption,'0'), " +
         " IFNULL(b.consumption_unit,'N/A'), " +
         " IFNULL(b.factor,''), " +
         " Case when b.tco2 > 1 then ROUND (b.tco2,2) when b.tco2 < 1 then ROUND (b.tco2,4) else b.tco2 end" +
         " FROM emission_assessment a     " +
         " INNER JOIN assesment_inventory b ON a.Id = b.activity_index and b.status = 1   " +
         " WHERE a.Status = 1 and a.id = ? and b.category_index IN(?)  ",nativeQuery = true)

 Optional<List<Object[]>> findDataForEAReport(Long eaId,String scope);

 @Query(value = "SELECT * from emission_assessment where status = 1 AND baseline_total_emission > 0",nativeQuery = true)
 Optional<List<EAModel>> getDataForBaseline();

 @Query(value = " Select id from emission_assessment where status = 1 and org_site_index IN (:siteIdValues) and period_index IN (:periodIdValues)",nativeQuery = true)
 Long[]  getEmissionIndexesAgainstSiteAndPeriod(@Param("siteIdValues") Long[] siteIndexes,@Param("periodIdValues") Long[] periodIndexes );

 @Query(value = " Select id from emission_assessment where status = 1 and org_site_index IN (:siteIdValues)",nativeQuery = true)
 Long[]  getEmissionIndexesAgainstSite(@Param("siteIdValues") Long[] siteIndexes );

 @Query(value = " Select id from emission_assessment where status = 1 and  period_index IN (:periodIdValues)",nativeQuery = true)
 Long[]  getEmissionIndexesAgainstPeriod(@Param("periodIdValues") Long[] periodIndexes );

 @Query(value = " Select * from emission_assessment where status = 1 and org_site_index IN (?) and period_index IN (?)",nativeQuery = true)
 List<EAModel> getEmissionAssessments(@Param("siteIdValues") Long[] orgSiteIndex, @Param("periodIdValues") Long[] PeriodIndex );

// @Query(value = " Select id from emission_assessment where status = 1  and period_index IN (:periodIdValues)",nativeQuery = true)
// Long[]  getEmissionIndexes2(@Param("periodIdValues") String[] periodIndexes );
}

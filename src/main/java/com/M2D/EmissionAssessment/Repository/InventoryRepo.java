package com.M2D.EmissionAssessment.Repository;

import com.M2D.EmissionAssessment.Model.InventoryModel;

import com.M2D.EmissionAssessment.Model.InventoryUploadHistoryModel;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Optional;
@Repository
    public interface InventoryRepo extends JpaRepository<InventoryModel, Long> {

    public List<InventoryModel> findInventoryModelByInventoryUploadHistoryModelIndexAndStatus(InventoryUploadHistoryModel model, String status);

    public InventoryModel findByIdAndStatusIn(Long id, List<String> status);

    @Query(value = "Select tag_index, sum(tco2/1000) from assesment_inventory where status = 1 and activity_index in(?) and tag_index > 0  GROUP BY tag_index", nativeQuery = true)
    List<Object[]> allTagsThroughOrgAndPeriodIndex(Long ActivityIndex);



    @Query(value = "Select tag_index, sum(tco2/1000) from assesment_inventory where status = 1 and tag_index >0 GROUP BY tag_index", nativeQuery = true)
    List<Object[]> alltags();

    @Query(value = "Select activity_index,count(*) from assesment_inventory where status = 1 GROUP BY activity_index", nativeQuery = true)
    List<Object[]> countByQuery();

        @Query(value = "Select activity_index,SUM(Round(tco2,0)) from assesment_inventory where status = 1 GROUP BY activity_index",nativeQuery = true)
        List<Object[]> totalByEmissionIdForAllEA();

        @Query(value = "Select SUM(Round(tco2,0)) from assesment_inventory where status = 1 AND activity_index = ? ",nativeQuery = true)
        Double totalEmissionByEID(Long activityIndex);

        @Query(value = "Select SUM(Round(tco2,0)) from assesment_inventory where status = 1 and activity_index = ?",nativeQuery = true)
        Optional<Integer> getTotal(Long activityIndex);




        @Query(value = "Select * from assesment_inventory where  activity_index = :id and status IN (:statusarray)",nativeQuery = true)
        Optional<List<InventoryModel>> findByActivityIndexAndStatusIn(@Param("id")Long id,@Param("statusarray") String arr[]);


        @Query(value = "Select a.id,b.id ,a.name,a.tco2,a.category_Index,b.period_index from assesment_inventory a INNER JOIN emission_assessment b on a.activity_index = b.id where  a.status != ? ",nativeQuery = true)
        Optional<List<Object[]>> findByStatusIsNot(String status);



    @Modifying
    @Transactional
    @Query(value = "UPDATE assesment_inventory SET baseline_tco2 = :tco2Value WHERE id IN (:idValue)", nativeQuery = true)
    void updateInvForBaselineDeActivate(@Param("tco2Value") String tco2Value, @Param("idValue") String[] idValue);

    @Modifying
    @Transactional
    @Query(value = "UPDATE assesment_inventory SET baseline_tco2 = tco2 WHERE id IN (:idValue)", nativeQuery = true)
    void updateInvForBaselineActivate( @Param("idValue") String[] idValue);


    @Query(value = "SELECT t.tag_index, COALESCE(SUM(ai.tco2)/1000, 0) AS total_tco2\n" +
            "FROM (SELECT DISTINCT tag_index FROM assesment_inventory) t\n" +
            "LEFT JOIN assesment_inventory ai ON t.tag_index = ai.tag_index and ai.status = 1\n" +
            "    AND ai.category_index IN (:idValue) where t.tag_index > 0 \n" +
            "GROUP BY t.tag_index ORDER BY t.tag_index ASC",nativeQuery = true)
    List<Object[]> getTagIndexAndTotalEmissionScopeWise(@Param("idValue") String[] categoryIndexes);

    @Query(value = "SELECT t.tag_index, COALESCE(SUM(ai.tco2)/1000, 0) AS total_tco2 " +
            "FROM (SELECT DISTINCT tag_index FROM assesment_inventory) t " +
            "LEFT JOIN assesment_inventory ai ON t.tag_index = ai.tag_index and ai.status = 1 and ai.activity_index IN (:emissionIdValues) " +
            " AND ai.category_index IN (:idValue) where t.tag_index > 0 " +
            "GROUP BY t.tag_index ORDER BY t.tag_index ASC",nativeQuery = true)
    List<Object[]> getTagIndexAndTotalEmissionScopeWiseAndEmissionWise(@Param("idValue") String[] categoryIndexes,@Param("emissionIdValues") Long[] emissionIndexes);

    @Query(value = "select SUM(tco2/1000) from assesment_inventory where status = 1 and category_index IN (:categoryIndexes) and activity_index IN (:emissionIndexes)",nativeQuery = true)
    Double getHistoricEmissions(@Param("categoryIndexes") String[] categoryIndexes,@Param("emissionIndexes") Long[] emissionIndexes);

//    @Modifying
//    @Transactional
//    @Query(value = "INSERT INTO assesment_inventory (" +
//            "name," +
//            "category_index," +
//            "subcategory_index," +
//            "factor," +
//            " factor_detail," +
//            "description," +
//            "tag_index," +
//            "gas_index," +
//            "consumption," +
//            "consumption_unit," +
//            "tco2," +
//            "baseline_tco2," +
//            "status," +
//            " created_date," +
//            "created_by," +
//            "updated_date," +
//            "updated_by," +
//            "standard_index," +
//            "usages," +
//            "usage_unit," +
//            "activity_index," +
//            "inventory_upload_history_index" +
//            " ) VALUES " +
//            " (:nameArray )," +
//            " (:categoryIndexArray)," +
//            " (:subcategoryIndexArray)," +
//            " (:factorArray)," +
//            " (:factorDetailArray)," +
//            " (:descriptionArray)," +
//            " (:tagIndexArray)," +
//            " (:gasIndexArray)," +
//            " (:consumptionArray)," +
//            " (:consumptionUnitArray)," +
//            " (:tco2Array)," +
//            " (:baselineTco2Array)," +
//            " (:statusArray)," +
//            " (:createdDateArray)," +
//            " (:createdByArray)," +
//            " (:updatedDateArray)," +
//            " (:updatedByArray)," +
//            " (:standardIndexArray)," +
//            " (:usagesArray)," +
//            " (:usageUnitArray)," +
//            " (:activity_index)," +
//            " (:inventory_upload_history_index) ", nativeQuery = true)
//    void saveByInsetQuery(
//        @Param("nameArray") String[] nameArray,
//        @Param("categoryIndexArray") Long[] categoryIndexArray,
//        @Param("subcategoryIndexArray") Long[] subcategoryIndexArray,
//        @Param("factorArray") String[] factorArray,
//        @Param("factorDetailArray") String[] factorDetailArray,
//        @Param("descriptionArray") String[] descriptionArray,
//        @Param("tagIndexArray") Long[] tagIndexArray,
//        @Param("gasIndexArray") Long[] gasIndexArray,
//        @Param("consumptionArray") String[] consumptionArray,
//        @Param("consumptionUnitArray") String[] consumptionUnitArray,
//        @Param("tco2Array") String[] tco2Array,
//        @Param("baselineTco2Array") String[] baselineTco2Array,
//        @Param("statusArray") String[] statusArray,
//        @Param("createdDateArray") ZonedDateTime[] createdDateArray,
//        @Param("createdByArray") String[] createdByArray,
//        @Param("updatedDateArray") ZonedDateTime[] updatedDateArray,
//        @Param("updatedByArray") String[] updatedByArray,
//        @Param("standardIndexArray") Long[] standardIndexArray,
//        @Param("usagesArray") String[] usagesArray,
//        @Param("usageUnitArray") String[] usageUnitArray,
//        @Param("activity_index") Long[] activity_index,
//        @Param("inventory_upload_history_index") Long[] inventory_upload_history_index
//
//
//    );
}
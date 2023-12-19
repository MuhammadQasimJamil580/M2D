package com.M2D.EmissionAssessment.Services;




import com.M2D.EmissionAssessment.DTO.Request.EADTO_Req;
import com.M2D.EmissionAssessment.DTO.Response.EADTO_Resp;
import com.M2D.EmissionAssessment.Model.EAModel;
import com.M2D.EmissionAssessment.Model.EmissionAlterationHistoryModel;

import java.util.List;

public interface EAService {

    List<EADTO_Resp> getAll();
    List<EADTO_Req> getAllEADropdown();
    List<EADTO_Req> getAllBaselineEADropdown();
    EADTO_Req getOneRecord( Long eaId);
    void saveEA(EADTO_Req eadto_req);
    void updateEA(EADTO_Req eadto_req, Long eaId);
    void deactivateEA(Long eaId);
    void activateEA(Long eaId);

    void baseLineThisEA(Long eaId);
    void removeBaselineForThisEA(Long eaId);

    EmissionAlterationHistoryModel mentainEmissionAlterationHistory(EAModel eaModel, String toalEmission, String createdBy);

}

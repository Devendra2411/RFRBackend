package com.ge.rfr.fspevent;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ge.rfr.common.model.RfrRegion;
import com.ge.rfr.fspevent.model.DistinctSiteNames;
import com.ge.rfr.fspevent.model.TrainDataInterface;
import com.ge.rfr.fspevent.model.RfrPgsFspEvent;

@Repository
public interface PgsFspEventDao extends JpaRepository<RfrPgsFspEvent, Integer>, JpaSpecificationExecutor<RfrPgsFspEvent> {

    List<RfrPgsFspEvent> findByEquipSerialNumber(String esnId);

    RfrPgsFspEvent findByEquipSerialNumberAndOutageId(String esnId, Integer outageId);

    @Query("Select r from RfrPgsFspEvent r where r.equipSerialNumber = :esnId AND r.outageId = :outageId AND r.region IN :regions")
    RfrPgsFspEvent findByEquipSerialNumberAndOutageIdAndRegionContaining(@Param("esnId") String esnId,
                                                                         @Param("outageId") Integer outageId,
                                                                         @Param("regions") List<RfrRegion> regions);

    List<DistinctSiteNames> findDistinctByAndRegionIn(List<RfrRegion> regions);

    List<DistinctSiteNames> findDistinctBy();

    List<TrainDataInterface> findBySiteName(String siteName);

    List<TrainDataInterface> findBySiteNameAndRegionIn(String siteName, List<RfrRegion> regions);
}
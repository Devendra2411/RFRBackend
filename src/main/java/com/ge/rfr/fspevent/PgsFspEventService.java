package com.ge.rfr.fspevent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ge.rfr.auth.SsoUser;
import com.ge.rfr.common.exception.InvalidJsonException;
import com.ge.rfr.common.model.RfrRegion;
import com.ge.rfr.common.util.RfrRegionHelper;
import com.ge.rfr.fspevent.model.DistinctSiteNames;
import com.ge.rfr.fspevent.model.EsnAndOutageDto;
import com.ge.rfr.fspevent.model.RfrPgsFspEvent;
import com.ge.rfr.fspevent.model.TrainDataDto;
import com.ge.rfr.fspevent.model.TrainDataInterface;
import com.ge.rfr.workflow.WorkflowDao;
import com.ge.rfr.workflow.model.RfrWorkflow;
import com.ge.rfr.workflow.model.dto.RfrWorkflowDetailsDto;

@Service
public class PgsFspEventService {


    private final WorkflowDao workflowDao;
    private PgsFspEventDao pgsFspEventDao;

    public PgsFspEventService(PgsFspEventDao pgsFspEventDao, WorkflowDao workflowDao) {
        this.pgsFspEventDao = pgsFspEventDao;
        this.workflowDao = workflowDao;
    }

    public RfrPgsFspEvent findByEquipSerialNumberAndOutageId(String esnId, Integer outageId) {
        return pgsFspEventDao.findByEquipSerialNumberAndOutageId(esnId, outageId);
    }

    public RfrPgsFspEvent findByEquipSerialNumberAndOutageIdAndRegionContaining(String esnId, Integer outageId,
                                                                                List<RfrRegion> regions) {
        return pgsFspEventDao.findByEquipSerialNumberAndOutageIdAndRegionContaining(esnId, outageId, regions);
    }

    public List<RfrWorkflowDetailsDto> getDistinctSiteNamesList(SsoUser user) {
        List<RfrWorkflowDetailsDto> siteNamesList = new ArrayList<>();
        List<RfrRegion> regions = new ArrayList<>();
        List<DistinctSiteNames> distinctSiteNamesList = new ArrayList<>();

        if (RfrRegionHelper.isAdmin(user)) {
            distinctSiteNamesList = pgsFspEventDao.findDistinctBy();
        } else if (RfrRegionHelper.getRegions(user, regions)) {
            distinctSiteNamesList = pgsFspEventDao.findDistinctByAndRegionIn(regions);
        }
        distinctSiteNamesList.forEach(obj -> {
            RfrWorkflowDetailsDto rfrWorkflowDetailsDto = new RfrWorkflowDetailsDto();
            rfrWorkflowDetailsDto.setSiteName(obj.getSiteName());
            siteNamesList.add(rfrWorkflowDetailsDto);
        });
        return siteNamesList;
    }

    public List<TrainDataDto> getBlocksForSite(SsoUser user, String siteName) {
        List<TrainDataInterface> trainIdAndEsnList = new ArrayList<>();
        List<RfrRegion> regions = new ArrayList<>();
        List<RfrWorkflow> rfrWorkflowList = workflowDao.findAll();

        // Getting List of All trains for Site Name
        if (RfrRegionHelper.isAdmin(user)) {
            trainIdAndEsnList = pgsFspEventDao.findBySiteName(siteName);
        }

        // Getting List of All trains for Site Name Based On Region
        else if (RfrRegionHelper.getRegions(user, regions)) {
            trainIdAndEsnList = pgsFspEventDao.findBySiteNameAndRegionIn(siteName, regions);
        }

        // Checking whether the esn and outage combination is already present in workflow
        for (RfrWorkflow workflow : rfrWorkflowList) {
            trainIdAndEsnList = trainIdAndEsnList.stream()
                    .filter(trainObj -> (!(workflow.getEquipSerialNumber().equals(trainObj.getEquipSerialNumber())
                            && workflow.getOutageId() == trainObj.getOutageId())))
                    .collect(Collectors.toList());
        }
        if (trainIdAndEsnList.isEmpty()) {
            throw new InvalidJsonException("All Combinations of ESN & Outage ID's are created");
        }

        Map<Object, Map<Object, List<TrainDataInterface>>> map = trainIdAndEsnList.stream()
                .collect(Collectors.groupingBy(TrainDataInterface::getTrainId,
                        Collectors.groupingBy(TrainDataInterface::getEquipSerialNumber)));
        List<TrainDataDto> trainDataDtoList = new ArrayList<>();

        //Iterate based on Train IDs
        for (Object key : map.keySet()) {
            TrainDataDto trainDataDto = new TrainDataDto();
            trainDataDto.setTrainId((String) key);
            List<EsnAndOutageDto> esnAndOutageDtoList = new ArrayList<>();
            //Iterate based on ESNs
            for (Object esn : map.get(key).keySet()) {
                EsnAndOutageDto esnAndOutageDto = new EsnAndOutageDto();
                esnAndOutageDto.setEquipSerialNumber((String) esn);
                Set<Integer> outagesList = new HashSet<>();

                //Setting the Outages for ESNs
                for (TrainDataInterface trainDataInterface : map.get(key).get(esn)) {
                    outagesList.add(trainDataInterface.getOutageId());
                }
                esnAndOutageDto.setOutagesList(outagesList);
                esnAndOutageDtoList.add(esnAndOutageDto);
            }
            trainDataDto.setEsnAndOutagesList(esnAndOutageDtoList);
            trainDataDtoList.add(trainDataDto);
        }

        // Sorting based on size of TrainData List for UI purposes
        trainDataDtoList.sort(Comparator.comparing(TrainDataDto::getSize)
                .reversed());
        return trainDataDtoList;
    }
}

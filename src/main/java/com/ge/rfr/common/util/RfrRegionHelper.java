package com.ge.rfr.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import com.ge.rfr.auth.SsoUser;
import com.ge.rfr.auth.UserRole;
import com.ge.rfr.common.model.RfrRegion;
import com.ge.rfr.common.model.User;
import com.ge.rfr.fspevent.PgsFspEventService;
import com.ge.rfr.workflow.model.RfrWorkflow;

import io.swagger.annotations.Api;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;

@Component
public class RfrRegionHelper {

	private static PgsFspEventService fspEventService;

	@Autowired
	private RfrRegionHelper(PgsFspEventService fspEventService) {
		RfrRegionHelper.fspEventService = fspEventService;
	}

	public static boolean getRegions(SsoUser user, List<RfrRegion> regions) {
		Set<UserRole> userRoles = user.getRoles();
		boolean isRegionalCoordinator = false;

		userRoles.forEach(role -> {

			if (role.toString().contains("RFR_COORDINATOR")) {
				regions.add(
						Enum.valueOf(RfrRegion.class, StringUtils.substringAfterLast(role.toString(), "COORDINATOR_")));
			}
		});

		if (!regions.isEmpty())
			isRegionalCoordinator = true;
		return isRegionalCoordinator;
	}

	public static boolean isAdmin(SsoUser user) {
		boolean isAdmin = false;
		if (user.getRoles().contains(UserRole.RFR_ADMIN)
				|| user.getRoles().contains(UserRole.RFR_INSTALLATION_DIRECTOR))
			isAdmin = true;

		return isAdmin;

	}

	public static boolean hasCreateAccess(SsoUser user, int outageId, String esnId) {
		List<RfrRegion> regions = new ArrayList<>();
		// Check If the user is Admin or Installation Director
		if (isAdmin(user))
			return true;
		// else if user is RFR_Coordinator
		// then get the list of regions user belongs to
		else if (getRegions(user, regions) && null != fspEventService
				.findByEquipSerialNumberAndOutageIdAndRegionContaining(esnId, outageId, regions)) {
			// Check if the equipment serial number and outage id is for the region user
			// belongs
			return true;
		}
		else
			throw new AccessDeniedException("Access is Denied");
	}

	public static boolean hasViewAccess(SsoUser user, RfrWorkflow workflow) {

		if (workflow.getAssignedEngineers().contains(User.fromSsoUser(user)))
			return true;
		else if (hasCreateAccess(user, workflow.getOutageId(), workflow.getEquipSerialNumber()))
			return true;
		else
			throw new AccessDeniedException("Access is Denied");
	}

}

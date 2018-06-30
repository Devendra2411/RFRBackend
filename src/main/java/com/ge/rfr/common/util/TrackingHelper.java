package com.ge.rfr.common.util;

import com.ge.rfr.auth.SsoUser;
import com.ge.rfr.common.model.ChangeTracking;
import com.ge.rfr.common.model.User;

/**
 * This class has helper methods defined for setting ChangeTracking
 *
 * @author 503055886
 */
public class TrackingHelper {

    /**
     * Helper method to set ChangeTracking audit trail log
     */
    public static ChangeTracking setChangeTracking(SsoUser ssoUser) {
        User user = new User();
        user.setFirstName(ssoUser.getFirstName());
        user.setLastName(ssoUser.getLastName());
        user.setSso(ssoUser.getSso());
        return new ChangeTracking(user);
    }

}

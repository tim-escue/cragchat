package com.cragchat.mobile.database;

import com.cragchat.mobile.model.Area;
import com.cragchat.mobile.model.Route;

/**
 * Created by timde on 9/21/2017.
 */

public interface CragChatDatasource {

    Area getArea(String key);
    Route getRoute(String key);

}

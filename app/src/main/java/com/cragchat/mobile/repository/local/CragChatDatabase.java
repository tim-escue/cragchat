package com.cragchat.mobile.repository.local;

import com.cragchat.mobile.model.Area;
import com.cragchat.mobile.model.Rating;
import com.cragchat.mobile.model.Route;
import com.cragchat.mobile.model.pojo.PojoArea;
import com.cragchat.mobile.model.pojo.PojoComment;
import com.cragchat.mobile.model.pojo.PojoRating;
import com.cragchat.mobile.model.pojo.PojoRoute;

import java.util.List;

/**
 * Created by timde on 9/21/2017.
 */

public interface CragChatDatabase {

    Area getArea(String areaKey);

    Area getAreaByName(String areaName);

    List<Route> getRoutes(String[] routeIds);

    List<Area> getAreas(String[] areaIds);

    List<Rating> getRatings(String entityKey);

    void update(PojoArea area);

    void update(PojoRating rating);

    void update(PojoComment comment);

    void updateRatings(List<PojoRating> ratings);

    void updateAreas(List<PojoArea> areas);

    void updateRoutes(List<PojoRoute> routes);

    void update(final PojoRoute route);

    List getQueryMatches(String query);

}

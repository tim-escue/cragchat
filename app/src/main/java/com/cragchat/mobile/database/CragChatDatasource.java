package com.cragchat.mobile.database;

import com.cragchat.mobile.model.Area;
import com.cragchat.mobile.model.Route;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by timde on 9/21/2017.
 */

public interface CragChatDatasource {

    Area getArea(String key);
    Route getRoute(String key);
    void updateBatch(String json);
    void updateSingle(String json);

    List<? extends Area> getAllAreas();
    List<? extends Route> getAllRoutes();

    }

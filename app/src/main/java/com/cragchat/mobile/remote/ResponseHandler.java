package com.cragchat.mobile.remote;

import android.content.Context;

import com.cragchat.mobile.comments.Comment;
import com.cragchat.mobile.model.Displayable;
import com.cragchat.mobile.model.Image;
import com.cragchat.mobile.model.LegacyRoute;
import com.cragchat.mobile.model.Rating;
import com.cragchat.mobile.model.Send;

import org.json.JSONObject;

public class ResponseHandler {

    public static Object parseResponse(Context con, String response) {
        response = response.trim();
        if (response.startsWith("JSON=")) {
            try {
                String jsonString = response.substring(5);
                JSONObject json = new JSONObject(jsonString);
                //System.out.println("PARSING JSON:" + jsonString);
                switch (json.getString("objectType")) {
                    case "commentEdit":
                        Comment c = Comment.decode(json);
                        return c;
                    case "rating":
                        Rating r = Displayable.decodeRating(json);
                        return r;
                    case "route":
                        LegacyRoute route = Displayable.decodeRoute(json);
                        return route;
                    case "image":
                        Image image = Image.decode(json);
                        return image;
                    case "send":
                        Send send = Send.decode(json);
                        return send;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (response.startsWith("IMAGE")) {
            String[] args = response.split("###");
            Image img = new Image(Integer.parseInt(args[1]), args[2], args[3], args[4], args[5]);
            return img;
        } else if (response.startsWith("COMMENT")) {
            String[] args = response.split("###");
            Comment com = new Comment(args[5], Integer.parseInt(args[3]),
                    Integer.parseInt(args[2]), args[4], Integer.parseInt(args[6]),
                    Integer.parseInt(args[7]), Integer.parseInt(args[8]), args[9], args[10]);
            return com;
        }
        return null;
    }
}

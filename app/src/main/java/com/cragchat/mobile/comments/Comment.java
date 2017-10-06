package com.cragchat.mobile.comments;

import android.app.Activity;

import com.cragchat.mobile.model.Datable;
import com.cragchat.mobile.user.User;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Comment implements Datable {

    public static final int COLUMN_SCORE = 0;
    public static final int COLUMN_ID = 1;
    public static final int COLUMN_DATE = 2;
    public static final int COLUMN_TEXT = 3;
    public static final int COLUMN_DISPLAY_ID = 4;
    public static final int COLUMN_PARENT_ID = 5;
    public static final int COLUMN_DEPTH = 6;
    public static final int COLUMN_AUTHOR_NAME = 7;
    public static final int COLUMN_TABLE_NAME = 8;

    private String comment;
    private int score;
    private String date;
    private int id;
    private int displayId;
    private int parent;
    private int depth;
    private String authorName;
    private List<Comment> children;
    private String table;

    public Comment(String comment, int id, int score, String date, int displayId,
                   int parent, int depth, String authorName, String table) {
        this.comment = comment;
        this.score = score;
        this.displayId = displayId;
        this.date = date;
        this.id = id;
        this.parent = parent;
        this.depth = depth;
        this.authorName = authorName;
        this.table = table;
    }

    public String getJSONString(Activity act, boolean edit) {
        Map<String, String> map = new HashMap<>();
        map.put("objectType", edit ? "commentEdit" : "comment");
        map.put("id", String.valueOf(id));
        map.put("comment", comment.replaceAll("\n", "<!!!>"));
        map.put("date", date);
        map.put("score", String.valueOf(score));
        map.put("displayId", String.valueOf(displayId));
        map.put("parent", String.valueOf(parent));
        map.put("depth", String.valueOf(depth));
        map.put("authorName", authorName);
        map.put("table", table);
        map.put("userToken", User.currentToken(act));
        return new JSONObject(map).toString();
    }

    public static Comment decode(JSONObject obj) {
        try {
            return new Comment(obj.getString("comment").replaceAll("<!!!>", "\n"), obj.getInt("id"), obj.getInt("score"), obj.getString("date"), obj.getInt("displayId"), obj.getInt("parent"), obj.getInt("depth"),
                    obj.getString("authorName"), obj.getString("table"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setText(String text) {
        this.comment = text;
    }

    public String getTable() {
        return table;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void addChild(Comment child) {
        if (children == null)
            children = new LinkedList<>();
        children.add(child);
    }

    public List<Comment> getChildren() {
        return children;
    }

    public int getDepth() {
        return depth;
    }

    public int getParent() {
        return parent;
    }

    public boolean isReply() {
        return parent != -1;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return comment;
    }

    public int getDisplayId() {
        return displayId;
    }

    public int getScore() {
        return score;
    }

    public String getDate() {
        return date;
    }

    public void setScore(int i) {
        score = i;
    }

}

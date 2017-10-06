package com.cragchat.mobile.comments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CommentManager {

    private List<Comment> comments;
    private List<Comment> parents;
    private Comparator<Comment> currentComparator;

    public CommentManager() {
        comments = new ArrayList<>();
        parents = new ArrayList<>();
        currentComparator = scoreComparator;
    }

    public void addComment(Comment comment) {
        //Log.d("COMMENT MANAGER", "Comment added:" + comment.getScore() + " date:" + comment.getDate());
        if (comment.getParent() == -1) {
            parents.add(comment);
        } else {
            find(comment.getParent()).addChild(comment);
            //Log.d("CommentManager", comment.getId() + " Adding as child to " + comment.getParent());
        }
        sort(currentComparator);
    }

    private Comment find(int id) {
        return find(parents, id);
    }

    private Comment find(List<Comment> comments, int id) {
        if (comments != null) {
            for (Comment i : comments) {
                if (i.getId() == id) {
                    return i;
                }
                Comment tryd = find(i.getChildren(), id);
                if (tryd != null) {
                    return tryd;
                }
            }
        }
        return null;
    }

    private void constructList() {
        comments.clear();
        for (Comment i : parents) {
            comments.add(i);
            addChildren(i);
        }
    }

    private void addChildren(Comment cur) {
        List<Comment> children = cur.getChildren();
        if (children != null) {
            for (Comment i : children) {
                comments.add(i);
                addChildren(i);
            }
        }
    }

    public List<Comment> getCommentList() {
        return comments;
    }

    public void sortByScore() {
        currentComparator = scoreComparator;
        sort(scoreComparator);
    }

    private void sort(Comparator<Comment> comparator) {
        Collections.sort(parents, comparator);
        for (Comment i : parents) {
            sort(i, comparator);
        }
        constructList();
    }

    private void sort(Comment comment, Comparator<Comment> comparator) {
        List<Comment> cur;
        if ((cur = comment.getChildren()) != null) {
            Collections.sort(cur, comparator);
            for (Comment i : cur) {
                sort(i, comparator);
            }
        }
    }

    public void sortByDate() {
        sort(dateComparator);
        currentComparator = dateComparator;
    }


    private Comparator<Comment> scoreComparator = new Comparator<Comment>() {
        @Override
        public int compare(Comment lhs, Comment rhs) {
            //System.out.println("Text:" + lhs.getText() + " score:" + lhs.getScore());
            //System.out.println("Text:" + rhs.getText() + " score:" + rhs.getScore());

            return rhs.getScore() - lhs.getScore();
        }
    };

    private Comparator<Comment> dateComparator = new Comparator<Comment>() {
        @Override
        public int compare(Comment lhs, Comment rhs) {
            return rhs.getDate().compareTo(lhs.getDate());
        }
    };

}

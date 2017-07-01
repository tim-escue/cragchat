package chat.crag.cragchat.comments;

import android.util.Log;
import chat.crag.cragchat.descriptor.Displayable;

import java.util.*;

public class ProfileCommentManager {

    private List<Comment> comments;
    private Comparator<Comment> currentComparator;

    public ProfileCommentManager() {
        comments = new LinkedList<>();
        currentComparator = scoreComparator;
    }

    public void addComment(Comment comment) {
        //Log.d("COMMENT MANAGER", "Comment added:" + comment.getScore() + " date:" + comment.getDate());
            comments.add(comment);
        sort(currentComparator);
    }

    public List<Comment> getCommentList() {
        return comments;
    }

    public void sortByScore() {
        currentComparator = scoreComparator;
        sort(scoreComparator);
    }

    private void sort(Comparator<Comment> comparator) {
        Collections.sort(comments, comparator);
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

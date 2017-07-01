package chat.crag.cragchat.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import chat.crag.cragchat.CragChatActivity;
import chat.crag.cragchat.descriptor.Area;
import chat.crag.cragchat.fragments.*;
import chat.crag.cragchat.sql.LocalDatabase;

public class AreaActivityPagerAdapter extends FragmentPagerAdapter {

    private Area parent;
    private int count;

    public AreaActivityPagerAdapter(CragChatActivity a, FragmentManager fm, Area parent) {
        super(fm);
        this.parent = parent;
        if (LocalDatabase.getInstance(a).findAreasWithin(parent).size() > 0) {
            count = 5;
        } else {
            count = 4;
        }
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return RecentActivityFragment.newInstance(parent.getId());
            case 1:
                return RouteListFragment.newInstance(parent);
            case 2:
                if (count == 4) {
                    return CommentSectionFragment.newInstance(parent.getId(), "DISCUSSION");
                } else {
                    return AreaListFragment.newInstance(parent);
                }
            case 3:
                if (count == 4) {
                    return LocationFragment.newInstance(parent.getId());
                } else {
                    return CommentSectionFragment.newInstance(parent.getId(), "DISCUSSION");
                }
            case 4:
                return LocationFragment.newInstance(parent.getId());
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Recent Activity";
            case 1:
                return "Routes";
            case 2:
                if (count == 4) {
                    return "Description";
                } else {
                    return "Areas";
                }
            case 3:
                if (count == 4) {
                    return "Location";
                } else {
                   return "Description";
                }
            case 4:
                return "Location";
            default:
                return "NULL";
        }
    }
}
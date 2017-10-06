package com.cragchat.mobile.database;

import android.content.Context;

import com.cragchat.mobile.R;
import com.cragchat.mobile.activity.MainActivity;
import com.cragchat.mobile.database.models.RealmArea;
import com.cragchat.mobile.database.models.RealmRoute;
import com.cragchat.mobile.model.Area;
import com.cragchat.mobile.model.Route;

import java.util.List;
import java.util.Scanner;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by timde on 9/28/2017.
 */

public class RealmDatabase implements CragChatDatasource {

    RealmDatabase(Context context) {
        Realm.init(context);
    }

    @Override
    public Area getArea(String key) {
        Realm realm = Realm.getDefaultInstance();
        Area area = realm.where(RealmArea.class).equalTo(RealmArea.FIELD_KEY, key).findFirst();
        realm.close();
        return area;
    }

    @Override
    public Route getRoute(String key) {
        Realm realm = Realm.getDefaultInstance();
        Route area = realm.where(RealmRoute.class).equalTo(RealmRoute.FIELD_KEY, key).findFirst();
        realm.close();
        return area;
    }

    /*
        //USED FOR CREATING REALM INITIAL DATA FROM RAW RESOURCE FILES


    Realm realm = Realm.getDefaultInstance();
                    System.out.println("logged in successfully as timsqdev");
                    realm.executeTransaction(new Realm.Transaction() {
        @Override
        public void execute(Realm realm) {
            realm.deleteAll();
            System.out.println("Creating ozone and other areas");
            RealmArea ozone = new RealmArea("Ozone", "Ozone", 0, 0, null, new RealmList<RealmArea>(), new RealmList<RealmRoute>());
            ozone = realm.copyToRealm(ozone);

            Scanner scanner = new Scanner(MainActivity.this.getResources().openRawResource(R.raw.walls));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] params = line.split("#");
                RealmArea newArea = new RealmArea(params[0], params[0] + ozone.getName(), 0, 0, ozone, new RealmList<RealmArea>(), new RealmList<RealmRoute>());
                newArea = realm.copyToRealm(newArea);
                ozone.getSubAreas().add(newArea);
            }
            scanner.close();

            System.out.println("Creating routes");
            scanner = new Scanner(MainActivity.this.getResources().openRawResource(R.raw.routes));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] params = line.split("#");

                String type = params[3];
                if (type.contains("Top Rope")) {
                    type = "Trad"; // only one rout ehas tope rope and its other type is trad
                }

                RealmArea parent = realm.where(RealmArea.class).equalTo("name", params[2]).findFirst();
                RealmRoute route = new RealmRoute(params[0] + parent.getName(), params[0], type, 0, 0, parent);
                route = realm.copyToRealm(route);
                parent.getRoutes().add(route);
            }

            System.out.println("Finished copying to realm");
            List<RealmArea> areas = realm.where(RealmArea.class).findAll();
            System.out.println("Areas(" + areas.size() + "): ");
            for (RealmArea area : areas) {
                System.out.println("\t" + area.getName());
                for (RealmArea subArea : area.getSubAreas()) {
                    System.out.println("\t\t" + subArea.getName());
                }
                for (RealmRoute route : area.getRoutes()) {
                    System.out.println("\t\t" + route.getName());
                }
            }
            List<RealmRoute> routes = realm.where(RealmRoute.class).findAll();
            System.out.println("Routes(" + routes.size() + "):  ");
            for (RealmRoute route : routes) {
                System.out.println("\t" + route.getName());
            }
        }
    });

     */
}

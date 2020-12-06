package khpi.cs.se.db.semesterwork.utils;

import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsRoute;
import khpi.cs.se.db.semesterwork.model.Store;
import khpi.cs.se.db.semesterwork.model.User;
import khpi.cs.se.db.semesterwork.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class GoogleApiUtils {

    private static  StoreService storeService;

    @Autowired
    public void init(StoreService storeService){
        this.storeService = storeService;
    }

    private GoogleApiUtils(){}

    public static List<Store> sortedStores(User user) {
        List<Store> stores = (List<Store>) storeService.getAll();
        stores.sort((Store o1, Store o2) -> {
            GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyC4x39YK96qpnY_7lfUxL9A1rJqolx3rXE");
            DirectionsRoute[] result1 = null;
            try {
                result1 = DirectionsApi.getDirections(context, user.getCountry() + ", " +
                                user.getCity() + ", " + user.getStreet() + ", " + user.getHousenumber(),
                        o1.getCountry() + ", " +
                                o1.getCity() + ", " + o1.getStreet() + ", " + o1.getHousenumber()).await();
            } catch (Exception e) {
                e.printStackTrace();
            }
            DirectionsRoute[] result2 = null;
            try {
                result2 = DirectionsApi.getDirections(context,
                        user.getCountry() + ", " + user.getCity() + ", " + user.getStreet() + ", "
                                + user.getHousenumber(),
                        o2.getCountry() + ", " + o2.getCity() + ", " + o2.getStreet() + ", "
                                + o2.getHousenumber()).await();
            } catch (Exception e) {
                e.printStackTrace();
            }

            List<DirectionsRoute> routes1 = sortRoute(result1);
            List<DirectionsRoute> routes2 = sortRoute(result2);
            return Long.compare(routes1.get(0).legs[0].duration.inSeconds,
                    routes2.get(0).legs[0].duration.inSeconds);
        });

        return stores;
    }


    private static List<DirectionsRoute> sortRoute(DirectionsRoute... routes) {
        return Stream.of(routes).sorted((DirectionsRoute o1, DirectionsRoute o2) -> {
            DirectionsLeg[] legs1 = (DirectionsLeg[]) Stream.of(o1.legs).sorted((DirectionsLeg leg1, DirectionsLeg leg2) -> {
                return Long.compare(leg1.durationInTraffic.inSeconds, leg2.durationInTraffic.inSeconds);
            }).toArray();
            DirectionsLeg[] legs2 = (DirectionsLeg[]) Stream.of(o2.legs).sorted((DirectionsLeg leg1, DirectionsLeg leg2) -> {
                return Long.compare(leg1.durationInTraffic.inSeconds, leg2.durationInTraffic.inSeconds);
            }).toArray();
            return Long.compare(legs1[0].durationInTraffic.inSeconds, legs2[0].durationInTraffic.inSeconds);

        }).collect(Collectors.toList());
    }

    public static boolean checkAddress (User user){
        String country = "Ukraine, Kharkiv, Sumskaya, 50";
        GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyC4x39YK96qpnY_7lfUxL9A1rJqolx3rXE");
        DirectionsRoute[] result1 = null;
        try {
            result1 = DirectionsApi.getDirections(context, user.getCountry() + ", " +
                            user.getCity() + ", " + user.getStreet() + ", " + user.getHousenumber(),
                    country).await();
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
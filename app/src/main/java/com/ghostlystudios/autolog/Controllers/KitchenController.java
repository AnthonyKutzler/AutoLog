package com.ghostlystudios.autolog.Controllers;

import android.content.Context;

import com.ghostlystudios.autolog.Models.Kitchen;

import java.util.List;

/**
 * Kitchen List/Map Controller
 */

public class KitchenController {
    private Context context;
    public KitchenController(Context context){
        this.context = context;
    }

    /**
     * Return List of Kitchens for a particular Route
     * @param routeNumber (int)
     * @return List<Kitchen>
     */
    public List<Kitchen> getKitchenList(int routeNumber){
        return new DatabaseAdapter(context).getKitchensByRouteNumber(routeNumber, null);
    }

    /*public Map<Double, Kitchen> getKitchenMap(int routeNumber){
        Map<Double, Kitchen> doubleKitchenInfoMap = new HashMap<>();
        Cursor cursor = new DatabaseAdapter(context).getAllKitchensByRouteNumber(routeNumber,
                new String[] {DatabaseFinals.CUSTOMER_COLUMNS[1], DatabaseFinals.CUSTOMER_COLUMNS[6],
                        DatabaseFinals.CUSTOMER_COLUMNS[7]});
        while (cursor.moveToNext()) {
            doubleKitchenInfoMap.put(cursor.getDouble(1), new Kitchen(cursor.getString(0),
                    cursor.getDouble(1)));
        }
        return doubleKitchenInfoMap;
    }*/

}

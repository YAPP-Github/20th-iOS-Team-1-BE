package com.yapp.pet.global.util;

public class DistanceUtil {

    private static final int EARTH = 6371;

    public static Double getDistanceBetweenUserAndClub(Double startLatitude, Double endLatitude, Double startLongitude, Double endLongitude) {
        double distanceLatitude = Math.toRadians(endLatitude - startLatitude);
        double distanceLongitude = Math.toRadians(endLongitude - startLongitude);

        startLatitude = Math.toRadians(startLatitude);
        endLatitude = Math.toRadians(endLatitude);

        double haversinDistance = haversin(distanceLatitude) + Math.cos(startLatitude) * Math.cos(endLatitude) * haversin(distanceLongitude);

        double distance = 2 * Math.atan2(Math.sqrt(haversinDistance), Math.sqrt(1 - haversinDistance));

        return EARTH * distance;
    }

    private static double haversin(double distanceLatitude) {
        return Math.pow(Math.sin(distanceLatitude / 2), 2);
    }
}

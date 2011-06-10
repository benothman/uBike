/**
 * Reimplementation of Mark McClures Javascript PolylineEncoder
 * All the mathematical logic is more or less copied by McClure
 *  
 * @author Mark Rambow
 * @e-mail markrambow[at]gmail[dot]com
 * @version 0.1
 * 
 */
package com.ubike.processor;

import com.ubike.model.TrackPoint;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

/**
 *
 */
public class PolylineEncoder {

    private int numLevels = 18;
    private int zoomFactor = 2;
    private double verySmall = 0.00001;
    private boolean forceEndpoints = true;
    private double[] zoomLevelBreaks;
    private HashMap<String, Double> bounds;

    /**
     * 
     * @param numLevels
     * @param zoomFactor
     * @param verySmall
     * @param forceEndpoints
     */
    public PolylineEncoder(int numLevels, int zoomFactor, double verySmall, boolean forceEndpoints) {

        this.numLevels = numLevels;
        this.zoomFactor = zoomFactor;
        this.verySmall = verySmall;
        this.forceEndpoints = forceEndpoints;

        this.zoomLevelBreaks = new double[numLevels];

        for (int i = 0; i < numLevels; i++) {
            this.zoomLevelBreaks[i] = verySmall * Math.pow(this.zoomFactor, numLevels - i - 1);
        }
    }

    /**
     *
     */
    public PolylineEncoder() {
        this.zoomLevelBreaks = new double[numLevels];

        for (int i = 0; i < numLevels; i++) {
            this.zoomLevelBreaks[i] = verySmall * Math.pow(this.zoomFactor, numLevels - i - 1);
        }
    }

    /**
     * Douglas-Peucker algorithm, adapted for encoding
     *
     * @return HashMap [EncodedPoints;EncodedLevels]
     */
    public HashMap<String, String> dpEncode(List<TrackPoint> trackPoints) {
        int i, maxLoc = 0;
        Stack<int[]> stack = new Stack<int[]>();
        double[] dists = new double[trackPoints.size()];
        double maxDist, absMaxDist = 0.0, temp = 0.0;
        int[] current;

        if (trackPoints.size() > 2) {
            int[] stackVal = new int[]{0, (trackPoints.size() - 1)};
            stack.push(stackVal);

            while (stack.size() > 0) {
                current = stack.pop();
                maxDist = 0;

                for (i = current[0] + 1; i < current[1]; i++) {
                    temp = this.distance(trackPoints.get(i), trackPoints.get(current[0]), trackPoints.get(current[1]));
                    if (temp > maxDist) {
                        maxDist = temp;
                        maxLoc = i;
                        if (maxDist > absMaxDist) {
                            absMaxDist = maxDist;
                        }
                    }
                }
                if (maxDist > this.verySmall) {
                    dists[maxLoc] = maxDist;
                    int[] stackValCurMax = {current[0], maxLoc};
                    stack.push(stackValCurMax);
                    int[] stackValMaxCur = {maxLoc, current[1]};
                    stack.push(stackValMaxCur);
                }
            }
        }

        HashMap<String, String> hm = createEncodings(trackPoints, dists);
        String encodedPoints = hm.get("encodedPoints").replace("\\", "\\\\");
        String encodedLevels = encodeLevels(trackPoints, dists, absMaxDist);

        hm.put("encodedPoints", encodedPoints);
        hm.put("encodedLevels", encodedLevels);
        return hm;
    }

    /**
     * distance(p0, p1, p2) computes the distance between the point p0 and the
     * segment [p1,p2]. This could probably be replaced with something that is a
     * bit more numerically stable.
     *
     * @param p0
     * @param p1
     * @param p2
     * @return
     */
    public double distance(TrackPoint p0, TrackPoint p1, TrackPoint p2) {
        double u, out = 0.0;

        if (p1.getLatitude() == p2.getLatitude() && p1.getLongitude() == p2.getLongitude()) {
            out = Math.sqrt(Math.pow(p2.getLatitude() - p0.getLatitude(), 2) + Math.pow(p2.getLongitude() - p0.getLongitude(), 2));
        } else {
            u = ((p0.getLatitude() - p1.getLatitude()) * (p2.getLatitude() - p1.getLatitude()) + (p0.getLongitude() - p1.getLongitude()) * (p2.getLongitude() - p1.getLongitude())) / (Math.pow(p2.getLatitude() - p1.getLatitude(), 2) + Math.pow(p2.getLongitude() - p1.getLongitude(), 2));

            if (u <= 0) {
                out = Math.sqrt(Math.pow(p0.getLatitude() - p1.getLatitude(),
                        2) + Math.pow(p0.getLongitude() - p1.getLongitude(), 2));
            }
            if (u >= 1) {
                out = Math.sqrt(Math.pow(p0.getLatitude() - p2.getLatitude(),
                        2) + Math.pow(p0.getLongitude() - p2.getLongitude(), 2));
            }
            if (0 < u && u < 1) {
                out = Math.sqrt(Math.pow(p0.getLatitude() - p1.getLatitude() - u * (p2.getLatitude() - p1.getLatitude()), 2) + Math.pow(p0.getLongitude() - p1.getLongitude() - u * (p2.getLongitude() - p1.getLongitude()), 2));
            }
        }
        return out;
    }

    /**
     *
     * @param coordinate
     * @return
     */
    private static int floor1e5(double coordinate) {
        return (int) Math.floor(coordinate * 1e5);
    }

    /**
     *
     * @param num
     * @return
     */
    private static String encodeSignedNumber(int num) {
        int sgn_num = num << 1;
        if (num < 0) {
            sgn_num = ~(sgn_num);
        }
        return (encodeNumber(sgn_num));
    }

    /**
     *
     * @param num
     * @return
     */
    private static String encodeNumber(int num) {

        StringBuffer encodeString = new StringBuffer();

        while (num >= 0x20) {
            int nextValue = (0x20 | (num & 0x1f)) + 63;
            encodeString.append((char) (nextValue));
            num >>= 5;
        }

        num += 63;
        encodeString.append((char) (num));

        return encodeString.toString();
    }

    /**
     * Now we can use the previous function to march down the list of points and
     * encode the levels. Like createEncodings, we ignore points whose distance
     * (in dists) is undefined.
     */
    private String encodeLevels(List<TrackPoint> points, double[] dists, double absMaxDist) {
        int i;
        StringBuffer encoded_levels = new StringBuffer();

        if (this.forceEndpoints) {
            encoded_levels.append(encodeNumber(this.numLevels - 1));
        } else {
            encoded_levels.append(encodeNumber(this.numLevels - computeLevel(absMaxDist) - 1));
        }
        for (i = 1; i < points.size() - 1; i++) {
            if (dists[i] != 0) {
                encoded_levels.append(encodeNumber(this.numLevels - computeLevel(dists[i]) - 1));
            }
        }
        if (this.forceEndpoints) {
            encoded_levels.append(encodeNumber(this.numLevels - 1));
        } else {
            encoded_levels.append(encodeNumber(this.numLevels - computeLevel(absMaxDist) - 1));
        }

        return encoded_levels.toString();
    }

    /**
     * This computes the appropriate zoom level of a point in terms of it's
     * distance from the relevant segment in the DP algorithm. Could be done in
     * terms of a logarithm, but this approach makes it a bit easier to ensure
     * that the level is not too large.
     */
    private int computeLevel(double absMaxDist) {
        int lev = 0;
        if (absMaxDist > this.verySmall) {
            lev = 0;
            while (absMaxDist < this.zoomLevelBreaks[lev]) {
                lev++;
            }
            return lev;
        }
        return lev;
    }

    /**
     * 
     * @param points
     * @param dists
     * @return
     */
    private HashMap<String, String> createEncodings(List<TrackPoint> points, double[] dists) {
        StringBuffer encodedPoints = new StringBuffer();

        double maxlat = points.get(0).getLatitude();
        double minlat = points.get(0).getLatitude();
        double maxlon = points.get(0).getLongitude();
        double minlon = points.get(0).getLongitude();

        int plat = 0, plng = 0, count = 0;
        double avgSpeed = 0.0, maxSpeed = 0.0;

        for (int i = 0; i < points.size(); i++) {

            maxlat = maxlat < points.get(i).getLatitude() ? points.get(i).getLatitude() : maxlat;
            minlat = minlat > points.get(i).getLatitude() ? points.get(i).getLatitude() : minlat;

            maxlon = maxlon < points.get(i).getLongitude() ? points.get(i).getLongitude() : maxlon;
            minlon = minlon > points.get(i).getLongitude() ? points.get(i).getLongitude() : minlon;

            if (points.get(i).getSpeedKnot() > 0) {
                avgSpeed += points.get(i).getSpeedKnot();
                count++;
            }

            maxSpeed = points.get(i).getSpeedKnot() > maxSpeed ? points.get(i).getSpeedKnot() : maxSpeed;

            if (dists[i] != 0 || i == 0 || i == points.size() - 1) {
                TrackPoint point = points.get(i);

                int late5 = floor1e5(point.getLatitude());
                int lnge5 = floor1e5(point.getLongitude());

                int dlat = late5 - plat;
                int dlng = lnge5 - plng;

                plat = late5;
                plng = lnge5;

                encodedPoints.append(encodeSignedNumber(dlat));
                encodedPoints.append(encodeSignedNumber(dlng));
            }
        }

        avgSpeed = avgSpeed / (count == 0 ? 1 : count);

        HashMap<String, String> my_hm = new HashMap<String, String>();
        my_hm.put("encodedPoints", encodedPoints.toString());
        my_hm.put("maxSpeed", "" + maxSpeed);
        my_hm.put("avgSpeed", "" + avgSpeed);
        my_hm.put("maxlat", "" + maxlat);
        my_hm.put("minlat", "" + minlat);
        my_hm.put("maxlon", "" + maxlon);
        my_hm.put("minlon", "" + minlon);
        my_hm.put("avgLat", "" + ((maxlat + minlat) / 2));
        my_hm.put("avgLon", "" + ((maxlon + minlon) / 2));

        return my_hm;
    }

    /**
     * 
     * @param bounds
     */
    public void setBounds(HashMap<String, Double> bounds) {
        this.bounds = bounds;
    }

    /**
     *
     * @param tracks
     * @param level
     * @param step
     * @return
     */
    public static HashMap<String, String> createEncodings(List<TrackPoint> tracks, int level) {

        StringBuffer encodedPoints = new StringBuffer();
        StringBuffer encodedLevels = new StringBuffer();

        double maxlat = tracks.get(0).getLatitude();
        double minlat = tracks.get(0).getLatitude();
        double maxlon = tracks.get(0).getLongitude();
        double minlon = tracks.get(0).getLongitude();
        double avgSpeed = 0.0, maxSpeed = 0.0;
        int plat = 0, plng = 0, count = 0;

        for (TrackPoint trackpoint : tracks) {
            int late5 = floor1e5(trackpoint.getLatitude());
            int lnge5 = floor1e5(trackpoint.getLongitude());
            int dlat = late5 - plat;
            int dlng = lnge5 - plng;
            plat = late5;
            plng = lnge5;
            encodedPoints.append(encodeSignedNumber(dlat)).append(encodeSignedNumber(dlng));
            encodedLevels.append(encodeNumber(level));

            maxlat = maxlat < trackpoint.getLatitude() ? trackpoint.getLatitude() : maxlat;
            minlat = minlat > trackpoint.getLatitude() ? trackpoint.getLatitude() : minlat;

            maxlon = maxlon < trackpoint.getLongitude() ? trackpoint.getLongitude() : maxlon;
            minlon = minlon > trackpoint.getLongitude() ? trackpoint.getLongitude() : minlon;

            if (trackpoint.getSpeedKnot() > 0) {
                avgSpeed += trackpoint.getSpeedKnot();
                count++;
            }

            maxSpeed = trackpoint.getSpeedKnot() > maxSpeed ? trackpoint.getSpeedKnot() : maxSpeed;
        }

        avgSpeed = avgSpeed / (count == 0 ? 1 : count);

        HashMap<String, String> my_hm = new HashMap<String, String>();
        my_hm.put("encodedPoints", encodedPoints.toString());
        my_hm.put("encodedLevels", encodedLevels.toString());
        my_hm.put("maxSpeed", "" + maxSpeed);
        my_hm.put("avgSpeed", "" + avgSpeed);

        my_hm.put("maxlat", "" + maxlat);
        my_hm.put("minlat", "" + minlat);
        my_hm.put("maxlon", "" + maxlon);
        my_hm.put("minlon", "" + minlon);
        my_hm.put("avgLat", "" + ((maxlat + minlat) / 2));
        my_hm.put("avgLon", "" + ((maxlon + minlon) / 2));

        return my_hm;
    }

    /**
     * @return the map bounds
     */
    public HashMap<String, Double> getBounds() {
        return bounds;
    }
}

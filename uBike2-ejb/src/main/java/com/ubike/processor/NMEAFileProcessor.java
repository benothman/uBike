/*
 *  Copyright 2009 Olivier LIECHTI <olivier.liechti@heig-vd.ch>.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package com.ubike.processor;


import com.ubike.model.TrackPoint;
import com.ubike.model.Trip;
import com.ubike.services.TripManagerLocal;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;

/**
 *
 * @author oliechti
 * http://eppleton.sharedhost.de/blog/?p=142
 */
public class NMEAFileProcessor implements FileProcessor {



    @EJB
    private TripManagerLocal tripManager;

    private List<TrackPoint> trackPoints = new LinkedList<TrackPoint>();
    private double totalDistance = 0.0;
    private Date startTime = null;
    private Date endTime = null;
    private long totalTime = 0;

    public void processFile(File inputFile, NMEAFileProcessorContext context) {

        Map<String, String> readValues = new HashMap<String, String>();
        Set<String> readSentences = new HashSet<String>();

        TrackPoint marker = null;

        try {
            BufferedReader br = new BufferedReader(new FileReader(inputFile));
            String line = br.readLine();


            while (line != null) {
                String[] fields = line.split(",");

                if (fields.length == 0) {
                    line = br.readLine();
                    continue;
                }


                String recordType = fields[0];

                // we are reading a new record
                if (readSentences.contains(recordType)) {
                    processTrackPoint(readValues);

                    if (trackPoints.size() > 3) {
                        TrackPoint tp = trackPoints.get(trackPoints.size() - 1);
                        TrackPoint previousTrackPoint = trackPoints.get(trackPoints.size() - 2);
                        if (Math.abs(tp.getTimestamp() - previousTrackPoint.getTimestamp()) > 10 * 60 * 1000) {
                            if (marker == null) marker = trackPoints.get(0);
                            long tripStartTime = marker.getTimestamp();
                            long tripEndTime = tp.getTimestamp();

                            // TODO verify code !!!

                            //Trip trip = new Trip(new Date(tripStartTime), new Date(tripEndTime), totalDistance, trackPoints);
                            Trip trip = new Trip();
                            trip.setStartDate(new Date(tripStartTime));
                            trip.setEndDate(new Date(tripEndTime));
                            trip.setDistance(totalDistance);
                            trackPoints.remove(tp);
                            context.addTrip(trip);
                            marker = tp;
                            trackPoints = new LinkedList<TrackPoint>();
                            totalDistance = 0;
                        }
                    }

                    readValues.clear();
                    readSentences.clear();
                }

                if (recordType.equals("$GPGGA")) {
                    readSentences.add("$GPGGA");
                    readValues.put("time", fields[1]);
                    readValues.put("latitude", fields[2] + "," + fields[3]);
                    readValues.put("longitude", fields[4] + "," + fields[5]);
                    readValues.put("fixQuality", fields[6]);
                    readValues.put("numberOfSatellites", fields[7]);
                    readValues.put("altitude", fields[9]);
                } else if (recordType.equals("$GPRMC")) {
                    readSentences.add("$GPRMC");
                    readValues.put("time", fields[1]);
                    readValues.put("latitude", fields[3] + "," + fields[4]);
                    readValues.put("longitude", fields[5] + "," + fields[6]);
                    readValues.put("speedKnot", fields[7]);
                    readValues.put("date", fields[9]);
                } else if (recordType.equals("$GPVTG")) {
                    readSentences.add("$GPVTG");
                    readValues.put("speedKnot", fields[5]);
                    readValues.put("speedKmh", fields[7]);
                }
                line = br.readLine();
            }

            if (trackPoints.size() > 2) {
                long tripStartTime = trackPoints.get(0).getTimestamp();
                long tripEndTime = trackPoints.get(trackPoints.size() - 1).getTimestamp();

                // TODO verify the following code !!!

                //Trip trip = new Trip(tripStartTime, tripEndTime, totalDistance, trackPoints);
                Trip trip = new Trip();
                trip.setStartDate(new Date(tripStartTime));
                trip.setEndDate(new Date(tripEndTime));
                trip.setDistance(totalDistance);
                context.addTrip(trip);
            }

        //System.out.println("Total trip distance: " + totalDistance);
        //System.out.println("Total trip time    : " + (nmeaTimeToMillis(lastTime) - nmeaTimeToMillis(startTime)));
        //System.out.println("Start time: " + new Date(trackPoints.get(0).getTimestamp()));
        //System.out.println("End time  : " + new Date(trackPoints.get(trackPoints.size()-1).getTimestamp()));

        } catch (IOException ex) {
            Logger.getLogger(NMEAFileProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void processTrackPoint(Map<String, String> readValues) {
        //System.out.println(readValues);
        try {
            TrackPoint tp = new TrackPoint();

            /**
            tp.setFixQuality(Integer.parseInt(readValues.get("fixQuality")));
            if (tp.getFixQuality() == 0) {
                return;
            }
             */

            double latitude = nmeaToDeg(readValues.get("latitude"));
            double longitude = nmeaToDeg(readValues.get("longitude"));
            double altitude = Double.parseDouble(readValues.get("altitude"));


            //tp.setLatitude(latitude);
            tp.getGpsCoord().setLatitude(latitude);
            //tp.setLongitude(longitude);
            tp.getGpsCoord().setLongitude(longitude);
            //tp.setAltitude(altitude);
            tp.getGpsCoord().setAltitude(altitude);

            //tp.setNumberOfSatellites(Integer.parseInt(readValues.get("numberOfSatellites")));

            tp.setTimestamp(nmeaDateTimeToMillis(readValues.get("date") + readValues.get("time")));

            // 1 knot = 1.852 km/h
            //tp.setSpeed(Double.parseDouble(readValues.get("speedKnot")) * 1.852);

            if (trackPoints.size() > 1) {
                TrackPoint previousTrackPoint = trackPoints.get(trackPoints.size() - 1);
                double relativeDistance = previousTrackPoint.distanceTo(tp);
                totalDistance += relativeDistance;
            }
            trackPoints.add(tp);
        } catch (Exception e) {
            System.out.println("Error processing trackpoint: " + e.getMessage());
        }
    }

    private long nmeaDateTimeToMillis(String nmeaDateTime) {
        int day = Integer.parseInt(nmeaDateTime.substring(0, 2));
        int month = Integer.parseInt(nmeaDateTime.substring(2, 4));
        int year = 100 + Integer.parseInt(nmeaDateTime.substring(4, 6));

        int hour = Integer.parseInt(nmeaDateTime.substring(6, 8));
        int min = Integer.parseInt(nmeaDateTime.substring(8, 10));
        int sec = Integer.parseInt(nmeaDateTime.substring(10, 12));

        Date date = new Date(year, month, day, hour, min, sec);
        return date.getTime();
    }

    private long nmeaTimeToMillis(String nmeaTime) {
        int hour = Integer.parseInt(nmeaTime.substring(0, 2));
        int min  = Integer.parseInt(nmeaTime.substring(2, 4));
        int sec  = Integer.parseInt(nmeaTime.substring(4, 6));
        Date date = new Date(1970, 1, 1, hour, min, sec);
        
        return date.getTime();
    }

    private double nmeaToDeg(String latitudeOrLongitude) {
        String[] fields = latitudeOrLongitude.split(",");
        double value = Double.parseDouble(fields[0]);
        String hemisphere = fields[1];

        int latAngle = (int) (value / 100);
        double latMin = (value - (100 * latAngle)) / 60;
        double result = latAngle + latMin;

        if (hemisphere.equals("S") || hemisphere.equals("W")) {
            result = result * -1;
        }
        return result;
    }

    public void processFile(File inputFile, FileProcessorContext context) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

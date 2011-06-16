/*
 * Copyright 2011, Nabil Benothman, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package com.ubike.processor;

import com.ubike.util.StatisticManager;
import com.ubike.model.RawGpsFile;
import com.ubike.model.TrackPoint;
import com.ubike.model.Trip;
import com.ubike.util.UbikeEntity;
import com.ubike.model.UbikeUser;
import com.ubike.services.GPSFileServiceLocal;
import com.ubike.services.StatisticServiceLocal;
import com.ubike.services.TripManagerLocal;
import com.ubike.services.TripServiceLocal;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Benothman
 */
public class RawFileProcessor implements GpsFileProcessor {

    private static final Logger logger = Logger.getLogger(RawFileProcessor.class.getName());
    public static final String GPGGA = "$GPGGA";
    public static final String GPRMC = "$GPRMC";
    public static final String GPGLL = "$GPGLL";
    private double totalDisatance = 0.0;
    long minTimestamp = Long.MAX_VALUE;
    long maxTimestamp = Long.MIN_VALUE;
    private TripManagerLocal tml;
    private UbikeUser user;
    private Calendar cal = Calendar.getInstance();
    private StatisticManager statManager;
    private TripServiceLocal tripService;
    private GPSFileServiceLocal gpsFileService;
    private StatisticServiceLocal statisticService;

    /**
     *
     * @param tripManager
     * @param user
     */
    private RawFileProcessor(TripManagerLocal tripManager, UbikeUser user) {
        super();
        this.tml = tripManager;
        this.user = user;
        this.statManager = new StatisticManager(getTml());
    }

    /**
     * 
     * @param user 
     */
    private RawFileProcessor(UbikeUser user) {
        super();
        this.user = user;
    }

    /**
     * @return a new instance of <code>RawFileProcessor</code> 
     */
    public static RawFileProcessor create(TripManagerLocal tml, UbikeUser user) {
        return new RawFileProcessor(tml, user);
    }

    /**
     * Create a new instance of {@code RawFileProcessor}
     * 
     * @param user the current user
     * @return a new instance of {@code RawFileProcessor}
     */
    public static RawFileProcessor create(UbikeUser user) {
        return new RawFileProcessor(user);
    }

    /* (non-Javadoc)
     * @see com.ubike.processor.GpsFileProcessor#process(java.io.File)
     */
    @Override
    public void process(File inputFile) throws Exception {

        LinkedList<TrackPoint> tracks = new LinkedList<TrackPoint>();

        BufferedReader br = new BufferedReader(new FileReader(inputFile));
        String line = null, tokens[];
        double latitude, longitude, altitude, tmpValue;
        long tmp = 0;
        int x;

        while ((line = br.readLine()) != null) {
            tokens = line.split(",");
            if (tokens.length == 0) {
                continue;
            }

            TrackPoint trackPoint = null;

            if (tokens[0].equals(GPGGA)) {
                // $GPGGA,123519,4807.038,N,01131.000,E,1,08,0.9,545.4,M,46.9,M,,*47
                // 4807.038,N   Latitude 48 deg 07.038' N
                // 01131.000,E  Longitude 11 deg 31.000' E

                int fixQuality = Integer.parseInt(tokens[6]);
                if (fixQuality == 0) {
                    // The GPS line entry is not valid
                    continue;
                }

                // dRetVal = Int(sNMEA / 100) + (sNMEA - Int(sNMEA / 100) * 100) / 60
                tmpValue = Double.parseDouble(tokens[2]);
                x = (int) (tmpValue / 100);
                latitude = (tokens[3].equals("S") ? -1 : 1) * (x + (tmpValue - x * 100) / 60);

                tmpValue = Double.parseDouble(tokens[4]);
                x = (int) (tmpValue / 100);
                longitude = (tokens[5].equals("W") ? -1 : 1) * (x + (tmpValue - x * 100) / 60);

                int satNum = Integer.parseInt(tokens[7]);
                altitude = Double.parseDouble(tokens[9]);

                trackPoint = new TrackPoint(latitude, longitude, altitude, tmp);
                trackPoint.setFixQuality(fixQuality);
                trackPoint.setNumberOfSatellites(satNum);

            } else if (tokens[0].equals(GPRMC)) {
                // $GPRMC,142959.780,A,4647.3516,N,00631.2997,E,0.60,45.06,150707,,*3A
                // 4807.038,N   Latitude 48 deg 07.038' N
                // 01131.000,E  Longitude 11 deg 31.000' E

                tmpValue = Double.parseDouble(tokens[3]);
                x = (int) (tmpValue / 100);
                latitude = (tokens[4].equals("S") ? -1 : 1) * (x + (tmpValue - x * 100) / 60);

                tmpValue = Double.parseDouble(tokens[5]);
                x = (int) (tmpValue / 100);
                longitude = (tokens[6].equals("W") ? -1 : 1) * (x + (tmpValue - x * 100) / 60);

                if (latitude == 0 && longitude == 0) {
                    // The GPS line entry is not valid
                    continue;
                }

                double speedKnot = tokens[7].equals("") ? 0.0 : Double.parseDouble(tokens[7]);
                long timestamp = getFromString(tokens[9] + tokens[1]);
                tmp = timestamp;

                minTimestamp = timestamp < minTimestamp ? timestamp : minTimestamp;
                maxTimestamp = timestamp > maxTimestamp ? timestamp : maxTimestamp;

                trackPoint = new TrackPoint(latitude, longitude, 0.0, timestamp);
                trackPoint.setSpeedKnot(speedKnot);

            } else if (tokens[0].equals(GPGLL)) {
                // $GPGLL,4642.3628,N,00630.1560,E,125256.518,A*33
                // 4916.46,N    Latitude 49 deg. 16.45 min. North
                // 12311.12,W   Longitude 123 deg. 11.12 min. West

                tmpValue = Double.parseDouble(tokens[1]);
                x = (int) (tmpValue / 100);
                latitude = (tokens[2].equals("S") ? -1 : 1) * (x + (tmpValue - x * 100) / 60);

                tmpValue = Double.parseDouble(tokens[3]);
                x = (int) (tmpValue / 100);
                longitude = (tokens[4].equals("W") ? -1 : 1) * (x + (tmpValue - x * 100) / 60);

                if (latitude == 0 && longitude == 0) {
                    // The GPS line entry is not valid
                    continue;
                }

                trackPoint = new TrackPoint(latitude, longitude, 0, tmp);

            } else {
                continue;
            }

            totalDisatance += tracks.isEmpty() ? 0.0 : trackPoint.distanceTo(tracks.getLast());
            tracks.addLast(trackPoint);
        }
        
        // adding Trip to the database
        Trip trip = initializeTrip(tracks);
        if (trip != null) {
            logger.log(Level.INFO, "Add trip to datastore");
            trip.setOwner(user);
            this.tripService.create(trip);
            double avg = (user.getUserProfile().getAvgSpeed() * user.getTrips().size() + trip.getAvgSpeed()) / (user.getTrips().size() + 1);
            user.getTrips().add(trip);
            user.getUserProfile().setAvgSpeed(getDoubleValue(avg));
            user.getUserProfile().setTotalDistance(getDoubleValue(user.getUserProfile().getTotalDistance() + trip.getDistance()));
            user.getUserProfile().setTotalDuration(user.getUserProfile().getTotalDuration() + trip.getDuration());

            RawGpsFile gpsFile = new RawGpsFile(inputFile.getName(), inputFile.length(),
                    java.util.Calendar.getInstance().getTime());
            this.gpsFileService.create(gpsFile);

            /*
            updateEntityStatistics(user, trip);
            user.getMemberShips().size();
            for (MemberShip m : user.getMemberShips()) {
            updateEntityStatistics(m.getGroup(), trip);
            }
             */
        } else {
            logger.log(Level.INFO, "The trip is null");
        }
    }

    /**
     * Initialize a trip from the given track points
     * @param tracks a list of track points
     * @return a new Trip instance
     */
    public Trip initializeTrip(List<TrackPoint> tracks) {
        // Create the trip composed by TrackPoints
        if (!tracks.isEmpty() && totalDisatance > 0) {

            PolylineEncoder encoder = new PolylineEncoder();
            HashMap<String, String> my_hm = encoder.dpEncode(tracks);
            //HashMap<String, String> my_hm = PolylineEncoder.createEncodings(tracks, 13);

            totalDisatance = getDoubleValue(totalDisatance);
            double avgSpeed = getDoubleValue(Double.parseDouble(my_hm.get("avgSpeed")));
            double maxSpeed = getDoubleValue(Double.parseDouble(my_hm.get("maxSpeed")));
            int delta = (int) ((maxTimestamp - minTimestamp) / 1000);

            Trip trip = new Trip(new Date(minTimestamp), new Date(maxTimestamp), delta,
                    totalDisatance, avgSpeed, maxSpeed);

            trip.setMapCode(my_hm.get("encodedPoints"));
            trip.setMapLevels(my_hm.get("encodedLevels"));
            trip.setAvgLat(Double.parseDouble(my_hm.get("avgLat")));
            trip.setAvgLon(Double.parseDouble(my_hm.get("avgLon")));
            trip.setStartLat(tracks.get(0).getLatitude());
            trip.setStartLon(tracks.get(0).getLongitude());
            trip.setEndLat(tracks.get(tracks.size() - 1).getLatitude());
            trip.setEndLon(tracks.get(tracks.size() - 1).getLongitude());
            return trip;
        }

        return null;
    }

    /**
     * Update the statistics of the given entity with the given trip data
     * @param entity
     * @param trip
     */
    private void updateEntityStatistics(UbikeEntity entity, Trip trip) {
        statManager.updateEntityStatistics(entity, trip);
    }

    /**
     * 
     * @param value
     * @return a double value with less or equal than 3 fraction digits.
     */
    private double getDoubleValue(double value) {
        String tmpTab[] = ("" + value).split("\\.");
        if (tmpTab[1].length() > 3) {
            tmpTab[1] = tmpTab[1].substring(0, 3);
            value = Double.parseDouble(tmpTab[0] + "." + tmpTab[1]);
        }
        return value;
    }

    /**
     * Convert the given String to a date
     *
     * @param str
     * @return
     */
    private long getFromString(String dateTime) {

        // Example : 280383123519 =>  Date - 28th of March 1983 at 12:35:19
        int day = Integer.parseInt(dateTime.substring(0, 2));
        int month = Integer.parseInt(dateTime.substring(2, 4)) - 1;
        int year = Integer.parseInt("20" + dateTime.substring(4, 6));
        int hour = Integer.parseInt(dateTime.substring(6, 8));
        int minute = Integer.parseInt(dateTime.substring(8, 10));
        int second = Integer.parseInt(dateTime.substring(10, 12));
        cal.set(year, month, day, hour, minute, second);

        return cal.getTimeInMillis();
    }

    /**
     * @return the tml
     */
    public TripManagerLocal getTml() {
        return tml;
    }

    /**
     * @param tml the tml to set
     */
    public void setTml(TripManagerLocal tml) {
        this.tml = tml;
    }

    /**
     * @return the tripService
     */
    public TripServiceLocal getTripService() {
        return tripService;
    }

    /**
     * @param tripService the tripService to set
     */
    public void setTripService(TripServiceLocal tripService) {
        this.tripService = tripService;
    }

    /**
     * @return the gpsFileService
     */
    public GPSFileServiceLocal getGpsFileService() {
        return gpsFileService;
    }

    /**
     * @param gpsFileService the gpsFileService to set
     */
    public void setGpsFileService(GPSFileServiceLocal gpsFileService) {
        this.gpsFileService = gpsFileService;
    }

    /**
     * @return the statisticService
     */
    public StatisticServiceLocal getStatisticService() {
        return statisticService;
    }

    /**
     * @param statisticService the statisticService to set
     */
    public void setStatisticService(StatisticServiceLocal statisticService) {
        this.statisticService = statisticService;
    }
}

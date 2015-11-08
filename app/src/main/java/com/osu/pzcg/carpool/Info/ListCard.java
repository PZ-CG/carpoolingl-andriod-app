package com.osu.pzcg.carpool.Info;

/**
 * Created by GoThumbers on 11/6/15.
 */
public class ListCard {

        private String name;
        private String time;
        private String departure;
        private String destination;
        private String seats;

        public ListCard(String name,String time, String departure,String destination, String seats)
        {
            this.name = name;
            this.time = time;
            this.departure = departure;
            this.destination = destination;
            this.seats = seats;
        }

        public String getName()
        {
            return name;
        }
        public void setName(String name)
        {
            this.name = name;
        }
        public String getTime()
        {
            return time;
        }
        public void setTime(String time)
        {
            this.time = time;
        }

        public String getDestination()
        {
            return destination;
        }
        public void setDestination(String destination)
        {
            this.destination = destination;
        }

        public String getDeparture()
        {
            return departure;
        }
        public void setDeparture(String departure)
        {
            this.departure = departure;
        }
        public String getSeats()
        {
            return seats;
        }
        public void setSeats(String seats)
        {
            this.seats = seats;
        }

}

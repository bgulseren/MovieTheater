package Theater;

import java.time.Duration; 
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;



public class Movie {
	
	private ArrayList<ShowTime> showTimes;
	private String movieName;
	private boolean earlyAccess;
	private LocalDateTime earlyReservationDate;
	private LocalDateTime normalReservationDate;
	private Date releaseDate;
	private ShowTime selectedShowtime;
	private ArrayList<Theater> currentTheaters;
	
	
	public Movie(String name, ArrayList<ShowTime> showTimes, LocalDateTime normalReservationDate) {
		movieName = name;
		this.showTimes = showTimes;
		this.normalReservationDate = normalReservationDate;
		if(Duration.between(LocalDateTime.now(), normalReservationDate).toDays() > 5) {
			earlyAccess = true;
			earlyReservationDate = normalReservationDate.minusDays(5);
		}
		else {
			earlyAccess = false;
			earlyReservationDate = null;
		}
				
	}
	public Movie(String movieName, Date releaseDate) {
        setMovieName(movieName);
        setReleaseDate(releaseDate);
        showTimes = new ArrayList<>();
    }
	
	public void addShowTime(ShowTime showTime) {
	        showTimes.add(showTime);
	}
	
	
	public void selectShowTime(LocalDateTime time) {
		selectedShowtime = new ShowTime(time);
	}
	
	public void selectShowTime(ShowTime time) {
		this.selectedShowtime = time;
	}


	public ArrayList<ShowTime> getShowTimes() {
		return showTimes;
	}


	public void setShowTimes(ArrayList<ShowTime> showTimes) {
		this.showTimes = showTimes;
	}


	public String getMovieName() {
		return movieName;
	}


	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}


	public boolean isEarlyAccess() {
		return earlyAccess;
	}


	public void setEarlyAccess(boolean earlyAccess) {
		this.earlyAccess = earlyAccess;
	}


	public LocalDateTime getEarlyReservationDate() {
		return earlyReservationDate;
	}


	public void setEarlyReservationDate(LocalDateTime earlyReservationDate) {
		this.earlyReservationDate = earlyReservationDate;
	}


	public LocalDateTime getNormalReservationDate() {
		return normalReservationDate;
	}


	public void setNormalReservationDate(LocalDateTime normalReservationDate) {
		this.normalReservationDate = normalReservationDate;
	}


	public ShowTime getSelectedShowtime() {
		return selectedShowtime;
	}
	

	public ArrayList<Theater> getCurrentTheaters() {
		return currentTheaters;
	}


	public void setCurrentTheaters(ArrayList<Theater> currentTheaters) {
		this.currentTheaters = currentTheaters;
	}
	public Date getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}
	
	@Override
    public String toString() {
        return "Movie: " + movieName + "\nShowTimes: " + showTimes + "\nRelease Date: " + releaseDate + "\n";
    }
}

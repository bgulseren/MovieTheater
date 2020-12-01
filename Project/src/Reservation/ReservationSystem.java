package Reservation;

import Payment.MakePaymentGUI; 

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import Theater.*;
import Theater.Movie;

//import Theater.ShowTime;
public class ReservationSystem {

    private ArrayList<Voucher> vouchers;
    private ArrayList<Reservation> reservations;
    private ArrayList<Session> sessions;
    private MakePaymentGUI makePaymentGUI;
    
    
    public ReservationSystem(MakePaymentGUI makePaymentGUI, ArrayList<Session> sessions) {
    	
        setMakePaymentGUI(makePaymentGUI);
        vouchers = new ArrayList<>();
        reservations = new ArrayList<>();
        this.sessions = sessions; 
    }

    public String cancelReservation(int reservationId) {
        for (Reservation reservation : reservations) {
            if (reservation.getReservationId() == reservationId) {
                if (checkForExpiry(reservation)) {
                    if (reservation.getUserName().equals("noUser")) {
                        Voucher voucher = createVoucherRegularUser(reservation);
                        reservations.remove(reservation);
                        return "Cancellation successful\nThe following voucher has been emailed to you\n" +
                                voucher.toString() + "\nYour voucher is for 85% of the original amount";
                    } else {
                        Voucher voucher = createVoucherRegisteredUser(reservation);
                        reservations.remove(reservation);
                        return "Cancellation successful\nThe following voucher has been emailed to you\n" +
                                voucher.toString() + "\nYour voucher is for the full original amount";
                    }
                } else
                    return "Movie starts in less than 3 days, can't cancel anymore";
            }
        }
        return "Reservation doesn't exist";
    }

    private Voucher createVoucherRegularUser(Reservation reservation) {
        double amount = 0;
        for (Ticket ticket : reservation.getTickets())
            amount += ticket.getPrice();
        int vouchNum = vouchers.get(vouchers.size() - 1).getVouchNum() + 1;
        Voucher voucher = new Voucher(vouchNum, (amount * .85));
        vouchers.add(voucher);
        return voucher;
    }

    private Voucher createVoucherRegisteredUser(Reservation reservation) {
        double amount = 0;
        for (Ticket ticket : reservation.getTickets())
            amount += ticket.getPrice();
        int vouchNum = vouchers.get(vouchers.size() - 1).getVouchNum() + 1;
        Voucher voucher = new Voucher(vouchNum, amount);
        vouchers.add(voucher);
        return voucher;
    }

    private boolean checkForExpiry(Reservation reservation) {
        return reservation.getShowTime().minusDays(3).compareTo(java.time.LocalDateTime.now()) > 0;
    }

    public void loadVouchers(ResultSet rs) {
        try {
            while (rs.next()) {
                addVoucher(new Voucher(
                        rs.getInt("vouchNum"),
                        rs.getFloat("amount"),
                        rs.getDate("expiryDate")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadReservations(ResultSet rs) {
        try {
            while (rs.next()) {
                addReservation(new Reservation(
                        rs.getInt("reservationId"),
                        rs.getString("userName")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadTickets(ResultSet rs) {
        try {
            while (rs.next()) {
                Ticket ticket = new Ticket(rs.getString("movieName"),
                                           rs.getInt("seat"),
                                           modifyDate(rs.getDate("showTimes")),
                                           rs.getInt("room"),
                                           rs.getDouble("price"),
                                          rs.getInt("reservationId"));
                for (Reservation reservation : reservations)
                    if (reservation.getReservationId() == ticket.getReservationId())
                        reservation.addTicket(ticket);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void displayVouchers() {
        for (Voucher voucher : vouchers)
            System.out.println(voucher);
    }

    public void displayReservations() {
        for (Reservation reservation : reservations)
            System.out.println(reservation);
    }

    private Voucher createVoucher(Reservation reservation) {
        double amount = 0;
        for (Ticket ticket : reservation.getTickets())
            amount += ticket.getPrice();
        int vouchNum = vouchers.get(vouchers.size() - 1).getVouchNum() + 1;
        Voucher voucher = new Voucher(vouchNum, (amount * .85));
        vouchers.add(voucher);
        return voucher;
    }

    public Session searchForSession(Movie movie, ShowTime showtime, int roomNumber) {
    	for(Session s: sessions) {
    		if(s.getMovie().getMovieName().equals(movie.getMovieName()) &&
    				(s.getShowTime().getTime().compareTo(showtime.getTime()) == 0)  && roomNumber == s.getRoom().getRoomNumber()) {
        		
    			return s;
    			
    			
        	}
    	}
    	
    	return null;
    }
    

    private void addVoucher(Voucher voucher) {
        vouchers.add(voucher);
    }

    private void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }
    

    public void setMakePaymentGUI(MakePaymentGUI makePaymentGUI) {
        this.makePaymentGUI = makePaymentGUI;
    }
    
    public LocalDateTime modifyDate(Date originalDate) {
        return new java.sql.Timestamp(originalDate.getTime()).toLocalDateTime();
    }
}

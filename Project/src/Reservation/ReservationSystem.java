package Reservation;

import Theater.Movie;
import Theater.ShowTime;
import Payment.MakeTicketPaymentGUI;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;


public class ReservationSystem {

    private MakeTicketPaymentGUI makeTicketPaymentGUI; //TODO: remove
    private ManageReservations manageReservations;
    private ArrayList<Voucher> vouchers;
    private ArrayList<Reservation> reservations;
    private ArrayList<Session> sessions;
    private Reservation lastReservation;
    private int lastReservationId;
    
    
    public ReservationSystem(MakeTicketPaymentGUI makeTicketPaymentGUI, ArrayList<Session> sessions) {
        setMakePaymentGUI(makeTicketPaymentGUI);
        vouchers = new ArrayList<>();
        reservations = new ArrayList<>();
        setSessions(sessions);
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
    
    
    public Reservation searchForReservation(int reservationId) {
    	for (Reservation reservation : reservations) {
            if (reservation.getReservationId() == reservationId) {
            	return reservation;
            }
    	}
    	return null;
    }
    

    public double applyVoucher(int vouchNum, double amount) {
        for (Voucher voucher : vouchers)
            if (voucher.getVouchNum() == vouchNum)
                if (voucher.getExpiryDate().compareTo(new Date()) > 0) {
                    if (voucher.getAmount() >= amount)
                        return 0;
                    else
                        return amount - voucher.getAmount();
                } else
                    return -1;
        return -1;
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
                
                lastReservationId = rs.getInt("reservationId") + 1;
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
        lastReservationId = reservation.getReservationId() + 1;
        
    }
    
    public void generateReservation(String userName, Session activeSession) {
    	lastReservationId++;
    	Reservation res = new Reservation(lastReservationId, userName);
        reservations.add(res);
        
        //Generate tickets for each seat belonging to the reservation
        String movieName = activeSession.getMovie().getMovieName();
        LocalDateTime showTime;
        
        if (activeSession.getShowTime() != null) {
        	showTime = activeSession.getShowTime().getTime();
        } else {
        	showTime = LocalDateTime.now();
        }
        
        double price = 0;
        int room = 0;
        
        if (getManageReservations() != null) {
            room = activeSession.getRoom().getRoomNumber();
            price = getManageReservations().getReservationGUI().getSeatsBeingSelected().size() * 12;
        } else {
            room = activeSession.getRoom().getRoomNumber();
            price = activeSession.getSelectedSeats().size() * 12;
        }

        
        for (int i = 0; i < getManageReservations().getReservationGUI().getSeatsBeingSelected().size(); i++) {
        	int seat = getManageReservations().getReservationGUI().getSeatsBeingSelected().get(i);
        	res.generateTicket(movieName, seat, showTime, price, room);
        }
        
    	this.lastReservation = res;
    }

    public void setSessions(ArrayList<Session> sessions) {
        this.sessions = sessions;
    }
    
    public void setMakePaymentGUI(MakeTicketPaymentGUI makeTicketPaymentGUI) {
        this.makeTicketPaymentGUI = makeTicketPaymentGUI;
    }
    
    public LocalDateTime modifyDate(Date originalDate) {
        return new java.sql.Timestamp(originalDate.getTime()).toLocalDateTime();
    }
    
    public Reservation getLastReservation() {
		return lastReservation;
	}
    
    public void setManageReservations(ManageReservations manageReservations) {
		this.manageReservations = manageReservations;
	}
    
    public ManageReservations getManageReservations() {
		return manageReservations;
	}
}

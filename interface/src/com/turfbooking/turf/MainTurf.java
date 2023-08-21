package com.turfbooking.turf;

//In built packages
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.*;

//User defined packages
import com.turfbooking.admin.*;
import com.turfbooking.user.*;

class InvalidPhoneNo extends Exception {
    String message;

    public InvalidPhoneNo(String message) {
        this.message = message;
    }

}

public class MainTurf extends Admin implements Booking, UserAbilities {

    @Override
    public void contactSupervisor() {
        System.out.println(" ------Football------");
        System.out.println(
                " Name of Supervisor : Ram Dharrao\n Phone no. : 9273651102\n Email ID : dharraoram@gmail.com");
        System.out.println();
        System.out.println(" ------Basketball------");
        System.out.println(
                " Name of Supervisor : Rahul Pawar\n Phone no. : 8760241358\n Email ID : rahulpawar96@gmail.com");
        System.out.println();
        System.out.println(" ------Cricket------");
        System.out.println(
                " Name of Supervisor : Kermi Lande\n Phone no. : 6210894572\n Email ID : landekermi@gmail.com");
        System.out.println();
    }

    @Override
    public void makeABooking() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String sport;
        String date;
        String bookname;
        String bookphno;
        int slotno;
        try {
            System.out.println("Enter the sport to book turf:");
            sport = br.readLine();
            sport = sport.substring(0, 1).toUpperCase() + sport.substring(1).toLowerCase();
            if (!sport.equals("Football") && !sport.equals("Basketball") && !sport.equals("Cricket")) {
                System.out.println("Please enter valid sport.");
                return;
            }
            System.out.println("Enter the date to book turf:");
            date = br.readLine();
            String query;
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/turfbookingsystem", "root",
                    "password");
            Statement stmt = con.createStatement();
            if (Integer.parseInt(date.substring(0, 4)) < 2022
                    || (Integer.parseInt(date.substring(5, 7)) > 12 || Integer.parseInt(date.substring(5, 7)) < 1)
                    || (Integer.parseInt(date.substring(8)) > 31 || Integer.parseInt(date.substring(8)) < 1)) {
                System.out.println("Please enter valid date.");
                return;
            }
            System.out.println("Available slots are:");
            query = "select * from slots where slotno NOT IN (select slotno from slotsbooking where bookingdate=\""
                    + date + "\") and sportname=\""+sport+"\";";
            ResultSet rs = stmt.executeQuery(query);
            System.out.println("Slot No.\tStart Time \tEnd Time \tPrice");
            while (rs.next()) {
                System.out.println(
                        " " + rs.getInt(1) + "\t\t" + rs.getInt(2) + "\t\t" + rs.getInt(3) + "\t\t" + rs.getFloat(4));
            }
            System.out.println("Enter name to book slot:");
            bookname = br.readLine();
            bookname = bookname.toUpperCase();
            System.out.println("Enter contact number to book slot:");
            bookphno = br.readLine();
            try {
                int len = bookphno.length();
                if (len < 10 || len > 10) {
                    throw new InvalidPhoneNo("Invalid Slot Number, the phone no should be of 10 digits ");
                }
            } catch (InvalidPhoneNo ex) {
                ex.printStackTrace();
                return;
            }
            System.out.println("Enter the desired slot number to book:");
            slotno = Integer.parseInt(br.readLine());
            query = "select slotno from slots where slotno IN (select slotno from slotsbooking where bookingdate=\""
                    + date + "\");";
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                if (slotno == rs.getInt(1)) {
                    System.out.println("Please enter valid slot.");
                    return;
                }
            }
            query = "insert into slotsbooking(slotno,bookingdate,Name,ContactNo) values(" + slotno + ",\"" + date
                    + "\",\"" + bookname + "\",\"" + bookphno + "\");";
            stmt.executeUpdate(query);
            System.out.println("Slot booked successfully!");
            con.close();

        } catch (SQLException se) {
            System.out.println("There was an error connecting to the database.Please try again.");
        } catch (ClassNotFoundException ce) {
            System.out.println("An error has occured.Please try again.");
        } catch (NumberFormatException nf) {
            System.out.println("Please enter valid choice.");
        } catch (IOException io) {
            System.out.println("An error has occured.Please try again");
        }

    }

    @Override
    public void viewBooking() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            String bookname;
            int orderno;
            System.out.println("Enter the name under which slot is booked:");
            bookname = br.readLine();
            bookname = bookname.toUpperCase();
            System.out.println("Enter the order number:");
            orderno = Integer.parseInt(br.readLine());
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/turfbookingsystem", "root",
                    "password");
            Statement stmt = con.createStatement();
            String query;
            query = "select * from slotsbooking where orderno=" + orderno + " and Name=\"" + bookname + "\";";
            ResultSet rs = stmt.executeQuery(query);
            if (!rs.next()) {
                System.out.println("Order does not exist. Please try again.");
                return;
            }
            System.out.println("***********RECEIPT***********");
            query = "select orderno,Name,ContactNo,slots.slotno,sportname,startime,endtime,price from slots,slotsbooking where orderno="
                    + orderno + " and Name=\"" + bookname
                    + "\" and slots.slotno=(select slotno from slotsbooking where orderno=" + orderno + ");";
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                System.out.println("ORDER NO. : " + rs.getInt(1));
                System.out.println("NAME : " + rs.getString(2));
                System.out.println("CONTACT NUMBER : " + rs.getString(3));
                System.out.println("SLOT NUMBER : " + rs.getInt(4));
                System.out.println("SPORT : " + rs.getString(5));
                System.out.println("SLOT TIMINGS:");
                System.out.println("START TIME : " + rs.getInt(6) + " Hours");
                System.out.println("END TIME : " + rs.getInt(7) + " Hours");
                System.out.println("PRICE : Rs." + rs.getFloat(8));
                System.out.println("---------------");
                System.out.println("*****************************");
            }
        } catch (SQLException se) {
            System.out.println("There was an error connecting to the database.Please try again.");
        } catch (ClassNotFoundException ce) {
            System.out.println("An error has occured.Please try again.");
        } catch (NumberFormatException nf) {
            System.out.println("Please enter valid choice.");
        } catch (IOException io) {
            System.out.println("An error has occured.Please try again");
        }
    }

    public static void main(String[] args) throws IOException {
        try (Scanner in = new Scanner(System.in)) {
            Admin admin = new Admin();
            MainTurf user = new MainTurf();
            int d, d1;
            System.out.println("*********TURF SYSTEM**********");
            System.out.println("\n 1. User\n 2. Admin ");
            System.out.println("Enter choice:");
            int ch = in.nextInt();
            if (ch == 1) {
                do {

                    System.out.println(
                            "TURF BOOKING SYSTEM\n 1. View Prices\n 2. View Slots\n 3. Make A Booking\n 4. View Booking\n 5. Contact Admin\n");
                    System.out.println("Enter choice:");
                    int c1 = in.nextInt();
                    switch (c1) {
                        case 1: {
                            System.out.println("Enter the sport out of Football, Basketball or Cricket:");
                            String sport = in.next();
                            Booking.viewPrice(sport);
                            break;
                        }
                        case 2: {

                            System.out.println("Enter the sport out of Football, Basketball or Cricket:");
                            String sport = in.next();
                            System.out.println("Enter the date to view available slots(yyyy-mm-dd):");
                            String date = in.next();
                            UserAbilities.viewSlots(date, sport);
                            break;
                        }
                        case 3: {
                            user.makeABooking();
                            break;
                        }
                        case 4: {
                            user.viewBooking();
                            break;
                        }
                        case 5: {
                            user.contactSupervisor();
                            break;
                        }
                        default: {
                            System.out.println("Invalid choice. Please try again.");
                            break;
                        }
                    }
                    System.out.println("Press 1 to continue or 0 to exit:");
                    d = in.nextInt();
                } while (d == 1);

            } else if (ch == 2) {
                do {
                    System.out.println(
                            "TURF BOOKING SYSTEM\n 1. Add Time Slots\n 2. Delete Time Slots\n 3. View Profits\n 4. View All Booking\n");
                    System.out.println("Enter choice:");
                    int c2 = in.nextInt();
                    switch (c2) {
                        case 1: {
                            System.out.println("Enter the sport out of Football, Basketball or Cricket:");
                            String sport = in.next();
                            System.out.println("Enter the Slot Number:");
                            int slotno = in.nextInt();
                            admin.slot(slotno, sport);
                            break;
                        }
                        case 2: {

                            System.out.println("Enter the Slot Number:");
                            int slotno = in.nextInt();
                            admin.slot(slotno);
                            break;
                        }
                        case 3: {
                            admin.showProfits();
                            break;
                        }
                        case 4: {
                            admin.viewAllBookings();
                            break;
                        }
                        default: {
                            System.out.println("Invalid choice. Please try again.");
                            break;
                        }

                    }
                    System.out.println("Press 1 to continue or 0 to exit:");
                    d1 = in.nextInt();
                } while (d1 == 1);
            } else {
                System.out.println("Invalid Input");
            }
        }

    }
}

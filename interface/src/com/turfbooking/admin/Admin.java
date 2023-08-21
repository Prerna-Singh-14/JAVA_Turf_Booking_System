package com.turfbooking.admin;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class Admin extends AdminAbilities {

    @Override
    public void slot(int slotno, String sport) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            String query;
            int starttime, endtime;
            float price;
            sport = sport.substring(0, 1).toUpperCase() + sport.substring(1);
            if (!sport.equals("Football") && sport.equals("Basketball") && sport.equals("Cricket")) {
                System.out.println("Please enter valid sport.");
                return;
            }
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/turfbookingsystem", "root",
                    "password");
            Statement stmt = con.createStatement();
            System.out.println("Enter the start time:");
            starttime = Integer.parseInt(br.readLine());
            query = "select * from slots where startime=" + starttime + " and sportname=\"" + sport + "\";";
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                System.out.println("Slot time already exists. Please enter a valid time.");
                return;
            }
            System.out.println("Enter the end time:");
            endtime = Integer.parseInt(br.readLine());
            query = "select * from slots where endtime=" + endtime + " and sportname=\"" + sport + "\";";
            rs = stmt.executeQuery(query);
            if (rs.next()) {
                System.out.println("Slot time already exists. Please enter a valid time.");
                return;
            }
            System.out.println("Enter price of the slot:");
            price = Float.parseFloat(br.readLine());
            query = "insert into slots values(" + slotno + "," + starttime + "," + endtime + "," + price + ",\"" + sport
                    + "\");";
            stmt.executeUpdate(query);
            System.out.println("Slot added successfully!");
            con.close();
        } catch (SQLException se) {
            System.out.println("There was an error connecting to the database.Please try again.");
        } catch (IOException ie) {
            System.out.println("There was an error while entering data. Please try again.");
        } catch (ClassNotFoundException ce) {
            System.out.println("An error has occured.Please try again.");
        }
    }

    @Override
    public void slot(int slotno) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/turfbookingsystem", "root",
                    "password");
            Statement stmt = con.createStatement();
            String query;
            query = "select * from slots where slotno=" + slotno + ";";
            ResultSet rs = stmt.executeQuery(query);
            if (!rs.next()) {
                System.out.println("Slot time does not exist. Please enter a valid time.");
                return;
            }
            query = "delete from slots where slotno=" + slotno + ";";
            stmt.executeUpdate(query);
            System.out.println("Slot deleted successfully!");
            query = "select * from slots;";
            rs = stmt.executeQuery(query);
            System.out.println("Slot No.\tStart Time\tEnd Time \tPrice");
            while (rs.next()) {
                System.out.println(
                        " " + rs.getInt(1) + "\t           " + rs.getInt(2) + "\t           " + rs.getInt(3)
                                + "\t        "
                                + rs.getFloat(4));
            }
            con.close();
        } catch (SQLException se) {
            System.out.println("There was an error connecting to the database.Please try again.");
        } catch (ClassNotFoundException ce) {
            System.out.println("An error has occured.Please try again.");
        }
    }

    @Override
    public void showProfits() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/turfbookingsystem", "root",
                    "password");
            Statement stmt = con.createStatement();
            System.out.println("MENU");
            System.out.println("1. View a single day's profits");
            System.out.println("2. View total profits");
            int ch;
            System.out.println("Enter choice:");
            ch = Integer.parseInt(br.readLine());
            if (ch == 1) {
                double profit = 0.0;
                String date;
                System.out.println("Enter date to view profits(YYYY-MM_DD format):");
                date = br.readLine();
                String query;
                query = "select * from slotsbooking where bookingdate=\"" + date + "\";";
                ResultSet rs = stmt.executeQuery(query);
                if (!rs.next()) {
                    System.out.println("No data available for given date. Please try again.");
                    return;
                }
                query = "select slotsbooking.slotno,slots.price from slotsbooking,slots where slotsbooking.slotno=slots.slotno and bookingdate=\""
                        + date + "\";";
                rs = stmt.executeQuery(query);
                System.out.println("Slot No. \tPrice");
                while (rs.next()) {
                    System.out.println(" " + rs.getInt(1) + "\t        " + rs.getFloat(2));
                }
                query = "select slots.price from slots,slotsbooking where bookingdate=\"" + date
                        + "\" and slots.slotno=slotsbooking.slotno;";
                rs = stmt.executeQuery(query);
                while (rs.next()) {
                    profit = profit + rs.getFloat(1);
                }
                System.out.println("Total Profits=" + profit);
            } else if (ch == 2) {
                double profit = 0.0;
                String query;
                query = "select * from slotsbooking;";
                ResultSet rs = stmt.executeQuery(query);
                if (!rs.next()) {
                    System.out.println("No data available for given date. Please try again.");
                    return;
                }
                query = "select slotsbooking.slotno,slots.price from slotsbooking,slots where slotsbooking.slotno=slots.slotno;";
                rs = stmt.executeQuery(query);
                System.out.println("Slot No. \tPrice");
                while (rs.next()) {
                    System.out.println(" " + rs.getInt(1) + "\t        " + rs.getFloat(2));
                }
                query = "select slots.price from slots,slotsbooking where slots.slotno=slotsbooking.slotno;";
                rs = stmt.executeQuery(query);
                while (rs.next()) {
                    profit = profit + rs.getFloat(1);
                }
                System.out.println("Total Profits=" + profit);
            }
            con.close();
        } catch (SQLException se) {
            System.out.println("There was an error connecting to the database.Please try again.");
        } catch (IOException ie) {
            System.out.println("There was an error while entering data. Please try again.");
        } catch (ClassNotFoundException ce) {
            System.out.println("An error has occured.Please try again.");
        }
    }

    @Override
    public void viewAllBookings() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/turfbookingsystem", "root",
                    "password");
            Statement stmt = con.createStatement();
            System.out.println("MENU");
            System.out.println("1. View a single day's bookings");
            System.out.println("2. View total bookings");
            int ch;
            System.out.println("Enter choice:");
            ch = Integer.parseInt(br.readLine());
            if (ch == 1) {
                String query;
                String date;
                System.out.println("Enter date to view bookings:");
                date = br.readLine();
                query = "select * from slotsbooking where bookingdate=\"" + date + "\";";
                ResultSet rs = stmt.executeQuery(query);
                System.out.println("Slotno      Bookingdate     Name        Contact Details");
                while (rs.next()) {
                    System.out.println(rs.getInt(1) + "\t    " + rs.getString(2) + "\t    " + rs.getString(3) + "\t    "
                            + rs.getString(4));
                }
            } else if (ch == 2) {
                String query;
                query = "select * from slotsbooking;";
                ResultSet rs = stmt.executeQuery(query);
                System.out.println("Slotno      Bookingdate     Name        Contact Details");
                while (rs.next()) {
                    System.out.println(rs.getInt(1) + "\t    " + rs.getString(2) + "\t    " + rs.getString(3) + "\t    "
                            + rs.getString(4));
                }
            } else {
                System.out.println("Please enter a valid choice.");
            }
            con.close();
        } catch (SQLException se) {
            System.out.println("There was an error connecting to the database.Please try again.");
        } catch (IOException ie) {
            System.out.println("There was an error while entering data. Please try again.");
        } catch (ClassNotFoundException ce) {
            System.out.println("An error has occured.Please try again.");
        } catch (NumberFormatException nf) {
            System.out.println("Please enter valid choice.");
        }
    }
}

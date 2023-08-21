package com.turfbooking.user;
import java.sql.*;

public interface UserAbilities {
    static void viewSlots(String date, String sport) {
        sport = sport.substring(0, 1).toUpperCase() + sport.substring(1).toLowerCase();
        if (!sport.equals("Football") && !sport.equals("Basketball") && !sport.equals("Cricket")) {
            System.out.println("Please enter valid sport.");
            return;
        }
        try {
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
            query = "select * from slots where slotno NOT IN (select slotno from slotsbooking where bookingdate=\""
                    + date + "\") and sportname=\""+sport+"\";";
            ResultSet rs = stmt.executeQuery(query);
            System.out.println("Slot No.\tStart Time \tEnd Time \tPrice");
            while (rs.next()) {
                System.out.println(
                        " " + rs.getInt(1) + "\t\t" + rs.getInt(2) + "\t\t" + rs.getInt(3) + "\t\t" + rs.getFloat(4));
            }
            con.close();
        } catch (SQLException se) {
            System.out.println("There was an error connecting to the database.Please try again.");
        } catch (ClassNotFoundException ce) {
            System.out.println("An error has occured.Please try again.");
        } catch (NumberFormatException nf) {
            System.out.println("Please enter valid choice.");
        }
    }

    public void contactSupervisor();

    public void makeABooking();

    /*
     * MAKE A BOOKING
     * BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
     * String sport;
     * String date;
     * String bookname;
     * String bookphno;
     * int slotno;
     * System.out.println("Enter the sport to book turf:");
     * sport = br.readLine();
     * sport = sport.substring(0, 1).toUpperCase() +
     * sport.substring(1).toLowerCase();
     * if (!sport.equals("Football") && !sport.equals("Basketball") &&
     * !sport.equals("Cricket")) {
     * System.out.println("Please enter valid sport.");
     * return;
     * }
     * System.out.println("Enter the date to book turf:");
     * date = br.readLine();
     * try {
     * String query;
     * Class.forName("com.mysql.cj.jdbc.Driver");
     * Connection con =
     * DriverManager.getConnection("jdbc:mysql://localhost:3306/turfbookingsystem",
     * "root",
     * "password");
     * Statement stmt = con.createStatement();
     * if (Integer.parseInt(date.substring(0, 4)) < 2022
     * || (Integer.parseInt(date.substring(5, 7)) > 12 ||
     * Integer.parseInt(date.substring(5, 7)) < 1)
     * || (Integer.parseInt(date.substring(8)) > 31 ||
     * Integer.parseInt(date.substring(8)) < 1)) {
     * System.out.println("Please enter valid date.");
     * return;
     * }
     * query =
     * "select * from slots where slotno NOT IN (select slotno from slotsbooking where bookingdate=\""
     * + date + "\");";
     * ResultSet rs = stmt.executeQuery(query);
     * System.out.println("Available slots:");
     * System.out.println("Slotno\t     Starttime\tEndtime\t   Price\t       Sport"
     * );
     * while (rs.next()) {
     * System.out.println(" " + rs.getInt(1) + "\t       " + rs.getInt(2) + "\t    "
     * + rs.getInt(3) + "\t   "
     * + rs.getFloat(4) + "\t    " + rs.getString(5));
     * }
     * System.out.println("Enter name to book slot:");
     * bookname = br.readLine();
     * bookname=bookname.toUpperCase();
     * System.out.println("Enter contact number to book slot:");
     * bookphno = br.readLine();
     * System.out.println("Enter the desired slot number to book:");
     * slotno = Integer.parseInt(br.readLine());
     * query =
     * "select slotno from slots where slotno IN (select slotno from slotsbooking where bookingdate=\""
     * + date + "\");";
     * rs = stmt.executeQuery(query);
     * while (rs.next()) {
     * if (slotno == rs.getInt(1)) {
     * System.out.println("Please enter valid slot.");
     * return;
     * }
     * }
     * query = "insert into slotsbooking(slotno,bookingdate,Name,ContactNo) values("
     * + slotno + ",\"" + date
     * + "\",\"" + bookname + "\",\"" + bookphno + "\");";
     * stmt.executeUpdate(query);
     * System.out.println("Slot booked successfully!");
     * con.close();
     * 
     * }
     * catch(SQLException se)
     * {
     * System.out.
     * println("There was an error connecting to the database.Please try again.");
     * }
     * catch(IOException ie)
     * {
     * System.out.
     * println("There was an error while entering data. Please try again.");
     * }
     * catch(ClassNotFoundException ce)
     * {
     * System.out.println("An error has occured.Please try again.");
     * }*/
     
    public void viewBooking();
    /*
     * VIEW A BOOKING
     * BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
     * try
     * {
     * String bookname;
     * int orderno;
     * System.out.println("Enter the name under which slot is booked:");
     * bookname=br.readLine();
     * bookname=bookname.toUpperCase();
     * System.out.println("Enter the order number:");
     * orderno=Integer.parseInt(br.readLine());
     * Class.forName("com.mysql.cj.jdbc.Driver");
     * Connection con =
     * DriverManager.getConnection("jdbc:mysql://localhost:3306/turfbookingsystem",
     * "root",
     * "password");
     * Statement stmt = con.createStatement();
     * String query;
     * query="select * from slotsbooking where orderno="+orderno+" and Name=\""
     * +bookname+"\";";
     * ResultSet rs = stmt.executeQuery(query);
     * if(!rs.next())
     * {
     * System.out.println("Order does not exist. Please try again.");
     * return;
     * }
     * System.out.println("***********RECEIPT***********");
     * query="select orderno,Name,ContactNo,slots.slotno,sportname,startime,endtime,price from slots,slotsbooking where orderno="
     * +orderno+" and Name=\""
     * +bookname+"\" and slots.slotno=(select slotno from slotsbooking where orderno="
     * +orderno+");";
     * rs=stmt.executeQuery(query);
     * while(rs.next())
     * {
     * System.out.println("ORDER NO. : "+rs.getInt(1));
     * System.out.println("NAME : "+rs.getString(2));
     * System.out.println("CONTACT NUMBER : "+rs.getString(3));
     * System.out.println("SLOT NUMBER : "+rs.getInt(4));
     * System.out.println("SPORT : "+rs.getString(5));
     * System.out.println("SLOT TIMINGS:");
     * System.out.println("START TIME : "+rs.getInt(6)+" Hours");
     * System.out.println("END TIME : "+rs.getInt(7)+" Hours");
     * System.out.println("PRICE : Rs."+rs.getFloat(8));
     * System.out.println("---------------");
     * System.out.println("*****************************");
     * }
     * }
     * catch(SQLException se)
     * {
     * System.out.
     * println("There was an error connecting to the database.Please try again.");
     * }
     * catch(IOException ie)
     * {
     * System.out.
     * println("There was an error while entering data. Please try again.");
     * }
     * catch(ClassNotFoundException ce)
     * {
     * System.out.println("An error has occured.Please try again.");
     * }
     */
}

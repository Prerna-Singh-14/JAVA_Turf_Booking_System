package com.turfbooking.admin;
public abstract class AdminAbilities {

    public AdminAbilities()
    {}
    abstract public void slot(int slotno, String sport);

    abstract  public void slot(int slotno);

    abstract public void showProfits();

    abstract public void viewAllBookings();

}

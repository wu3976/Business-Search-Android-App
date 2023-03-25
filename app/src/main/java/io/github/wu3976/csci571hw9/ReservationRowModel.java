package io.github.wu3976.csci571hw9;

public class ReservationRowModel {
    public String serial_num, name, date, time, email;
    public ReservationRowModel(String serial_num, String name, String date,
                               String time, String email) {
        this.serial_num = serial_num;
        this.name = name;
        this.date = date;
        this.time = time;
        this.email = email;
    }
}

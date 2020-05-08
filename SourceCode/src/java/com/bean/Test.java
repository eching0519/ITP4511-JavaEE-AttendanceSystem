/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bean;

import com.db.SystemDB;
import java.sql.ResultSet;

/**
 *
 * @author EChing
 */
public class Test {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/attendance_system";
        String username = "root";
        String pw = "";
        SystemDB db = new SystemDB(url, username, pw);

        int studentId = 180000001;
        String[] names = {"Sunil Puckett", "Mattie Dean", "Sabiha Singleton", "Anthony Kemp", "Gina Dodd", "Kaiden Estes", "Saara Black", "Malak Read", "Shani Woodard", "Wayne Elliott", "Edith Mathews",
            "Andrei Mccormack", "Imaad Mason", "Hudson Schwartz", "Emma Childs", "Chantel Bate", "Eliana Cervantes", "Nikki Carter", "Aiden Golden", "Kathy Franklin", "Bill Bartlett", "Krystal Odonnell",
            "Rylee Currie", "Javan Browne", "Isla Arias", "Brandon-Lee Solis", "Humphrey Mill", "Arian Macgregor", "Kira Lozano", "Janine Spears"};

        for (int i = 0; i < names.length; i++) {
            db.addStudent((studentId + i) + "", (studentId + i) + "", names[i], "IT114105");
        }

    }

}

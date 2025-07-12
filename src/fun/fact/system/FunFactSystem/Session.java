/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fun.fact.system.FunFactSystem;

/**
 *
 * @author Fabiha
 */
public class Session {
    private static String loggedInEmail;

    public static void setEmail(String Email) {
        loggedInEmail = Email;
    }

    public static String getEmail() {
        return loggedInEmail;
    }
}

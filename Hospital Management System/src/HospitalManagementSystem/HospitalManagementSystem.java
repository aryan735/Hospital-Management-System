package HospitalManagementSystem;

import com.mysql.cj.SimpleQuery;
import org.w3c.dom.ls.LSOutput;

import javax.print.Doc;
import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {
    private static final String url ="jdbc:mysql://localhost:3306/Hospital";
    private static final String userName = "root";
    private static final String password = "@aryan@2014";

    public static void main(String[] args) {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(System.in);

        try{
            //connection established with the database
            Connection connection = DriverManager.getConnection(url,userName,password);
            Patient patient = new Patient(connection,scanner);
            while (true) {
            Doctor doctor= new Doctor(connection);
            System.out.println("HOSPITAL MANAGEMENT SYSTEM");
            System.out.println("1. Add Patients");
            System.out.println("2. View Patients");
            System.out.println("3. View Doctors");
            System.out.println("4. Book Appointments");
            System.out.println("5. Exit");


                System.out.print("Enter Your Option : ");
                int option = scanner.nextInt();

                switch (option) {
                    case 1: {
                        // Add patient
                        patient.addPatient();
                        System.out.println();
                        break;
                    }
                    case 2: {
                        // View patient
                        patient.viewPatients();
                        System.out.println();
                        break;
                    }
                    case 3: {
                        // View doctor
                        doctor.viewDoctor();
                        System.out.println();
                        break;
                    }
                    case 4: {
                        // Book appointment
                        bookAppoinment(patient, doctor, connection, scanner);
                        System.out.println();
                        break;
                    }
                    case 5: {
                        // Exit the loop
                        System.out.println("Thank You For Visiting!!!");
                        return;
                    }
                    default: {
                        System.out.println("Please Enter a Valid Option!!!");
                        break;
                    }
                }
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void bookAppoinment(Patient patient, Doctor doctor,Connection connection,Scanner scanner){
        System.out.print("Enter Patient Id :");
        int  patientId = scanner.nextInt();
        System.out.print("Enter Doctor Id :");
        int doctorId  = scanner.nextInt();
        System.out.println("Enter the Appointment Date (DD-MM-YYYY)");
        String appointmentDate = scanner.next();
        if (patient.getPatientById(patientId)&& doctor.getDoctorById(doctorId)){
            if(checkDoctorAvailiblity(doctorId,appointmentDate,connection)){
                String appointmentQuery = "INSERT  INTO appointments(patient_id,doctor_id,appointment_id) VALUES(?,?,?)";
                try{
                    PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery);
                    preparedStatement.setInt(1,patientId);
                    preparedStatement.setInt(2,doctorId);
                    preparedStatement.setString(3,appointmentDate);
                    int affectedRows = preparedStatement.executeUpdate();
                    if (affectedRows>0){
                        System.out.println("Appointment Booked");
                    }else {
                        System.out.println("Failed to book the Appointment");
                    }
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }else {
                System.out.println("Doctor is not available on this date");
            }

        }

    }
    public static boolean checkDoctorAvailiblity(int doctorId,String appointmentDate,Connection connection){
        //cont(*) means how many rows comes
        String query = "SELECT COUNT(*) FROM appointments WHERE doctorId=?,appointmentDate = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,doctorId);
            preparedStatement.setString(2,appointmentDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                int count = resultSet.getInt(1);
                /*so it will check that if a single row is available in db so it means doctor is not available
                if no rows available in db it means doctor is available*/
                if(count == 0){
                  return true;
                }else {
                    return false;
                }
            }
        }catch (SQLException e){
            e.printStackTrace();;
        }
        return false;
    }
}

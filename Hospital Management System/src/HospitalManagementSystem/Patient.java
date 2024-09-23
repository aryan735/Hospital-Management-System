package HospitalManagementSystem;

import java.nio.channels.ScatteringByteChannel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {
    private Connection connection;
    private Scanner scanner;

    public Patient(Connection connection, Scanner scanner){
        this.connection=connection;
        this.scanner=scanner;
    }

    public void addPatient(){
        System.out.print("Enter Patient Name :");
        String name = scanner.next();
        System.out.print("Enter Patient Age :");
        int age = scanner.nextInt();
        System.out.print("Enter Patient Gender :");
        String gender = scanner.next();

        try{
            String query = "INSERT INTO Patients(name,age,gender) VALUES(?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,name);
            preparedStatement.setInt(2,age);
            preparedStatement.setString(3,gender);
            int affectedRows = preparedStatement.executeUpdate();
            if(affectedRows>0){
                System.out.println("Patient Added Successfully");
            }else {
                System.out.println("Failed To Add Patient");
            }
        }catch(SQLException e){
            e.getStackTrace();
        }

    }
     public void viewPatients(){
        String query = "select * from Patients";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            //hold the patient table from database and print the table
            ResultSet resultSet = preparedStatement.executeQuery(query);
            if (!resultSet.isBeforeFirst()) { // isBeforeFirst() returns false if there are no rows
                System.out.println("No patient registered.");
            } else {
                System.out.println("Patients: ");
                System.out.println("+------------+--------------------+-----------+-----------+");
                System.out.println("| Patient Id | Name               | Age       | Gender    |");
                System.out.println("+------------+--------------------+-----------+-----------+");
                while (resultSet.next()) {//it will set a pointer on our data and print the data line by line
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    int age = resultSet.getInt("age");
                    String gender = resultSet.getString("gender");
                    System.out.printf("| %-10s | %-18s | %-9s | %9s |\n", id, name, age, gender);
                    System.out.println("+------------+--------------------+-----------+-----------+");
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
     }
     public boolean getPatientById(int id){
        String query = "SELECT * FROM Patients WHERE id = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){/*it checks that any data exists or not, if any data comes
                                     from the database then it will retuen true and if not then return false*/
                return true;
            }else {
                return false;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
     }

}

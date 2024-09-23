package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Doctor {
    private Connection connection;


    public Doctor(Connection connection){
        this.connection=connection;

    }


    public void viewDoctor(){
        String query = "select * from doctors";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            //hold the patient table from database and print the table
            ResultSet resultSet = preparedStatement.executeQuery(query);
            System.out.println("Doctor: ");
            System.out.println("+------------+--------------------+--------------------+");
            System.out.println("| Doctor Id  | Name               | Specialization     |");
            System.out.println("+------------+--------------------+--------------------+");
            while(resultSet.next()){//it will set a pointer on our data and print the data line by line
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
               String specialization = resultSet.getString("specialization");
                System.out.printf("| %-10s | %-19s| %-18s |\n",id,name,specialization);
                System.out.println("+------------+--------------------+--------------------+");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    public boolean getDoctorById(int id){
        String query = "SELECT * FROM  doctors WHERE id = ?";
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

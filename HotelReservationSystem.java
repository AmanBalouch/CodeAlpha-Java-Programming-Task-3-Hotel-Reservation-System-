import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Scanner;
public class HotelReservationSystem {
    private static String url="jdbc:mysql://127.0.0.1:3306/HOTEL_db";
    private static String user="root";
    private static String pass="Pakistan 123";
    static Scanner input=new Scanner(System.in);
    static Connection connection;
    static Statement statement;
    static ResultSet resultSet;
    static String sql;
    static int reservation_id,verification_code;
    static{
        try{
            connection=DriverManager.getConnection(url,user,pass);
            statement=connection.createStatement();
        }
        catch(Exception e){
        }
    }
    public static void main(String []Args){
        System.out.println("\t\t\t\t\t\t\t\t____________________________________");
        System.out.println("\t\t\t\t\t\t\t\tWelcome to Hotel Reservation System");
        System.out.println("\t\t\t\t\t\t\t\t____________________________________");
        while(true){
            System.out.println("_________________");
            System.out.println("1)Availabe Rooms.\n2)Make Reservation.\n3)View Reservation detail.\n4)Update Reservation with reservation id and verification_code.\n5)Cancel Reservation.\n6)Exit");
            System.out.print("Enter your choice here:");
            String choice=input.nextLine();
            if(choice.equals("1"))
                seeAvailableRooms();
            else if(choice.equals("2"))
                makeReservation();
            else if(choice.equals("3"))      
                viewReservation(1);  //When checker=0 donot take input first time and when checker!=0 take input all time.
            else if(choice.equals("4"))
                updateReservation(); 
            else if(choice.equals(5)){
                while(true){
                    try{
                        System.out.println("_______________________");
                        System.out.print("Enter reservation ID:");
                        reservation_id=input.nextInt();
                        input.nextLine();
                        System.out.print("Enter verification code:");
                        verification_code=input.nextInt();
                        input.nextLine();
                        break;
                    }
                    catch(Exception e){
                        System.out.println(e);
                    }
                }
                cancelReservation(reservation_id,verification_code); 
            }   
            else if(choice.equals("6")){
                exit();
                break;
            }
            else
                System.out.println("Your choice is invalid please enter choice again");     
        }
    }

    public static void  seeAvailableRooms(){
        String selection="";
        while(true){
            System.out.println("______________");
            System.out.println("Enter catagory.\n1)Single.\n2)Couple.\n3)Family.\n4)All");
            System.out.print("Enter your choice here:");
            String choice=input.nextLine();
            if(choice.equals("1")){
                selection="Single";
                break;
            }
            else if(choice.equals("2")){
                selection="Couple";
                break;
            }
            else if(choice.equals("3")){
                selection="Family";
                break;
            }
            else if(choice.equals("4")){
                selection="4";
                break;
            }
            else{
                System.out.println("You entered a wrong choice plz select again");
            }
        } 
        if(selection.equals("4"))
            sql= "SELECT a.room_number, a.catagory FROM ROOMS_DETAIL AS a " +
            "LEFT JOIN RESERVATION_DETAIL AS b ON a.room_number = b.room_number " +
            "WHERE b.room_number IS NULL";
        else    
            sql= "SELECT a.room_number, a.catagory FROM ROOMS_DETAIL AS a " +
            "LEFT JOIN RESERVATION_DETAIL AS b ON a.room_number = b.room_number " +
            "WHERE b.room_number IS NULL AND a.catagory='"+selection+"'";
        try{
            resultSet=statement.executeQuery(sql);
            System.out.println("____________________________");
                System.out.println("Room_number        Catagory");
                System.out.println("____________________________");
            while(resultSet.next()){
                int room_number=resultSet.getInt("room_number");
                String catagory=resultSet.getString("catagory");
                System.out.printf("%-19d%-10s\n",room_number,catagory);
            }
        }
        catch(Exception e){
        }
    }

    public static void makeReservation(){
        try{
            System.out.println("_______________");
            System.out.print("Enter your Name:");
            String name=input.nextLine();
            System.out.print("Enter your contact number:");
            String number=input.nextLine();
            int room_number=takeInputAndCheckRoomDetail();
            System.out.print("Enter the integer code by which you can change or cancel your resevation:");
            verification_code=input.nextInt();
            input.nextLine();
            String payment="";
            while(payment.equals("")){
                System.out.println("_____________________");
                System.out.println("You want to pay with:\n1)easypaisa.\n2)JazzCash.\n3)Bank account.");
                System.out.print("Enter your choice here:");
                String choice=input.nextLine();
                if(choice.equals("1"))
                    payment="EasyPaisa";
                else if(choice.equals("2"))
                    payment="Jazzcash";
                else if(choice.equals("3"))
                    payment="Bank";
                else
                    System.out.println("The choice is incorrect please select again :")   ;         
            }
            sql="INSERT INTO RESERVATION_DETAIL (verification_code, name, contact_number, room_number, payment, date) "+
                "VALUES ("+verification_code+",'"+name+"','"+number+"',"+room_number+",'"+payment+"',NOW())"; 
            statement.executeUpdate(sql);
            sql="SELECT reservation_id,date FROM RESERVATION_DETAIL "+
                "WHERE room_number="+room_number;
            resultSet=statement.executeQuery(sql);    
            resultSet.next();
            reservation_id=resultSet.getInt("reservation_id");
            Timestamp date = resultSet.getTimestamp("date");
            System.out.println("Reservation done the details are given below:");
            System.out.println("_______________________________________________________________________________________________________________________");
            System.out.println("Reservation_id   Verification_code   Name                  Contact_Number   Room_number   Payment_Detail   Date");
            System.out.printf("%-17d%-20d%-22s%-17s%-14d%-17s%s%n",reservation_id,verification_code,name,number,room_number,payment,date);
            System.out.println("_______________________________________________________________________________________________________________________");
        }
        catch(Exception e){
            System.out.println(e);
            makeReservation();
        }
    }

    public static int takeInputAndCheckRoomDetail(){
        int room_number=0;
        while(true){
            try {
                System.out.print("Enter room number:");
                room_number=input.nextInt();
                input.nextLine();
                sql="SELECT Count(room_number) FROM ROOMS_DETAIL "+
                    "WHERE room_number="+room_number;
                resultSet=statement.executeQuery(sql);
                resultSet.next();
                if(resultSet.getInt("Count(room_number)")==1){
                    sql="SELECT Count(room_number) FROM RESERVATION_DETAIL "+
                    "WHERE room_number="+room_number;
                    resultSet=statement.executeQuery(sql);
                    resultSet.next();
                    if(resultSet.getInt("Count(room_number)")==0)
                        break;
                    else
                        System.out.println("This room is already booked plz select any other room.");
                } 
                else{  
                    System.out.println("This room number is not exist in our hotel please enter room number again");
                    continue;
                }   
            } catch (Exception e) {
                System.out.println(e);
                break;
            }  
        }
        return room_number;
    }

    public static Boolean viewReservation(int checker){
        try{
            int i=1;
            while(i<=4){
                if(checker!=0){
                    System.out.println("_______________________");
                    System.out.print("Enter reservation ID:");
                    reservation_id=input.nextInt();
                    input.nextLine();
                    System.out.print("Enter verification code:");
                    verification_code=input.nextInt();
                    input.nextLine();
                }
                checker=1;
                sql="SELECT * FROM RESERVATION_DETAIL "+
                    "WHERE reservation_id="+reservation_id+" AND verification_code="+verification_code;
                resultSet=statement.executeQuery(sql);
                if(resultSet.isBeforeFirst()){
                    resultSet.next();
                    String name=resultSet.getString("name");
                    String number=resultSet.getString("contact_number");
                    int room_number=resultSet.getInt("room_number");
                    String payment=resultSet.getString("payment");
                    Timestamp date = resultSet.getTimestamp("date");
                    System.out.println("The reservation Details Are given below:");
                    System.out.println("_______________________________________________________________________________________________________________________");
                    System.out.println("Reservation_id   Verification_code   Name                  Contact_Number   Room_number   Payment_Detail   Date");
                    System.out.printf("%-17d%-20d%-22s%-17s%-14d%-17s%s%n",reservation_id,verification_code,name,number,room_number,payment,date);
                    System.out.println("_______________________________________________________________________________________________________________________");
                    return true;
                }
                else{
                    System.out.println("Reservation id or verification code is wrong plz enter again."); 
                }   
                i++;
            }
            if(i==5){
                System.out.println("You enter wrong id or verification_code many time ,now you are out of attemps.");
                return false;
            }
        }
        catch(Exception e){
            System.out.println(e);
            viewReservation(1);
        }
        return null;
    }

    public static void updateReservation(){
        try{
            if(viewReservation(1)){
                while(true){
                    System.out.println("___________________");
                    System.out.println("You want to change:\n1)Room_number.\n2)Verification_Code.\n3)Name.\n4)Contact_number.\n5)All information.");
                    System.out.print("Enter your choice here:");
                    String choice=input.nextLine();
                    if(choice.equals("1")){
                        int room_number=takeInputAndCheckRoomDetail();
                        sql="UPDATE RESERVATION_DETAIL "+
                            "SET room_number="+room_number+
                            " Where reservation_id="+reservation_id;
                        break;
                    }
                    else if(choice.equals("2")){
                        System.out.print("Enter verification_code:");
                        verification_code=input.nextInt();
                        sql="UPDATE RESERVATION_DETAIL "+
                            "SET verification_code="+verification_code+
                            " Where reservation_id="+reservation_id;
                        break;    
                    }
                    else if(choice.equals("3")){
                        System.out.print("Enter your Name:");
                        String name=input.nextLine();
                        sql="UPDATE RESERVATION_DETAIL "+
                            "SET name='"+name+"'"+
                            " Where reservation_id="+reservation_id;
                        break;
                    }
                    else if(choice.equals("4")){
                        System.out.print("Enter your contact number:");
                        String number=input.nextLine();
                        sql="UPDATE RESERVATION_DETAIL "+
                            "SET contact_number='"+number+"'"+
                            " Where reservation_id="+reservation_id;
                        break;    
                    }
                    else if(choice.equals("5")){
                        cancelReservation(reservation_id,verification_code);
                        makeReservation();    
                        return;
                    }
                    else
                        System.out.println("You entered a wrong input please enter choice again:");
                }   
                statement.executeUpdate(sql);
                System.out.println("Reservation Updated Succesfully.");
                viewReservation(0);
            }
        }
        catch(Exception e){};
    }

    public static void cancelReservation(int reservation_id,int verificatoion_code){
        sql="DELETE FROM RESERVATION_DETAIL "+
            "WHERE reservation_id = "+reservation_id+
            " AND verification_code="+verification_code;
        try{    
            statement.executeUpdate(sql);
        }
        catch(Exception e ){};
    }

    public static void exit(){
        System.out.print("Exiting");
        for(int i=0;i<=4;i++){
            try {
                Thread.sleep(1000);
                System.out.print(" .");
            }catch(Exception e){}  
        }
    }

}

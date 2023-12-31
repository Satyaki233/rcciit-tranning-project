package com.rcciit.project.backend.repository;


import com.rcciit.project.backend.POJO.Member;
import com.rcciit.project.backend.connect.DatabaseConnection;
import org.springframework.stereotype.Repository;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MemberRepository {


    public Member registerMember(Member member) throws SQLException {

        String query="insert into members values('"+member.getId()+"','"+member.getName()+"','"+member.getEmail()+"','"+member.getPhoneNumber()+"','"+member.getRole()+"')";
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement(query);
            int i = ps.executeUpdate();
            System.out.println(i);
            if(i>0){

                System.out.println("member inserted !");

                return member;
            }

        } catch (Exception e) {
            System.out.println("Not inserted");
            throw new SQLException("message");
        } finally {

            if(ps != null)ps.close();
            if(con != null)con.close();
        }
        return new Member();
    }
    
    
    //for cheack the login value
    public List<Member> loginMember(Member member) throws SQLException {
    	List<Member> membersList = new ArrayList<>();


    	String query = "select * from members where name=? and phone_number=?";
    	
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        

        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, member.getName());
            ps.setString(2,member.getPhoneNumber());
            rs = ps.executeQuery();
            
            while(rs.next()){
                Member mbr = new Member();
                mbr.setId(rs.getString("id"));
                mbr.setName(rs.getString("name"));
                mbr.setEmail(rs.getString("email"));
                mbr.setPhoneNumber(rs.getString("phone_number"));
                mbr.setRole(rs.getString("role"));

                membersList.add(mbr);
            }



        } catch (Exception e) {
        	System.out.println(e);
            throw new SQLException("message");
        } finally {
            if(ps != null)ps.close();
            if(con != null)con.close();
        }
        return membersList;
    }


    public List<Member> getMemberByRole(String role) throws SQLException{
        String query = null;
        if(!role.equalsIgnoreCase("ALL")) {
            query = "select * from members where role='"+role+"'";
        }else{
            query = "select * from members order by role";
        }



        List<Member> membersList = new ArrayList<>();

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            int i =this.getTableSizeByRole(role);
            if(i==0) return membersList;
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while(rs.next()){
                Member mbr = new Member();
                mbr.setId(rs.getString("id"));
                mbr.setName(rs.getString("name"));
                mbr.setEmail(rs.getString("email"));
                mbr.setPhoneNumber(rs.getString("phone_number"));
                mbr.setRole(rs.getString("role"));

                membersList.add(mbr);
            }
        } catch (Exception e) {
            throw new SQLException("Error in sql syntax");
        } finally {
            if(rs!=null){rs.close();}
            if(ps!=null){ps.close();}
            if(con!=null){con.close();}
        }

        return membersList;
    }



    public int getTableSizeByRole(String role) throws Exception{
        String query = null;
        if(role.equals("ALL")){
            query = "select count(*) as count from members";
        }
        else{
            query = "select count(*) as count from members where role='"+role+"'";
        }

        int i=0;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        con = DatabaseConnection.getConnection();
        ps = con.prepareStatement(query);
        rs=ps.executeQuery();
        rs.next();
        i=rs.getInt(1);
        if(rs!=null){rs.close();}
        if(ps!=null){ps.close();}
        if(con!=null){con.close();}
        return i;


    }

    public int getAudinece() throws Exception{
        return this.getTableSizeByRole("AUDIENCE");
    }
}

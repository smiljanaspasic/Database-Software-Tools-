/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.etf.sab.student;

import java.util.List;
import rs.etf.sab.operations.CityOperations;
import java.sql.*;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Smiljana
 */
public class ss140588_CityOperations implements CityOperations {

    @Override
    public int insertCity(String name, String postalCode) {
       Connection conn = DB.getInstance().getConnection();
        String insert = "insert into dbo.Grad(Naziv, PostBr) VALUES(?, ?)";

        try(PreparedStatement ps = conn.prepareStatement(insert);) {
   
            ps.setString(1, name);
            ps.setString(2, postalCode);

            ps.executeUpdate();

            try (PreparedStatement select = conn.prepareStatement
            ("select MAX(IdGrad) as Max from dbo.Grad where PostBr = ?")){				
            select.setString(1, postalCode);
            ResultSet rs = select.executeQuery();
            rs.next();
            Integer id = rs.getInt("Max");

            if(id > 0) return id;
            else return -1;
            }
            catch (SQLException e) {
                return -1;
            }
        } catch (SQLException e) {
            return -1;
        } 
    }
    

   @Override
    public int deleteCity(String... names) {
       int cntrizbrisani = 0;
        try{
            Connection conn = DB.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement("delete from dbo.Grad where Naziv = ?");
            for(int i = 0; i < names.length; i++){
                ps.setString(1, names[i]);
                cntrizbrisani += ps.executeUpdate();
            }
            return cntrizbrisani;
        }
        catch (SQLException ex) {
            Logger.getLogger(ss140588_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }
    

    @Override
    public boolean deleteCity(int idCity) {
        Connection conn = DB.getInstance().getConnection();
        String delete = "delete from dbo.Grad where IdGrad = ?";
        try(PreparedStatement ps = conn.prepareStatement(delete)){
            ps.setInt(1, idCity);
            return ps.executeUpdate()==1;
        }catch (SQLException ex) {
            Logger.getLogger(ss140588_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public List<Integer> getAllCities() {
        Connection conn = DB.getInstance().getConnection();
        List<Integer> svigradovi = new LinkedList<>();
        String select = "select IdGrad from Grad";
        try (
                PreparedStatement ps = conn.prepareStatement(select);
                ResultSet rs = ps.executeQuery();) {
            while (rs.next()) {
                svigradovi.add(rs.getInt(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return svigradovi;
    }
    
    

    
}

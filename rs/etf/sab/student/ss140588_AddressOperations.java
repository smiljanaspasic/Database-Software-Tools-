/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.etf.sab.student;
import java.util.List;
import java.sql.*;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.AddressOperations;
/**
 *
 * @author Smiljana
 */
public class ss140588_AddressOperations implements AddressOperations {

    @Override
    public int insertAddress(String street, int number, int cityId, int xCord, int yCord) {
        Connection conn = DB.getInstance().getConnection();
        String insert = "insert into dbo.Adresa (IdGrad, Ulica, Broj, X, Y) values(?,?,?,?,?)";
        try(PreparedStatement ps = conn.prepareStatement(insert);){
            ps.setInt(1, cityId);
            ps.setString(2, street);
            ps.setInt(3, number);
            ps.setInt(4, xCord);
            ps.setInt(5, yCord);
            
            ps.executeUpdate();

            try (PreparedStatement select = conn.prepareStatement
            ("select MAX(IdAdresa) as Max FROM dbo.Adresa")){				
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
        } }
    

    @Override
    public int deleteAddresses(String name, int number) {
        Connection conn = DB.getInstance().getConnection();
        String delete="delete from dbo.Adresa where Ulica = ? and Broj = ?";
        try(PreparedStatement ps = conn.prepareStatement(delete);){
            ps.setString(1, name);
            ps.setInt(2, number);
            return ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_AddressOperations.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    @Override
    public boolean deleteAdress(int idAddress) {
       Connection conn = DB.getInstance().getConnection();
       String delete="delete from dbo.Adresa where IdAdresa = ?";
       try{
           
            PreparedStatement ps = conn.prepareStatement(delete);
            ps.setInt(1, idAddress);
            return ps.executeUpdate()==1;
        }
        catch (SQLException ex) {
            Logger.getLogger(ss140588_AddressOperations.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public int deleteAllAddressesFromCity(int idCity) {
         Connection conn = DB.getInstance().getConnection();
         String delete="delete from dbo.Adresa where IdGrad = ?";
         try (PreparedStatement ps = conn.prepareStatement(delete);) {
          ps.setInt(1, idCity);
            return ps.executeUpdate();
         }  catch (SQLException ex) {
            Logger.getLogger(ss140588_AddressOperations.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    @Override
    public List<Integer> getAllAddresses() {
        Connection conn = DB.getInstance().getConnection();
        String select = "select IdAdresa from Adresa";
        List<Integer> lista = new LinkedList<>();
         try{
            PreparedStatement ps = conn.prepareStatement(select);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                lista.add(rs.getInt("IdAdresa"));
            }
            return lista;
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_AddressOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }
    

    @Override
    public List<Integer> getAllAddressesFromCity(int idCity) {
        Connection conn = DB.getInstance().getConnection();
        List<Integer> lista = new LinkedList<>();
        String select = "select IdAdresa from Adresa where IdGrad = ?";
        try{
            PreparedStatement ps = conn.prepareStatement(select);
            ps.setInt(1, idCity);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                lista.add(rs.getInt("IdAdresa"));
            }
            
            if (lista.isEmpty()) return null;
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_AddressOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }
    
}

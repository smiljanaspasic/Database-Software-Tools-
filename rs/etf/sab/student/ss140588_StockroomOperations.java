/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.etf.sab.student;
import rs.etf.sab.operations.StockroomOperations;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Smiljana
 */
public class ss140588_StockroomOperations implements StockroomOperations {

    @Override
    public int insertStockroom(int address) {
         Connection conn = DB.getInstance().getConnection();
        String provera = "declare @gradprovera int; " +
                        "select @gradprovera = (select IdGrad from Adresa where IdAdresa = ?)\n" +
                        "select count(*) from dbo.Magacin M join dbo.Adresa A on M.IdAdresa = A.IdAdresa\n" +
                        "where A.IdGrad = @gradprovera\n";
        try(PreparedStatement ps = conn.prepareStatement(provera);) {
           
            ps.setInt(1, address);
            ResultSet r = ps.executeQuery();
            r.next();
            Integer br = r.getInt(1);
            if(br > 0) return -1;
    
            String insert = "insert into dbo.Magacin (IdAdresa) values (?)";

            try(PreparedStatement stmt = conn.prepareStatement(insert);) {
                
                stmt.setInt(1, address);
                if (stmt.executeUpdate() == 0) return -1;


                try (PreparedStatement select = conn.prepareStatement
                ("SELECT MAX(IdMagacin) AS Max FROM dbo.Magacin ")){				
                ResultSet rs = select.executeQuery();
                rs.next();
                Integer id = rs.getInt("Max");

                if(id > 0) return id;
                else return -1;
                }
                catch (SQLException  ex) {
                Logger.getLogger(ss140588_StockroomOperations.class.getName()).log(Level.SEVERE, null, ex);
                return -1;
                }
            } catch (SQLException  ex) {
                Logger.getLogger(ss140588_StockroomOperations.class.getName()).log(Level.SEVERE, null, ex);
                return -1;
            }
        } catch (SQLException  ex) {
            Logger.getLogger(ss140588_StockroomOperations.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    @Override
    public boolean deleteStockroom(int idStockroom) {
         Connection conn = DB.getInstance().getConnection();
         try(PreparedStatement ps = conn.prepareStatement("delete from dbo.Magacin where IdMagacin = ?");){
            
            
            ps.setInt(1, idStockroom);
            return ps.executeUpdate()==1;
        }
        catch (SQLException ex) {
            Logger.getLogger(ss140588_StockroomOperations.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public int deleteStockroomFromCity(int idCity) {
        Connection conn = DB.getInstance().getConnection();
        String query="select IdMagacin from Magacin\n" +
                      "where IdAdresa = (\n" +
                      "select top(1) IdAdresa from Adresa A \n" +
                      "where IdGrad = ? and IdAdresa = A.IdAdresa\n" +
                       ")";
            try(PreparedStatement ps = conn.prepareStatement(query)){
            
            ps.setInt(1, idCity);
            ResultSet rs = ps.executeQuery();
            if (rs.next() == false) return -1;
            Integer id = rs.getInt("IdMagacin");
            query="delete from Magacin where IdMagacin = ?";
            try(PreparedStatement stmt = conn.prepareStatement(query);){
                
                stmt.setInt(1, id);
                return ((stmt.executeUpdate()==1)?id:-1);
                
            }
            catch (SQLException ex) {
                Logger.getLogger(ss140588_StockroomOperations.class.getName()).log(Level.SEVERE, null, ex);
                return -1;
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(ss140588_StockroomOperations.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        } }

    @Override
    public List<Integer> getAllStockrooms() {
       Connection conn = DB.getInstance().getConnection();
       String select="select IDMagacin from dbo.Magacin";
       List<Integer> magacini = new LinkedList<>();
        try (
                PreparedStatement ps = conn.prepareStatement(select);
                ResultSet rs = ps.executeQuery();) {
            while (rs.next()) {
                magacini.add(rs.getInt(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_StockroomOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return magacini;
    }
    
    
}

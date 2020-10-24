/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.etf.sab.student;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.GeneralOperations;

/**
 *
 * @author Smiljana
 */
public class ss140588_GeneralOperations implements GeneralOperations {

    @Override
    public void eraseAll() {
         try {
            Connection con = DB.getInstance().getConnection();
            String erase = 
			        "delete  from Administrator\n" +
					"delete  from RanijeVozio\n" +
					"delete  from Kargo\n" +   
					"delete  from KurirZahtev\n" +
					"delete  from Magacin\n" +
					"delete  from [Plan]"+	               
                    "delete from Vozi\n" +
                    "delete  from Vozilo\n" +
                    "delete  from Kurir\n" +
					"delete  from Paket\n" +
                    "delete  from Korisnik\n" +   
                    "delete  from  Adresa\n" +
                    "delete from Grad\n"   ;
            PreparedStatement ps  = con.prepareStatement(erase);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}

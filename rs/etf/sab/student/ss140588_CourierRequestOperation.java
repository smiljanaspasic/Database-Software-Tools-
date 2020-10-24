/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.etf.sab.student;
import java.util.List;
import java.sql.*;
import java.util.LinkedList;
import rs.etf.sab.operations.CourierRequestOperation;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Smiljana
 */
public class ss140588_CourierRequestOperation implements CourierRequestOperation {

    @Override
    public boolean insertCourierRequest(String userName, String driverLicenceNumber) {
        Connection conn = DB.getInstance().getConnection();
        String insert = 
            "if not exists (select * from Kurir where IdKorisnik = (select IdKorisnik from Korisnik where KorIme = ?)) " +
            "begin " +
            "insert into KurirZahtev(IdKorisnik, BrVozackeDozvole) " +
            "values ((select IdKorisnik from Korisnik where KorIme = ?), ?) " +
            "end";

        try(PreparedStatement ps = conn.prepareStatement(insert);) {
            
            ps.setString(1, userName);
            ps.setString(2, userName);
            ps.setString(3, userName);

            return ps.executeUpdate()==1;
            
            
        } catch (SQLException e) {
            return false;
        } 
    }

    @Override
    public boolean deleteCourierRequest(String userName) {
        Connection conn = DB.getInstance().getConnection();
        String insert="select IdKorisnik from Korisnik where KorIme = ?";
         try(PreparedStatement ps = conn.prepareStatement(insert);) {
             ps.setString(1, userName);
              return ps.executeUpdate()==1;
         
         }catch (SQLException e) {
            return false;
    } }

    @Override
    public boolean changeDriverLicenceNumberInCourierRequest(String userName, String licencePlateNumber) {
        Connection conn = DB.getInstance().getConnection();
        String query = "update KurirZahtev \n" +
                                "set BrVozackeDozvole = ? \n" +
                                "where IdKorisnik = ( \n" +
                                "select IdKorisnik from Korisnik \n" +
                                "where KorIme = ?)";
        try(PreparedStatement ps = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);) {
            ps.setString(2, userName);
            ps.setString(1, licencePlateNumber);
            return ps.executeUpdate()==1;
        }catch (SQLException  ex) {
            Logger.getLogger(ss140588_CourierRequestOperation.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public List<String> getAllCourierRequests() {
      Connection conn = DB.getInstance().getConnection();
       List<String> kuriri = new LinkedList<>();
       try(PreparedStatement ps = conn.prepareStatement("select KorIme from KurirZahtev KZ join Korisnik K on KZ.IdKorisnik = K.IdKorisnik");
            ResultSet rs = ps.executeQuery();) {
           while(rs.next()){
        kuriri.add(rs.getString("KorIme")); }
       }catch (SQLException  ex) {
            Logger.getLogger(ss140588_CourierRequestOperation.class.getName()).log(Level.SEVERE, null, ex);
           
        }
       return kuriri;
    }

    @Override
    public boolean grantRequest(String userName) {
        String brojvozacke;
        int korisnik=-1;
        Connection conn = DB.getInstance().getConnection();
        String query = "select IdKorisnik from Korisnik where KorIme = ?";
        try (PreparedStatement ps = conn.prepareStatement(query);) {
            ps.setString(1, userName);
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()) {
                    korisnik= rs.getInt(1);
                }
            } catch (SQLException ex) {
                Logger.getLogger(ss140588_CourierRequestOperation.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_CourierRequestOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (korisnik == -1) {
            return false;
        }

        query = "select * from KurirZahtev where IdKorisnik = ?";
        try (PreparedStatement ps = conn.prepareStatement(query);) {
            ps.setInt(1, korisnik);
            try (ResultSet rs = ps.executeQuery();) {
                if (!rs.next()) {
                    return false;
                } else {
                    brojvozacke = rs.getString(1);
                }
            } catch (SQLException ex) {
                Logger.getLogger(ss140588_CourierRequestOperation.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_CourierRequestOperation.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

        query = "delete from KurirZahtev where IdKorisnik = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query);) {
            stmt.setInt(1, korisnik);
            stmt.execute();
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
       query = "insert into Kurir (IdKorisnik, BrVozackeDozvole, BrIsporucenihPaketa, Profit, Status) VALUES (?, ?, 0, 0.000, 0)";
        try (PreparedStatement ps1 = conn.prepareStatement(query);) {
            ps1.setInt(1, korisnik);
            ps1.setString(2, brojvozacke);
            ps1.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_CourierRequestOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.etf.sab.student;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.DriveOperation;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.DriveOperation;
/**
 *
 * @author Smiljana
 */
public class ss140588_DriveOperation implements DriveOperation {

    @Override
    public boolean planingDrive(String courierUsername) {
        int idkurir=-1;
        Connection conn = DB.getInstance().getConnection();
        String query = "select IdKorisnik from Korisnik where KorIme = ?";
        try (PreparedStatement ps = conn.prepareStatement(query);) {
            ps.setString(1, courierUsername);
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()) {
                    idkurir= rs.getInt(1);
                }
            } catch (SQLException ex) {
                Logger.getLogger(ss140588_DriveOperation.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_DriveOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(idkurir==-1) {return false;}
        boolean jekurir=false;
        query = "select * from Kurir where IdKorisnik = ?";
        try (PreparedStatement ps = conn.prepareStatement(query);) {
            ps.setInt(1, idkurir);
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()) {
                    jekurir=true;
                }
            } catch (SQLException ex) {
                Logger.getLogger(ss140588_DriveOperation.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_DriveOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(jekurir==false) {return false;}
        boolean dalivozi=false;
        query = "select * from Vozi where IdKorisnik = ?";
        try (PreparedStatement ps = conn.prepareStatement(query);) {
            ps.setInt(1, idkurir);
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()) {
                    dalivozi=true;
                }
            } catch (SQLException ex) {
                Logger.getLogger(ss140588_DriveOperation.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_DriveOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(dalivozi==false) {return false;}
        int idgrad=-1;
        query
                = "select Adresa.IdGrad\n"
                + "from Korisnik join Adresa ON (Korisnik.IdAdresa = Adresa.IdAdresa)\n"
                + "where Korisnik.IdKorisnik = ?";
        try (PreparedStatement ps = conn.prepareStatement(query);) {
            ps.setInt(1, idkurir);
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()) {
                    idgrad=rs.getInt(1);
                }
            } catch (SQLException ex) {
                Logger.getLogger(ss140588_DriveOperation.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_DriveOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(idgrad==-1) {return false;}
        int idvozila=-1;
        query
                = " select IdVozilo\n"
                + " from Vozilo\n"
                + " where\n"
                + "    Slobodno = 1 AND\n"
                + "    IdMagacin =\n"
                + "    (select Adresa.IdAdresa\n"
                + "    from Magacin join Adresa ON (Magacin.IdAdresa = Adresa.IdAdresa)\n"
                + "    where Adresa.IdGrad = ?) ";

        try (PreparedStatement ps = conn.prepareStatement(query);) {
            ps.setInt(1, idvozila);
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()) {
                    idvozila= rs.getInt(1);
                }
            } catch (SQLException ex) {
                Logger.getLogger(ss140588_DriveOperation.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_DriveOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(idvozila==-1) { return false; }
        int adresamagacina=-1;
        query = "select Magacin.IdAdresa from Magacin join Adresa ON (Magacin.IdAdresa = Adresa.IdAdresa) where Adresa.IdGrad = ?";
        try (PreparedStatement ps = conn.prepareStatement(query);) {
            ps.setInt(1, idgrad);
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()) {
                    adresamagacina= rs.getInt(1);
                } 
            } catch (SQLException ex) {
                Logger.getLogger(ss140588_DriveOperation.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_DriveOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(adresamagacina==-1) return false;
        int trenutnaadr=adresamagacina;
        int trenutnigrad=idgrad;
        double kapacitet=-1;
        query = "select Kapacitet from Vozilo where IdVozilo = ?";
        try (PreparedStatement ps = conn.prepareStatement(query);) {
            ps.setInt(1, idvozila);
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()) {
                    kapacitet= rs.getBigDecimal(1).doubleValue();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ss140588_DriveOperation.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_DriveOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(kapacitet==-1) {return false;}
        double preostalikapacitet=kapacitet;
        return true;
        
    }

    @Override
    public int nextStop(String courierUsername) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Integer> getPackagesInVehicle(String courierUsername) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

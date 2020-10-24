/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.etf.sab.student;
import java.math.BigDecimal;
import java.util.List;
import java.sql.*;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.CourierOperations;
/**
 *
 * @author Smiljana
 */
public class ss140588_CourierOperations implements CourierOperations {

    @Override
    public boolean insertCourier(String courierUserName, String driverLicenceNumber) {
         Connection conn = DB.getInstance().getConnection();
         int idkurira=-1;
         String query = "select IdKorisnik from Korisnik where KorIme = ?";
        try (PreparedStatement ps = conn.prepareStatement(query);) {
            ps.setString(1, courierUserName);
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()) {
                    idkurira=rs.getInt(1);
                }
            } catch (SQLException ex) {
                Logger.getLogger(ss140588_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(idkurira==-1) { return false;  }
        query = "select * from Kurir where IdKorisnik = ? OR BrVozackeDozvole = ?";
        try (PreparedStatement ps1 = conn.prepareStatement(query);) {
            ps1.setInt(1, idkurira);
            ps1.setString(2, driverLicenceNumber);
            try (ResultSet rs = ps1.executeQuery();) {
                if (rs.next()) {
                    return false;
                }
            } catch (SQLException ex) {
                Logger.getLogger(ss140588_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

        query = "insert into Kurir (IdKorisnik, BrVozackeDozvole, BrIsporucenihPaketa, Profit, [Status]) VALUES (?, ?, 0, 0.000, 0 )";
        try (PreparedStatement ps1 = conn.prepareStatement(query);) {
            ps1.setInt(1, idkurira);
            ps1.setString(2, driverLicenceNumber);
            ps1.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public boolean deleteCourier(String courierUserName) {
        Connection conn = DB.getInstance().getConnection();
         int idkurira=-1;
         String query = "select IdKorisnik from Korisnik where KorIme = ?";
        try (PreparedStatement ps = conn.prepareStatement(query);) {
            ps.setString(1, courierUserName);
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()) {
                    idkurira=rs.getInt(1);
                }
            } catch (SQLException ex) {
                Logger.getLogger(ss140588_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(idkurira==-1) { return false;  }
        boolean jestekurir=false;
        query = "select * from Kurir where IdKorisnik = ?";
        try (PreparedStatement ps = conn.prepareStatement(query);) {
            ps.setInt(1, idkurira);
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()) {
                    jestekurir=true;
                }
            } catch (SQLException ex) {
                Logger.getLogger(ss140588_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(jestekurir==false) { return false; }
        query = "delete from Kurir where IdKorisnik = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query);) {
            stmt.setInt(1, idkurira);
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public List<String> getCouriersWithStatus(int statusOfCourier) {
       if (statusOfCourier < 0 || statusOfCourier > 1) {
            return null;
        }
        Connection conn = DB.getInstance().getConnection();
        String query = "select Korisnik.KorIme from Kurir join Korisnik on (Kurir.IdKorisnik=Korisnik.IdKorisnik) where Kurir.Status = ?";
        List<String> kuriri = new LinkedList<>();
        try (PreparedStatement ps = conn.prepareStatement(query);) {
            ps.setInt(1, statusOfCourier);
            try (ResultSet rs = ps.executeQuery();) {
                while (rs.next()) {
                    kuriri.add(rs.getString(1));
                }
            } catch (SQLException ex) {
                Logger.getLogger(ss140588_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return kuriri;
    }

    @Override
    public List<String> getAllCouriers() {
        Connection conn = DB.getInstance().getConnection();
        String query = "select Korisnik.KorIme from Kurir join Korisnik ON (Kurir.IdKorisnik=Korisnik.IdKorisnik) ORDER BY Profit DESC";
        List<String> kuriri = new LinkedList<>();
        try (
                PreparedStatement ps = conn.prepareStatement(query);
                ResultSet rs = ps.executeQuery();) {
            while (rs.next()) {
                kuriri.add(rs.getString(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return kuriri;
    }

    @Override
    public BigDecimal getAverageCourierProfit(int numberOfDeliveries) {
        Connection conn = DB.getInstance().getConnection();
        BigDecimal bd;
        if (numberOfDeliveries == -1) {
           String query = "select AVG(Profit) from Kurir";
            try (
                    PreparedStatement ps = conn.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();) {
                if (rs.next()) {
                    bd = rs.getBigDecimal(1);
                    if (bd != null) {
                        return bd;
                    } else {
                        return new BigDecimal(0);
                    }
                } else {
                    return new BigDecimal(0);
                }
            } catch (SQLException ex) {
                Logger.getLogger(ss140588_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
           String query = "select AVG(Profit) FROM Kurir WHERE BrIsporucenihPaketa = ?";
            try (PreparedStatement ps = conn.prepareStatement(query);) {
                ps.setInt(1, numberOfDeliveries);
                try (ResultSet rs = ps.executeQuery();) {
                    if (rs.next()) {
                        bd = rs.getBigDecimal(1);
                        if (bd != null) {
                            return bd;
                        } else {
                            return new BigDecimal(0);
                        }
                    } else {
                        return new BigDecimal(0);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(ss140588_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (SQLException ex) {
                Logger.getLogger(ss140588_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
    
    
}

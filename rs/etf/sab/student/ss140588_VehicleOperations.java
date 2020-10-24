/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.etf.sab.student;
import java.math.BigDecimal;
import rs.etf.sab.operations.VehicleOperations;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Smiljana
 */
public class ss140588_VehicleOperations implements VehicleOperations {

    @Override
    public boolean insertVehicle(String licencePlateNumber, int fuelType, BigDecimal fuelConsumtion, BigDecimal capacity) {
       Connection conn = DB.getInstance().getConnection();
       String query = "select * from Vozilo where RegBroj = ?";
       try (PreparedStatement ps = conn.prepareStatement(query);) {
            ps.setString(1, licencePlateNumber);
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()) {
                    return false;
                }
            } catch (SQLException ex) {
                Logger.getLogger(ss140588_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
       if (fuelType != 0 && fuelType != 1 && fuelType != 2) {
            return false;
        }
      query = "insert into Vozilo (RegBroj, TipGoriva, Potrosnja, Kapacitet, Slobodno, IdMagacin) VALUES (?, ?, ?, ?, 1, NULL)"; // slobodno: 1-jeste(u magacinu je) 0-nije(nije u magacinu)
        try (PreparedStatement stmt = conn.prepareStatement(query);) {
            stmt.setString(1, licencePlateNumber);
            stmt.setInt(2, fuelType);
            stmt.setBigDecimal(3, fuelConsumtion);
            stmt.setBigDecimal(4, capacity);
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public int deleteVehicles(String... licencePlateNumbers) {
        Connection conn = DB.getInstance().getConnection();
         String delete = "delete from Vozilo where RegBroj = ?";
        int obrisani = 0;
         try( PreparedStatement ps = conn.prepareStatement(delete);) {
            for (String licencePlateNumber : licencePlateNumbers ) {
                ps.setString(1, licencePlateNumber);
                obrisani += ps.executeUpdate();
            } 
           
        } catch (SQLException e) {
          
        }
         return obrisani;
    }

    @Override
    public List<String> getAllVehichles() {
       Connection conn = DB.getInstance().getConnection();
        String select = "select RegBroj from Vozilo";
        List<String> vozila = new LinkedList<>();
   try (
                PreparedStatement ps = conn.prepareStatement(select);
                ResultSet rs = ps.executeQuery();) {
            while (rs.next()) {
                vozila.add(rs.getString("RegBroj"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return vozila;
    }

    @Override
    public boolean changeFuelType(String licensePlateNumber, int fuelType) {
         Connection con = DB.getInstance().getConnection();
         String update = "update Vozilo \n" +
                        "set TipGoriva = ? \n" +
                        "where RegBroj = ?  and Slobodno = 1 and IdMagacin is not null";
        try(PreparedStatement ps = con.prepareStatement(update);) {
                   
            ps.setInt(1, fuelType);
            ps.setString(2, licensePlateNumber);
            return ps.executeUpdate()==1;
            
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean changeConsumption(String licensePlateNumber, BigDecimal fuelConsumption) {
            Connection con = DB.getInstance().getConnection();
         String update = "update Vozilo \n" +
                        "set Potrosnja = ? \n" +
                        "where RegBroj = ?  and Slobodno = 1 and IdMagacin is not null";
        try(PreparedStatement ps = con.prepareStatement(update);) {
                   
            ps.setBigDecimal(1, fuelConsumption);
            ps.setString(2, licensePlateNumber);
            return ps.executeUpdate()==1;
            
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean changeCapacity(String licensePlateNumber, BigDecimal capacity) {
        Connection con = DB.getInstance().getConnection();
         String update = "update Vozilo \n" +
                        "set Kapacitet = ? \n" +
                        "where RegBroj = ?  and Slobodno = 1 and IdMagacin is not null";
        try(PreparedStatement ps = con.prepareStatement(update);) {
                   
            ps.setBigDecimal(1, capacity);
            ps.setString(2, licensePlateNumber);
            return ps.executeUpdate()==1;
            
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean parkVehicle(String licensePlateNumber, int idStockroom) {
        Connection conn = DB.getInstance().getConnection();
        int idvozila=-1;
        String query = "select IdVozilo from Vozilo where RegBroj = ?";
        try (PreparedStatement ps = conn.prepareStatement(query);) {
            ps.setString(1, licensePlateNumber);
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()) {
                  idvozila=rs.getInt(1);
                }
            } catch (SQLException ex) {
                Logger.getLogger(ss140588_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (idvozila == -1) {
            return false;
        }
        int IdAdrese;
        query="select IdAdresa from Magacin where IdMagacin=?";
          try(PreparedStatement ps1 = conn.prepareStatement(query);) {
              ps1.setInt(1, idStockroom);
           
           IdAdrese=ps1.executeUpdate();
            
        } catch (SQLException e) {
            return false;
        }       
         query = "select * from Adresa where IdAdresa = ?";
         boolean postoji=false;
        try (PreparedStatement ps2 = conn.prepareStatement(query);) {
            ps2.setInt(1, IdAdrese);
            try (ResultSet rs2 = ps2.executeQuery();) {
                if (rs2.next()) {
                    postoji=true;
                }
            } catch (SQLException ex) {
                Logger.getLogger(ss140588_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (postoji == false) {
            return false;
        }
        postoji=false;
          query = "select * from Magacin where IdAdresa = ?";
        try (PreparedStatement ps3 = conn.prepareStatement(query);) {
            ps3.setInt(1, IdAdrese);
            try (ResultSet rs3 = ps3.executeQuery();) {
                if (rs3.next()) {
                    postoji= true;
                }
            } catch (SQLException ex) {
                Logger.getLogger(ss140588_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
         if (postoji == false) {
            return false;
        }
        query = "select Slobodno from Vozilo where IdVozilo = ?";
        try (PreparedStatement ps4 = conn.prepareStatement(query);) {
            ps4.setInt(1, idvozila);
            try (ResultSet rs4 = ps4.executeQuery();) {
                if (!rs4.next()) {
                    return false;
                } else if (rs4.getInt(1) == 1) {
                    return false;
                }
            } catch (SQLException ex) {
                Logger.getLogger(ss140588_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

        query = "update Vozilo set IdMagacin = ?, Slobodno = 1 where IdVozilo = ?";
        try (PreparedStatement ps5 = conn.prepareStatement(query);) {
            ps5.setInt(1, idStockroom);
            ps5.setInt(2, idvozila);
            ps5.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false; 
    }
    
    
}

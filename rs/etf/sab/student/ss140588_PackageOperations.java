/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.etf.sab.student;
import java.math.BigDecimal;
import java.sql.*;
import java.util.List;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.PackageOperations;
/**
 *
 * @author Smiljana
 */
public class ss140588_PackageOperations implements PackageOperations {

    @Override
    public int insertPackage(int addressFrom, int addressTo, String userName, int packageType, BigDecimal weight) {
        int idkorisnik=-1;
        Connection conn = DB.getInstance().getConnection();
        String query = "select IdKorisnik from Korisnik where KorIme = ?";
        try (PreparedStatement ps = conn.prepareStatement(query);) {
            ps.setString(1, userName);
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()) {
                    idkorisnik=rs.getInt(1);
                }
            } catch (SQLException ex) {
                Logger.getLogger(ss140588_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (idkorisnik == -1) {
            return -1;
        }
        if (packageType != 0
                && packageType != 1
                && packageType != 2
                && packageType != 3) {
            return -1;
        }
         if (weight == null) {
            weight = new BigDecimal(10);
        }
        query = "insert into Paket (Tip, Tezina, [Status], AdrPoslata, AdrDostave, Cena, ZahtevKreiran, ZahtevPrihvacen, IdKorisnik, Lokacija) "
                + "values (?, ?, 0, ?, ?, 0, GETDATE(), NULL, ?, NULL)";
        try (PreparedStatement ps1 = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);) {
            ps1.setInt(1, packageType);
            ps1.setBigDecimal(2, weight);
            ps1.setInt(3, addressFrom);
            ps1.setInt(4, addressTo);
            ps1.setInt(5, idkorisnik);
            ps1.executeUpdate();
            ResultSet rs1 = ps1.getGeneratedKeys();
            if (rs1.next()) {
                return rs1.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1; 
    }
 @Override
    public int getDeliveryStatus(int  packageId) {
       Connection conn = DB.getInstance().getConnection();
        String query = "select * from Paket where IdPaket = ?";
        boolean postoji=false;
        try (PreparedStatement ps = conn.prepareStatement(query);) {
            ps.setInt(1, packageId);
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()) {
                   postoji=true;
                }
            } catch (SQLException ex) {
                Logger.getLogger(ss140588_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(postoji==false) {return -1;}
        query = "select Status from Paket where IdPaket = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query);) {
            stmt.setInt(1, packageId);
            try (ResultSet rs1 = stmt.executeQuery();) {
                if (rs1.next()) {
                    return rs1.getInt(1);
                }
            } catch (SQLException ex) {
                Logger.getLogger(ss140588_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }
    @Override
    public boolean acceptAnOffer(int packageId) {
        Connection conn = DB.getInstance().getConnection();
        String query = "select * from Paket where IdPaket = ?";
        boolean postoji=false;
        try (PreparedStatement ps = conn.prepareStatement(query);) {
            ps.setInt(1, packageId);
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()) {
                   postoji=true;
                }
            } catch (SQLException ex) {
                Logger.getLogger(ss140588_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(postoji==false) {return false;}
        if ((getDeliveryStatus(packageId) == -1)||(getDeliveryStatus(packageId) != 0)) {
            return false;
        }
        query = "update Paket set ZahtevPrihvacen = GETDATE(), Status=? where IdPaket = ?";
        try (PreparedStatement ps1 = conn.prepareStatement(query);) {
            ps1.setInt(1, 1);
            ps1.setInt(2, packageId);
            ps1.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public boolean rejectAnOffer(int packageId) {
        Connection conn = DB.getInstance().getConnection();
        String query = "select * from Paket where IdPaket = ?";
        boolean postoji=false;
        try (PreparedStatement ps = conn.prepareStatement(query);) {
            ps.setInt(1, packageId);
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()) {
                   postoji=true;
                }
            } catch (SQLException ex) {
                Logger.getLogger(ss140588_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(postoji==false) {return false;}
        if ((getDeliveryStatus(packageId) == -1)||(getDeliveryStatus(packageId) != 0)) {
            return false;
        }
        query = "update Paket set Status=? where IdPaket = ?";
        try (PreparedStatement ps1 = conn.prepareStatement(query);) {
            ps1.setInt(1, 4);
            ps1.setInt(2, packageId);
            ps1.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public List<Integer> getAllPackages() {
         Connection conn = DB.getInstance().getConnection();
        String query = "select IdPaket from Paket";
        List<Integer> paketi = new LinkedList<>();
        try (
                PreparedStatement ps = conn.prepareStatement(query);
                ResultSet rs = ps.executeQuery();) {
            while (rs.next()) {
                paketi.add(rs.getInt(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return paketi;
    }

    @Override
    public List<Integer> getAllPackagesWithSpecificType(int type) {
        List<Integer> paketi = new LinkedList<>();
        if (type != 0
                && type != 1
                && type != 2
                && type != 3) {
            return paketi;
        }
         Connection conn = DB.getInstance().getConnection();
        String query = "select IdPaket from Paket where Tip = ?";
        try (PreparedStatement ps = conn.prepareStatement(query);) {
            ps.setInt(1, type);
            try (ResultSet rs = ps.executeQuery();) {
                while (rs.next()) {
                    paketi.add(rs.getInt(1));
                }
            } catch (SQLException ex) {
                Logger.getLogger(ss140588_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return paketi;
    }

    @Override
    public List<Integer> getAllUndeliveredPackages() {
        Connection conn = DB.getInstance().getConnection();
        String query = "select IdPaket from Paket where Status IN (?, ?)";
        List<Integer> paketi = new LinkedList<>();
        try (PreparedStatement ps = conn.prepareStatement(query);) {
            ps.setInt(1, 1);
            ps.setInt(2, 2);
            try (ResultSet rs = ps.executeQuery();) {
                while (rs.next()) {
                    paketi.add(rs.getInt(1));
                }
            } catch (SQLException ex) {
                Logger.getLogger(ss140588_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return paketi;
    }
    

    @Override
    public List<Integer> getAllUndeliveredPackagesFromCity(int cityId) {
       
        List<Integer> paketi = new LinkedList<>();
        boolean postoji=false;
        Connection conn = DB.getInstance().getConnection();
        String query = "select * from Grad where IdGrad = ?";

        try (PreparedStatement ps = conn.prepareStatement(query);) {
            ps.setInt(1, cityId);
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()) {
                    postoji=true;
                }
            } catch (SQLException ex) {
                Logger.getLogger(ss140588_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(postoji==false) { return paketi; }

        
        query = "select IdPaket from Paket where AdrPoslata IN (select IdAdresa from Adresa where IdCity = ?) AND Status IN (?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(query);) {
            stmt.setInt(1, cityId);
            stmt.setInt(2, 1);
            stmt.setInt(3, 2);
            try (ResultSet rs = stmt.executeQuery();) {
                while (rs.next()) {
                    paketi.add(rs.getInt(1));
                }
            } catch (SQLException ex) {
                Logger.getLogger(ss140588_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }

        return paketi;
    }

    @Override
    public List<Integer> getAllPackagesCurrentlyAtCity(int cityId) {
        List<Integer> paketi = new LinkedList<>();
        boolean postoji=false;
        Connection conn = DB.getInstance().getConnection();
        String query = "select * from Grad where IdGrad = ?";

        try (PreparedStatement ps = conn.prepareStatement(query);) {
            ps.setInt(1, cityId);
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()) {
                    postoji=true;
                }
            } catch (SQLException ex) {
                Logger.getLogger(ss140588_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(postoji==false) { return paketi; }
        
          query = "select Paket.IdPaket\n"
                + "from Paket join Adresa ON (Paket.AdrPoslata = Adresa.IdAdresa)\n"
                + "where Status = 1 AND Adresa.IdGrad = ?";

        try (PreparedStatement ps = conn.prepareStatement(query);) {
            ps.setInt(1, cityId);
            try (ResultSet rs = ps.executeQuery();) {
                while (rs.next()) {
                    paketi.add(rs.getInt(1));
                }
            } catch (SQLException ex) {
                Logger.getLogger(ss140588_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }

        
          query = "select Paket.IdPaket\n"
                + "from Paket join Adresa ON (Paket.AdrDostave = Adresa.IdAdresa)\n"
                + "where Status = 3 AND Adresa.IdGrad = ?";

        try (PreparedStatement ps = conn.prepareStatement(query);) {
            ps.setInt(1, cityId);
            try (ResultSet rs = ps.executeQuery();) {
                while (rs.next()) {
                    paketi.add(rs.getInt(1));
                }
            } catch (SQLException ex) {
                Logger.getLogger(ss140588_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        query = "SELECT Magacin.IdAdresa from Magacin join Adresa ON (Magacin.IdAdresa = Adresa.IdAdresa) where Adresa.IdGrad = ?";
        int postojimag=-1;
        try (PreparedStatement ps = conn.prepareStatement(query);) {
            ps.setInt(1, cityId);
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()) {
                    postojimag=rs.getInt(1);
                
                }
            } catch (SQLException ex) {
                Logger.getLogger(ss140588_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (postojimag == -1) {
            return paketi;
        }

        query
                = "select IdPaket\n"
                + "from Paket\n"
                + "where Status = 2 AND IdPaket NOT IN (select IdPaket from Kargo) AND Lokacija = ?";

        try (PreparedStatement ps = conn.prepareStatement(query);) {
            ps.setInt(1, postojimag);
            try (ResultSet rs = ps.executeQuery();) {
                while (rs.next()) {
                    paketi.add(rs.getInt(1));
                }
            } catch (SQLException ex) {
                Logger.getLogger(ss140588_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_StockroomOperations.class.getName()).log(Level.SEVERE, null, ex);
        }

        return paketi;
    }

    @Override
    public boolean deletePackage(int packageId) {
        Connection conn = DB.getInstance().getConnection();
        String query = "select * from Paket where IdPaket = ?";
        boolean postoji=false;
        try (PreparedStatement ps = conn.prepareStatement(query);) {
            ps.setInt(1, packageId);
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()) {
                   postoji=true;
                }
            } catch (SQLException ex) {
                Logger.getLogger(ss140588_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(postoji==false) {return false;}
        if (getDeliveryStatus(packageId) == -1) { return false; }
         if((getDeliveryStatus(packageId) != 0)&&(getDeliveryStatus(packageId) != 4)) {
            return false;
        }
        query = "delete from Paket where IdPaket = ?";
        try (PreparedStatement ps = conn.prepareStatement(query);) {
            ps.setInt(1, packageId);
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public boolean changeWeight(int packageId, BigDecimal newWeight) {
         Connection conn = DB.getInstance().getConnection();
        String query = "select * from Paket where IdPaket = ?";
        boolean postoji=false;
        try (PreparedStatement ps = conn.prepareStatement(query);) {
            ps.setInt(1, packageId);
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()) {
                   postoji=true;
                }
            } catch (SQLException ex) {
                Logger.getLogger(ss140588_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(postoji==false) {return false;}
        if ((getDeliveryStatus(packageId) == -1)||(getDeliveryStatus(packageId) != 0)) {
            return false;
        }
        query = "update Paket set Tezina = ? where IdPaket = ?";
        try (PreparedStatement ps = conn.prepareStatement(query);) {
            ps.setBigDecimal(1, newWeight);
            ps.setInt(2, packageId);
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public boolean changeType(int packageId, int newType) {
        Connection conn = DB.getInstance().getConnection();
        String query = "select * from Paket where IdPaket = ?";
        boolean postoji=false;
        try (PreparedStatement ps = conn.prepareStatement(query);) {
            ps.setInt(1, packageId);
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()) {
                   postoji=true;
                }
            } catch (SQLException ex) {
                Logger.getLogger(ss140588_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(postoji==false) {return false;}
        if ((getDeliveryStatus(packageId) == -1)||(getDeliveryStatus(packageId) != 0)) {
            return false;
        }
        query = "update Paket set Tip = ? where IdPaket = ?";
        try (PreparedStatement ps = conn.prepareStatement(query);) {
            ps.setInt(1, newType);
            ps.setInt(2, packageId);
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

   

    @Override
    public BigDecimal getPriceOfDelivery(int packageId) {
        Connection conn = DB.getInstance().getConnection();
        String query = "select * from Paket where IdPaket = ?";
        boolean postoji=false;
        try (PreparedStatement ps = conn.prepareStatement(query);) {
            ps.setInt(1, packageId);
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()) {
                   postoji=true;
                }
            } catch (SQLException ex) {
                Logger.getLogger(ss140588_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(postoji==false) {return new BigDecimal(-1);}
        query = "select Cena from Paket where IdPaket = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query);) {
            stmt.setInt(1, packageId);
            try (ResultSet rs = stmt.executeQuery();) {
                if (rs.next()) {
                    return rs.getBigDecimal(1);
                }
            } catch (SQLException ex) {
                Logger.getLogger(ss140588_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new BigDecimal(-1);
    }

    @Override
    public int getCurrentLocationOfPackage(int packageId) {
         Connection conn = DB.getInstance().getConnection();
        String query = "select * from Paket where IdPaket = ?";
        boolean postoji=false;
        try (PreparedStatement ps = conn.prepareStatement(query);) {
            ps.setInt(1, packageId);
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()) {
                   postoji=true;
                }
            } catch (SQLException ex) {
                Logger.getLogger(ss140588_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(postoji==false) {return -2;}
        int statuspaketa=getDeliveryStatus(packageId);
        if (statuspaketa == -1) {
            return -2;
        }
        if (statuspaketa != 1
                && statuspaketa != 2
                && statuspaketa != 3) {
            return -2;
        }
        query
                = "select *\n"
                + "from Kargo\n"
                + "where IdPaket = (select IdPaket from Paket where Status = 2 AND IdPaket = ?)";

        try (PreparedStatement ps = conn.prepareStatement(query);) {
            ps.setInt(1, packageId);
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()) {
                    return -1;
                }
            } catch (SQLException ex) {
                Logger.getLogger(ss140588_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
                return -2;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            return -2;
        }

        switch (statuspaketa) {
            case 1:
                query
                        = "select IdGrad\n"
                        + "from Adresa\n"
                        + "where IdAdresa = (select AdrPoslata from Paket where IdPaket = ?)";
                break;
                 case 2:
                query
                        = "select IdGrad\n"
                        + "from Adresa\n"
                        + "where IdAdresa = (select Lokacija from Paket where IdPaket = ?)";
                break;
            case 3:
                query
                        = "select IdGrad\n"
                        + "from Adresa\n"
                        + "where IdAdresa = (select AdrDostave from Paket where IdPaket = ?)";
                break;
           
        }

        try (PreparedStatement ps = conn.prepareStatement(query);) {
            ps.setInt(1, packageId);
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            } catch (SQLException ex) {
                Logger.getLogger(ss140588_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }

        return -2;

    }
    

    @Override
    public Date getAcceptanceTime(int packageId) {
                Connection conn = DB.getInstance().getConnection();
        String query = "select * from Paket where IdPaket = ?";
        boolean postoji=false;
        try (PreparedStatement ps = conn.prepareStatement(query);) {
            ps.setInt(1, packageId);
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()) {
                   postoji=true;
                }
            } catch (SQLException ex) {
                Logger.getLogger(ss140588_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(postoji==false) {return null;}
        if ((getDeliveryStatus(packageId) == -1)||(getDeliveryStatus(packageId) != 1)) {
            return null;
        }
        Date datum=null;
        query = "select ZahtevPrihvacen from Paket where IdPaket = ?";
        try (PreparedStatement ps = conn.prepareStatement(query);) {
            ps.setInt(1, packageId);
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()) {
                    datum = rs.getDate(1);
                }
            } catch (SQLException ex) {
                Logger.getLogger(ss140588_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return datum;
    }
    
}

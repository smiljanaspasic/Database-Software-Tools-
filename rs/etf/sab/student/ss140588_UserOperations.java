/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.etf.sab.student;
import java.util.regex.*;
import java.util.List;
import java.sql.*;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.UserOperations;

/**
 *
 * @author Smiljana
 */
public class ss140588_UserOperations implements UserOperations {

    @Override
    public boolean insertUser(String userName, String firstName, String lastName, String password, int idAddress) {
         Connection conn = DB.getInstance().getConnection();
        String select = "select * from Korisnik where KorIme = ?";
        try (PreparedStatement ps = conn.prepareStatement(select);) {
            ps.setString(1, userName);
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()) {
                    return false;
                }
            } catch (SQLException ex) {
                Logger.getLogger(ss140588_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

        if (Character.isLowerCase(firstName.charAt(0)) || Character.isLowerCase(lastName.charAt(0))) {
            return false;
        }

        select = "select * from Adresa where IdAdresa = ?";
        try (PreparedStatement ps = conn.prepareStatement(select);) {
            ps.setInt(1, idAddress);
            try (ResultSet rs = ps.executeQuery();) {
                if (!rs.next()) {
                    return false;
                }
            } catch (SQLException ex) {
                Logger.getLogger(ss140588_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

        String pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&._])[A-Za-z\\d@$!%*?&._]{8,}$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(password);
        if (!m.find()) {
            return false;
        }
        select = "insert into Korisnik (Ime, Prezime, KorIme, Sifra, IdAdresa) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(select);) {
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, userName);
            stmt.setString(4, password);
            stmt.setInt(5, idAddress);
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public boolean declareAdmin(String userName) {
         Connection conn = DB.getInstance().getConnection();
        String query = "select * from [Korisnik] join [Administrator] on [Korisnik].IdKorisnik = [Administrator].IdKorisnik where [Korisnik].KorIme = ?";
        try (PreparedStatement ps = conn.prepareStatement(query);) {
            ps.setString(1, userName);
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()) {
                    return false;
                }
            } catch (SQLException ex) {
                Logger.getLogger(ss140588_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }

        int idkorisnika=-1;
        query = "select IdKorisnik from Korisnik where KorIme = ?";
         try (PreparedStatement prepared = conn.prepareStatement(query);) {
            prepared.setString(1, userName);
            try (ResultSet rs = prepared.executeQuery();) {
                if (rs.next()) {
                   idkorisnika=rs.getInt(1);
                }
            } catch (SQLException ex) {
                Logger.getLogger(ss140588_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (idkorisnika == -1) {
            return false;
    }
         query = "insert into Administrator(IdKorisnik) values (?)";
        try (PreparedStatement stmt = conn.prepareStatement(query);) {
            stmt.setInt(1, idkorisnika);
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    @Override
    public int getSentPackages(String... userNames) {
        Connection conn = DB.getInstance().getConnection();
        String query= "select * from Korisnik where KorIme = ?";
         boolean korisnikpostoji = false;
        try (PreparedStatement ps = conn.prepareStatement(query);) {
            for (String userName : userNames) {
                ps.setString(1, userName);
                try (ResultSet rs = ps.executeQuery();) {
                    if (rs.next()) {
                        korisnikpostoji = true;
                        break;
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(ss140588_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (!korisnikpostoji) {
                return -1;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }

        int broj = 0;
        query= "select COUNT(Paket.IdPaket)\n"
                + "from Paket join [Korisnik] ON (Paket.IdKorisnik = [Korisnik].IdKorisnik)\n"
                + "where [Paket].Status = 3 AND [Korisnik].KorIme = ?";
                
        try (PreparedStatement stmt = conn.prepareStatement(query);) {
            for (String userName : userNames) {
                stmt.setString(1, userName);
                try (ResultSet rs = stmt.executeQuery();) {
                    if (rs.next()) {
                        broj += rs.getInt(1);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(ss140588_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return broj;
    }

    @Override
    public int deleteUsers(String... usernames) {
      Connection conn = DB.getInstance().getConnection();
        String delete = "delete from Korisnik where KorIme = ?";
        int brobrisanih = 0;
        try (PreparedStatement ps = conn.prepareStatement(delete);) {
         for(int i = 0; i < usernames.length; i++){
                ps.setString(1,usernames[i]);
                brobrisanih += ps.executeUpdate();
            }
          
        
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
          return brobrisanih;
    }

    @Override
    public List<String> getAllUsers() {
        Connection conn = DB.getInstance().getConnection();
        String select = "select KorIme from Korisnik";
        List<String> korisnici = new LinkedList<>();
         try (
                PreparedStatement ps = conn.prepareStatement(select);
                ResultSet rs = ps.executeQuery();) {
            while (rs.next()) {
                korisnici.add(rs.getString("KorIme"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ss140588_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return korisnici;
    }
    
    
}

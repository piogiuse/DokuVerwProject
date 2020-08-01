/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dokuverwproject.LOGIC;
import dokuverwproject.DATA.DBConn;
import dokuverwproject.GUI.NotifyFrame;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Giuseppe & Falk
 */
public class ErinnerungenListe {
    
    public void ansichtAktualisieren() {

    }
    private long groeße = 0;
    private DefaultTableModel model = null; // Zugriff auf Tabelle in ErinngerungenFrame

    public ErinnerungenListe(DefaultTableModel model) {
        this.model = model;
    }

    public Boolean erinnerungenLaden() {
        DBConn dbc = new DBConn();
        Connection con = dbc.getConnection();
        if (con != null) {

            this.setGroeße(0);
            model.setRowCount(0);

            Object[] row = new Object[7];

            Statement stmt = null;
            String query = "SELECT * FROM `erinnerungen`";

            try {
                stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {

                    long id = rs.getLong(1);
                    String titel = rs.getString(2);
                    String inhalt = rs.getString(3);
                    Timestamp faellig = rs.getTimestamp(4);
                    Integer erledigt = rs.getInt(5);
                    String pfad = rs.getString(6);
                    Timestamp stamp = rs.getTimestamp(7);

                    SimpleDateFormat sdfDate = new SimpleDateFormat("E, dd.MM.yyyy");
                    sdfDate.setTimeZone(TimeZone.getTimeZone("MEZ"));

                    SimpleDateFormat sdfTime = new SimpleDateFormat("kk:mm");
                    sdfTime.setTimeZone(TimeZone.getTimeZone("MEZ"));

                    String s_stamp = sdfDate.format(stamp) + " " + sdfTime.format(stamp) + " Uhr";

                    row[0] = id;
                    row[1] = titel;
                    row[2] = inhalt;
                    row[3] = faellig;
                    row[4] = erledigt;
                    row[5] = pfad;
                    row[6] = s_stamp;

                    model.addRow(row);

                    this.setGroeße(this.getGroeße() + 1);
                }
                ;

                stmt.close();
                return true;

            } catch (Exception e) {
                System.out.println(e.toString());
                e.printStackTrace();
            }
        } else {
            NotifyFrame nf = new NotifyFrame("Fehler", "Fehler beim Zugriff auf die Datenbank");
        }
        return false;
    }


    public void erinnerungLöschen(Erinnerung erinnerung) {
        // lösche Referenz auf erinngerung und starte garbage collector
        erinnerung = null;
        System.gc();
    }
    
    public void erinnerungÄndern(Erinnerung erinnerung,Timestamp zeit, String inhalt) {
        // setze inhalt und zeit auf entsprechende Variablen von erinnerung
        erinnerung.setInhalt(inhalt);
        erinnerung.setFällig(zeit);
    }
    
    public void aendereErledigt(Erinnerung erinnerung) {
        //
        erinnerung.setErledigt( !erinnerung.getErledigt() );
    }

    public long getGroeße() {
        return this.groeße;
    }

    public void setGroeße(long größe) {
        this.groeße = groeße;
    }


}

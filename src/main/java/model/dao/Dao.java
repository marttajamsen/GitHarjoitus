package model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import model.Asiakas;

public class Dao {
	private Connection con = null;
	private ResultSet rs = null;
	private PreparedStatement stmtPrep = null;
	private String sql;
	private String db = "Myynti.sqlite";

	private Connection yhdista() {
		Connection con = null;
		String path = System.getProperty("catalina.base");
		path = path.substring(0, path.indexOf(".metadata")).replace("\\", "/");
		String url = "jdbc:sqlite:" + path + db;
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection(url);
			System.out.println("Yhteys avattu.");
		} catch (Exception e) {
			System.out.println("Yhteyden avaus ep�onnistui.");
			e.printStackTrace();
		}
		return con;
	}

	public ArrayList<Asiakas> listaaKaikki() {
		ArrayList<Asiakas> asiakkaat = new ArrayList<Asiakas>();
		sql = "SELECT * FROM asiakkaat";
		try {
			con = yhdista();
			if (con != null) {
				stmtPrep = con.prepareStatement(sql);
				rs = stmtPrep.executeQuery();
				if (rs != null) {

					while (rs.next()) {
						Asiakas asiakas = new Asiakas();
						asiakas.setEtunimi(rs.getString(1));
						asiakas.setSukunimi(rs.getString(2));
						asiakas.setSposti(rs.getString(3));
						asiakas.setPuhelin(rs.getString(4));
						asiakkaat.add(asiakas);
					}
				}
			}
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return asiakkaat;
	}

	public ArrayList<Asiakas> listaaKaikki(String hakusana) {
		ArrayList<Asiakas> asiakkaat = new ArrayList<Asiakas>();
		sql = "SELECT * FROM myynti WHERE puhelin LIKE ? or merkki LIKE ? or malli LIKE ?";
		try {
			con = yhdista();
			if (con != null) {
				stmtPrep = con.prepareStatement(sql);
				stmtPrep.setString(1, "%" + hakusana + "%");
				stmtPrep.setString(2, "%" + hakusana + "%");
				stmtPrep.setString(3, "%" + hakusana + "%");
				rs = stmtPrep.executeQuery();
				if (rs != null) {
					while (rs.next()) {
						Asiakas asiakas = new Asiakas();
						asiakas.setEtunimi(rs.getString(1));
						asiakas.setSukunimi(rs.getString(2));
						asiakas.setPuhelin(rs.getString(3));
						asiakas.setSposti(rs.getString(4));
						asiakkaat.add(asiakas);
					}
				}
			}
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return asiakkaat;
	}

	public boolean lisaaAsiakas(Asiakas asiakas) {
		boolean paluuArvo = true;
		sql = "INSERT INTO autot VALUES(?,?,?,?)";
		try {
			con = yhdista();
			stmtPrep = con.prepareStatement(sql);
			stmtPrep.setString(1, asiakas.getEtunimi());
			stmtPrep.setString(2, asiakas.getSukunimi());
			stmtPrep.setString(3, asiakas.getPuhelin());
			stmtPrep.setString(4, asiakas.getSposti());
			stmtPrep.executeUpdate();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
			paluuArvo = false;
		}
		return paluuArvo;
	}

	public boolean poistaAsiakas(String puhelin) {
		boolean paluuArvo = true;
		sql = "DELETE FROM myynti WHERE puhelin=?";
		try {
			con = yhdista();
			stmtPrep = con.prepareStatement(sql);
			stmtPrep.setString(1, puhelin);
			stmtPrep.executeUpdate();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
			paluuArvo = false;
		}
		return paluuArvo;
	}

	public Asiakas etsiAsiakas(String puhelin) {
		Asiakas asiakas = null;
		sql = "SELECT * FROM myynti WHERE puhelin=?";
		try {
			con = yhdista();
			if (con != null) {
				stmtPrep = con.prepareStatement(sql);
				stmtPrep.setString(1, puhelin);
				rs = stmtPrep.executeQuery();
				if (rs.isBeforeFirst()) {
					rs.next();
					asiakas = new Asiakas();
					asiakas.setEtunimi(rs.getString(1));
					asiakas.setSukunimi(rs.getString(2));
					asiakas.setPuhelin(rs.getString(3));
					asiakas.setSposti(rs.getString(4));
				}
			}
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return asiakas;
	}

	public boolean muutaAsiakas(Asiakas asiakas, String puhelin) {
		boolean paluuArvo = true;
		sql = "UPDATE myynti SET etunimi=?, sukunimi=?, puhelin=?, sposti=? WHERE puhelin=?";
		try {
			con = yhdista();
			stmtPrep = con.prepareStatement(sql);
			stmtPrep.setString(1, asiakas.getEtunimi());
			stmtPrep.setString(2, asiakas.getSukunimi());
			stmtPrep.setString(3, asiakas.getPuhelin());
			stmtPrep.setString(4, asiakas.getSposti());
			stmtPrep.setString(5, puhelin);
			stmtPrep.executeUpdate();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
			paluuArvo = false;
		}
		return paluuArvo;
	}
}

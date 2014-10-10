/*
 *	@Author Firman Hidayat
 */

package id.co.firman.backup;

import id.co.firman.umum.enkripsi;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class CreateBackupPropertiesFile {

	public CreateBackupPropertiesFile() {
		PropertyConfigurator.configure("log4j.properties");
	}

	public void createPropertiesFile() {
		try {
			enkripsi enkripsi = new enkripsi();
			Properties properties = new Properties();
			properties.setProperty("folderbackup", "E:/backup/surat/");
			properties.setProperty("nama_database", "surat");
			properties.setProperty("user", enkripsi.encrypt("root"));
			properties.setProperty("password", enkripsi.encrypt("123456"));
			properties.setProperty("jamBackup", "12:35:00");
			properties.store(
					new FileOutputStream("backup_database.properties"),
					"Konfigurasi Backup MySQL");
			log.debug("File backup_database.properties berhasil dibuat");
		} catch (IOException ex) {
			log.error("Gagal membuat file backup_database.properties", ex);
		}
	}

	static Logger log = Logger.getRootLogger();

}
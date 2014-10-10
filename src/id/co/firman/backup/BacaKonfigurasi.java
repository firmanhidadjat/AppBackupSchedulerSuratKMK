/*
 *	@Author Firman Hidayat
 */

package id.co.firman.backup;

import id.co.firman.umum.enkripsi;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class BacaKonfigurasi
{

    public BacaKonfigurasi()
    {
        PropertyConfigurator.configure("log4j.properties");
    }

    public String[] baca_konfigurasi()
        throws Exception
    {
        String hasil[] = new String[5];
        Properties properties = new Properties();
        try
        {
            log.debug("Membuka file backup_database.properties. ");
            properties.load(new FileInputStream("backup_database.properties"));
            log.debug("backup_database.properties berhasil dibuka");
        }
        catch(IOException ex)
        {
            log.info("Gagal membuka file backup_database.properties");
            log.info("Membuat file backup_database.properties baru.");
            CreateBackupPropertiesFile buatfile = new CreateBackupPropertiesFile();
            buatfile.createPropertiesFile();
            try
            {
                log.info("Membuka file backup_database.properties yang baru dibuat. ");
                properties.load(new FileInputStream("backup_database.properties"));
                log.info("Berhasil membuka file backup_database.properties yang baru dibuat.");
            }
            catch(IOException ex1)
            {
                log.error("Gagal membaca backup_database.properties yang baru", ex1);
            }
        }
        enkripsi enkripsi = new enkripsi();
        String folderbackup = properties.getProperty("folderbackup");
        String nama_database = properties.getProperty("nama_database");
        String user = enkripsi.decrypt(properties.getProperty("user"));
        String password = enkripsi.decrypt(properties.getProperty("password"));
        String jamBackup = properties.getProperty("jamBackup");
        hasil[0] = folderbackup;
        hasil[1] = nama_database;
        hasil[2] = user;
        hasil[3] = password;
        hasil[4] = jamBackup;
        return hasil;
    }

    static Logger log = Logger.getRootLogger();

}
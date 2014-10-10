/*
 *	@Author Firman Hidayat
 */

package id.co.firman.backup;

import java.util.Timer;
import java.util.TimerTask;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class AppBackup {

	public AppBackup() {
		PropertyConfigurator.configure("log4j.properties");
	}

	public static void main(String args[]) {
		Timer timer = new Timer();
		int delay = 1000;
		timer.schedule(new TimerTask() {

			public void run() {
				BacaKonfigurasi baca = new BacaKonfigurasi();
				String properti[] = null;
				try {
					properti = baca.baca_konfigurasi();
					String folderBackup = properti[0];
					String namaDataBase = properti[1];
					String user = properti[2];
					String password = properti[3];
					String jamBackup = properti[4];
					BackupFunction fungsi = new BackupFunction();
					if (fungsi.jamTertentu(jamBackup)) {
						AppBackup.log
								.info((new StringBuilder(
										"Rangkaian proses backup dimulai\nJam backup \t: "))
										.append(jamBackup).append("\n")
										.append("Folder    \t: ")
										.append(folderBackup).append("\n")
										.append("Nama DB   \t: ")
										.append(namaDataBase).toString());
						if (fungsi.backupMysql(folderBackup, namaDataBase,
								user, password))
							fungsi.hapusFileBackupPalingLama(fungsi
									.tampilkanDaftarFiles(folderBackup));
						else
							AppBackup.log
									.error("Terjadi kesalahan. Tidak menghapus file backup terlama");
					}
				} catch (Exception ex) {
					AppBackup.log.error(ex.getMessage(), ex);
				}
			}

		}, delay, 1000L);
	}

	static Logger log = Logger.getRootLogger();

}
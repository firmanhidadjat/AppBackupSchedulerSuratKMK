/*
 *	@Author Firman Hidayat
 */

package id.co.firman.backup;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class BackupFunction
{

    public BackupFunction()
    {
        PropertyConfigurator.configure("log4j.properties");
    }

    public boolean backupMysql(String folderbackup, String namadb, String user, String passwd)
    {
        int berhasil = -1;
        try
        {
            Calendar sekarang = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            String namafile = formatter.format(sekarang.getTime());
            String perintah = null;
            if(passwd.equals(""))
                perintah = (new StringBuilder()).append("mysqldump --routines -u ").append(user).append(" --add-drop-database -B ").append(namadb).append(" -r ").append(folderbackup).append(namafile).append(".sql").toString();
            else
                perintah = (new StringBuilder()).append("mysqldump --routines -u ").append(user).append(" -p").append(passwd).append(" --add-drop-database -B ").append(namadb).append(" -r ").append(folderbackup).append(namafile).append(".sql").toString();
            Process proses = null;
            try
            {
                log.info("Melakukan proses backup.");
                proses = Runtime.getRuntime().exec(perintah);
                BufferedReader stdError = new BufferedReader(new InputStreamReader(proses.getErrorStream()));
                BufferedReader stdInput = new BufferedReader(new InputStreamReader(proses.getInputStream()));
                String s = "";
                for(String s1 = null; (s1 = stdError.readLine()) != null;)
                    s = (new StringBuilder(String.valueOf(s))).append(s1).append("\n").toString();

                if(s.length() != 0)
                {
                    log.warn((new StringBuilder("Pesan eksekusi perintah backup :\n")).append(s).toString());
                } else
                {
                    s = "";
                    for(String s1 = null; (s1 = stdInput.readLine()) != null;)
                        s = (new StringBuilder(String.valueOf(s))).append(s1).append("\n").toString();

                    log.info((new StringBuilder("Pesan eksekusi perintah backup :\n")).append(s).toString());
                }
            }
            catch(IOException ex)
            {
                log.error(ex.getMessage(), ex);
            }
            try
            {
                berhasil = proses.waitFor();
                if(berhasil == 0)
                    log.info((new StringBuilder("Proses backup selesai tanpa masalah. Termination code = ")).append(berhasil).append(". Nama file backup = ").append(namafile).append(".sql").toString());
                else
                    log.error((new StringBuilder("Proses backup selesai dengan error. Termination code =")).append(berhasil).toString());
            }
            catch(InterruptedException ex)
            {
                log.error(ex.getMessage(), ex);
            }
        }
        catch(Exception e)
        {
            log.error(e.getMessage(), e);
        }
        finally
        {
            return berhasil == 0;
        }
    }

    public String waktuSekarang()
    {
        String waktu_sekarang = null;
        Calendar sekarang = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        waktu_sekarang = formatter.format(sekarang.getTime());
        return waktu_sekarang;
    }

    public boolean jamTertentu(String jam)
    {
        String sekarang = waktuSekarang();
        String jam_sekarang = sekarang.substring(sekarang.length() - 9, sekarang.length()).trim();
        return jam_sekarang.equals(jam.trim());
    }

    public Vector tampilkanDaftarFiles(String path_backup)
    {
        Vector daftar_files = new Vector();
        daftar_files.clear();
        String path = path_backup;
        File folder = new File(path);
        File listOfFiles[] = folder.listFiles();
        if(listOfFiles != null)
        {
            for(int i = 0; i < listOfFiles.length; i++)
                if(listOfFiles[i].isFile())
                {
                    String files = listOfFiles[i].getName();
                    daftar_files.add((new StringBuilder()).append(path).append(files).toString());
                }

        } else
        {
            log.info((new StringBuilder("Direktori ")).append(path).append(" kosong.").toString());
        }
        return daftar_files;
    }

    public void hapusFileBackupPalingLama(Vector daftar_files)
    {
        try
        {
            Collections.sort(daftar_files);
            if(daftar_files.size() > 4)
            {
                for(int i = daftar_files.size() - 5; i >= 0; i--)
                {
                    File f1 = new File((String)daftar_files.get(i));
                    log.debug((new StringBuilder("Menghapus file ")).append(daftar_files.get(i)).toString());
                    if(!f1.delete())
                        log.error((new StringBuilder("Gagal menghapus file ")).append(daftar_files.get(i)).toString());
                    else
                        log.info((new StringBuilder("File ")).append(daftar_files.get(i)).append(" dihapus").toString());
                }

            }
        }
        catch(Exception e)
        {
            log.error(e.getMessage(), e);
        }
    }

    static Logger log = Logger.getRootLogger();

}
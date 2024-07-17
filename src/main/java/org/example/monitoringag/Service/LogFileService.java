package org.example.monitoringag.Service;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

@Service
public class LogFileService {
    @Autowired
    ILogService logService;

    private static final String REMOTE_HOST = "192.168.33.11";
    private static final int REMOTE_PORT = 22;
    private static final String USERNAME = "ftpserver";
    private static final String PASSWORD = "root";
    private static final String REMOTE_DIRECTORY = "/home/ftpserver/logfile/ApplicationLogs";

   String[] appLogFolderNames={"Default","LifeCycleLog","MappingLog","PalmyraScheduler","SLALogger","logInit"};


    public List<String> getRemoteLogFilesAndAddOrUpdateItems() {
        List<String> logFileNames = new ArrayList<>();

        JSch jsch = new JSch();
        Session session = null;
        ChannelSftp sftpChannel = null;

        try {
            // Establish session
            session = jsch.getSession(USERNAME, REMOTE_HOST, REMOTE_PORT);
            session.setPassword(PASSWORD);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            System.out.println("connecté à palmyra avec succée !!");

            // Open SFTP channel
            sftpChannel = (ChannelSftp) session.openChannel("sftp");
            sftpChannel.connect();

            // List files in the remote directory
            for (String folder : appLogFolderNames){
                Vector<ChannelSftp.LsEntry> files = sftpChannel.ls(REMOTE_DIRECTORY +"/"+ folder + "/*.log");
                //afecting file to a list
                for (ChannelSftp.LsEntry entry : files) {
                    logFileNames.add(folder + ":" + entry.getFilename());
                    //add or update item for each file
                    logService.addOrUpdateItem(
                            "10607",
                            folder + ":" + entry.getFilename(),
                            "log[/home/vagrant/ApplicationLogs/"+folder+"/"+entry.getFilename()+",,,,skip]",
                            7,
                            2,
                            "30s"
                    );
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sftpChannel != null) {
                sftpChannel.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }

        return logFileNames;
    }
}

package com.wsudesc.otavio;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.Socket;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderErrorException;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.files.SearchV2Result;
import com.dropbox.core.v2.files.WriteMode;

/**
 * Hello world!
 *
 */
public class Client {
    private static DropboxConfig[] dbClients = new DropboxConfig[3];

    private static void setClient() {
        for (int i = 0; i < 3; i++) {
            String filePath = String.format("dbU%dToken.properties", i + 1);
            dbClients[i] = new DropboxConfig(i + 1, filePath);
        }
    }

    public static Socket getJob() throws Exception {
        Socket clientSocket = new Socket("ens5", 4355);
        return clientSocket;
    }

    public static void main(String args[]) {
        setClient();

        try {
            while (true) {

                Socket clientSocket = getJob();

                DataOutputStream outToServer = new DataOutputStream(
                        clientSocket.getOutputStream());
                DataInputStream inFromServer = new DataInputStream(clientSocket.getInputStream());

                // Get Job type (upload|download)
                clientSocket.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

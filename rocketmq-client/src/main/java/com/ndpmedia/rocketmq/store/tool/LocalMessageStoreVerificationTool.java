/*
 * Copyright (c) 2015. All Rights Reserved.
 */
package com.ndpmedia.rocketmq.store.tool;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocalMessageStoreVerificationTool {

    private static final String STORE_FILE_NAME_REGEX = "\\d+";

    private static final Pattern STORE_FILE_NAME_PATTERN = Pattern.compile(STORE_FILE_NAME_REGEX);

    private static final int MAGIC_CODE = 0xAABBCCDD ^ 1880681586 + 8;


    public static void main(String[] args) throws IOException {
        if (1 != args.length) {
            System.out.println("Usage: java -cp rocketmq-client-3.2.2.jar com.ndpmedia.rocketmq.store.tool.LocalMessageStoreVerificationTool /path/to/store");
            return;
        }

        checkRecursively(new File(args[0]));
    }

    private static void checkRecursively(File file) throws IOException {
        if (file.isFile()) {
            checkFile(file);
        } else {
            String[] files = file.list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    File f = new File(dir, name);
                    if (f.isDirectory()) {
                        return true;
                    }

                    Matcher matcher = STORE_FILE_NAME_PATTERN.matcher(name);
                    return matcher.matches();
                }
            });

            for (String f : files) {
                checkRecursively(new File(file, f));
            }
        }
    }

    private static void checkFile(File file) throws IOException {

        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
        boolean hasError = false;
        while (randomAccessFile.getFilePointer() < randomAccessFile.length() - 4) {
            int msgSize = randomAccessFile.readInt();
            int magicCode = randomAccessFile.readInt();

            if (magicCode != MAGIC_CODE) {
                System.err.println("Illegal magic code found! Position: " + (randomAccessFile.getFilePointer() - 4));
                hasError = true;
                break;
            }

            byte[] data = new byte[msgSize];
            randomAccessFile.readFully(data);
            System.out.println("Msg Size: " + msgSize);
            System.out.println("MSG Body: " + new String(data, Charset.forName("UTF-8")));
        }

        if (hasError) {
            System.err.println("Fatal: File " + file.getAbsolutePath() + " is tampered!");
        }

    }

}

package com.sapienter.jbilling.server.process.task;

import com.sapienter.jbilling.common.Util;
import junit.framework.TestCase;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * @author Brian Cowdery
 * @since 08-06-2010
 */
public class ScpUploadTaskTest extends TestCase {

    public ScpUploadTask task = new ScpUploadTask(); // task under test
    public String baseDir =  Util.getSysProp("base_dir");

    public ScpUploadTaskTest() {
    }

    public ScpUploadTaskTest(String name) {
        super(name);
    }

    public void testCollectFilesNonRecursive() throws Exception {
        File path = new File(baseDir);
        List<File> files = task.collectFiles(path, ".*\\.properties", false);

        assertEquals(1, files.size());
        assertEquals("entityNotifications.properties", files.get(0).getName());
    }

    public void testCollectFilesRecursive() throws Exception {
        File path = new File(baseDir);

        List<File> nonRecursive = task.collectFiles(path, ".*\\.jar", false);

        assertEquals(0, nonRecursive.size());

        List<File> files = task.collectFiles(path, ".*\\.jar", true);

        assertEquals(2, files.size());
        assertEquals("jbilling_api.jar", files.get(0).getName());
        assertEquals("jbilling.jar", files.get(1).getName());
    }

    public void testCollectfilesCompoundRegex() throws Exception {
        File path = new File(baseDir);
        List<File> files = task.collectFiles(path, "(.*\\.jar|.*\\.jpg$)", true);

        Arrays.toString(files.toArray());
        assertEquals(3, files.size());
    }

/*
    public void testScpUpload() throws Exception {
        File path = new File(baseDir);
        List<File> files = task.collectFiles(path, ".*entity-1\\.jpg$", true);

        assertEquals(1, files.size());

        // todo: fill in when testing
        String host = "";
        String username = "";
        String password = "";

        task.upload(files, null, host, username, password);        
    }
*/
}

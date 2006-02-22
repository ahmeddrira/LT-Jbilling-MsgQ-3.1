/*
The contents of this file are subject to the Jbilling Public License
Version 1.1 (the "License"); you may not use this file except in
compliance with the License. You may obtain a copy of the License at
http://www.jbilling.com/JPL/

Software distributed under the License is distributed on an "AS IS"
basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
License for the specific language governing rights and limitations
under the License.

The Original Code is jbilling.

The Initial Developer of the Original Code is Emiliano Conde.
Portions created by Sapienter Billing Software Corp. are Copyright 
(C) Sapienter Billing Software Corp. All Rights Reserved.

Contributor(s): ______________________________________.
*/

/*
 * Created on Nov 15, 2004
 *
 */
package com.sapienter.jbilling.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

/**
 * @author Emil
 *
 */
public class DocumentationIndex {

    static private BufferedReader reader = null;
    
    public static void main(String[] args) {
        try {
            // find my properties
            Properties globalProperties = new Properties();
            FileInputStream gpFile = new FileInputStream("indexing.properties");
            globalProperties.load(gpFile);
            
            // read the directory
            String dirName = globalProperties.getProperty("directory");
            File dir = new File(dirName);
            String filesNames[] = dir.list();
            
            Vector entries = new Vector();
            for (int f = 0; f < filesNames.length; f++) {
                File thisFile = new File(dirName + "/" + filesNames[f]);
                // skip directories
                if (!thisFile.isDirectory() && !filesNames[f].equals(
                        "index.html")) {
                    entries.add(filesNames[f]);
                }
            }
            // sort them by name
            Collections.sort(entries);
            
            // create the result file
            FileOutputStream result = new FileOutputStream(new File(dirName + 
                    "/index.html"));
            result.write("<html><body>".getBytes());
            
            for (Iterator it = entries.iterator(); it.hasNext();) {
                String entry = (String) it.next();
                System.out.println("Adding entry" + entry);

                if (entry.endsWith(".htm")) {
                    // it is an html page, process it
                    String htmlentry = entry.replaceAll(" ", "%20");
                    String link = "<a href=" + htmlentry+ ">";
                    
                    reader = new BufferedReader( new FileReader(
                            dir + "/" + entry) ); 
                    // find the title
                    String title = getText("title");
                    link += title + "</a><br/>\n";
                    
                    result.write(link.getBytes());
                }

            }
            result.write("</body></html>".getBytes());
            result.close();
            
            System.out.println("Done.");
            
        } catch (FileNotFoundException e) {
            System.err.println("Could not open file. " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    static String getText(String tagName) throws IOException {
        StringBuffer retValue = new StringBuffer();
        String line = reader.readLine();
        while (line != null) {
            if (line.indexOf("<" + tagName + ">") >= 0) {
                return line.substring(line.indexOf(">") + 1, line.lastIndexOf('<'));
            }
            
            line = reader.readLine();
        }
        
        return null;
    }

}

/*
    jbilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2007 Sapienter Billing Software Corp. and Emiliano Conde

    This file is part of jbilling.

    jbilling is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    jbilling is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with jbilling.  If not, see <http://www.gnu.org/licenses/>.
*/

/*
 * Created on Jun 14, 2004
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.server.invoice;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import sun.jdbc.rowset.CachedRowSet;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PRAcroForm;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.SimpleBookmark;
import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.common.Util;
import com.sapienter.jbilling.interfaces.InvoiceEntityLocal;
import com.sapienter.jbilling.interfaces.PaperInvoiceBatchEntityLocal;
import com.sapienter.jbilling.interfaces.PaperInvoiceBatchEntityLocalHome;
import com.sapienter.jbilling.server.notification.NotificationBL;
import com.sapienter.jbilling.server.process.BillingProcessBL;
import com.sapienter.jbilling.server.util.Constants;
import com.sapienter.jbilling.server.util.EventLogger;
import com.sapienter.jbilling.server.util.PreferenceBL;

/**
 * @author Emil
 */
public class PaperInvoiceBatchBL {
    private JNDILookup EJBFactory = null;
    private PaperInvoiceBatchEntityLocalHome batchHome = null;
    private PaperInvoiceBatchEntityLocal batch = null;
    private static final Logger LOG = Logger.getLogger(PaperInvoiceBatchBL.class);
    private EventLogger eLogger = null;

    public PaperInvoiceBatchBL(Integer batchId) 
            throws NamingException, FinderException {
        init();
        set(batchId);
    }
    
    public PaperInvoiceBatchBL(PaperInvoiceBatchEntityLocal batch) 
    		throws NamingException {
    	init();
    	this.batch = batch;
    }

    public PaperInvoiceBatchBL() throws NamingException {
        init();
    }

    private void init() throws NamingException {
        eLogger = EventLogger.getInstance();        
        EJBFactory = JNDILookup.getFactory(false);
        batchHome = (PaperInvoiceBatchEntityLocalHome) 
                EJBFactory.lookUpLocalHome(
                PaperInvoiceBatchEntityLocalHome.class,
                PaperInvoiceBatchEntityLocalHome.JNDI_NAME);
    }

    public PaperInvoiceBatchEntityLocal getEntity() {
        return batch;
    }
    
    public PaperInvoiceBatchEntityLocalHome getHome() {
        return batchHome;
    }

    public void set(Integer id) throws FinderException {
        batch = batchHome.findByPrimaryKey(id);
    }

    /**
     * This method will create a record if there's none for the given
     * process id, otherwise it will return the existing one
     * @param processId
     * @return
     */
    public PaperInvoiceBatchEntityLocal createGet(Integer processId) 
            throws NamingException, FinderException, CreateException {
        BillingProcessBL process = new BillingProcessBL(processId);
        batch = process.getEntity().getBatch();
        if (batch == null) {
            PreferenceBL preference = new PreferenceBL();
            preference.set(process.getEntity().getEntityId(), 
                    Constants.PREFERENCE_PAPER_SELF_DELIVERY);
            batch = batchHome.create(new Integer(0), 
                    preference.getEntity().getIntValue());
            process.getEntity().setBatch(batch);
        }
        return batch;
    }

    /**
     * Will take all the files generated by the process and 'paste' them
     * into a big one, deleting the originals.
     * This then will facilitate the printing of a batch.
     */
    public void compileInvoiceFilesForProcess(Integer entityId) 
    		throws DocumentException, IOException {
    	String filePrefix = Util.getSysProp("base_dir") + "invoices/" + 
            entityId + "-";
    	// now go through each of the invoices
        // first - sort them
        Vector invoices = new Vector(batch.getInvoices());
        Collections.sort(invoices, new InvoiceEntityComparator());
        Integer[] invoicesIds = new Integer[invoices.size()];


    	for (int f = 0; f < invoices.size(); f++) {
    		InvoiceEntityLocal invoice = (InvoiceEntityLocal) invoices.get(f);
            invoicesIds[f] = invoice.getId();
    	}
        
        compileInvoiceFiles(filePrefix, batch.getId().toString(), entityId, 
                invoicesIds);
    }

    /**
     * Takes a list of invoices and replaces the individual PDF files for one
     * single PDF in the destination directory.
     * @param destination
     * @param prefix
     * @param entityId
     * @param invoices
     * @throws PdfFormatException
     * @throws IOException
     */
    public void compileInvoiceFiles(String destination, String prefix,
            Integer entityId, Integer[] invoices)
            throws DocumentException, IOException {
        
        String filePrefix = Util.getSysProp("base_dir") + "invoices/"
                + entityId + "-";
        String outFile = destination + prefix + "-batch.pdf";

        
        int pageOffset = 0;
        ArrayList master = new ArrayList();
        Document document = null;
        PdfCopy  writer = null;
        for(int f = 0; f < invoices.length ; f++) {
            // we create a reader for a certain document
            PdfReader reader = new PdfReader(filePrefix + invoices[f] + "-invoice.pdf");
            reader.consolidateNamedDestinations();
            // we retrieve the total number of pages
            int numberOfPages = reader.getNumberOfPages();
            List bookmarks = SimpleBookmark.getBookmark(reader);
            if (bookmarks != null) {
                if (pageOffset != 0)
                    SimpleBookmark.shiftPageNumbers(bookmarks, pageOffset, null);
                master.addAll(bookmarks);
            }
            pageOffset += numberOfPages;
            
            if (f == 0) {
                // step 1: creation of a document-object
                document = new Document(reader.getPageSizeWithRotation(1));
                // step 2: we create a writer that listens to the document
                writer = new PdfCopy(document, new FileOutputStream(outFile));
                // step 3: we open the document
                document.open();
            }
            // step 4: we add content
            PdfImportedPage page;
            for (int i = 0; i < numberOfPages; ) {
                ++i;
                page = writer.getImportedPage(reader, i);
                writer.addPage(page);
            }
            PRAcroForm form = reader.getAcroForm();
            if (form != null)
                writer.copyAcroForm(reader);
            
            //release and delete 
            writer.freeReader(reader);
            reader.close();
            File file = new File(filePrefix + invoices[f] + "-invoice.pdf");
            file.delete();
        }
        if (!master.isEmpty())
            writer.setOutlines(master);
        // step 5: we close the document
        document.close();

        LOG.debug("PDF batch file is ready " + outFile);
    }

    
    public void sendEmail() 
    		throws FinderException, NamingException {
    	Integer entityId = batch.getProcess().getEntityId();
    	
    	PreferenceBL prefBL = new PreferenceBL();
    	prefBL.set(entityId, Constants.PREFERENCE_PAPER_SELF_DELIVERY);
    	Boolean selfDelivery = new Boolean(prefBL.getInt() == 1);
    	// If the entity doesn't want to delivery the invoices, then
    	// sapienter has to. Entity 1 is always sapienter.
        Integer pritingEntity;
    	if (!selfDelivery.booleanValue()) {
            pritingEntity = new Integer(1);
    	} else {
    	    pritingEntity = entityId;
        }
    	try {
            NotificationBL.sendSapienterEmail(pritingEntity, "invoice_batch",
            		Util.getSysProp("base_dir") + "invoices/" + entityId + 
            		"-" + batch.getId() + "-batch.pdf", null);
        } catch (Exception e) {
            LOG.error("Could no send the email with the paper invoices " +
                    "for entity " + entityId, e);
        } 
    }
    
    public String generateFile(CachedRowSet cachedRowSet, Integer entityId, 
            String realPath) throws SQLException,
            SessionInternalError, NamingException, DocumentException,
            IOException, FinderException {
        NotificationBL notif = new NotificationBL();
        Vector invoices = new Vector();

        int generated = 0;
        while (cachedRowSet.next()) {
            Integer invoiceId = new Integer(cachedRowSet.getInt(1));
            InvoiceBL invoice = new InvoiceBL(invoiceId);
            LOG.debug("Generating paper invoice " + invoiceId);
            notif.generatePaperInvoiceAsFile(invoice.getEntity());
            invoices.add(invoiceId);
            
            // no more than 1000 invoices at a time, please
            generated++;
            if (generated >= 1000) break;
        }

        if (generated > 0) {
            // merge all these files into a single one
            String hash = String.valueOf(System.currentTimeMillis());
            Integer[] invoicesIds = new Integer[invoices.size()];
            invoices.toArray(invoicesIds);
            compileInvoiceFiles(realPath.substring(0, 
                    realPath.indexOf("_FILE_NAME_")) + "/", 
                    entityId + "-" + hash, entityId, invoicesIds);
    
            return entityId + "-" + hash + "-batch.pdf";
        } else {
            // there was no rows in that query ...
            return null;
        }
    }
    
}

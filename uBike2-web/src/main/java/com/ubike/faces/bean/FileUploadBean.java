/*
 * Copyright 2011, Nabil Benothman, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package com.ubike.faces.bean;

import com.ubike.model.UbikeUser;
import com.ubike.services.MessageServiceLocal;
import com.ubike.services.jms.FileMessage;
import com.ubike.util.CustomArrayList;
import com.ubike.util.CustomList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;

/**
 * {@code FileUploadBean}
 * <p>This class is user as a managed bean for file upload.</p>
 * Created on Jun 6, 2011 at 7:17:22 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
@ManagedBean(name = "fileUploadBean")
@RequestScoped
public class FileUploadBean extends AbstractBean {

    private int uploadsAvailable = 5;
    private boolean autoUpload = false;
    private boolean uploadCompleted = false;
    private boolean useFlash = true;
    private boolean uploadPanelOpen;
    private UbikeUser user;
    private CustomList<UploadedItem> items;
    @EJB
    private MessageServiceLocal messageProducer;

    /**
     * Creates a new instance of {@code FileUploadBean}
     */
    public FileUploadBean() {
        super();
    }

    @PostConstruct
    protected void init() {
        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        this.user = (UbikeUser) ctx.getSessionMap().get("user");
        this.items = new CustomArrayList<UploadedItem>();
    }

    /**
     * 
     * @param event
     * @throws java.io.IOException
     */
    public void upload(FileUploadEvent event) throws IOException {
        UploadedFile item = event.getUploadedFile();
        uploadsAvailable--;
        try {
            final File my_file = save(item);
            if (my_file != null && my_file.exists()) {
                UploadedItem uItem = new UploadedItem(my_file.getName(), my_file.length());
                items.add(uItem);
                FileMessage message = new FileMessage();
                message.setFilename(my_file.getName());
                message.setAbsolutePath(my_file.getAbsolutePath());
                message.setUserId(user.getId());
                messageProducer.sendObjectMessage(message);
            }
        } catch (Exception exp) {
            logger.log(Level.SEVERE, "An error occurs while uploading file or "
                    + "sending Asynchronous message", exp);
        }
    }

    /**
     * Save the uploaded file given by the <tt>UploadItem</tt>to file System.
     *
     * @param item The <tt>UploadItem</tt> that contains all uploaded file details
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    public File save(UploadedFile item) throws FileNotFoundException, IOException {

        int lastIndex = item.getName().lastIndexOf('.');

        String saveName = user.getId() + "_"
                + item.getName().replaceAll("[\\\\/><\\|\\s\"'{}()\\[\\]]+", "_");
        String extension = ".txt";
        if (lastIndex >= 0) {
            extension = item.getName().substring(lastIndex);
        }

        // Create a temporary file placed in the default system temp folder

        File file = File.createTempFile(saveName, extension);

        FileOutputStream fos = new FileOutputStream(file);
        fos.write(item.getData());
        fos.flush();
        fos.close();

        return file;
    }

    /**
     * 
     * @return 
     */
    public boolean getPaginate() {
        return this.items.size() > 10;
    }

    /**
     * Complete the upload
     * @return <tt>success</tt>
     */
    public String uploadComplete() {
        uploadCompleted = true;
        return BaseBean.SUCCESS;
    }

    /**
     *
     * @return
     */
    public String clearUploadData() {
        setUploadsAvailable(5);
        return null;
    }

    /**
     * @return The number of available uploads that that we can do
     */
    public int getUploadsAvailable() {
        return this.uploadsAvailable;
    }

    /**
     * @return True if the auto-upload configuration is set to <tt>true</tt> else
     * False
     */
    public boolean isAutoUpload() {
        return this.autoUpload;
    }

    /**
     * @param autoUpload
     */
    public void setAutoUpload(boolean autoUpload) {
        this.autoUpload = autoUpload;
    }

    /**
     * @return True if the upload is completed
     */
    public boolean isUploadCompleted() {
        return this.uploadCompleted;
    }

    /**
     * @param uploadCompleted
     */
    public void setUploadCompleted(boolean uploadCompleted) {
        this.uploadCompleted = uploadCompleted;
    }

    /**
     * @return
     */
    public boolean isUploadPanelOpen() {
        return this.uploadPanelOpen;
    }

    /**
     * @param uploadPanelOpen
     */
    public void setUploadPanelOpen(boolean uploadPanelOpen) {
        this.uploadPanelOpen = uploadPanelOpen;
    }

    /**
     * @return the useFlash
     */
    public boolean isUseFlash() {
        return this.useFlash;
    }

    /**
     * @param useFlash the useFlash to set
     */
    public void setUseFlash(boolean useFlash) {
        this.useFlash = useFlash;
    }

    /**
     * @param n modify the available uploads number
     */
    public void setUploadsAvailable(int n) {
        this.uploadsAvailable = n;
    }

    /**
     * @return
     */
    public long getTimeStamp() {
        return System.currentTimeMillis();
    }

    /**
     * @return the items
     */
    public CustomList<UploadedItem> getItems() {
        return items;
    }

    /**
     * 
     */
    public static class UploadedItem implements Serializable {

        private String filename;
        private long length;

        /**
         * Create a new instance of {@code UploadedItem}
         */
        public UploadedItem() {
            super();
        }

        /**
         * Create a new instance of {@code UploadedItem}
         * 
         * @param filename
         * @param length 
         */
        public UploadedItem(String filename, long length) {
            this.filename = filename;
            this.length = length;
        }

        /**
         * @return the filename
         */
        public String getFilename() {
            return filename;
        }

        /**
         * @param filename the filename to set
         */
        public void setFilename(String filename) {
            this.filename = filename;
        }

        /**
         * @return the length
         */
        public long getLength() {
            return length;
        }

        /**
         * @param length the length to set
         */
        public void setLength(long length) {
            this.length = length;
        }
    }
}

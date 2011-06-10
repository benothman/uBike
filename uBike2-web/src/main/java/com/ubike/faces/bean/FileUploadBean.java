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
import com.ubike.processor.RawFileProcessor;
import com.ubike.services.TripManagerLocal;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
@ManagedBean(name ="fileUploadBean")
@RequestScoped
public class FileUploadBean {

    private int uploadsAvailable = 5;
    private boolean autoUpload = false;
    private boolean uploadCompleted = false;
    private boolean useFlash = true;
    private boolean uploadPanelOpen;
    @EJB
    private TripManagerLocal tripManager;
    private UbikeUser user;

    /**
     * Creates a new instance of UserBean
     */
    public FileUploadBean() {
        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        this.user = (UbikeUser) ctx.getSessionMap().get("user");
    }

    /**
     * 
     * @param event
     * @throws java.io.IOException
     */
    public void listener(FileUploadEvent event) throws IOException {
        UploadedFile item = event.getUploadedFile();
        uploadsAvailable--;
        try {
            final File my_file = save(item);
            if (my_file != null && my_file.exists()) {
                //UbikeMessage message = new UbikeMessage(my_file.getName(), my_file.getPath());
                //message.setUser(user);
                //message.setTripManager(tripManager);
                if (my_file.getName().endsWith(".xml")) {
                    // starting a new Normalized file processor
                    //message.setType(UbikeMessage.NORMALIZED_GPS_FILE);
                    //NormalizedFileProcessor.getInstance(tripManager, user).process(new File(my_file.getPath()));
                } else if (my_file.getName().matches(".+[\\.][tT][xX][tT]") || my_file.getName().matches(".+[\\.][nN][mM][eE][aA]")) {
                    //message.setType(UbikeMessage.RAW_GPS_FILE);
                    // starting a new Raw file processor
                    RawFileProcessor.create(tripManager, user).process(new File(my_file.getPath()));
                }
                //UbikeJmsProducer.getInstance().sendMessage(message);
            }
        } catch (Exception exp) {
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

        String filename = "/tmp/" + user.getId() + "_" + item.getName();
        File file = new File(filename);

        if (file.exists() || !file.createNewFile()) {
            throw new IOException("File already exist or File creation problem");
        }

        FileOutputStream fos = new FileOutputStream(file);
        fos.write(item.getData());
        fos.flush();
        fos.close();

        return file;
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
}

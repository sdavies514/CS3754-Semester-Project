/*
 * Created by Thomas Corley on 2017.12.04  * 
 * Copyright © 2017 Osman Balci. All rights reserved. * 
 */
package com.mycompany.EntityBeans;

import com.mycompany.managers.Constants;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author tj
 */
@Entity
@Table(name = "ProjectFile")
@XmlRootElement

@NamedQueries({
    @NamedQuery(name = "ProjectFile.findAll", 
            query = "SELECT p FROM ProjectFile p")
    , @NamedQuery(name = "ProjectFile.findById", 
            query = "SELECT p FROM ProjectFile p WHERE p.id = :id")
    , @NamedQuery(name = "ProjectFile.findProjectFilesByProjectId", 
            query = "SELECT p FROM ProjectFile p WHERE p.projectId.id = :projectId")
    , @NamedQuery(name = "ProjectFile.findByFileLocation", 
            query = "SELECT p FROM ProjectFile p WHERE p.fileLocation = :fileLocation")})

public class ProjectFile implements Serializable {

    /*
    ========================================================
    Instance variables representing the attributes (columns)
    of the ProjectFile table in the ThoughtwareDB database.
    ========================================================
     */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "file_location")
    private String fileLocation;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 3, max = 32)
    @Column(name = "file_size")
    private String fileSize;
    
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    @ManyToOne
    private Project projectId;

    /*
    ===================================================================
    Class constructors for instantiating a ProjectFile entity object to
    represent a row in the ProjectFile table in the ThoughtwareDB database.
    ===================================================================
     */
    public ProjectFile() {
    }

    public ProjectFile(Integer id) {
        this.id = id;
    }

    public ProjectFile(Integer id, String fileLocation, String fileSize) {
        this.id = id;
        this.fileLocation = fileLocation;
        this.fileSize = fileSize;
    }
    
    public ProjectFile(String fileLocation, Project id, String fileSize) {
        this.fileLocation = fileLocation;
        this.projectId = id;
        this.fileSize = fileSize;
    }

    /*
    ======================================================
    Getter and Setter methods for the attributes (columns)
    of the ProjectFile table in the ThoughtwareDB database.
    ======================================================
     */
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }
    
    public String getFileSize() {
        return fileSize;
    }
    
    public void setFileSize(String sz) {
        this.fileSize = sz;
    }

    public Project getProjectId() {
        return projectId;
    }

    public void setProjectId(Project projectId) {
        this.projectId = projectId;
    }

    /*
    ================
    Instance Methods
    ================
     */
    /**
     * @return Generates and returns a hash code value for the object with id
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    /**
     * Checks if the ProjectFile object identified by 'object' is the same as the ProjectFile object identified by 'id'
     *
     * @param object The ProjectFile object identified by 'object'
     * @return True if the ProjectFile 'object' and 'id' are the same; otherwise, return False
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProjectFile)) {
            return false;
        }
        ProjectFile other = (ProjectFile) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    /**
     * @return the String representation of a ProjectFile id
     */
    @Override
    public String toString() {
        return id.toString();
    }
    
    /*
    ===================================================
    The following method is added to the generated code
    ===================================================
     */
    public String getFilePath() {
        return Constants.PROJECT_FILES_ABSOLUTE_PATH + getFileLocation();
    }
}

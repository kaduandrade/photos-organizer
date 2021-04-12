package com.kaduandrade;

import java.io.File;
import java.util.Calendar;
import java.util.List;

public class OptionsConfig {

    public OptionsConfig(Calendar date, File file, String targetFolder, boolean moveOriginal, boolean createFolderByDate, List<FileTypes> fileTypesList) {
        this.date = date;
        this.file = file;
        this.targetFolder = targetFolder;
        this.moveOriginal = moveOriginal;
        this.createFolderByDate = createFolderByDate;
        this.fileTypesList = fileTypesList;
    }

    private final Calendar date;
    private final File file;
    private final String targetFolder;
    private final boolean moveOriginal;
    private final boolean createFolderByDate;
    private final List<FileTypes> fileTypesList;

    public Calendar getDate() {
        return date;
    }

    public File getFile() {
        return file;
    }

    public String getTargetFolder() {
        return targetFolder;
    }

    public boolean isMoveOriginal() {
        return moveOriginal;
    }

    public boolean isCreateFolderByDate() {
        return createFolderByDate;
    }

    public List<FileTypes> getFileTypesList() {
        return fileTypesList;
    }
}

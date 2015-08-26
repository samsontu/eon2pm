package edu.stanford.smi.eon.execEngine;

public interface IDataSourceFactory {
    public IDataSource createDataSource(String initFileFullPath) throws Exception;  
    // initFile must contain parameters, such as database(db) server Address, 
    // db port, db name, db username, db password, etc..          

}

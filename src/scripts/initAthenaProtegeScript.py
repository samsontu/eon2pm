"""
 
 Add the argument to the classpath.
 If the argument is a file, it is directly added to sys.path
 If the argument is a directory, the directory path is added to the classpath, as well as all 
 the jar files in the directory or its subdirectories.
 
 Olivier Dameron (dameron@smi.stanford.edu)
 09/20/2004
 
 Make kb global to all modules (i.e., a builtin)
 Add _scriptsDir to the path
 Samson Tu (swt@stanford.edu)
 

"""
# execfile("/Users/tu/Documents/Dropbox/Scripts/Python/VA-related-scripts/ATHENAProject/initAthenaProtegeScript.py")
_scriptsDir = "/Users/tu/Documents/Dropbox/Scripts/Python/VA-related-scripts/ATHENAProject/athena"

import sys
from java.io import File
def addToClasspath(dirPath):
    rootDir = File(dirPath)
    if (rootDir.isFile()):
        sys.path = sys.path + [dirPath]
        return
    if (rootDir.isDirectory()):
        sys.path = sys.path + [dirPath]
        # recursively add the subdirectories and the jar files
        for i in rootDir.listFiles():
            if (i.isDirectory()):
                addToClasspath(i.getPath())
            if (i.isFile()):
                if ((i.getName())[-4:] == ".jar"):
                    sys.path = sys.path + [i.getAbsolutePath()]
                        
#addToClasspath("C:\\Program Files\\Protege_3.3\\plugins\\edu.stanford.smi.protege.script")
#addToClasspath("C:\\apps\\jython\\Lib")
addToClasspath(_scriptsDir)
import __builtin__
__builtin__.kb = kb
from KBQueries import getDataElements,printCollateralActions, \
    getReferencedInstancesOfNamedInstance, printReferencedInstancesOfNamedInstance,\
    printSlotValuesOfInstances, getReferencedInstancesOfInstance, getReferencedFrames
from PrintClassHierarchy import displayDomainConceptHierarchy

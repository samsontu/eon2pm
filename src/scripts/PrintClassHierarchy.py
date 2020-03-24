# PrintClassHierarchy.py
""" Module to print Concept Hierarchy
Display Domain concepts in an indented hierarchy

Available functions
- displayDomainConceptHierarchy: Display domain concepts in an indented hierarchy
- printPropertyHierarchy: Display property hierarchy in an ind
Output format is either 'CSV' or 'HTML'. Default is HTML.

Sample calls
displayDomainConceptHierarchy("Anti-Hypertensive_Drugs_Inclusive", sys.stdout, "CSV")
displayDomainConceptHierarchy("Anti-Hypertensive_Drugs_Inclusive",
    "/Users/tu/Documents/Dropbox/VA/antihypertensives.html", "HTML")
printPropertyHierarchy("http://who.int/icd#postcoordinationAxis", "/Users/tu/Desktop/postcoordinationaxes.txt", None)

"""
from utilities import *
import sys
import string

INCREMENTALINDENT = 4

"""
Installation site

execfile("/Users/tu/Documents/Dropbox/Scripts/Python/VA-related-scripts/ATHENAProject/initAthenaProtegeScript.py")
execfile("/Users/tu/Documents/Dropbox/Scripts/Python/VA-related-scripts/ATHENAProject/athena/PrintClassHierarchy.py")

Sample test code
topCl = b.getCls("CCS-7")
processICDCls(topCls, 0, sys.stdout, None, None)
displayDomainConceptHierarchyWithICD("CCS-7", sys.stdout, None)
displayDomainConceptHierarchyWithICD("Medical_Conditions_Class", "C:/Dropbox/VA/MedicalConditions.html", "HTML")
displayDomainConceptHierarchy("CCS-7", sys.stdout, None)
displayDomainConceptHierarchy("Medical_Conditions_Class", "/Users/tu/Desktop/postcoordinationaxes.txt", None)
displayDomainConceptHierarchy("Medical_Conditions_Class", "/Users/tu/Desktop/Dropbox/VA/DMMedicalConditions.txt", None)
"""

def displayDomainConceptHierarchy(top, outputFile, outputFormat):
    """ Print domain concept hierarchy, with kb-name in parenthesis, if it's different from
    pretty name
    """
    outputDestination = None
    try:
        topCls = kb.getCls(top)
        if (topCls != None):
            outputDestination = open(outputFile, 'w')
            printHeader(outputDestination, outputFormat)
            processCls(topCls, 0, outputDestination, outputFormat)
            printFooter(outputDestination, outputFormat)
    finally:
        if outputDestination:
            outputDestination.close()

def processCls(cls, indent, outputDestination, outputFormat):
    """ Recursively traverse class hierarchy to generate hierarchical display
    """
    printNodeCls(cls, indent, outputDestination, outputFormat)
    subclasses = cls.getDirectSubclasses()
    if (subclasses != None):
        beginList(indent, outputDestination, outputFormat)
        for sub in subclasses:
            processCls(sub, indent+INCREMENTALINDENT, outputDestination, outputFormat)
        endList(indent, outputDestination, outputFormat)

def printPropertyHierarchy(top, outputFile, outputFormat, printRange):
    outputDestination = None
    try:
        topProp = kb.getSlot(top)
        if (topProp != None):
            outputDestination = open(outputFile, 'w')
            printHeader(outputDestination, outputFormat)
            processProp(topProp, 0, outputDestination, outputFormat, printRange)
            printFooter(outputDestination, outputFormat)
    finally:
        if outputDestination:
            outputDestination.close()

def processProp(prop, indent, outputDestination, outputFormat, printRange):
    """ Recursively traverse property hierarchy to generate hierarchical display
    """
    printNodeProp(prop, indent, outputDestination, outputFormat, printRange)
    subprops = prop.getDirectSubslots()
    if (subprops != None):
        beginList(indent, outputDestination, outputFormat)
        for sub in subprops:
            processProp(sub, indent+INCREMENTALINDENT, outputDestination, outputFormat, printRange)
        endList(indent, outputDestination, outputFormat)

def printNodeProp(prop, indent, outputDestination, outputFormat, printRange):
    """ print property name, domain, range
    """
    if (not (outputFormat == CSV)):
        printBlanks(outputDestination,indent, outputFormat)
        beginListItem(outputDestination, outputFormat)
        range = prop.getAllowedClses()
        if (printRange):
            title = prop.getName()+"("
            first = 1
            for r in range:
                if (first):
                    first  =0
                else:
                    title=title+", "
                title = title+r.getName()
            title = title+")"
        else:
            title = prop.getName()
        outputDestination.write(title)
        endListItem(outputDestination, outputFormat)
   




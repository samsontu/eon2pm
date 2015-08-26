# On OT:  execfile("E:/apps/eclipse/workspace/eonpm/src/scripts/ATHENA-KB.py")

# At Stanford: execfile("/Users/tu/workspace/eonpm/src/scripts/ATHENA-KB.py")
#
# 1. Display Domain concepts in an indented hierarchy, showing unmapped ICD children
# For each concept that has some ICD code as children, find the children of ICD codes
# that are not siblings of the ICD codes. 
# For example, foobar has class1, icd1, icd2 as children. icd1 has children icd2, icd3, 
# then icd3 is the missing child of foobar.
# 
# Also add NNN codes to the list when NNN.0 is in the hierarchy.
# 
# topCls=kb.getCls("CCS-7")
# processCls(topCls, 0, sys.stdout, None, None)
# displayDomainConceptHierarchy("CCS-7", sys.stdout, None)
# displayDomainConceptHierarchy("Medical_Conditions_Class", "C:/Dropbox/VA/MissingICDs.html", "HTML")
#
# 2. Print Collateral Actions of a guideline
#
# 3. scripts that refactor the ATHENA KB
#
# 4. Get frames referenced in a guideline (e.g., KB drugs)

import sys
import string
from java.lang import Float

from com.ziclix.python.sql import zxJDBC as sql
ICDQUERY = "SELECT  child FROM icd9parentchild WHERE parent = '"
INCREMENTALINDENT = 4
BLANK=" "
isInHierarchy = 1
addedToHierarchy = 2
isNotInHierarchy = 0
CSV="CSV"
HTML="HTML"

def displayDomainConceptHierarchy(top, outputFile, format):
    outputDestination=None
    db=openDB("jdbc:mysql://localhost:3306/", "umls", "username", "password", "com.mysql.jdbc.Driver")
    cursor = db.cursor()
    try:
        topCls=kb.getCls(top)
        if (topCls != None):
            outputDestination=open(outputFile, 'w')
            printHeader(outputDestination, format)
            processCls(topCls, 0, outputDestination, cursor, format)
            printFooter(outputDestination, format)
    finally:
        if outputDestination:
            outputDestination.close() 

def openDB(host, db, user, pword, driver):
    from com.ziclix.python.sql import zxJDBC
    d, u, p, v =host+db , user, pword, driver
    db=zxJDBC.connect(d, u, p, v)
    return db

def getICDsubCodes(ICD, cursor):
    subcodes = []
    cursor.execute(ICDQUERY+ ICD +"'") 
    results = cursor.fetchall()
    if results:
        for r in results:
            subcodes.append( r[0])
    return subcodes

def processCls(cls, indent, outputDestination, cursor, format):
    printNodeCls(cls, indent, outputDestination, format)
    subclasses = cls.getDirectSubclasses()
    if (subclasses != None):
        ICDChildrenCls = findICDChildren(subclasses)
        ICDChildren = map(normalize, map(lambda x: x.getName(), ICDChildrenCls))
        added = findGeneralCode(ICDChildren)
        extendedChildren = setUnion(added, ICDChildren)
        missingICDs = findMissingICDs( extendedChildren, cursor)
        beginList(indent, outputDestination, format)
        for icd in ICDChildren:
            printICDChildren(icd, cursor, indent+INCREMENTALINDENT, outputDestination, isInHierarchy, format, cls)
        for icd in added:
            printICDChildren(icd, cursor, indent+INCREMENTALINDENT, outputDestination, addedToHierarchy, format, cls)
        for icd in missingICDs:
            printICDChildren(icd, cursor, indent+INCREMENTALINDENT, outputDestination, isNotInHierarchy, format, cls)
        for sub in subclasses:
            if (not (sub in ICDChildrenCls)):
                processCls(sub, indent+INCREMENTALINDENT, outputDestination, cursor, format)
        endList(indent, outputDestination, format)

def printNodeCls(nodeCls, indent, outputDestination, format):
    if (not (format == CSV)):
        printBlanks(outputDestination,indent, format)
        beginListItem(outputDestination, format)
        outputDestination.write(nodeCls.getBrowserText())
        endListItem(outputDestination, format)

def findGeneralCode(ICDChildren):
    toAdd = []
    for child in ICDChildren:
        if ((child[-2:] == ".0") and (not (child[0:-2] in ICDChildren))):
            toAdd.append(child[0:-2])
    return toAdd
    
def    findICDChildren(subclasses):
    ICDs = []
    for sub in subclasses:
        if isICD(sub):
            ICDs.append(sub)
    return ICDs
        
def findMissingICDs(ICDChildrenCodes, cursor):
# Find all subcodes of ICDChildren that are not in ICDChildren
    missingICDs = []
    for icd in ICDChildrenCodes:
        subcodes = getICDsubCodes(icd, cursor)
        missing = setDifference(subcodes, ICDChildrenCodes)
        missingICDs.extend(setDifference(missing, missingICDs))
    return missingICDs

def setDifference(l1, l2):
    difference = []
    for i in l1:
        if (not (i in l2)):
            difference.append(i)
    return difference

def setUnion(l1, l2):
    union = []
    union.extend(l2)
    for i in l1:
        if (not (i in l2)):
            union.append(i)
    return union
    
def normalize(icd):
# Remove last "."
    if (icd[len(icd) -1] == '.'):
        return icd[0:-1]
    else:
        return icd
    
def printICDChildren(icd, cursor, indent, outputDestination, printOption, format, parentCls):
    if (format == CSV):
        outputDestination.write(parentCls.getName()+"\t"+parentCls.getBrowserText()+"\t")
    printBlanks(outputDestination,indent, format)
    beginListItem(outputDestination, format)
    if (printOption == isNotInHierarchy):
        beginBold(outputDestination, format)
    else:
        if (printOption == addedToHierarchy):
            beginItalic(outputDestination, format)
        else:
            outputDestination.write("\t")
    outputDestination.write(icd)
    title = getTitle(icd, cursor)
    if (format == CSV):
        outputDestination.write("\t"+title)
    else:
        outputDestination.write("  ("+title+")")
    if (printOption == addedToHierarchy):
        endItalic(outputDestination, format)    
    if (printOption == isNotInHierarchy):
        endBold(outputDestination, format)
    endListItem(outputDestination, format)

def getTitle(icd, cursor):
    icd = normalize(icd)
    title=[]
    cursor.execute("select distinct childString from icd9parentchild where child =" +"'"+icd+"'") 
    results = cursor.fetchall()
    if results:
        for r in results:
            title.append( r[0])
    if (len(title) == 0):
        return "No such ICD code!"
    else:
        if (len(title) > 1):
            print icd + "has more than 1 titles"
    return title[0]

def printBlanks(outputDestination, n, format):
    if (not (format == CSV)):
        for i in range(n):
               outputDestination.write(BLANK)

def beginList(indent, outputDestination, format):
    if (format == HTML):
        outputDestination.write("\n")
        printBlanks(outputDestination,indent)
        outputDestination.write("<ul>\n")
        

def    endList(indent, outputDestination, format):
    if (format == HTML):
        outputDestination.write("\n")
        printBlanks(outputDestination,indent)
        outputDestination.write("</ul>\n")

def beginListItem(outputDestination, format):
    if (format == HTML):
        outputDestination.write("<li>")

def endListItem(outputDestination, format):
    if (format == HTML):
        outputDestination.write("</li>\n")
    else:
        outputDestination.write("\n")
        
def beginBold(outputDestination, format):
    if (format == HTML):
        outputDestination.write("<strong>")
    else:
        if (format == CSV):
            outputDestination.write("Candidate\t")

def endBold(outputDestination, format):
    if (format == HTML):
        outputDestination.write("</strong>")

def beginItalic(outputDestination, format):
    if (format == HTML):
        outputDestination.write("<italic>*** ")
    else:
        outputDestination.write("Added 3-digit code\t")

def endItalic(outputDestination, format):
    if (format == HTML):
        outputDestination.write(" ****</italic>")

def printHeader(outputDestination, format):
    if (format == HTML):
        outputDestination.write("<html><head></head><body>\n")
    elif (format == "XML"):
        outputDestination.write("<result>\n")
    elif (format == CSV):
        outputDestination.write("Class name\tPretty name\tStatus\tICD\tICD title\n")

def printFooter(outputDestination, format):
    if (format == HTML):
        outputDestination.write("</body></html>\n")
    elif (format == "XML"):
        outputDestination.write("</result>\n")

def isICD(node):
    name = node.getName()
    return (name[1].isdigit() and name[2].isdigit())
    
def isLeafNode(nodeCls):
    return (len(collection2List(nodeCls.getSubclasses())) == 0)


# This is a set of scripts that refactor the ATHENA KB

def listUnusedLeafClasses(top):
    #Leaf node + no frame reference node through user-defined slot
    unreferencedLeaves = []
    topCls = kb.getCls(top)
    if topCls:
        subclasses = topCls.getSubclasses()
        leafClasses = []
        for sub in subclasses:
            if (len(collection2List(sub.getSubclasses())) == 0):
                leafClasses.append(sub)
        print leafClasses
        for leaf in leafClasses:
            refs = leaf.getReferences()
            referenced = 0
            for ref in refs:
                slot = ref.getSlot()
                if (slot.isSystem() == 0):
                    referenced = 1
            if (referenced == 0):
                unreferencedLeaves.append(leaf)
    return unreferencedLeaves

def getLeafClasses(topCls):
    leafClasses = []
    if topCls:
        if (len(collection2List(topCls.getSubclasses())) == 0):
            leafClasses.append(topCls)
        else:
            subclasses = topCls.getSubclasses()
            for sub in subclasses:
                if (len(collection2List(sub.getSubclasses())) == 0):
                    leafClasses.append(sub)
    return leafClasses
    
def removeDuplicates(list):
    if (list == []):
        return []
    set = []
    for i in list:
        if (not i in set):
            if (set == []):
                set = [i]
            else:
                set.append(i)
    return set

def getReferencedLeafClassesOfRoots(rootCls, topCls):
    roots = rootCls.getInstances()
    set = []
    for root in roots:
        if (set == []):
            set = getReferencedLeafClasses(root, topCls)
        else:
            set.extend(getReferencedLeafClasses(root, topCls))
    return removeDuplicates(set)

def getReferencedLeafClasses(root, topCls):    
    clsReferences = getClsReferences(root, [], [], topCls)    
    leafCls = []    
    for cls in clsReferences:
        if (leafCls == []):
            leafCls= getLeafClasses(cls)
        else:
            leafCls.extend(getLeafClasses(cls))
    return removeDuplicates(leafCls)
    
def generateSupportInstances():
    compIndSlot = kb.getSlot("Compelling_Indications")
    relIndSlot = kb.getSlot("Relative_Indications")
    diCls = kb.getCls("Drug_Indication_Relation")
    crelCls = kb.getCls("Drug_Indication_Relation")
    degreeSlot = kb.getSlot("degree")
    drugSlot = kb.getSlot("Drug")
    indSlot = kb.getSlot("indication")
    labelSlot = kb.getSlot("label")
    drugClassNameSlot = kb.getSlot("Drug_Class_Name")
    drugUsages = kb.getCls("Drug_Usage").getInstances()
    for d in drugUsages:
        compelind = d.getOwnSlotValues(compIndSlot)
        relind = d.getOwnSlotValues(relIndSlot)
        if (compelind != None):
            for ci in compelind:
                di = diCls.createDirectInstance("CompellingInd" + ci.getName() + d.getBrowserText())
                di.setOwnSlotValue(degreeSlot, "compelling")
                di.setOwnSlotValue(drugSlot, d.getOwnSlotValue(drugClassNameSlot))
                di.setOwnSlotValue(indSlot, ci)
        if (relind != None):
            for ci in relind:
                di = diCls.createDirectInstance("RelativeInd" + ci.getName() + d.getBrowserText())
                di.setOwnSlotValue(degreeSlot, "relative")
                di.setOwnSlotValue(drugSlot, d.getOwnSlotValue(drugClassNameSlot))
                di.setOwnSlotValue(indSlot, ci)
                

topDomainCls = kb.getCls("Medications_Class")
instanceType = kb.getSlot("eligibility_criteria").getValueType()
clsType = kb.getSlot("encounter_type").getValueType()
domainTerm = kb.getSlot("domain_term")

def getClsReferences(inst, visited, allCls, topDomainCls):
    if (inst in visited):
        return allCls
    else:
        visited.append(inst)
    slots = inst.getOwnSlots()
    for slot in slots:
        if (not (slot.isSystem())):
            values = inst.getOwnSlotValues(slot)
            if (values != None):
                slotType = inst.getOwnSlotValueType(slot)
                if ((slot == domainTerm) or (slotType == clsType) or (slotType == instanceType)): #if v is instance or class, recurse
                    for value in values:
                        if (((slot == domainTerm) or (slotType == clsType)) and value.hasSuperclass(topDomainCls)): # if v is class and a subclass of Medical Domain Class
                            if (not (value in allCls)):
                                allCls.append(value)
                        allCls = getClsReferences(value, visited, allCls, topDomainCls)
    return allCls

def getLocalDiagnosticClasses(allCls):
    diagCls = kb.getCls("Medical_Domain_Class")
    subs = collection2List(diagCls.getSubclasses())
    for s in subs:
        if ((not (s.isIncluded()))and (not (s in allCls))):
            allCls.append(s)
    return allCls
        
def collection2List(collection):
    l = []
    for i in collection:
        l.append(i)
    return l
    
def getLocalClsReferences(instName):
    localCls = []
    inst = kb.getInstance(instName)
    refs = inst.getReferences()
    for ref in refs:
        print ref
        frame = ref.getFrame()
        cls = kb.getCls(frame.getName())
        print cls
        if cls:
            if (not (cls.isSystem())):
                if (not (cls.isIncluded())):
                    localCls.append(cls)
    return localCls

def uploadRecommendations(inputFile):
    # 15 columns. First column is instance name
    # other columns are property values
    first = 1
    lineCount = 0
    slots = [""]
    maxNumColumn = 15
    cls = kb.getCls("Performance_Measure_Criterion")
    for line in open(inputFile):
        lineCount = lineCount+1
        line = line.rstrip()
        if (line == ""):
            continue
        if (first):
            first = 0
            i = 1
            slotStrings = string.split(line, "\t")
            while (i < maxNumColumn):
                slots.append(kb.getSlot(slotStrings[i]))
                i=i+1
        else:
            insertRecommendation(line,slots, cls, maxNumColumn, lineCount)
            
def insertRecommendation(line, slots, cls, maxNumColumn, lineCount):
    entries = string.split(line, "\t")
    i=0
    lineCount = 0
    numColumn = min([maxNumColumn, len(entries)])
    while (i< numColumn):
        if (i==0):
            instance = cls.createDirectInstance(entries[0]+str(lineCount))
        else:
            if (string.find(entries[i], "@@") > 0):
                values = string.split(entries[i], "@@")
                for value in values:
                    instance.addOwnSlotValue(slots[i], value)
            else:
                if ((entries[i] != "") and (entries[i] != None)):
                    entryData = entries[i].replace("&&&", "\n")
                    # if (string.find(entries[i], "&&&") > 0):
                        # print entryData
                    if (i in [2, 3,4,5,6]): #source
                        entryData = kb.getInstance("PMSource"+entryData.replace(" ", ""))
                        print entryData
                        instance.addOwnSlotValue(slots[i], entryData)
                    else:
                        instance.setOwnSlotValue(slots[i], entryData)
        i=i+1
    
# Print Collateral Actions of a guideline
drugUsagesSlot = kb.getSlot("drug_usages")
collateralActionsSlot = kb.getSlot("collateral_actions")
labelProp = kb.getSlot("label")
moodProp = kb.getSlot("mood")
actionsProp = kb.getSlot("actions")
messageTypeProp = kb.getSlot("message_type")
ruleInProp = kb.getSlot("rule_in_condition")
messageProp = kb.getSlot("message")

def printCollateralActions(guideline, htmlFile):
    htmlOutput=open(htmlFile, 'w')
    printCollateralActionsToOutput(guideline, htmlOutput)
    htmlOutput.close()     

def printCollateralActionsToOutput(guideline, htmlOutput):
    printHeader(htmlOutput, "HTML")
    drugUsages = guideline.getOwnSlotValues( drugUsagesSlot) 
    for du in drugUsages:
        ca = du.getOwnSlotValues(collateralActionsSlot)
        if (ca):
            htmlOutput.write(du.getOwnSlotValue(labelProp)+"<ul>")
            for a in ca:
                printCollateralAction(htmlOutput, a)
            htmlOutput.write("</ul>\n")
    printFooter(htmlOutput, "HTML")       

def printCollateralAction(htmlOutput, collatAction):
    mood = collatAction.getOwnSlotValue(moodProp)
    if (not mood):
        mood = " "
    else:
        mood= " "+mood +": "
    actions = collatAction.getOwnSlotValues(actionsProp)
    for action in actions:
        messageType = action.getOwnSlotValue(messageTypeProp)
        message = action.getOwnSlotValue(messageProp)
        label = action.getOwnSlotValue(labelProp)
        ruleIn = action.getOwnSlotValue(ruleInProp)
        if (messageType):
            messageTypeString = "<i>"+messageType +"</i>"
        else:
            messageTypeString = " "
        if (message):
            messageString = message
        else:
            messageString = label
        if (ruleIn):
            ruleInString = " if ("+ruleIn.getBrowserText()+")"
        else:
            ruleInString = " "
        htmlOutput.write("<li>" + mood+ ruleInString + messageString + "</li>\n")

# Usage: 
# referenced = getReferencedFrames(kb.getInstance(guidelineInstanceName), Set([]))
# medConditions = getMedicalConditions(referenced)
# getUsages(medConditions, referenced)
#
# e.g., For ATHENA HF Astronaut, the guidelineInstanceName is "ATHENA_HF_Class120000".
# referenced = getReferencedFrames(kb.getInstance("ATHENA_HF_Class120000"), Set([]))
# medConditions = getMedicalConditions(referenced)
# getUsages(medConditions, referenced)

# Get frames referenced in a guideline
from sets import Set    
def getReferencedFrames(frame, referencedFrames):
    referencedFrames.add(frame)
    ownSlots = frame.getOwnSlots()
    for slot in ownSlots:
        if (slot == kb.getSlot(":DIRECT-TYPE")):
            continue
        if (slot == kb.getSlot(":DIRECT-INSTANCES")):
            continue
        if (slot == kb.getSlot(":DIRECT-SUPERCLASSES")):
            continue
        if (slot == kb.getSlot(":DIRECT-SUBCLASSES")):
            continue
        slotValues = frame.getOwnSlotValues(slot)
        for value in slotValues:
            if (isFrame(value) and (not (value in referencedFrames))):
                referencedFrames = getReferencedFrames(value, referencedFrames)
    return referencedFrames
    
import edu.stanford.smi.protege.model.Frame    
def isFrame(obj):
    return isinstance(obj, edu.stanford.smi.protege.model.Frame)

# get referenced medical conditions    
def getMedicalConditions(referenced):
    conditions = kb.getCls("Medical_Conditions_Class").getSubclasses()
    return referenced.intersection(Set(conditions))

# Get usage of a particular set of frames (e.g., medical conditions) 
def getUsages(frames, filterSet):
    for f in frames:
        print f.getBrowserText()
        for r in f.getReferences():
            toPrint = 1
            if (r.getSlot().getName() == ":DIRECT-TYPE"):
                continue
            if (r.getSlot().getName() ==":DIRECT-SUPERCLASSES"):
                continue
            if (r.getSlot().getName()==":DIRECT-INSTANCES"):
                continue
            if (r.getSlot().getName()==":DIRECT-SUBCLASSES"):
                continue
            if (filterSet):
                if (not (r.getFrame() in filterSet)):
                    toPrint = 0
            if (toPrint == 1):
                print "    In '"+ r.getFrame().getBrowserText()+"'("+r.getFrame().getDirectType().getName()+"), used as "+r.getSlot().getName()

'''
Get criteria with temporal specifications involved in laboratory tests and vitals

getGuidelineCriteriaQueriesWithTimeSpec("ATHENA_GlycemicControl_Class130049")
getGuidelineCriteriaQueriesWithTimeSpec("ATHENA_HTN_Dashboard_Class10000")
getGuidelineCriteriaQueriesWithTimeSpec("ATHENA_HF_Class120000")

'''            
_criteriaAndQueryTypesWithTemporalSpec = ["General_Comparison_Criterion", "Numeric_Term_Criterion", 
    "Presence_Criterion", "Numeric_Term_Query", "Qualitative_Term_Query"]
_domainTermSlot = ["domain_term", "numeric_domain_term", "domain_term", "numeric_domain_term", 
"qualitative_domain_term"]
_timeSlot = ["valid_window", "valid_window", "period", "period", "period"]
_domainTermType = ["Class", "Class", "Class", "String", "String"]
    
def getGuidelineCriteriaQueriesWithTimeSpec(guideline):
    guidelineInst = kb.getInstance(guideline)
    if (not guidelineInst):
        print "Illegal guideline "+guideline
        return
    print "Guideline "+guidelineInst.getBrowserText()+"--- Parameters and Their Use in Criteria and Queries with Time Specification\n"
    referencedFrames = getReferencedFrames(guidelineInst, Set([]))
    getCriteriaAndQueriesInvolvingDataElement(referencedFrames, "Diagnostic_Procedures_Class")
    getCriteriaAndQueriesInvolvingDataElement(referencedFrames, "Medical_Conditions_Class")
    getCriteriaAndQueriesInvolvingDataElement(referencedFrames, "Patient_History_Class")
    getCriteriaAndQueriesInvolvingDataElement(referencedFrames, "Findings")
    getCriteriaAndQueriesInvolvingDataElement(referencedFrames, "Laboratory_Tests")
    getCriteriaAndQueriesInvolvingDataElement(referencedFrames, "Vital_Signs")
    getCriteriaAndQueriesInvolvingDataElement(referencedFrames, "Procedure_finding")
    getCriteriaAndQueriesInvolvingDataElement(referencedFrames, "Medications_Class")
    getCriteriaAndQueriesInvolvingDataElement(referencedFrames, "Therapeutic_Procedure")
    getCriteriaAndQueriesInvolvingDataElement(referencedFrames, "nutritional supplements")
    for criterion in Set(kb.getCls("Structured_Query").getInstances()).intersection(referencedFrames):
        print "Structured_Query("+criterion.getBrowserText()+")"
    for criterion in Set(kb.getCls("PAL_Query").getInstances()).intersection(referencedFrames):
        print "PAL_Query("+criterion.getBrowserText()+")"
    for criterion in Set(kb.getCls("PAL_Criterion").getInstances()).intersection(referencedFrames):
        print "PAL_Criterion("+criterion.getBrowserText()+")"


    
def getCriteriaAndQueriesInvolvingDataElement(referencedFrames, dataElementType):
    
    dataElementCls = kb.getCls(dataElementType)
    if (not dataElementCls):
        print "Illegal dataElementType "+dataElementType
        return
    referencedCriteriaAndQueries = Set([])
    for criteriaQueryType in _criteriaAndQueryTypesWithTemporalSpec:
        referencedCriteriaAndQueries.union(Set(kb.getCls(criteriaQueryType).getInstances()).intersection(
            referencedFrames))
    for cls in dataElementCls.getSubclasses():
        if (not (cls in referencedFrames)):
            continue
        criteriaAndQueriesWithDataElement = []
        for criteriaType, domainTermSlot, timeSlot, domainTermType in zip(
            _criteriaAndQueryTypesWithTemporalSpec, _domainTermSlot, _timeSlot, _domainTermType):
            for criterion in Set(kb.getCls(criteriaType).getInstances()).intersection(referencedFrames ):
                if  (((domainTermType == "Class") and 
                      (criterion.getOwnSlotValue(kb.getSlot(domainTermSlot)) == cls)) or
                     ((domainTermType == "String") and 
                      (criterion.getOwnSlotValue(kb.getSlot(domainTermSlot)) == cls.getName()))):
                    if (criterion.getOwnSlotValue(kb.getSlot(timeSlot))):
                        temporalSpec = criterion.getOwnSlotValue(kb.getSlot(timeSlot)).getBrowserText()
                    else:
                        temporalSpec = "No time specification"
                    criteriaAndQueriesWithDataElement.append(criteriaType+
                        " ("+criterion.getBrowserText()+"): "+temporalSpec)
        if (not (criteriaAndQueriesWithDataElement == [])):
            print cls.getName()
            for item in criteriaAndQueriesWithDataElement:
                print "\t"+item

def getGenericDrugs():
    genericDrugs = []
    topMedCls = kb.getCls("Medications_Class")
    for sub in topMedCls.getSubclasses():
        nsubs = len(collection2List(sub.getSubclasses()))
        if (nsubs <= 1):
            genericDrugs.append(sub)
    return [x.getName() for x in genericDrugs]
    
def getReferencedFramesOfGivenType(guideline, dataElementType, useSubclass):
    guidelineInst = kb.getInstance(guideline)
    if (not guidelineInst):
        print "Illegal guideline "+guideline
        return
    dataElementCls = kb.getCls(dataElementType)
    referencedFrames = getReferencedFrames(guidelineInst, Set([]))
    if (dataElementCls):
        if (useSubclass):
            return referencedFrames.intersection(Set(dataElementCls.getSubclasses()))
        else:
            return referencedFrames.intersection(Set(dataElementCls.getInstances()))
    else:
        return referencedFrames

def getReferencedAndSubclassFramesOfGivenType(guideline, dataElementType, useSubclass):
    referenced = getReferencedFramesOfGivenType(guideline, dataElementType, useSubclass)
    referencedAndSubclass = referenced 
    for ref in referenced:
        if (kb.isMetaCls(ref.getDirectType())):
            referencedAndSubclass = referencedAndSubclass.union(ref.getSubclasses())
    return referencedAndSubclass

def getKBDrugs(guideline, output):
#    return sort(x.getName() for x in getReferencedAndSubclassFramesOfGivenType(guideline, "Medications_Class", 1))
    drugs = getReferencedAndSubclassFramesOfGivenType(guideline, "Medications_Class", 1)
    drugNames = map (lambda x: x.getName(), drugs)
    leafNodes = map (lambda x: (len(collection2List(x.getSubclasses())) == 0), drugs)
    for dn, isLeaf in zip(drugNames, leafNodes):
        output.write( "%s\t%d\n" % (dn, isLeaf))
    return drugs

def printKBDrugsToFile(guideline, fileName):
    output = open(fileName, 'w')
    getKBDrugs(guideline, output)
    output.close()

def setIncreaseDoseCeiling():
    for gd in kb.getCls("Guideline_Drug").getInstances():
        LBofHighestLevel =getLBofHighestLevel(collection2List(gd.getOwnSlotValues(kb.getSlot("dose_level_ranges"))))
        if (LBofHighestLevel):
            gd.setOwnSlotValue(kb.getSlot("increase_dose_ceiling"), Float(LBofHighestLevel))
            
def getLBofHighestLevel(ranges):
    LL = 0.0
    if (ranges):
        for r in ranges:
            cLL = r.getOwnSlotValue(kb.getSlot("lower_limit"))
            if ((cLL) and (cLL > LL)):
                LL = cLL
    if (LL > 0.0):
        return LL
    else:
        return None


'''
Programmatically construct queries for parameterized messages

'''
KBs = ["HTN", "HF", "Glycemic Control", "CKD", "Lipid"]
contexts = ["Algorithm Messages", "Consultation Messages", "collateral messages"]
aParamString = " <p>*$id$*<b>-   ?label</b>%#?value#%</p>   "
varCollection = ["ATHENA_CVD_Class20000", "ATHENA_CVD_Class17"]
palRanges = { "Algorithm Messages" : """(defrange ?message :FRAME Parameterized_Message) 
(defrange ?algorithm :FRAME Management_Diagram)
(defrange ?action :FRAME Action_Choice)
(defrange ?string :FRAME Parameterized_String)""",
"Consultation Messages":"""(defrange ?message :FRAME Parameterized_Message)
(defrange ?algorithm :FRAME Management_Diagram)
(defrange ?action :FRAME Consultation_Action_Step)
(defrange ?scenario :FRAME Scenario)
(defrange ?consult :FRAME Consultation_Guideline)
(defrange ?string :FRAME Parameterized_String)""", 
"collateral messages": """(defrange ?message :FRAME Message)
(defrange ?guideline :FRAME Management_Guideline)
(defrange ?drugUsage :FRAME Drug_Usage)
          (defrange ?collateral :FRAME Collateral_Action)
          (defrange ?string :FRAME Parameterized_String)"""
}
palStatements = {"Algorithm Messages" : """(findall ?string 
         (exists ?algorithm (and (label ?algorithm \"**glName**\")
                                 (exists ?action (and (steps ?algorithm ?action)
                                                      (exists ?message (and (actions ?action ?message)
                                                                            (parameterized_message ?message ?string))
                                                      )))
                            )))""",
 "Consultation Messages":"""(findall ?string 
         (exists ?algorithm (and (label ?algorithm \"**glName**\")
                                 (exists ?scenario (and (steps ?algorithm ?scenario)
                                                        (exists ?consult (and (consultation_template ?scenario ?consult)
                                                                              (exists ?action (and (steps ?consult ?action)
                                                                                         (exists ?message (and (actions ?action ?message)
                                                                                             (parameterized_message ?message ?string))
                                                                                         ))))))))))""",  
"collateral messages": """(findall ?string 
         (and (exists ?guideline (and (label ?guideline \"**glName**\")
                                      (exists ?drugUsage (and (or (drug_usages ?guideline ?drugUsage))
                                                              (exists ?collateral (and  (collateral_actions ?drugUsage ?collateral)
                                                                                       (exists ?message (and (actions ?collateral ?message)
                                                                                       (parameterized_message ?message ?string))
                                                         )))))))))
 """}
glNames = {"HTN":"JNC8_Dashboard", 
           "HF":"ATHENA HF Astronaut", 
           "Glycemic Control":"ATHENA DM:  Glycemic Control Dashboard", 
           "CKD":"ATHENA CKD dashboard", 
           "Lipid":"Lipid Management ACC AHA"
           }
algNames = {"HTN":"JNC8_Dashboard", 
           "HF":"ATHENA HF Astronaut algorithm", 
           "Glycemic Control":"Glycemic Control Dashboard III", 
           "CKD":"CKD dashboard", 
           "Lipid":"Statin recommendations III"
           }


def createParamMsgQuery():
    for gl in KBs:
        for context in contexts:
            #find the action choice actionChoice
            choices = kb.getCls("Action_Choice").getInstances()
            actionChoice = None
            for c in choices:
                if ((c.getOwnSlotValue(labelProp).find(gl) != -1) and (c.getOwnSlotValue(labelProp).find(context) != -1)):
                    actionChoice = c
                    break
            if (actionChoice == None):
                print gl+" "+context+ " does not have an action choice"
                break
            paramMsg = kb.createInstance(None, kb.getCls("Parameterized_Message"))
            actionChoice.addOwnSlotValue(kb.getSlot("actions"), paramMsg)
            paramString = kb.createInstance(None, kb.getCls("Parameterized_String"))
            paramMsg.setOwnSlotValue(kb.getSlot("parameterized_message"), paramString)
            paramMsg.setOwnSlotValue(kb.getSlot("message_type"), kb.getCls("Recommendation"))
            paramMsg.setOwnSlotValue(labelProp, gl+" Parameterized "+context)
            paramString.setOwnSlotValue(labelProp, gl+ " Parameterized "+context)
            paramString.setOwnSlotValue(kb.getSlot("value"), aParamString)
            varValueSet = kb.createInstance(None, kb.getCls("Variable_ValueSet"))
            paramString.setOwnSlotValue(kb.getSlot("multivalued_variables"), varValueSet)
            for var in varCollection:
                varValueSet.addOwnSlotValue(kb.getSlot("property_values_to_get"), kb.getInstance(var))
            varValueSet.setOwnSlotValue(labelProp, gl+" Parameterized "+context)
            #varValueSet.setOwnSlotValue(kb.getSlot("property_values_to_get"), varCollection)
            ATHpalQuery= kb.createInstance(None, kb.getCls("PAL_Query"))
            varValueSet.setOwnSlotValue(kb.getSlot("derivation_expression"), ATHpalQuery)
            ATHpalQuery.setOwnSlotValue(labelProp, "Query "+gl+" Parameterized "+context)
            ATHpalQuery.setOwnSlotValue(kb.getSlot("case_variable"), "$patient_id")
            palQuery = kb.createInstance(None, kb.getCls("PAL-QUERY"))
            ATHpalQuery.setOwnSlotValue(kb.getSlot("PAL_query"), palQuery)
            palQuery.setOwnSlotValue(kb.getSlot(":PAL-NAME"), "Query "+gl+" Parameterized "+context)
            palQuery.setOwnSlotValue(kb.getSlot(":PAL-RANGE"), palRanges[context])
            if (context == "collateral messages"):
                palStatement = palStatements[context].replace("**glName**", glNames[gl])
            else:
                palStatement = palStatements[context].replace("**glName**", algNames[gl])
            palQuery.setOwnSlotValue(kb.getSlot(":PAL-STATEMENT"), palStatement)

'''    
getGuidelineCriteriaQueriesWithTimeSpec("ChronicPainOpioid_Instance_1")
 printKBDrugsToFile("ATHENA_HTN_Dashboard_Class10000", "/Users/tu/workspace/eonpm/resources/HTN_KBDrugs.txt")
 printKBDrugsToFile("ATHENA_GlycemicControl_Class130049", "/Users/tu/workspace/eonpm/resources/GlycemicControl_KBDrugs.txt")
 printKBDrugsToFile("ATHENA_CKD_Class40022", "/Users/tu/workspace/eonpm/resources/CKD_KBDrugs.txt")
 printKBDrugsToFile("ATHENA_HF_Class120000", "/Users/tu/workspace/eonpm/resources/HF_KBDrugs.txt")
 printKBDrugsToFile("Lipid_dashboard_Class0", "/Users/tu/workspace/eonpm/resources/Lipid_dashboard_KBDrugs.txt")
 
'''

import re

def preprocessMessages(inputFile, inKBChangedMessagesFile):
	inKBChangedMessages = open(inKBChangedMessagesFile, 'w')

def processMessages(inputFile, notInKBChangedMessagesFile, notWritableChangedMessagesFile,
	inKBChangedMessagesFile, preprocess):
	inKBChangedMessages = open(inKBChangedMessagesFile, 'w')
	allInputText=open(inputFile, 'r').read()
	if (not preprocess):
		notInKBChangedMessages = open(notInKBChangedMessagesFile, 'w')
		notWritableChangedMessages = open(notWritableChangedMessagesFile, 'w')
	else:
		notInKBChangedMessages =None
		notWritableChangedMessages =None
	instanceIDs = getAllMatches(allInputText, r"Message\$([a-zA-Z0-9_]+)\$\*")
	messages = getAllMatches(allInputText, r"\%\#(.+?(?=\#\%))")
	for instanceID, newMessage in zip(instanceIDs, messages):
		 instance = kb.getInstance(instanceID)
		 if (instance != None):
		 	oldMessage= getOldMessage(instance)
		 	if (newMessage != oldMessage):
		 		if (instance.isIncluded()):
		 			writeMessage(notWritableChangedMessages, instanceID, newMessage)
		 		else:
		 			setMessage(instance, newMessage)
		 			writeMessage(inKBChangedMessages, instanceID, newMessage)
		 else:
		 	writeMessage(notInKBChangedMessages, instanceID, newMessage)
	input.close()
	inKBChangedMessages.close()
	if (not preprocess):
		notInKBChangedMessages.close()	
		notWritableChangedMessages.close()
		
def writeMessage(destination, instanceID, message):
	if (destination != None):
		destination.write('<!--Message$%s$*-->%#%s#%\n'% (instanceID, message))
		
def getAllMatches(input, regexp):
	pattern = re.compile(regexp, re.DOTALL)
	return pattern.findall(input)
	


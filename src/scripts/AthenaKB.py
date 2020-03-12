"""

# execfile("/Users/tu/Documents/Dropbox/Scripts/Python/VA-related-scripts/ATHENAProject/athena/AthenaKB.py")
"""

from sets import Set
from KBQueries import *
# Should have run initAthenaProtegeScript.py

topDomainCls = kb.getCls("Medications_Class")
instanceType = kb.getSlot("eligibility_criteria").getValueType()
clsType = kb.getSlot("encounter_type").getValueType()
domainTerm = kb.getSlot("domain_term")

# Module: Refactor KB
# This is a set of scripts that refactor the ATHENA KB

def listUnusedLeafClasses(top):
    #Leaf node + no frame reference node through user-defined slot
    unreferencedLeaves = []
    topCls = kb.getCls(top)
    if topCls:
        subclasses = topCls.getSubclasses()
        leafClasses = []
        for sub in subclasses:
            if (len(list(sub.getSubclasses())) == 0):
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
        if (len(list(topCls.getSubclasses())) == 0):
            leafClasses.append(topCls)
        else:
            subclasses = topCls.getSubclasses()
            for sub in subclasses:
                if (len(list(sub.getSubclasses())) == 0):
                    leafClasses.append(sub)
    return leafClasses

# turs a list into a set 
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

def getPrimitiveReferencedLeafClasses(root, topcls):
    referencedLeaves = getReferencedLeafClasses(root, topCls)
    defined = kb.getCls("Diagnostic_Term_Metaclass")
    return list(filter (lambda x: (not x.hasDirectType(defined)), referencedLeaves))

def printPrettyList(list):
	p = kb.getSlot("PrettyName")
	for c in list:
		if (c.getOwnSlotValue(p)):
			print c.getName()+ "\t\t"+ c.getOwnSlotValue(p)
		else:
			print c.getName()		

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
    subs = list(diagCls.getSubclasses())
    for s in subs:
        if ((not (s.isIncluded()))and (not (s in allCls))):
            allCls.append(s)
    return allCls
        
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

    

"""
Module: printReport: Print report about medical conditions, medications, lab and vitals, adverse
   reactions, etc. that are used by the kb

Usage: 

guidelineInstanceName = "ATHENA_HTN_Dashboard_Class10000"
referenced = getReferencedFrames(kb.getInstance(guidelineInstanceName), Set([]))
medConditions = getMedicalConditions(referenced)
getUsages(medConditions, referenced)

e.g., For ATHENA HF Astronaut, the guidelineInstanceName is "ATHENA_HF_Class120000".
referenced = getReferencedFrames(kb.getInstance("ATHENA_HF_Class120000"), Set([]))
medConditions = getMedicalConditions(referenced)
getUsages(medConditions, referenced)

hf = "ATHENA_HF_Class120000"
ckd = "ATHENA_CKD_Class40022"
lipid = "Lipid_dashboard_Class0"
htn = "ATHENA_HTN_Dashboard_Class10000"
dm = "ATHENA_GlycemicControl_Class130049"
ot = "ChronicPainOpioid_Instance_2"
"""

def printPotentialErrors(top):
    referenced = getReferencedFrames(kb.getInstance(top), Set([]))
    medConditions = getMedicalConditionsNotDefined(referenced)
    medCondCheck = getMedicalConditions(referenced)
    sys.stdout.write ( "Warning: ")
    printCommaSeparatedList(medConditions - medCondCheck, stdout)
    print " is/are not instances of Medical_Conditions_Metaclass\n"
		
def printInstancesWithClassesAndTime(inst):
    p1 = kb.getSlot("period")
    p2 = kb.getSlot("valid_time")
    a = kb.getSlot("aggregation_operator")
    for i in inst:
        if (i.getOwnSlotValue(p1)):
            temporalSpec = i.getOwnSlotValue(p1).getBrowserText()
        elif (i.getOwnSlotValue(p2)):
            temporalSpec= i.getOwnSlotValue(p2).getBrowserText()
        else:
            temporalSpec="None"
        if (i.getOwnSlotValue(a)):
        	aggregation = i.getOwnSlotValue(a)
        else:
        	aggregation = "None"
        print i.getDirectType().getName()+"\t'"+i.getBrowserText()+"'\t"+aggregation+"\t"+temporalSpec
	
def printInstancesWithClasses(inst):
	for i in inst:
		print i.getDirectType().getName()+"\t\t"+i.getBrowserText()
            

# Get usage of a particular set of frames (e.g., medical conditions) 
# Inputs:
#   frames: the set of frames of a particular kind (e.g., medical conditions)
#   filterSet: the set of frames for which usage reports should be generated
# Output:
#   listing of frames' usages

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

"""
Get criteria with temporal specifications involved in laboratory tests and vitals

Available function
getGuidelineCriteriaQueriesWithTimeSpec : Get guideline criteria and queries that have a temporal component

Test calls
getGuidelineCriteriaQueriesWithTimeSpec("ATHENA_GlycemicControl_Class130049", 1)
getGuidelineCriteriaQueriesWithTimeSpec("ATHENA_HTN_Dashboard_Class10000")
getGuidelineCriteriaQueriesWithTimeSpec("ATHENA_HF_Class120000")

"""        
_criteriaAndQueryTypesWithTemporalSpec = ["General_Comparison_Criterion", "Numeric_Term_Criterion", 
    "Presence_Criterion", "Numeric_Term_Query", "Qualitative_Term_Query"]
_domainTermSlot = ["domain_term", "numeric_domain_term", "domain_term", "numeric_domain_term", 
"qualitative_domain_term"]
_timeSlot = ["valid_window", "valid_window", "period", "period", "period"]
_aggregation = "aggregation_operator"
_domainTermType = ["Class", "Class", "Class", "String", "String"]
_dmGuideline = "ATHENA_GlycemicControl_Class130049"
    

def getGuidelineCriteriaQueriesWithTimeSpec(guideline, filterMostRecent):
    guidelineInst = kb.getInstance(guideline)
    if (not guidelineInst):
        print "Illegal guideline "+guideline
        return
    print "Guideline "+guidelineInst.getBrowserText()+"--- Parameters and Their Use in Criteria and Queries with Time Specification\n"
    referencedFrames = getReferencedFrames(guidelineInst, Set([]))
    getCriteriaAndQueriesInvolvingDataElement(referencedFrames, "Diagnostic_Procedures_Class", filterMostRecent)
    getCriteriaAndQueriesInvolvingDataElement(referencedFrames, "Medical_Conditions_Class", filterMostRecent)
    getCriteriaAndQueriesInvolvingDataElement(referencedFrames, "Patient_History_Class", filterMostRecent)
    getCriteriaAndQueriesInvolvingDataElement(referencedFrames, "Findings", filterMostRecent)
    getCriteriaAndQueriesInvolvingDataElement(referencedFrames, "Laboratory_Tests", filterMostRecent)
    getCriteriaAndQueriesInvolvingDataElement(referencedFrames, "Vital_Signs", filterMostRecent)
    getCriteriaAndQueriesInvolvingDataElement(referencedFrames, "Procedure_finding", filterMostRecent)
    getCriteriaAndQueriesInvolvingDataElement(referencedFrames, "Medications_Class", filterMostRecent)
    getCriteriaAndQueriesInvolvingDataElement(referencedFrames, "Therapeutic_Procedure", filterMostRecent)
    getCriteriaAndQueriesInvolvingDataElement(referencedFrames, "nutritional supplements", filterMostRecent)
    for criterion in Set(kb.getCls("Structured_Query").getInstances()).intersection(referencedFrames):
    	if (criterion.getOwnSlotValue(kb.getSlot(_aggregation))):
            aggregationSpec = criterion.getOwnSlotValue(kb.getSlot(_aggregation))
        else:
            aggregationSpec = "No aggregation operator"
        if (filterMostRecent):
            type = criterion.getOwnSlotValue(kb.getSlot("type"))
            if (type != None) and (type.getName() == "Numeric_Entry"
                or (type.getName() == "Note_Entry")):
                print "\tStructured_Query("+criterion.getBrowserText()+ ")\t"+ aggregationSpec
        else:
            print "\tStructured_Query("+criterion.getBrowserText()+ ")\t"+ aggregationSpec
    for criterion in Set(kb.getCls("PAL_Query").getInstances()).intersection(referencedFrames):
        print "\tPAL_Query("+criterion.getBrowserText()+")"
    for criterion in Set(kb.getCls("PAL_Criterion").getInstances()).intersection(referencedFrames):
        print "\tPAL_Criterion("+criterion.getBrowserText()+")"

def getCriteriaAndQueriesInvolvingDataElement(referencedFrames, dataElementType, filterMostRecent):
    #
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
                    if (criterion.getOwnSlotValue(kb.getSlot(_aggregation))):
                        aggregationSpec = criterion.getOwnSlotValue(kb.getSlot(_aggregation))
                    else:
                        aggregationSpec = "No aggregation operator"
                    if (filterMostRecent):
                        if ((aggregationSpec == "No aggregation operator") or
                            (aggregationSpec == "most_recent") or
                            (temporalSpec == "No time specification")):
                            continue
                        criteriaAndQueriesWithDataElement.append(criteriaType+
                        " ("+criterion.getBrowserText()+"): \t"+temporalSpec+ "; \t"+ aggregationSpec)
        if (not (criteriaAndQueriesWithDataElement == [])):
            print cls.getName()
            for item in criteriaAndQueriesWithDataElement:
                print "\t"+item

def getGenericDrugs():
    genericDrugs = []
    topMedCls = kb.getCls("Medications_Class")
    for sub in topMedCls.getSubclasses():
        nsubs = len(list(sub.getSubclasses()))
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
    
'''
Guideline model entities with decision points
1. Management_Guideline
    - eligibility_criteria
2. Management_Diagram
    - Scenario
3. Action_Choice
    - strict_rule_in_condition
4. Case_Step: count number of case steps
5. Consultation_Action_Step
    - rule_in
6. Action_Specification
    - rule_in_condition
7. Evaluate_Start_Activity
    - count number of alternatives
8. Evaluate_Substitution_Activity
    - count number of alternatives
''' 

def getReferencedInstances(referenced, className):
    conditions = kb.getCls(className).getInstances()
    return referenced.intersection(Set(conditions))

def countDecisionPoints(referencedFrames):
    decisionPoints = 2 #eligibility + scenario
    for a in getReferencedInstances(referencedFrames, "Action_Choice"):
        if (hasChoice(a.getOwnSlotValue(kb.getSlot("strict_rule_in_condition")))):
            decisionPoints = decisionPoints+1
    print "with Action Choices " + str(decisionPoints)
    decisionPoints = decisionPoints+ len(getReferencedInstances(referencedFrames, "Case_Step"))
    print "with Case Steps "+str(decisionPoints)
    for a in getReferencedInstances(referencedFrames, "Consultation_Action_Step"):
        if (hasChoice(a.getOwnSlotValue(kb.getSlot("rule_in")))):
            decisionPoints = decisionPoints+1
    print "with Consultation Action Steps "+str(decisionPoints)
    for a in getReferencedInstances(referencedFrames, "Action_Specification"):
        if (hasChoice(a.getOwnSlotValue(kb.getSlot("rule_in_condition")))):
            decisionPoints = decisionPoints+1
    print "with Action Specifications "+str(decisionPoints)
    alt = Set([])
    for a in getReferencedInstances(referencedFrames, "Evaluate_Start_Activity"):
        alt = alt.union(Set(a.getOwnSlotValues(kb.getSlot("alternatives"))))
    decisionPoints = decisionPoints+len(alt)
    print "with Evaluate Start Activities "+str(decisionPoints)
    for a in getReferencedInstances(referencedFrames, "Evaluate_Substitution_Activity"):
        decisionPoints = decisionPoints+len(a.getOwnSlotValues(kb.getSlot("alternatives")))
    return decisionPoints
        
def hasChoice(criterion):
    if ((criterion == None) or (criterion.getBrowserText() == "true") or (criterion.getBrowserText() == "false")):
        return 0
    else:
        return 1
        
        
'''
Dump instance information into tab-delimited format

DumpToSheet(outputFile, className, directInstancesp, filteredInstances, classesWhoseInstancesToExpand, delimiter)

outputFile: path to a local file that holds output
className: name of class whose instances should be processed
directInstancesp: if true, get only direct instances; otherwise get instances of subclasses as well
filteredInstances: if not null, the list of instances that should be processed. 
    If null, all instances of the specified class should be processed
classesWhoseInstancesToExpand: if the value of a slot is an instance of a class in this 
    list, then the slot values of this value will be concatenated together in a cell, 
    using 'delimiter' as the delimiter of the concatenated values. Instances of these
    classes should only have data values (not instances) and should only have 
    single-valued slots. The rationale for this construct is that some trivial 
    classes have slots (e.g., text and language) that should be represented as a single
    entity.

For single-valued slots, write out as
instance-name  slot1  slot2 ... slot3

For multi-valued slots, write out as
instanceName slotName value

All single-valued slots will be written out first

'''
def DumpToSheet(outputFile, className, directInstancesp, filteredInstances, classesWhoseInstancesToExpand, delimiter):
    try :
        output = open(outputFile, 'w')
        cls = kb.getCls(className)
        if cls:
            if filteredInstances:
                instances = filteredInstances
            else:
                if (directInstancesp):
                    instances = cls.getDirectInstances()
                else:
                    instances = cls.getInstances()
        if (instances):
            generateSingleValuedSlotsData(output, cls, instances, classesWhoseInstancesToExpand, delimiter)
            generateMultivaluedSlotsData(output, cls, instances, classesWhoseInstancesToExpand, delimiter)
        output.close()
    except IOError:
        print "Can't open ", outputFile

def generateSingleValuedSlotsData(output, cls, instances, classesWhoseInstancesToExpand, delimiter):
    classesWhoseInstancesToExpand = map (getCls, classesWhoseInstancesToExpand)
    slots = []
    for slot in cls.getTemplateSlots():
        if (cls.getTemplateSlotMaximumCardinality(slot) == 1):
            slots.append(slot)
    output.write("ID")
    for slot in slots:
        output.write('\t%s' % (slot.getName()))
    for instance in instances:
        output.write('\n%s' % (instance.getName()))
        for slot in slots:
            output.write('\t%s' % (generateCellValue(cls, slot, instance.getOwnSlotValue(slot), 
                classesWhoseInstancesToExpand, delimiter)))

def generateMultivaluedSlotsData(output, cls, instances, classesWhoseInstancesToExpand, delimiter):
    slots = []
    for slot in cls.getTemplateSlots():
        print slot.getName(), " ", cls.getTemplateSlotMaximumCardinality(slot)
        if (cls.getTemplateSlotMaximumValue(slot) < 1):
            slots.append(slot)
    output.write("ID\tSlot\tValue")
    for instance in instances:
        output.write("\n")
        for slot in slots:
            values = list(instance.getOwnSlotValues(slot))
            if (values):
                for value in instance.getOwnSlotValues(slot):
                    cellValue = generateCellValue(cls, slot, value, classesWhoseInstancesToExpand, delimiter)
                    if (cellValue):
                        output.write('\n%s\t%s\t%s' % (instance.getName(), slot.getName(), cellValue))

from edu.stanford.smi.protege.model import ValueType                
def generateCellValue(cls, slot, value, classesWhoseInstancesToExpand, delimiter):
    if (value):
        valueType = cls.getTemplateSlotValueType( slot)
        if (valueType == ValueType.INSTANCE):
            if (Set(value.getDirectTypes()).intersection(Set(classesWhoseInstancesToExpand))):
                valueString = getDelimitedValue(value, delimiter)
            else:
                valueString = value.getName()
            return valueString
        elif (valueType == ValueType.CLS):
            return value.getName()
        else:
            return str(value)
    else:
        return ""
        
def getDelimitedValue(value, delimiter):
    slots = value.getOwnSlots()
    first = 1
    valueString=""
    for slot in slots:
        if(not slot.isSystem()):
            queriedValue = value.getOwnSlotValue(slot)
            if (not queriedValue):
                queriedValue = "None"
            else:
                queriedValue = str(queriedValue)
            if (first):
                valueString = queriedValue
                first = 0
            else:
                valueString = valueString+delimiter+queriedValue
    return valueString


"""
Add increase_dose_ceiling and max_recommended_dose slot values from GuidelineDrug dose-level
ranges; print dose ranges of guideline drugs

Available function
putAllDoseCeiling : put increase dose ceiling and max_recommended_dose 
printGuidelineDrugsDoseRanges: print dose ranges of GuidelineDrugs

Test calls
printDoseLevelRange(sys.stdout, kb.getInstance("ATHENA_GlycemicControl_Instance_100000"))
printGuidelineDrugsDoseRanges(sys.stdout, kb.getCls("Guideline_Drug").getInstances())


"""

from java.lang import Float
def putDoseCeilings(guidelineDrug):
    doseLevelRanges = guidelineDrug.getOwnSlotValues(kb.getSlot("dose_level_ranges"))
    drugLabel = guidelineDrug.getOwnSlotValue(kb.getSlot("label"))
    maxRecommendedDoseLevel = guidelineDrug.getOwnSlotValue(kb.getSlot("max_recommended_dose_level"))
    if (maxRecommendedDoseLevel):
        for dlr in doseLevelRanges:
            doseLevel = dlr.getOwnSlotValue(kb.getSlot("abstract_value"))
            if (doseLevel):
                if (doseLevel == maxRecommendedDoseLevel):
                    maxIncreaseDose = dlr.getOwnSlotValue(kb.getSlot("lower_limit"))
                    if (maxIncreaseDose):
                        guidelineDrug.setOwnSlotValue(kb.getSlot("increase_dose_ceiling"), Float(maxIncreaseDose))
                    else:
                        print "No min dose for high dose level of "+drugLabel
                    maxValue = dlr.getOwnSlotValue(kb.getSlot("upper_limit"))
                    if (maxValue):
                        guidelineDrug.setOwnSlotValue(kb.getSlot("max_recommended_dose"), Float(maxValue))
                    else:
                        print "No max value for the high dose range of "+drugLabel

            else:
                print "No nominal (abstract) dose level for a dose level range of "+drugLabel
    else:
        print "No max_recommended_dose_level for "+drugLabel
    increaseDoseCeiling = guidelineDrug.getOwnSlotValue(kb.getSlot("increase_dose_ceiling"))
    maxDose = guidelineDrug.getOwnSlotValue(kb.getSlot("max_recommended_dose"))
    if (not (increaseDoseCeiling)):
        print "No increase dose ceiling for "+drugLabel
    if (not (maxDose)):
        print "No max dose for "+drugLabel
                    
def putAllDoseCeiling():
    gds = kb.getCls("Guideline_Drug").getInstances()
    for gd in gds:
        putDoseCeilings(gd)
        
def putMaxRecommDose(guidelineDrug):
    doseLevelRanges = guidelineDrug.getOwnSlotValues(kb.getSlot("dose_level_ranges"))
    drugLabel = guidelineDrug.getOwnSlotValue(kb.getSlot("label"))
    maxRecommendedDoseLevel = guidelineDrug.getOwnSlotValue(kb.getSlot("max_recommended_dose_level"))
    if (maxRecommendedDoseLevel and maxRecommendedDoseLevel.getName() == "Medium_Dose"):
        print drugLabel + " has medium dose as the maximum recommended dose level"
    else:
        for dlr in doseLevelRanges:
            doseLevel = dlr.getOwnSlotValue(kb.getSlot("abstract_value"))
            if (doseLevel):
                if ((doseLevel.getName() == "High_Dose") or (doseLevel.getName() == "High_Dose(high/low)")):
                    maxValue = dlr.getOwnSlotValue(kb.getSlot("upper_limit"))
                    if (maxValue):
                        guidelineDrug.setOwnSlotValue(kb.getSlot("max_recommended_dose"), Float(maxValue))
                    else:
                        print "No max value for the high dose range of "+drugLabel
                else:
                    print "No high dose level for "+drugLabel
            else:
                print "No nominal (abstract) dose level for a dose level range of "+drugLabel
                
def printDoseLevelRange(outputDestination, doseLevel):
    """Print the range of the dose-levels of a guideline drugUsagesSlo
       assertion: doseLevel is an instance of Range_Mapping_Entry
    """
    ll = doseLevel.getOwnSlotValue(kb.getSlot("lower_limit"))
    ul = doseLevel.getOwnSlotValue(kb.getSlot("upper_limit"))
    av = doseLevel.getOwnSlotValue(kb.getSlot("abstract_value"))
    if (ll):
        llString = roundFloat(ll,1)
    else:
        llString ="None"
    if (ul):
        ulString = roundFloat(ul, 1)
    else:
        ulString = "None"
    if (av):
        avString = av.getName()
    else:
        avString = "None"
    outputDestination.write(avString+"\t"+llString+"\t"+ulString)
    
def printGuidelineDrugsDoseRanges(outputDestination, gDrugs):
    for gDrug in gDrugs:
        doseLevels = gDrug.getOwnSlotValues(kb.getSlot("dose_level_ranges"))
        if (doseLevels != None):
            outputDestination.write("\n"+gDrug.getOwnSlotValue(kb.getSlot("label"))+"\n")
            for doseLevel in doseLevels:
                outputDestination.write("\t")
                printDoseLevelRange(outputDestination, doseLevel)
                outputDestination.write("\n")
 

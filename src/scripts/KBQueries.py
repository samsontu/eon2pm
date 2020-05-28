#!/usr/bin/env python
# -*- coding: utf-8 -*-
"""
execfile("/Users/tu/Documents/Dropbox/Scripts/Python/VA-related-scripts/ATHENAProject/initAthenaProtegeScript.py")
execfile("/Users/tu/Documents/Dropbox/Scripts/Python/VA-related-scripts/ATHENAProject/athena/KBQueries.py")

Available functions:
- getDataElements(guidelineInstanceName, outputFile, subclassP, includeDefined) : 
  List primitive medical task. If subclassP is 'true' then include subclasses of the data elements
- getReferencedInstancesOfNamedInstance(namedInstance, classes):
    Return the set of instances referenced from a named instance (e.g., guideline) where
    the instances are of the specified classes
- printReferencedInstancesOfNamedInstance(namedInstance, classes, slots, "html")
    Print the type, label, and slot values of instances returned by getReferencedInstancesOfNamedInstance
- getReferencedInstancesOfInstance(instance, classList)
    Get instances of type classList that can be referenced from a given instance
- printReferencedInstancesOfIndividuals(listOfIndividuals, classes, slots)
    Print the type, label, and slot values of instances that can be referenced
    from a given set of individuals
- printSlotValuesOfInstances(instances,  slotList, "html"):
    Print html type, label, and slot values of instances
- printCollateralActions(guideline, htmlFile)


Sample calls:
inst = kb.getInstance("ATHENA_GlycemicControl_Class130049")
inst = kb.getInstance("ATHENA_HTN_Dashboard_Class110000")
getReferencedFrames(inst, Set([]))
getDataElements("ATHENA_GlycemicControl_Class130049", "/Users/tu/workspace/DMkbnames.txt", 1, 1)
getDataElements("ATHENA_HTN_Dashboard_Class110000", "/Users/tu/workspace/HTNkbnames.txt", 0, 0)
getDataElements("ATHENA_HTN_Dashboard_Class110000", "/Users/tu/workspace/HTNkbnamesIncludeSubclasses.txt", 1, 0)
getReferencedInstancesOfNamedInstance("ATHENA_GlycemicControl_Class130049", 
    ["On_Screen_Message"])
printReferencedInstancesOfNamedInstance("ATHENA_GlycemicControl_Class130049", 
    ["On_Screen_Message"], ["message"], "html", stdout)
printReferencedInstancesOfIndividuals(list of individuals, ["On_Screen_Message"], ["message"]))
printCollateralActions("ATHENA_GlycemicControl_Class130049", stdout)
printReferencedInstancesOfNamedInstance("ATHENA_GlycemicControl_Class130049", 
    ["On_Screen_Message"], ["message"], "html", sys.stdout)
printReferencedInstancesOfNamedInstance("ATHENA_GlycemicControl_Class130049", 
    ["Parameterized_String"], ["value"], "html", stdout)
printReferencedInstancesOfNamedInstance("ATHENA_GlycemicControl_Class130049", 
    ["On_Screen_Message"], [":NAME"], stdout)
printReferencedInstancesOfNamedInstance("ATHENA_GlycemicControl_Class130049", 
    ["Parameterized_String"], [":NAME"], "html", stdout)

printReferencedInstancesOfNamedInstance("ATHENA_GlycemicControl_Class130049", [ "PAL_Criterion"], ["label"], "html", sys.stdout)
printReferencedInstancesOfNamedInstance("ATHENA_HTN_Dashboard_Class110000", [ "PAL_Criterion"], ["label"], "html", sys.stdout)

"""

from sets import Set
import sys
import os
from utilities import *
import  edu.stanford.smi.protege.model.Frame



def getDataElements(guidelineInstanceName, outputFile, subclassP, includeDefined):
    """
    Get all "medical task" classes (with or without their subclasses) that are referenced in a guideline
    The "includeDefined" flag determines whether defined classes or only primitive classes
    These are kb terms that need mappings from EHR
    "primitive class" is a class that is not
      - a Diagnostic_Term_Metaclass medical conditions

    Additional checks may exclude:
      - a Derived_Parameter_Metaclass instance
      - (in OT) a Measured_Parameters_Metaclass
    but they are not implemented now
    """
    savedStdout = sys.stdout
    try:
        out = open(outputFile, 'w')
    except IOError:
        out = 0
    try:
        if out:
            sys.stdout = out
        if subclassP:
            referenced = includeSubclasses(getReferencedFrames(kb.getInstance(guidelineInstanceName), Set([])))
        else:
            referenced = getReferencedFrames(kb.getInstance(guidelineInstanceName), Set([]))
        if (includeDefined):
            medConditions = getReferencedMedicalConditions(referenced)
            print("\n****Referenced Medical Conditions (including defined classes): KB Name followed by Pretty Name, if any")
        else:
            medConditions = getReferencedMedicalConditionsNotDefined(referenced)
            print("\n****Referenced Medical Conditions that are not diagnostic term metaclass instances: KB Name followed by Pretty Name, if any")
        printKBNames(medConditions)
        print("\n****Other Referenced Diagnostic Classes: KB Name followed by Pretty Name, if any")
        otherDiagnosticClasses = getReferencedOtherDiagnosticClasses(referenced)
        printKBNames(otherDiagnosticClasses)
        meds = getReferencedMedications(referenced)
        print("\n****Referenced Medication Classes: KB Name followed by Pretty Name, if any")
        printKBNames(meds)
        otherTherapeuticClasses = getReferencedOtherTherapeuticClasses(referenced)
        print("\n****Other Referenced Therapeutic Classes: KB Name followed by Pretty Name, if any")
        printKBNames(otherTherapeuticClasses)
    finally:
        if out:
            out.close()
            sys.stdout = savedStdout

def printKBNames(clses):
    pnSlot = kb.getSlot("PrettyName")
    for c in clses:
        p = c.getOwnSlotValue(pnSlot)
        if (p is None):
            if c.isIncluded():
                print('included: '+ c.getName())
            else:
                print(c.getName())
        else:
            if c.isIncluded():
                print('included: '+ c.getName() +" '"+p+"'")
            else:
                print(c.getName()+" '"+p+"'")


# # Get frames referenced in a guideline

def getReferencedFrames(frame, referencedFrames):
    referencedFrames.add(frame)
    ownSlots = frame.getOwnSlots()
    for slot in ownSlots:
        if (slot.isSystem()):
            continue
        slotValues = frame.getOwnSlotValues(slot)
        for value in slotValues:
            if (isFrame(value) and (not (value in referencedFrames))):
            	#print(frame.getBrowserText()+": "+slot.getName() +" has value " + value.getBrowserText()
                referencedFrames = getReferencedFrames(value, referencedFrames)
    return referencedFrames

# Given a set of referenced frames, include the subclasses of referenced frames
def includeSubclasses(referencedFrames):
    referencedAndSubclass = referencedFrames
    metaCls = kb.getCls(":CLASS")
    for ref in referencedFrames:
        if (ref.getDirectType()).hasSuperclass(metaCls):
            referencedAndSubclass = referencedAndSubclass.union(Set(ref.getSubclasses()))
    return referencedAndSubclass

def isFrame(obj):
    return isinstance(obj, edu.stanford.smi.protege.model.Frame)


# get referenced medical conditions 
def getReferencedMedicalConditions(referenced):
    conditions = kb.getCls("Medical_Conditions_Class").getSubclasses()
    return referenced.intersection(Set(conditions))

# get referenced medical conditions that are not defined classes
def getReferencedMedicalConditionsNotDefined(referenced):
    conditions = kb.getCls("Medical_Conditions_Class").getSubclasses()
    primitives = kb.getCls("Diagnostic_Term_Metaclass").getInstances()
    return referenced.intersection(Set(conditions)) - (Set(primitives))

def getReferencedOtherDiagnosticClasses(referenced):
    others = Set(kb.getCls("Diagnostic_Class").getSubclasses()) - Set(kb.getCls("Medical_Conditions_Class").getSubclasses())
    return referenced.intersection(others)

def getReferencedOtherTherapeuticClasses(referenced):
    others = Set(kb.getCls("Therapeutic_Class").getSubclasses()) - Set(kb.getCls("Medications_Class").getSubclasses())
    return referenced.intersection(others)

def getReferencedMedications(referenced):
    medications = kb.getCls("Medications_Class").getSubclasses()
    return referenced.intersection(Set(medications))

def getReferencedInstancesOfNamedInstance(namedInstance, classList):
    return getReferencedInstancesOfInstance(kb.getInstance(namedInstance), classList)

def getReferencedInstancesOfInstance(instance, classList):
    referenced = getReferencedFrames(instance, Set([]))
    instances = Set([])
    for typ in classList:
        instances = Set(kb.getInstances(kb.getCls(typ))).union(instances)
    return referenced.intersection(instances)
    
def printReferencedInstancesOfNamedInstance(namedInstance, classList, slotList, format, outputDevice):
    printSlotValuesOfInstances(getReferencedInstancesOfNamedInstance
        (namedInstance, classList), slotList,  format, outputDevice)
    
def printSlotValuesOfInstances(instances,  slotList, format, outputDevice):
    for inst in instances:
        allSlotValuesString = ""
        for slot in slotList:
            slotValuesString = ""
            slotValues = inst.getOwnSlotValues(kb.getSlot(slot))
            if slotValues:
                if isFrame(list(slotValues)[0]):
                    slotValues = [x.getBrowserText() for x in slotValues]
                #slotValues = [x.encode('utf-8') for x in slotValues]
                #slotValuesString = '|'.join(map(str, slotValues))
                for slotValue in slotValues: 
                    slotValuesString = slotValuesString+'|'+str(slotValue)
                    # # Note that basestring as an abstract type is removed in Python 3
                    # if isinstance(slotValue, basestring):
                    #     slotValuesString= (slotValuesString+'|'
                    #                        +''.join([i if ord(i) < 128 else ' ' for i in slotValue]))
                    # else:
                    #     slotValuesString = slotValuesString+'|'+str(slotValue)
                #print(slotValuesString
            allSlotValuesString = allSlotValuesString +'\t'+slotValuesString[1:] #remove leading '|'
        if format == "html":
            outputDevice.write("<p><b>"+inst.getDirectType().getName()+"</b>("
                  +inst.getName()+")<i>"
                  +inst.getBrowserText()+"</i>"
                  +allSlotValuesString+"</p>")
        else:
            outputDevice.write(inst.getDirectType().getName()+"\t"+inst.getName()+"\t"
                               +inst.getBrowserText()+allSlotValuesString+"\n")


# Module: Print Collateral Actions of a guideline

drugUsagesSlot = kb.getSlot("drug_usages")
collateralActionsSlot = kb.getSlot("collateral_actions")
labelProp = kb.getSlot("label")
moodProp = kb.getSlot("mood")
actionsProp = kb.getSlot("actions")
messageTypeProp = kb.getSlot("message_type")
ruleInProp = kb.getSlot("rule_in_condition")
messageProp = kb.getSlot("message")

def printCollateralActions(guidelineName, htmlFile):
    htmlOutput=open(htmlFile, 'w')
    guideline = kb.getInstance(guidelineName)
    if guideline != None:
        printCollateralActionsToOutput(guideline, htmlOutput)
    else:
        print("No such guideline instance ")+guidelineName
    htmlOutput.close()     

def printCollateralActionsToOutput(guideline, htmlOutput):
    printHeader(htmlOutput, "HTML")
    drugUsages = guideline.getOwnSlotValues( drugUsagesSlot) 
    for du in drugUsages:
        ca = du.getOwnSlotValues(collateralActionsSlot)
        if (ca != None):
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
        mood= " "+mood.getName() +": "
    actions = collatAction.getOwnSlotValues(actionsProp)
    for action in actions:
        messageType = action.getOwnSlotValue(messageTypeProp)
        message = action.getOwnSlotValue(messageProp)
        label = action.getOwnSlotValue(labelProp)
        ruleIn = action.getOwnSlotValue(ruleInProp)
        if (messageType):
            messageTypeString = "<i>"+messageType.getName() +"</i>"
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


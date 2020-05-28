

                   
# Round a float to a specific precision and return a string
def roundFloat(f, precision):
    factor = 1
    for _ in range(precision):
        factor = factor * 10
    if (((f*factor) % 1) > 0.5):
        return str((f*factor+1)//1/ factor)
    else:
        return str(f*factor//1/factor)
    
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

def isLeafNode(nodeCls):
    return (len(list(nodeCls.getSubclasses())) == 0)
    
def printCommaSeparatedList(mylist, output):
    first = 1
    for c in mylist:
        if first:
            output.write(c.getName())
            first = 0
        else:
            output.write(", "+c.getName())

BLANK = " "
CSV = "CSV"
HTML = "HTML"

def createPrettyName(cls):
    pnSlot = kb.getSlot("PrettyName")
    kbName=cls.getName().replace("_", " ")
    if (cls.getOwnSlotValue(pnSlot) is None):
        cls.setOwnSlotValue(pnSlot, kbName.title())


def printBlanks(outputDestination, n, outputFormat):
    if (not (outputFormat == CSV)):
        for i in range(n):
            outputDestination.write(BLANK)

def beginList(indent, outputDestination, outputFormat):
    if (outputFormat == HTML):
        outputDestination.write("\n")
        printBlanks(outputDestination,indent, outputFormat)
        outputDestination.write("<ul>\n")

def endList(indent, outputDestination, outputFormat):
    if (outputFormat == HTML):
        outputDestination.write("\n")
        printBlanks(outputDestination,indent, outputFormat)
        outputDestination.write("</ul>\n")

def beginListItem(outputDestination, outputFormat):
    if (outputFormat == HTML):
        outputDestination.write("<li>")

def endListItem(outputDestination, outputFormat):
    if (outputFormat == HTML):
        outputDestination.write("</li>\n")
    else:
        outputDestination.write("\n")

def beginBold(outputDestination, outputFormat):
    if (outputFormat == HTML):
        outputDestination.write("<strong>")

def endBold(outputDestination, outputFormat):
    if (outputFormat == HTML):
        outputDestination.write("</strong>")

def beginItalic(outputDestination, outputFormat):
    if (outputFormat == HTML):
        outputDestination.write("<italic>*** ")

def endItalic(outputDestination, outputFormat):
    if (outputFormat == HTML):
        outputDestination.write(" ****</italic>")

def printHeader(outputDestination, outputFormat):
    if (outputFormat == HTML):
        outputDestination.write("<html><head></head><body>\n")

def printFooter(outputDestination, outputFormat):
    if (outputFormat == HTML):
        outputDestination.write("</body></html>\n")


def printNodeCls(nodeCls, indent, outputDestination, outputFormat):
    if (not (outputFormat == CSV)):
        printBlanks(outputDestination,indent, outputFormat)
        beginListItem(outputDestination, outputFormat)
        if (nodeCls.getName() != nodeCls.getBrowserText()):
            title = nodeCls.getName()+"("+nodeCls.getBrowserText()+")"
        else:
            title = nodeCls.getName()
        outputDestination.write(title)
        endListItem(outputDestination, outputFormat)

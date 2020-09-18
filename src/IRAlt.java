import com.rma.io.DssFileManagerImpl;
import com.rma.io.RmaFile;
import hec.heclib.dss.HecDSSDataAttributes;
import hec.heclib.util.HecTime;
import hec.io.DSSIdentifier;
import hec.io.TimeSeriesContainer;
import hec2.model.DataLocation;
import hec2.plugin.model.ComputeOptions;
import hec2.plugin.model.ModelAlternative;
import hec2.plugin.selfcontained.SelfContainedPluginAlt;
import org.jdom.Document;
import org.jdom.Element;
import hec.heclib.dss.DSSPathname;


import java.util.ArrayList;
import java.util.List;


public class IRAlt extends SelfContainedPluginAlt {
    private List<DataLocation> _dataLocations;
    private static final String DocumentRoot = "IRAlt";
    private static final String AlternativeNameAttribute = "Name";
    private static final String AlternativeDescriptionAttribute = "Desc";
    private static final String EvalLocationElement = "EvaluationLocations";
    private List<EvaluationLocation> _evalLocs;

    private ComputeOptions _computeOptions;

    public IRAlt() {
        super();
        _dataLocations = new ArrayList<>();
    }

    public IRAlt(String name) {
        this();
        setName(name);
    }

    @Override
    public boolean saveData(RmaFile file) {
        if (file != null) {
            Element root = new Element(DocumentRoot);
            root.setAttribute(AlternativeNameAttribute, getName());
            root.setAttribute(AlternativeDescriptionAttribute, getDescription());
            if (_dataLocations != null) {
                saveDataLocations(root, _dataLocations);
            }
            Element evaluationLocations = new Element(EvalLocationElement);
            if  (_evalLocs!= null){
                for (DataLocation dl : _dataLocations){
                    Element locationElement = new Element("Location");
                    locationElement.setAttribute("Name", dl.getName());
                    locationElement.setAttribute("Parameter", dl.getParameter());
//                    Testing to add a new evalLoc
//                    _evalLocs.add(new EvaluationLocation(dl,15,"GreaterThan","ShowMessage","GET IN THE ARK!!!"));
                    for(EvaluationLocation el : _evalLocs){
                        //add to the element.
                        if(el.get_location().getName().equals(dl.getName())){
                            if(el.get_location().getParameter().equals(dl.getParameter())){
                                locationElement.addContent(el.writeToXML());
                            }
                        }
                    }
                    evaluationLocations.addContent(locationElement);
                }
            }
            root.addContent(evaluationLocations);
            Document doc = new Document(root);
            return writeXMLFile(doc,file);
        }
        return false;
    }

    @Override
    protected boolean loadDocument(org.jdom.Document dcmnt) {
        if (dcmnt != null) {
            org.jdom.Element ele = dcmnt.getRootElement();
            if (ele == null) {
                System.out.println("No root element on the provided XML Document");
                return false;
            }
            if (ele.getName().equals(DocumentRoot)) {
                setName(ele.getAttributeValue(AlternativeNameAttribute));
                setDescription(ele.getAttributeValue(AlternativeDescriptionAttribute));

            } else {
                System.out.println("XML document root was improperly named. ");
                return false;
            }
            if (_dataLocations == null) {
                _dataLocations = new ArrayList<>();
            }
            _dataLocations.clear();
//            Loading DataLocations from XML into the _dataLocations list
            loadDataLocations(ele, _dataLocations);
            setModified(false);
            if(_evalLocs ==null) {
                _evalLocs = new ArrayList<>();
            }
            _evalLocs.clear();
            for (DataLocation dloc :_dataLocations){
                loadEvalLocs(ele, dloc);
            }
        } else {
            System.out.println("XML Document was null.");
            return false;
        }
        return true;
    }
public boolean loadEvalLocs(Element ele, DataLocation dloc) {
    //                read the locations in the list
    //                return EvaluationLocations
    //                add them to the _evalLoc

    Element locationRoot = ele.getChild(EvalLocationElement);
    List objLocations = locationRoot.getChildren(); //these are the "Location" elements
    for (Object locObj : objLocations) {
        Element eleLocation = (Element) locObj;
        if (dloc.getName().equals(eleLocation.getAttributeValue("Name"))) {
            for (Object e : eleLocation.getChildren()) {
                Element eleEvaluations = (Element) e;
                _evalLocs.add(EvaluationLocation.readFromXML(eleEvaluations, dloc));
            }
        }
    }
    return true;
}
    public void setComputeOptions(ComputeOptions opts) {
        _computeOptions = opts;
    }

    @Override
    public boolean isComputable() {
        return true;
    }

    @Override
    public boolean compute() {
        boolean returnValue = true;
//        is casting to hec2.rts.model.ComputeOptions required?_computeOptions is of type hec2.plugin.model.ComputeOptions
//        hec2.rts.model.ComputeOptions extends hec2.plugin.model.ComputeOptions
        hec2.rts.model.ComputeOptions cco = (hec2.rts.model.ComputeOptions) _computeOptions;
        String dssFilePath = cco.getDssFilename();
        HecTime startTime = (_computeOptions.getRunTimeWindow().getStartTime());
        HecTime endTime = (_computeOptions.getRunTimeWindow().getEndTime());
        int i =0;
        for (EvaluationLocation el :_evalLocs){
            i=i+1;
            String dsspathname =  el.get_location().getLinkedToLocation().getDssPath();
            DSSIdentifier forecastDSSId = new DSSIdentifier(dssFilePath, dsspathname);
            forecastDSSId.setStartTime(startTime);
            forecastDSSId.setEndTime(endTime);
            addComputeMessage("Computing Evalutaion Location Number "+ i + el.get_location().getName());
            addComputeMessage("Reading " + dsspathname + " from " + dssFilePath+ System.lineSeparator());
            CompResult results = el.compute(forecastDSSId);
            for (String messages:results.computeMessages) {
                addComputeMessage(messages);
            }
            for (String emessages:results.errorMessages){
                addComputeErrorMessage(emessages);

            }
        }
        return returnValue;
    }

    @Override
    public int getModelCount() {
        return 0;
    }

    @Override
    public boolean cancelCompute() {
        return false;
    }

    @Override
    public String getLogFile() {
        return null;
    }

    private List<DataLocation> defaultDataLocations() {
        if (!_dataLocations.isEmpty()) {
            //locations have previously been set (most likely from reading
            //in an existing alternative file.
            for (DataLocation dl : _dataLocations) {
                String dlparts = dl.getDssPath();
                DSSPathname p = new DSSPathname(dlparts);
//                I believe this if block is only initiated for output locations.
                if (p.aPart().equals("") && p.bPart().equals("") && p.cPart().equals("") && p.dPart().equals("") && p.ePart().equals("") && p.fPart().equals("")) {
                    if (validLinkedToDssPath(dl)) {
//                        setDssParts(dl);
                    }
                }
            }
            return _dataLocations;
        }
        List<DataLocation> dlList = new ArrayList<>();
        //if there are no dataLocations, create a default location so that links can be initialized.
        DataLocation dloc = new DataLocation(this.getModelAlt(), _name, "Any");
        dlList.add(dloc);
        _dataLocations.add(dloc);
        return dlList;
    }

    private boolean validLinkedToDssPath(DataLocation dl) {
        DataLocation linkedTo = dl.getLinkedToLocation();
        String dssPath = linkedTo.getDssPath();
        return !(dssPath == null || dssPath.isEmpty());
    }

    public List<DataLocation> getInputDataLocations() {
        return defaultDataLocations();
    }

    public List<DataLocation> getOutputDataLocations() {
        ArrayList<DataLocation> emptyOutputLocations = new ArrayList<>();
        return emptyOutputLocations;
    }

    public boolean setDataLocations(List<DataLocation> dataLocations) {
        boolean retval = false;
        for (DataLocation dl : dataLocations) {
            int i = dataLocations.indexOf(dl);
            if (!_dataLocations.contains(dl)) {
                DataLocation linkedTo = dl.getLinkedToLocation();
                String dssPath = linkedTo.getDssPath();
                if (validLinkedToDssPath(dl)) {
                    setModified(true);
//                    setDssParts(dl);
                    _dataLocations.set(i, dl);
                    retval = true;
                }
            } else {
                DataLocation linkedTo = dl.getLinkedToLocation();
                String dssPath = linkedTo.getDssPath();
                if (validLinkedToDssPath(dl)) {
                    setModified(true);
//                    setDssParts(dl);
                    retval = true;
                }
            }
        }
        if (retval) { saveData();}
        return retval;
    }
}

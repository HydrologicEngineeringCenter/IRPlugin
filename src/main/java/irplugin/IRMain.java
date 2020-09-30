package irplugin;

import alteditor.Controller;
import com.rma.client.Browser;
import com.rma.factories.NewObjectFactory;
import com.rma.io.RmaFile;
import hec2.map.GraphicElement;
import hec2.model.DataLocation;
import hec2.model.ProgramOrderItem;
import hec2.plugin.CreatablePlugin;
import hec2.plugin.action.EditAction;
import hec2.plugin.action.OutputElement;
import hec2.plugin.lang.ModelLinkingException;
import hec2.plugin.lang.OutputException;
import hec2.plugin.model.ComputeOptions;
import hec2.plugin.model.ModelAlternative;
import hec2.plugin.selfcontained.AbstractSelfContainedPlugin;
import hec2.rts.client.RmiTextFileReaderJDialog;
import hec2.rts.plugin.RtsPlugin;
import hec2.rts.plugin.RtsPluginManager;
import hec2.rts.plugin.action.ComputeModelAction;
import hec2.rts.plugin.action.OutputElementImpl;
import hec2.rts.ui.RtsTabType;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import rma.util.RMAFilenameFilter;
import rma.util.RMAIO;
import com.rma.io.FileManagerImpl;


import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class IRMain extends AbstractSelfContainedPlugin<IRAlt> implements RtsPlugin, CreatablePlugin {
    //    cant have spaces in PluginName. PluginName will show in in model and forecast tree
    public static final String PluginName = "IRP";
    private static final String _pluginVersion = "1.0.0";
    //    this is the name of the directory model data will be stored (ex. rss)
    private static final String _pluginSubDirectory = "irp";
    //    the extension for plugin files (like the alternative)
    private static final String _pluginExtension = ".irp";

    public static void main(String[] args) {

        IRMain p = new IRMain();
    }

    public IRMain() {
        super();
        setName(PluginName);
        setProgramOrderItem(new ProgramOrderItem(PluginName,
                "A plugin constructed from the tutorial",
                false, 1, "IRP", "Images/riverware/png/WaterUser16.png"));
        RtsPluginManager.register(this);
    }

    @Override
    public void editAlternative(IRAlt irAlt) {
//        UIMain.main(new String[]{});is to run UI main as is

//        Sees if an existing editor window is present and repaints it.
        Window[] windows = Window.getWindows();
        for (Window window : windows){
            if (window instanceof Frame){
                Frame frame = (Frame) window;
                if (frame.isVisible() && frame.getTitle().equals("Alternative Editor")) {
                    frame.toFront();
                    frame.repaint();
                    return;
            }
            }
            //        Below runs javaFX gui in a Jframe.
        }
        JFrame window = new JFrame();
        window.setTitle("Alternative Editor");
        JFXPanel jfxPanel = new JFXPanel();
        Platform.runLater(() -> {

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample.fxml"));
                Parent root = loader.load();
                jfxPanel.setScene(new Scene(root, 950, 400));
                // Give the controller access to the main app
                Controller controller = loader.getController();
                controller.set_irAlt(irAlt);
                controller.initlocations();
//                controller.setMainApp();

                SwingUtilities.invokeLater(() -> {
                    window.add(jfxPanel);
                    window.pack();
                    window.setVisible(true);
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

        });


    }

    @Override
    protected IRAlt newAlternative(String s) {
        return new IRAlt(s);
    }

    @Override
    protected String getAltFileExtension() {
        return _pluginExtension;
    }

    @Override
    public String getPluginDirectory() {
        return _pluginSubDirectory;
    }

    @Override
    protected NewObjectFactory getAltObjectFactory() {
        return new IRAltFactory(this);
    }

    @Override
    public boolean compute(ModelAlternative ma) {
        IRAlt alt = getSimulationAlt(ma);
        if (alt != null) {
            alt.setComputeOptions(ma.getComputeOptions());
            if (_computeListeners != null && !_computeListeners.isEmpty()) {
                for (int i = 0; i < _computeListeners.size(); i++) {
                    alt.addComputeListener(_computeListeners.get(i));
                }
            }
            addComputeMessage("Starting Compute For " + alt.getName());
            return alt.compute();
        } else {
            addComputeErrorMessage("Failed to find Alternative for " + ma);
            return false;
        }
    }

    @Override
    public List<DataLocation> getDataLocations(ModelAlternative ma, int i) {
        List<DataLocation> emplist = new ArrayList<>();
        if (ma == null) {
            System.out.println("getDataLocations: No ModelAlternative found");
            return emplist;
        }
        IRAlt alt;
        if (ma.getComputeOptions() != null) {
            alt = getSimulationAlt(ma);
        } else {
            alt = getAlt(ma);
        }
        if (DataLocation.INPUT_LOCATIONS == i) {
            //input
            return alt.getInputDataLocations();
        } else {
            //output
            return alt.getOutputDataLocations();
        }
    }

    @Override
    public boolean setDataLocations(ModelAlternative ma, List<DataLocation> list) throws ModelLinkingException {
        IRAlt alt;
        if (ma.getComputeOptions() != null) {
            alt = getSimulationAlt(ma);
        } else {
            alt = getAlt(ma);
        }
        if (alt != null) {
            return alt.setDataLocations(list);
        }
        return false;
    }

    @Override
    public List<GraphicElement> getGraphicElements(ModelAlternative ma) {
        return null;
    }

    @Override
    public List<OutputElement> getOutputReports(ModelAlternative ma) {
        List<OutputElement> actions = new ArrayList<OutputElement>();
        String actCmd = "showReport";
        OutputElementImpl out = new OutputElementImpl("Report", PluginName, actCmd);
        actions.add(out);

        return actions;
    }

    @Override
    public boolean displayEditor(GraphicElement graphicElement) {
        return false;
    }

    @Override
    public boolean displayOutput(OutputElement outputElement, List<ModelAlternative> list) throws OutputException {
        ModelAlternative ma = outputElement.getModelAlternative();
        if (outputElement.getCommand()=="showReport"){
            String rundir = ma.getComputeOptions().getRunDirectory();
            String filename = rundir + RMAIO.separator + "IRreport.rpt";
            File file =  new File(filename);
            String input = "";
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            String line = null;
            while (true) {
                try {
                    if (!((line = reader.readLine()) != null)) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }

                input += line + "\n";
            }
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            JTextArea textArea = new JTextArea(input);
            JScrollPane scrollPane = new JScrollPane(textArea);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);

            scrollPane.setPreferredSize( new Dimension( 400, 400 )) ;
            JOptionPane.showMessageDialog(Browser.getBrowserFrame(),  scrollPane, "Impact Response Report",
                    JOptionPane.PLAIN_MESSAGE);
        }
        return true;
}

    @Override
    public List<EditAction> getEditActions(ModelAlternative ma) {
        List<EditAction> actions = new ArrayList<EditAction>();
        ComputeModelAction cation = new ComputeModelAction("Compute", PluginName, "computeModel");
        actions.add(cation);
        return actions;
    }

    @Override
    public void editAction(String s, ModelAlternative ma) {
    }

    @Override
    public boolean saveProject() {
        boolean success = true;
        for (IRAlt alt : _altList) {
            if (!alt.saveData()) {
                success = false;
                System.out.println("Alternative " + alt.getName() + " could not save.");
            }
        }
        return success;
    }

    @Override
    public boolean displayApplication() {
        return false;
    }

    @Override
    public String getVersion() {
        return _pluginVersion;
    }

    @Override
    public boolean copyModelFiles(ModelAlternative ma, String s, boolean b) {
        String source = RMAIO.concatPath(s, _pluginSubDirectory);
        String dest;
        RMAFilenameFilter filt = new RMAFilenameFilter("bak");
        if (b) {
//            for forecast creation and replace from base
            dest = RMAIO.concatPath(ma.getRunDirectory(), getPluginDirectory());
            FileManagerImpl.getFileManager().copyDirectory(source, dest, filt, null);
            return true;
        } else {
//            for copy to base
            dest = getDirectory();
        }
        FileManagerImpl.getFileManager().copyDirectory(source, dest, filt, null);
        return true;
    }

    @Override
    public List<EditAction> getGlobalEditActions(RtsTabType rtsTabType) {
        return null;
    }

    @Override
    public boolean closeForecast(String s) {
        return false;
    }

    public IRAlt getSimulationAlt(ModelAlternative ma) {
        if (ma == null)
        {
            return null;
        }
        ComputeOptions co = ma.getComputeOptions();
        if (co == null)
        {
            return null;
        }
        IRAlt alt = getAlt(ma);
        if (alt == null)
        {
            return null;
        }
        String altName = ma.getName();
        RmaFile file = alt.getFile();
        String runDir = getRunDirectory(co);
        String fname = file.getName();
        String runPath = runDir.concat(RmaFile.separator).concat(fname);
        RmaFile runFile = FileManagerImpl.getFileManager().getFile(runPath);
        IRAlt simAlt = newAlternative(runFile.getAbsolutePath());
        simAlt.setFile(runFile);
        simAlt.setProject(alt.getProject());
        simAlt.setName(altName);
        simAlt.readData();
        return simAlt;

    }

}
package nhs.genetics.cardiff;

import nhs.genetics.cardiff.framework.IlluminaRunParametersFile;
import nhs.genetics.cardiff.framework.IlluminaSampleSheetFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A program for reporting Illumina sequencing audits
 *
 * @author  Matt Lyon
 * @version 1.0
 * @since   2016-06-15
 */
public class Main {
    private static final Logger log = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {

        final String version = "1.0.0";

        if (args.length != 2){
            System.err.println("RunLog v" +version);
            System.err.println("Usage: RunLog <SampleSheet.csv> <RunInfo.xml>");
            System.err.println("Prints information about an Illumina sequencing run");
            System.exit(1);
        }

        boolean printHeaders = false;
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        final File file = new File("RunLog.txt");

        if (!file.exists()) printHeaders = true;

        try (PrintWriter printWriter = new PrintWriter(new FileOutputStream(file, true))){

            IlluminaSampleSheetFile illuminaSampleSheetFile = new IlluminaSampleSheetFile(new File(args[0]));
            illuminaSampleSheetFile.parseSampleSheet();
            illuminaSampleSheetFile.populateSampleSheetValues();

            IlluminaRunParametersFile illuminaRunParametersFile = new IlluminaRunParametersFile(new File(args[1]));
            illuminaRunParametersFile.parseRunParametersXml();

            /*Headers*/
            if (printHeaders) printWriter.println("Instrument\tRunId\tRunStartDate\tApplication\tAssay\tChemistry\tRead1Cycles\tRead2Cycles\tSampleSheetDate\tDescription\tExperimentName\tInvestigatorName\tWorkflow\tMCSVersion\tRTAVerison\tFlowcellPartNo\tFlowcellSerialNo\tFlowcellExpireDate\tPR2PartNo\tPR2SerialNo\tPR2ExpiryDate\tReagentPartNo\tReagentSerialNo\tReagentExpiryDate");

            /*RunInfo*/
            printWriter.print(illuminaRunParametersFile.getScannerID()); printWriter.print("\t");
            printWriter.print(illuminaRunParametersFile.getRunIdentifier()); printWriter.print("\t");
            printWriter.print(simpleDateFormat.format(illuminaRunParametersFile.getRunStartDate())); printWriter.print("\t");

            /*SampleSheetInfo*/
            printWriter.print(illuminaSampleSheetFile.getApplication()); printWriter.print("\t");
            printWriter.print(illuminaSampleSheetFile.getAssay()); printWriter.print("\t");
            printWriter.print(illuminaSampleSheetFile.getChemistry()); printWriter.print("\t");
            printWriter.print(illuminaSampleSheetFile.getCyclesRead1()); printWriter.print("\t");
            printWriter.print(illuminaSampleSheetFile.getCyclesRead2()); printWriter.print("\t");
            printWriter.print(simpleDateFormat.format(illuminaSampleSheetFile.getDate())); printWriter.print("\t");
            printWriter.print(illuminaSampleSheetFile.getDescription()); printWriter.print("\t");
            printWriter.print(illuminaSampleSheetFile.getExperimentName()); printWriter.print("\t");
            printWriter.print(illuminaSampleSheetFile.getInvestigatorName()); printWriter.print("\t");
            printWriter.print(illuminaSampleSheetFile.getWorkflow()); printWriter.print("\t");

            /*Software versions*/
            printWriter.print(illuminaRunParametersFile.getApplicationVersion()); printWriter.print("\t");
            printWriter.print(illuminaRunParametersFile.getRTAVersion()); printWriter.print("\t");

            /*ReagentInfo*/
            printWriter.print(illuminaRunParametersFile.getFlowcellPartNo()); printWriter.print("\t");
            printWriter.print(illuminaRunParametersFile.getFlowcellSerialNo()); printWriter.print("\t");
            printWriter.print(simpleDateFormat.format(illuminaRunParametersFile.getFlowcellExpireDate())); printWriter.print("\t");

            printWriter.print(illuminaRunParametersFile.getPr2PartNo()); printWriter.print("\t");
            printWriter.print(illuminaRunParametersFile.getPr2SerialNo()); printWriter.print("\t");
            printWriter.print(simpleDateFormat.format(illuminaRunParametersFile.getPr2ExpireDate())); printWriter.print("\t");

            printWriter.print(illuminaRunParametersFile.getReagentPartNo()); printWriter.print("\t");
            printWriter.print(illuminaRunParametersFile.getReagentSerialNo()); printWriter.print("\t");
            printWriter.print(simpleDateFormat.format(illuminaRunParametersFile.getReagentExpireDate()));

            printWriter.println();

            printWriter.close();
        } catch (IOException e){
            log.log(Level.SEVERE, "Could not parse file: " + e.getMessage());
            System.exit(1);
        }

    }
}

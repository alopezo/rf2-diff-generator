package org.ihtsdo.changeanalyzer;

import org.apache.log4j.Logger;
import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.doxia.siterenderer.Renderer;
import org.apache.maven.project.MavenProject;
import org.apache.maven.reporting.AbstractMavenReport;
import org.apache.maven.reporting.MavenReportException;

import java.io.*;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Reports all new concepts in a release file.
 * 
 * @author Vahram
 * @goal report-descriptions
 * @phase site
 */
public class DescriptionsReport extends AbstractMavenReport {

	private static final Logger logger = Logger.getLogger(DescriptionsReport.class);

	/**
	 * The Maven Project Object
	 * 
	 * @parameter default-value="${project}"
	 * @required
	 * @readonly
	 */
	private MavenProject project;

	/**
	 * Specifies the directory where the report will be generated
	 * 
	 * @parameter default-value="${project.reporting.outputDirectory}"
	 * @required
	 */
	private String outputDirectory;

	/**
	 * Specifies the directory of report source files
	 * 
	 * @parameter default-value="${project.reporting.outputDirectory}"
	 * @required
	 */
	private String inputDirectory;

	/**
	 * Specifies the directory of report source files
	 * 
	 * @parameter
	 * @required
	 */
	private String descriptionFileToReport;
	
	/**
	 * bundle
	 * 
	 * @parameter
	 * @required
	 */
	private String bundle;
	
	/**
	 * @component
	 * @required
	 * @readonly
	 */
	private Renderer siteRenderer;

	@Override
	protected void executeReport(Locale arg0) throws MavenReportException {
		try {
			Sink sink = getSink();
			SinkUtilities.headAndTitle(sink, "Descriptions Report");
			sink.body();

			File file = new File(inputDirectory, descriptionFileToReport);
			FileInputStream sortedFis = new FileInputStream(file);
			InputStreamReader sortedIsr = new InputStreamReader(sortedFis, "UTF-8");
			BufferedReader br = new BufferedReader(sortedIsr);
			SinkUtilities.createGeneralInfoSection(sink, br,0);

			sink.section2();
			sink.sectionTitle2();
			sink.text("Descriptions Table");
			sink.sectionTitle2_();

			SinkUtilities.createFilterSection(sink);
			String header = br.readLine();
			SinkUtilities.createConceptsTable(sink, br,header);

			sink.section2_();

			sink.body_();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected String getOutputDirectory() {
		return outputDirectory;
	}

	@Override
	protected MavenProject getProject() {
		return project;
	}

	@Override
	protected Renderer getSiteRenderer() {
		return siteRenderer;
	}

	@Override
	public String getDescription(Locale locale) {
		return getBundle(locale).getString("report.description");
	}

	@Override
	public String getName(Locale locale) {
		return getBundle(locale).getString("report.name");
	}

	@Override
	public String getOutputName() {
		return project.getArtifactId() + descriptionFileToReport.replaceAll(".txt", "") + "-Site-Report";
	}

	private ResourceBundle getBundle(Locale locale) {
		return ResourceBundle.getBundle(bundle);
	}

}

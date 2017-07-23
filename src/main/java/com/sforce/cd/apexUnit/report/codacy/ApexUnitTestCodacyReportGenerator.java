/* 
 * Copyright (c) 2016, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license.
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */

/*
 * Class to generate test report for ApexUnit run in JUnit test report format
 * @author adarsh.ramakrishna@salesforce.com
 */ 
 

package com.sforce.cd.apexUnit.report.codacy;

import java.io.FileWriter;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sforce.cd.apexUnit.ApexUnitUtils;
import com.sforce.cd.apexUnit.report.ApexReportBean;
import com.sforce.cd.apexUnit.report.ApexUnitCodeCoverageResults;

public class ApexUnitTestCodacyReportGenerator {
	private static Logger LOG = LoggerFactory.getLogger(ApexUnitTestCodacyReportGenerator.class);

	/**
	 * Generates a JUnit/Jenkins compliant test report in XML for the given job.
	 * 
	 * @param reportBeans
	 * @param reportFile
	 *            of the job whose test report is to be generated
	 * 
	 */
	public static void generateTestReport(ApexReportBean[] reportBeans, String reportFile) {
		if (reportBeans != null && reportBeans.length > 0) {
			
			JSONObject obj = new JSONObject();
			obj.put("total", ApexUnitCodeCoverageResults.orgWideCodeCoverage);
			
			ArrayList<JSONObject> clazzesCoverage = new ArrayList<JSONObject>(); 
			
			for (ApexReportBean clazzCoverage : reportBeans) {
				JSONObject clazz = new JSONObject();
				clazz.put("filename", "src/classes/" + clazzCoverage.getApexClassName() + ".cls");
				clazz.put("total", 20);
				clazz.put("coverage", new JSONObject());
				clazzesCoverage.add(clazz);
			}

			obj.put("fileReports", clazzesCoverage);
			
			try {
				FileWriter file = new FileWriter("codacy-coverage.json");				
				file.write(obj.toJSONString());
				System.out.println("Successfully Copied JSON Object to File...");
				System.out.println("\nJSON Object: " + obj);
				file.close();
			} catch(Exception ex) {
				LOG.error(ex.getMessage());
			}
			
		} else {
			ApexUnitUtils.shutDownWithErrMsg(
					"Unable to generate test report. " + "Did not find any test results for the job id");
		}
	}

}

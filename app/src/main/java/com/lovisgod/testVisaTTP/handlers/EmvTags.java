/*
 * *© Copyright 2020 Visa. All Rights Reserved.**
 *
 * NOTICE: The software and accompanying information and documentation (together, the “Software”) remain the property of
 * and are proprietary to Visa and its suppliers and affiliates. The Software remains protected by intellectual property
 * rights and may be covered by U.S. and foreign patents or patent applications. The Software is licensed and not sold.*
 *
 * By accessing the Software you are agreeing to Visa's terms of use (developer.visa.com/terms) and privacy policy (developer.visa.com/privacy).
 * In addition, all permissible uses of the Software must be in support of Visa products, programs and services provided
 * through the Visa Developer Program (VDP) platform only (developer.visa.com). **THE SOFTWARE AND ANY ASSOCIATED
 * INFORMATION OR DOCUMENTATION IS PROVIDED ON AN “AS IS,” “AS AVAILABLE,” “WITH ALL FAULTS” BASIS WITHOUT WARRANTY OR
 * CONDITION OF ANY KIND. YOUR USE IS AT YOUR OWN RISK.** All brand names are the property of their respective owners, used for identification purposes only, and do not imply
 * product endorsement or affiliation with Visa. Any links to third party sites are for your information only and equally
 * do not constitute a Visa endorsement. Visa has no insight into and control over third party content and code and disclaims
 * all liability for any such components, including continued availability and functionality. Benefits depend on implementation
 * details and business factors and coding steps shown are exemplary only and do not reflect all necessary elements for the
 * described capabilities. Capabilities and features are subject to Visa’s terms and conditions and may require development,
 * implementation and resources by you based on your business and operational details. Please refer to the specific
 * API documentation for details on the requirements, eligibility and geographic availability.*
 *
 * This Software includes programs, concepts and details under continuing development by Visa. Any Visa features,
 * functionality, implementation, branding, and schedules may be amended, updated or canceled at Visa’s discretion.
 * The timing of widespread availability of programs and functionality is also subject to a number of factors outside Visa’s control,
 * including but not limited to deployment of necessary infrastructure by issuers, acquirers, merchants and mobile device manufacturers.*
 */

package com.lovisgod.testVisaTTP.handlers;


public interface EmvTags {
	/** FCI Template Tlv Tag */
	short FCI_TEMPLATE = 0x6F;
	/** Directory Folder Name Tlv tag */
	short TAG_DF_NAME = (short) 0x84;
	/** FCI proprietary template tag */
	short FCI_PROPRIETARY_TEMPLATE = 0xA5;
	/** FCI Discretionary Data Tlv Tag */
	short TAG_FCI_ISSUER_DISCRETIONARY_DATA = (short) 0xBF0C;
	/** Directory entry Tlv tag */
	short DIRECTORY_ENTRY = 0x61;
	/** Application AID Tlv tag within FCI template */
	short TAG_AID = (short) 0x4F;
	/** Application label Tlv tag within FCI template */
	short TAG_APPLICATION_LABEL = (short) 0x50;
	/** Application Priority Indicator Tlv Tag */
	short TAG_APPLICATION_PRIORITY_INDICATOR = (short) 0x87;
	/** Application preferred name Tlv tag within FCI template */
	short TAG_APPLICATION_PREFERRED_NAME = (short) 0x9F12;
	/** Application Selection Registered Proprietary Data Tlv tag within FCI template */
	short TAG_APPLICATION_SELECTION_REG_DATA = (short)0x9F02;
}

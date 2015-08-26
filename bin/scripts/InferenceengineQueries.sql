-- db.BSICohortQuery=
select p.PatientSID, p.AdmitDateTime, p.DischargeDateTime
from [ORD_Doebbeling_20090916].[dflt].JLB_7__F_BloodHospitalizations_Groupings p,


-- Cultur view: PatientSID, SpecimenDateTime, Organism, Specimen_Released, Sample
-- Assume that methicillian-resistant organism is identifed by a single specimen name

-- db.MRSABSIBloodUrineCulutresQuery 

select distinct p.PatientSID, culture.SpecimenDateTime,'Blood' as Sample, 'MRSA' as Organism
from [ORD_Doebbeling_20090916].[dflt].JLB_7__F_BloodHospitalizations_Groupings p, 
[ORD_Doebbeling_20090916].[dflt].JLB_7__D_AllBlood_BySnomedCUI_Release_AndByMethI_Resistent culture
where culture.SpecimenDateTIme between dateadd(d, -9,p.AdmitDateTime) and p.DischargeDateTime 

union

select distinct p.PatientSID, culture.SpecimenDateTime,'Urine' as Sample, 'MRSA' as Organism
from [ORD_Doebbeling_20090916].[dflt].JLB_7__F_BloodHospitalizations_Groupings p, 
[ORD_Doebbeling_20090916].[dflt].JLB_7__D_AllUrine_BySnomedCUI_Release_AndByMethI_Resistent culture
where culture.SpecimenDAteTIme between dateadd(d, -9,p.AdmitDateTime) and p.DischargeDateTime 
order by p.PatientSID, culture.SpecimenDateTime



-- db.BSIContaminantsQuery =
select p.PatientSID, 'Non_common_skin_contaminant' as Organism, culture.Sample, culture.Specimen_Released, culture.SpecimenDateTime 
from [ORD_Doebbeling_20090916].[dflt].JLB_7__F_BloodHospitalizations_Groupings p, 
	Cultures_View culture,
	[ORD_Doebbeling_20090916].[dflt].MRSA_KBMappings as map
where culture.SpecimenDateTime between dateadd(d, -9,p.AdmitDateTime) and p.DischargeDateTime and
	p.PatientSID = culture.PatientSID and
	culture.Organism not in 
		(Select  culture.Organism from Cultures_View culture1, [ORD_Doebbeling_20090916].[dflt].MRSA_KBMappings as map1
		where culture1.Organsim = map1.DBTerm1 and
			map1.KBTerm = 'Common_skin_contaminant') and
	culture.Sample in 
		(Select  culture.Sample from Cultures_View culture2, [ORD_Doebbeling_20090916].[dflt].MRSA_KBMappings as map2
		where culture2.Organsim = map2.DBTerm1 and
			map2.KBTerm = 'Non-blood_non-urine_sterile_site') and
	culture.Specimen_Released in 
		(Select  culture.Specimen_Released from Cultures_View culture3, [ORD_Doebbeling_20090916].[dflt].MRSA_KBMappings as map3
		where culture3.Organsim = map3.DBTerm1 and
			map3.KBTerm = 'Non-blood_non-urine_sterile_site')




--db.intravasCatheterQuery=
select distinct p.PatientSID, doc.ReportTextDate, nlp.NLP_Concept 
	from [ORD_Doebbeling_20090916].[dflt].JLB_7__F_BloodHospitalizations_Groupings p,
	[ORD_Doebbeling_20090916].[dflt].[NLP-UTI] nlp, 
	[ORD_Doebbeling_20090916].[dflt].[JLB_7__H_TIU_Documents_blood] doc,
	[ORD_Doebbeling_20090916].[dflt].JLB_7__Z_concept_categories map 
	where doc.PatientSID = p.PatientSID and
		doc.TIUDocumentSID = nlp.TIU_ID and 
		nlp.Negation = 'positive' and
		map.[Column 3] = nlp.NLP_Concept and
		map.[Column 1] = 'Intravascular_catheter' and
		doc.ReportTextDate between dateadd(d, -9,p.AdmitDateTime) and p.DischargeDateTime 

--db.catheterSymptomsQuery =
select distinct doc.PatientSID, doc.ReportTextDate, nlp.NLP_Concept 
	from 
	[ORD_Doebbeling_20090916].[dflt].JLB_7__F_BloodHospitalizations_Groupings p,
	[ORD_Doebbeling_20090916].[dflt].[NLP-UTI] nlp, 
	[ORD_Doebbeling_20090916].[dflt].[JLB_7__H_TIU_Documents_urine] doc,
	[ORD_Doebbeling_20090916].[dflt].JLB_7__Z_concept_categories map 
	where doc.TIUDocumentSID = nlp.TIU_ID and 
		p.PatientSID = doc.PatientSID and
		nlp.Negation = 'positive' and
		map.[Column 3] = nlp.NLP_Concept and
		(map.[Column 1] = 'Inflammatory_change' or map.[Column 3] = 'Purulent_drainage') AND
		doc.ReportTextDate between dateadd(d, -9,p.AdmitDateTime) and p.DischargeDateTime 


--db.UTICohortQuery=
select * from [ORD_Doebbeling_20090916].[dflt].JLB_7__F_UrineHospitalizations_Groupings p,


--db.UTIBloodUrineCulutresQuery


--db.UTIContaminantsQuery = 

--db.fevertemperatureQuery = 
select p.PatientSID, vitals.VitalSignTakenDateTime 
from [ORD_Doebbeling_20090916].[dflt].JLB_7__F_UrineHospitalizations_Groupings p, 
	[ORD_Doebbeling_20090916].[dflt].[JLB_7__G_Vitals_ByAdmission] vitals
where	p.patientSID = vitals.patientSID and 
	vitals.VitalSignTakenDateTime between dateadd(d, -9,p.AdmitDateTime) and p.DischargeDateTime

-- Presence of urinary catheter. Note need mapping of specific terms to general term, not MRSA_KBMapping
-- db.urinaryCatheterQueryDebug = 
select distinct doc.PatientSID, doc.ReportTextDate, nlp.NLP_Concept 
	from 
	[ORD_Doebbeling_20090916].[dflt].JLB_7__F_UrineHospitalizations_Groupings p,
	[ORD_Doebbeling_20090916].[dflt].[NLP-UTI] nlp, 
	[ORD_Doebbeling_20090916].[dflt].[JLB_7__H_TIU_Documents_urine] doc,
	[ORD_Doebbeling_20090916].[dflt].JLB_7__Z_concept_categories map 
	where doc.TIUDocumentSID = nlp.TIU_ID and 
		p.PatientSID = doc.PatientSID and
		nlp.Negation = 'positive' and
		map.[Column 3] = nlp.NLP_Concept and
		map.[Column 1] = 'Urinary_catheter'

MERGE INTO CGP_NOI_FORM_DATA_INDEX idx
USING CGP_NOI_FORMS forms
ON (idx.id = forms.INDEX_ID)
WHEN MATCHED THEN UPDATE SET idx.CERTIFIER = substr(utl_raw.cast_to_varchar2(dbms_lob.substr(forms.formdata, 2000)),
                                                    instr(
                                                        utl_raw.cast_to_varchar2(dbms_lob.substr(forms.formdata, 2000)),
                                                        'userId', -1, 1) + 9,
                                                    instr(
                                                        utl_raw.cast_to_varchar2(dbms_lob.substr(forms.formdata, 2000)),
                                                        'firstName', -1, 1) - (instr(utl_raw.cast_to_varchar2(
                                                                                         dbms_lob.substr(forms.formdata,
                                                                                                         2000)),
                                                                                     'userId', -1, 1) + 9) - 3)
WHERE forms.CROMERR_ACTIVITY_ID IS NOT NULL;
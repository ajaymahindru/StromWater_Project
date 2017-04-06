INSERT INTO CGP_SYSTEM_PROPERTIES(ID, PROP_KEY, PROP_VALUE, DESCRIPTION) VALUES (1, 'disable_distribution', 'false', 'If set to true the system will skip distribution until the value is set back to false.  This is used for when ICIS goes down during normal hours to avoid errors from distribution.  Note: this value should be set to the string "true" not the sql boolean value true.');
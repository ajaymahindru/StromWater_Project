CREATE TABLE CGP_SYSTEM_PROPERTIES(
	ID NUMBER(19) PRIMARY KEY NOT NULL,
	PROP_KEY VARCHAR2(255) NOT NULL,
  PROP_VALUE VARCHAR2(4000),
  DESCRIPTION VARCHAR2(4000)
);
CREATE INDEX IDX_SYSTEM_PROP ON CGP_SYSTEM_PROPERTIES(PROP_KEY);
INSERT INTO CGP_SYSTEM_PROPERTIES(ID, PROP_KEY, PROP_VALUE, DESCRIPTION) VALUES (1, 'disable_distribution', 'false', 'If set to true the system will skip distribution until the value is set back to false.  This is used for when ICIS goes down during normal hours to avoid errors from distribution.  Note: this value should be set to the string "true" not the sql boolean value true.');
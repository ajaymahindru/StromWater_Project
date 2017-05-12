-- adding BCC notification category, with accepted_by_icis subcat
INSERT INTO cgp_ref_notifications(ID, CATEGORY, SUBCATEGORY, SUBCATEGORY_DESC) VALUES(62,'icis','failed', 'transaction failed');

-- adding cgp@epa.gov as a subscriber
INSERT INTO cgp_ref_subscribers(ID, SUBSCRIBER_EMAIL, DESCRIPTION) VALUES(43,'NPDESereporting@epa.gov', 'NPDES eReporting EPA contact');




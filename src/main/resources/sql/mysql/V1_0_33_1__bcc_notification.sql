-- adding BCC notification category, with accepted_by_icis subcat
INSERT INTO cgp_ref_notifications(ID, CATEGORY, SUBCATEGORY, SUBCATEGORY_DESC) VALUES(61,'bcc','accepted_by_icis', 'notification type');

-- adding cgp@epa.gov as a subscriber
INSERT INTO cgp_ref_subscribers(ID, SUBSCRIBER_EMAIL, DESCRIPTION) VALUES(42,'cgp@epa.gov', 'CGP EPA contact');




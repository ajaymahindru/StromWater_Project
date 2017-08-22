-- adding new FWS subscribers (Guam) per OES-823
insert into cgp_ref_subscribers(id, subscriber_email, first_name, last_name, description)
values(44,'catherine_brunson@fws.gov','Catherine', 'Brunson', 'FWS contact');
insert into cgp_ref_subscribers(id, subscriber_email, first_name, last_name, description)
values(45,'jacqueline_flores@fws.gov','Jacqueline', 'Flores', 'FWS contact');
-- removing erik_orsak@fws.gov from notifications per OES-811
delete from cgp_ref_subscribers where id=32;
delete from cgp_ref_subscriber_notif where subscriber_id=32;

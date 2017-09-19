-- removing chuck_ardizzone@fws.gov from notifications per OES-908
delete from cgp_ref_subscribers where id=35;
delete from cgp_ref_subscriber_notif where subscriber_id=35;
-- removing erik_orsak@fws.gov from notifications per OES-811
delete from cgp_ref_subscribers where id=32;
delete from cgp_ref_subscriber_notif where subscriber_id=32;
